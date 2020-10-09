package org.dcc360.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import org.dcc360.SetupKurwanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LogTabPageController {
    @FXML
    private TextArea logBox;
    @FXML
    private Button refreshLogs;

    public void initialize() {
        try {
            logBox.setEditable(false);
            logBox.appendText(getLogs());

            refreshLogs.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
                logBox.clear();
                logBox.appendText(getLogs());
                logBox.setScrollTop(1.7976931348623157E308D);
            });
        }
        catch (Exception e){
            e.printStackTrace();
            logBox.appendText(e.getMessage());
        }
        logBox.setScrollTop(1.7976931348623157E308D);
    }

    private static String getLogs() {
        try {
            Scanner s = (new Scanner(new File(SetupKurwanner.getInstallDir() + "log.txt"), "UTF-8")).useDelimiter("\n\\d+-\\d+-\\d+");
            StringBuilder textLogs = new StringBuilder();

            while(s.hasNext()) {
                textLogs.insert(0, "\n---------------\n").insert(0, s.findWithinHorizon("\\d+-\\d+", 0) + s.next());
            }

            return textLogs.toString();
        } catch (FileNotFoundException var2) {
            var2.printStackTrace();
            return var2.getMessage();
        }
    }
}
