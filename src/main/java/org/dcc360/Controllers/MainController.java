//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable {
    // Inject tab content.
    @FXML
    private AnchorPane dbTabPage;
    // Inject controller
    @FXML
    private DbTabPageController dbTabPageController;

    // Inject tab content.
    @FXML
    private AnchorPane logTabPage;
    // Inject controller
    @FXML
    private LogTabPageController logTabPageController;

    // Inject tab content.
    @FXML
    private AnchorPane filesTabPage;
    // Inject controller
    @FXML
    private FilesTabPageController filesTabPageController;

    // Inject tab content.
    @FXML
    private AnchorPane infoTabPage;
    // Inject controller
    @FXML
    private InfoTabPageController infoTabPageController;

    public void initialize(URL url, ResourceBundle rb) {
    }
}
