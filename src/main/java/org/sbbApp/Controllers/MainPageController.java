package org.sbbApp.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainPageController {

    @FXML
    BorderPane borderPanePage;

    @FXML
    Button buttonConnectionsPage;

    @FXML
    Button buttonStationBoard;

    @FXML
    Button buttonStationSearch;


    @FXML
    private void initialize() {
        loadPage("connectionsPage");
    }

    @FXML
    private void onButtonConnectionsPageClicked() {
        loadPage("connectionsPage");
    }

    @FXML
    private void onButtonStationSearchClicked() {
        loadPage("stationSearchPage");
    }

    @FXML
    private void onButtonStationStationBoardClicked() {
        loadPage("stationBoardPage");
    }

    public void loadPage(String fxmlName) {
        Parent root = null;
        try {
            root = FXMLLoader.load(MainPageController.class.getResource("/pages/" + fxmlName + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPanePage.setCenter(root);
    }

}
