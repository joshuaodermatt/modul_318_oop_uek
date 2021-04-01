package org.sbbApp.Controllers;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.Core.Transport;
import org.Models.StationBoard;
import org.Models.StationBoardRoot;
import org.Models.Stations;
import org.sbbApp.StationBoardTableViewObject;
import org.sbbApp.TypeAheadEngine;
import org.sbbApp.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

public class StationBoardController {
    @FXML
    Button buttonSearch;

    @FXML
    TextField textFieldStation;

    @FXML
    TableView<StationBoardTableViewObject> tableViewConnections;

    @FXML
    Label labelStationSearch;

    @FXML
    TableColumn<StationBoardTableViewObject, String> columnDeparture;
    @FXML
    TableColumn<StationBoardTableViewObject, String> columnName;
    @FXML
    TableColumn<StationBoardTableViewObject, String> columnPlatform;
    @FXML
    TableColumn<StationBoardTableViewObject, String> columnDestination;

    TypeAheadEngine typeAheadEngine = new TypeAheadEngine();

    SuggestionProvider<String> suggestionProvider = SuggestionProvider.create(new HashSet<>());


    @FXML
    private void initialize() {
        setTableViewCols();
        new AutoCompletionTextFieldBinding<>(textFieldStation, suggestionProvider);
    }

    @FXML
    private void onKeyTyped(KeyEvent keyEvent) {
        Utils.clearSpecialChars(keyEvent);
    }

    @FXML
    private void onKeyReleased(KeyEvent keyEvent) {
        try {
            typeAheadEngine.updateSuggestions(textFieldStation.getText());
            ArrayList<String> suggestions = typeAheadEngine.getSuggestions();
            suggestionProvider.clearSuggestions();
            suggestionProvider.addPossibleSuggestions(suggestions);
        } catch (Exception e) {
            suggestionProvider.clearSuggestions();
            e.printStackTrace();
        }

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            buttonSearch.fire();
        }

    }


    private void setTableViewCols() {
        columnDeparture.setCellValueFactory(new PropertyValueFactory<StationBoardTableViewObject, String>("departure"));
        columnName.setCellValueFactory(new PropertyValueFactory<StationBoardTableViewObject, String>("name"));
        columnPlatform.setCellValueFactory(new PropertyValueFactory<StationBoardTableViewObject, String>("platform"));
        columnDestination.setCellValueFactory(new PropertyValueFactory<StationBoardTableViewObject, String>("destination"));
    }

    @FXML
    private void onButtonSearchStationClicked() throws ParseException {
        labelStationSearch.setText("");
        Transport transport = new Transport();
        String inputStation = textFieldStation.getText();
        StationBoardRoot stationBoardRoot = null;
        ObservableList<StationBoardTableViewObject> mappedStationBoards = null;

        if (!inputStation.equals("")) {
            try {
                Stations stations = transport.getStations(inputStation);
                stationBoardRoot = transport.getStationBoard(inputStation, stations.stationList.get(0).Id);
                mappedStationBoards = mapStationBoardRoot(stationBoardRoot);
            } catch (Exception exception) {
                labelStationSearch.setText("No station found");
                exception.printStackTrace();
            }
        } else {
            labelStationSearch.setText("No station given");
        }


        tableViewConnections.setItems(mappedStationBoards);

    }


    private ObservableList<StationBoardTableViewObject> mapStationBoardRoot(StationBoardRoot stationBoardRoot) throws ParseException {
        ObservableList<StationBoardTableViewObject> mappedStationBoards = FXCollections.observableArrayList();

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat onlyTime = new SimpleDateFormat("HH:mm");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        onlyTime.setTimeZone(TimeZone.getTimeZone("UTC+1"));

        for (StationBoard stationBoard : stationBoardRoot.Entries) {
            Date departure = isoFormat.parse(stationBoard.Stop.Departure);

            StationBoardTableViewObject temp = new StationBoardTableViewObject(
                    onlyTime.format(departure),
                    stationBoard.Category + " " + stationBoard.Number,
                    stationBoard.Stop.platform,
                    stationBoard.To
            );
            mappedStationBoards.add(temp);
        }
        return mappedStationBoards;
    }

}






