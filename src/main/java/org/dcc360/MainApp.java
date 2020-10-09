//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dcc360.Services.Engine;

public class MainApp extends Application {

    public MainApp() {
    }

    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
            stage.setTitle("KURWANNER2.0");
            stage.setScene(new Scene(root, 600.0D, 300.0D));
            stage.setMinHeight(300D);
            stage.setMinWidth(600D);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop() {
        Engine.service.shutdown();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        if (!(new File(SetupKurwanner.geDBDir())).exists()) {
            XMLizer.writeDBDataToXML();
        }

        SetupKurwanner.InstallKurwanner();

        Engine.startEngine();

        launch(args);
    }

}
