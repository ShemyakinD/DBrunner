package org.dcc360.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.dcc360.Services.Loggator;
import org.dcc360.Services.SetupRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class LogTabPageController {
    @FXML
    private TextArea logBox;
    @FXML
    private Button refreshLogs;

    @FXML
    private ListView<String> logListView;

    private static String logPath = SetupRunner.getInstallDir() + "log.txt";

    public void initialize() {
        try {
            logBox.setEditable(false);
            logBox.appendText(getLogs(logPath));
            logBox.setScrollTop(Double.MAX_VALUE);

            logListView.setItems(readFileInList(logPath));

            refreshLogs.setOnAction(e -> {
                logBox.clear();
                logBox.appendText(getLogs(logPath));
                logBox.setScrollTop(Double.MAX_VALUE);
                logListView.setItems(readFileInList(logPath));
            });
        } catch (Exception e) {
            e.printStackTrace();
            logBox.appendText(e.getMessage());
        }
    }

    private static String getLogs(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            Loggator.commonLog(Level.SEVERE,"Ошибка чтения файла с логами в каталоге " + fileName);
            return ioe.getMessage();
        }
    }

    public static ObservableList<String> readFileInList(String fileName) {
        try {
            List<String> logList =  Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            logList.removeAll(Arrays.asList("", null," "));
            return FXCollections.observableArrayList( logList);
        }
        catch (IOException ioe) {
            Loggator.commonLog(Level.SEVERE,"Ошибка чтения файла с логами в каталоге " + fileName);
            return FXCollections.observableArrayList(ioe.getMessage());
        }
    }
}
