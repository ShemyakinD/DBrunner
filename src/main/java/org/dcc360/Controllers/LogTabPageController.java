package org.dcc360.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.dcc360.Database;
import org.dcc360.Services.Loggator;
import org.dcc360.Services.SetupRunner;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LogTabPageController {
    @FXML
    private Button refreshLogsButton;
    @FXML
    private Button openLogFileButton;
    @FXML
    private ComboBox<Level> logLevelBox;
    @FXML
    private ListView<String> logListView;

    private static String logPath = SetupRunner.getInstallDir() + "log.txt";

    public void initialize() {
        try {
            ObservableList<Level> logLevels = FXCollections.observableArrayList(Level.ALL, Level.SEVERE, Level.WARNING, Level.INFO);
            logLevelBox.setItems(logLevels);
            logLevelBox.getSelectionModel().selectFirst();
            /*logBox.setEditable(false);
            logBox.appendText(getLogs(logPath));
            logBox.setScrollTop(Double.MAX_VALUE);*/

            logListView.setItems(readFileInList(logPath, logLevelBox.getValue()));

            logLevelBox.setOnAction(e -> {
                refreshLogsButton.fire();
            });

            openLogFileButton.setOnAction(e -> {
                try {
                    Desktop.getDesktop().open(new File(logPath));
                }
                catch (IOException ioe){
                    Loggator.commonLog(Level.SEVERE,"Ошибка открытия файла с логами " + logPath);
                }

            });

            refreshLogsButton.setOnAction(e -> {
                logListView.setItems(readFileInList(logPath, logLevelBox.getValue()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private static String getLogs(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            Loggator.commonLog(Level.SEVERE,"Ошибка чтения файла с логами в каталоге " + fileName);
            return ioe.getMessage();
        }
    }*/

    public static ObservableList<String> readFileInList(String fileName, Level level) {
        try {
            List<String> logList =  Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            List<String> resultLogList = new ArrayList<>();
            if (level != Level.ALL) {
                boolean output = false;
                for (String logRow : logList) {
                    if (logRow.matches("(.*)\\[\\w+\\s*\\](.*)")) {
                        if (logRow.matches("(.*)\\[" + level.getName() + "\\s*\\](.*)"))
                            output = true;
                        else output = false;
                    }

                    if (output)
                        resultLogList.add(logRow);
                }
            }
            else resultLogList = logList;
            resultLogList.removeAll(Arrays.asList("", null," "));
            return FXCollections.observableArrayList(resultLogList);
        }
        catch (IOException ioe) {
            Loggator.commonLog(Level.SEVERE,"Ошибка чтения файла с логами в каталоге " + fileName);
            return FXCollections.observableArrayList(ioe.getMessage());
        }
    }
}
