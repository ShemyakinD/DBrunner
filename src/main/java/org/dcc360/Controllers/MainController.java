//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360.Controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ResourceBundle;
import java.util.Scanner;
/*import javafx.fxml.FXML;
import javafx.fxml.Initializable;*/
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.dcc360.Controllers.DbTabPageController;
import org.dcc360.Controllers.LogTabPageController;


public class MainController implements Initializable {
    // Inject tab content.
    @FXML private AnchorPane dbTabPage;
    // Inject controller
    @FXML private DbTabPageController dbTabPageController;

    // Inject tab content.
    @FXML private AnchorPane logTabPage;
    // Inject controller
    @FXML private LogTabPageController logTabPageController;

    public void initialize(URL url, ResourceBundle rb) {
        try {

        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    /*private void setupWathcer() throws IOException, InterruptedException {
        Path dir = Paths.get(SetupKurwanner.getInstallDir());
        WatchService watchService = FileSystems.getDefault().newWatchService();
        dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        while(true) {
            WatchKey watchKey;
            do {
                watchKey = watchService.take();
            } while(watchKey == null);

            watchKey.pollEvents().forEach((watchEvent) -> {
                String filename = watchEvent.context().toString();
                System.out.println(filename);
                this.logBox.clear();
                this.logBox.appendText(getLogs());
            });
            watchKey.reset();
            this.logBox.setScrollTop(1.7976931348623157E308D);
        }
    }*/
}
