package org.sbbApp.Controllers;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.Core.Transport;
import org.Models.Connection;
import org.Models.Connections;
import org.Models.Station;
import org.Models.Stations;
import org.sbbApp.ConnectionsTableViewObject;
import org.sbbApp.MailService;
import org.sbbApp.TypeAheadEngine;
import org.sbbApp.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

public class ConnectionsPageController {

    @FXML
    BorderPane borderPaneContent;

    @FXML
    TextField textFieldFromStation;

    @FXML
    TextField textFieldToStation;

    @FXML
    TextField textFieldTime;

    @FXML
    DatePicker datePickerCustomDate;

    @FXML
    Button buttonSearch;


    @FXML
    Label labelTimeMessage;

    @FXML
    Label labelMailMessage;

    @FXML
    Label labelStationsMessage;

    @FXML
    Label labelTableViewMessage;

    @FXML
    Button buttonMailConnection;

    @FXML
    Button buttonSwitchTextFieldContents;


    @FXML
    TableView<ConnectionsTableViewObject> tableViewConnections;

    @FXML
    TableColumn<ConnectionsTableViewObject, String> columnDeparture;
    @FXML
    TableColumn<ConnectionsTableViewObject, String> columnArrival;
    @FXML
    TableColumn<ConnectionsTableViewObject, String> columnPlatform;
    @FXML
    TableColumn<ConnectionsTableViewObject, String> columnDuration;

    SuggestionProvider<String> suggestionProvider = SuggestionProvider.create(new HashSet<>());

    private TypeAheadEngine typeAheadEngine = new TypeAheadEngine();

    @FXML
    private void initialize() {
        setTableViewCols();
        setDateAndTime();
        configureDatePicker();
        buttonMailConnection.setDisable(true);
        new AutoCompletionTextFieldBinding<>(textFieldFromStation, suggestionProvider);
        new AutoCompletionTextFieldBinding<>(textFieldToStation, suggestionProvider);
    }

    @FXML
    private void onKeyTyped(KeyEvent keyEvent) {
        Utils.clearSpecialChars(keyEvent);
    }

    @FXML
    private void onKeyReleased(KeyEvent keyEvent) {

        try {
            if (textFieldFromStation.isFocused()) {
                typeAheadEngine.updateSuggestions(textFieldFromStation.getText());
                ArrayList<String> suggestions = typeAheadEngine.getSuggestions();
                suggestionProvider.clearSuggestions();
                suggestionProvider.addPossibleSuggestions(suggestions);
            }
            if (textFieldToStation.isFocused()) {
                typeAheadEngine.updateSuggestions(textFieldToStation.getText());
                ArrayList<String> suggestions = typeAheadEngine.getSuggestions();
                suggestionProvider.clearSuggestions();
                suggestionProvider.addPossibleSuggestions(suggestions);
            }
        } catch (Exception e) {
            suggestionProvider.clearSuggestions();
        }

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            buttonSearch.fire();
        }

    }

    @FXML
    private void onButtonMailConnectionClicked() {
        labelMailMessage.setText("");
        try {
            loadPage("emailPage");
        } catch (Exception e) {
            labelMailMessage.setText("Feature not supported");
        }
    }

    @FXML
    private void onButtonSwitchTextFieldContentClicked() {
        String temp = textFieldFromStation.getText();
        textFieldFromStation.setText(textFieldToStation.getText());
        textFieldToStation.setText(temp);
    }


    public void loadPage(String fxmlName) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/pages/" + fxmlName + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPaneContent.setCenter(root);
    }


    private void configureDatePicker() {
        datePickerCustomDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
    }

    private void setDateAndTime() {
        textFieldTime.setText(getFormattedTime());
        datePickerCustomDate.setValue(LocalDate.now());
    }

    private String getFormattedTime() {
        String formattedTime = "";
        Date date = new Date();
        if (date.getHours() < 10)
            formattedTime += "0" + date.getHours();
        else
            formattedTime += String.valueOf(date.getHours());

        formattedTime += ":";

        if (date.getMinutes() < 10)
            formattedTime += "0" + date.getMinutes();
        else
            formattedTime += String.valueOf(date.getMinutes());
        return formattedTime;
    }

    private void setTableViewCols() {
        columnDeparture.setCellValueFactory(new PropertyValueFactory<ConnectionsTableViewObject, String>("departure"));
        columnArrival.setCellValueFactory(new PropertyValueFactory<ConnectionsTableViewObject, String>("arrival"));
        columnPlatform.setCellValueFactory(new PropertyValueFactory<ConnectionsTableViewObject, String>("platform"));
        columnDuration.setCellValueFactory(new PropertyValueFactory<ConnectionsTableViewObject, String>("duration"));
    }

    @FXML
    private void onButtonSearchClicked() throws ParseException {
        resetLabels();

        LocalDate pickedDate = datePickerCustomDate.getValue();
        String pickedTime = textFieldTime.getText();
        String from = textFieldFromStation.getText();
        String to = textFieldToStation.getText();

        Connections connections = null;
        if (validateParameters(pickedTime, from, to)) {
            Station fromStation = getStation(from);
            Station toStation = getStation(to);
            if (!(toStation == null) || !(fromStation == null))
                connections = submit(fromStation.Name, toStation.Name, pickedDate, pickedTime);
            else
                labelStationsMessage.setText("Either one ore two Stations are invalid");
        }

        ObservableList<ConnectionsTableViewObject> mappedConnections = null;

        if (connections != null) {
            mappedConnections = mapConnections(connections);
            tableViewConnections.setItems(mappedConnections);
            MailService.setConnection(mappedConnections.get(0));
            buttonMailConnection.setDisable(false);
        } else {
            labelTableViewMessage.setText("Sorry... could not find any connections");
        }

    }

    private Station getStation(String stationName) {
        Transport transport = new Transport();
        Stations stations = null;
        try {
            stations = transport.getStations(stationName);
        } catch (Exception exception) {
            return null;
        }
        if (stations.stationList.size() > 0)
            return stations.stationList.get(0);
        else
            return null;
    }

    private Connections submit(String from, String to, LocalDate pickedDate, String pickedTime) {
        Transport transport = new Transport();
        Connections connections = null;
        try {
            connections = transport.getConnectionsWithTime(from, to, pickedDate.toString(), pickedTime.toString());
        } catch (Exception exception) {
            System.out.println("oups..");
            exception.printStackTrace();
        }
        return connections;
    }

    private void resetLabels() {
        labelStationsMessage.setText("");
        labelTimeMessage.setText("");
        labelTableViewMessage.setText("");
    }

    private boolean validateParameters(String pickedTime, String textFieldFromStation, String textFieldToStation) {
        try {
            LocalTime time = LocalTime.parse(textFieldTime.getText());
        } catch (Exception exception) {
            labelTimeMessage.setText("Time entry must be of format: hh:mm");
            return false;
        }

        if (textFieldFromStation.equals("") || textFieldToStation.equals("")) {
            labelStationsMessage.setText("Please enter both Stations");
            return false;
        }

        return true;
    }


    private ObservableList<ConnectionsTableViewObject> mapConnections(Connections connections) throws ParseException {
        ObservableList<ConnectionsTableViewObject> mappedConnections = FXCollections.observableArrayList();

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat onlyTime = new SimpleDateFormat("HH:mm");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        onlyTime.setTimeZone(TimeZone.getTimeZone("UTC+1"));

        for (Connection connection : connections.ConnectionList) {
            Date departure = isoFormat.parse(connection.From.Departure);
            Date arrival = isoFormat.parse(connection.To.Arrival);

            ConnectionsTableViewObject temp = new ConnectionsTableViewObject(
                    onlyTime.format(departure),
                    onlyTime.format(arrival),
                    connection.From.Platform,
                    connection.Duration
            );
            mappedConnections.add(temp);
        }
        return mappedConnections;
    }


}
