package org.sbbApp.Controllers;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.Core.Transport;
import org.Models.Station;
import org.Models.Stations;
import org.sbbApp.TypeAheadEngine;

import java.util.ArrayList;
import java.util.HashSet;

public class StationSearchController {

    @FXML
    Button buttonStationSearch;

    @FXML
    TextField textFieldStation;

    @FXML
    ListView listViewStations;

    @FXML
    Label labelInputMessage;

    @FXML
    Label labelStationsMessage;

    SuggestionProvider<String> suggestionProvider = SuggestionProvider.create(new HashSet<>());

    TypeAheadEngine typeAheadEngine = new TypeAheadEngine();

    @FXML
    private void initialize() {
        buttonStationSearch.requestFocus();
        new AutoCompletionTextFieldBinding<>(textFieldStation, suggestionProvider);
    }


    @FXML
    private void onKeyPressed(KeyEvent keyEvent) {
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
            buttonStationSearch.fire();
        }
    }


    @FXML
    private void onButtonSearchStationClicked() {
        resetElements();

        Transport transport = new Transport();
        String inputStation = textFieldStation.getText();
        Stations stations = null;

        if (!inputStation.equals("")) {
            try {
                stations = transport.getStations(inputStation);
            } catch (Exception exception) {
                labelStationsMessage.setText("Sorry... no stations found");
                exception.printStackTrace();
            }
        } else {
            labelInputMessage.setText("No input given");
        }


        if (stations == null)
            labelStationsMessage.setText("Sorry... no stations found");

        if (stations.stationList.size() > 0) {
            for (Station station : stations.stationList) {
                String stationName = station.Name;
                listViewStations.getItems().add(stationName);
            }
        }

        textFieldStation.requestFocus();
    }

    private void resetElements() {
        labelInputMessage.setText("");
        labelStationsMessage.setText("");
        listViewStations.getItems().clear();
    }
}

