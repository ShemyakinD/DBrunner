//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dcc360.Services.Engine;
import org.dcc360.Services.Loggator;
import org.dcc360.Services.SetupRunner;
import org.dcc360.Services.XMLizer;

public class MainApp extends Application {

    public MainApp() {
    }

    public void start(Stage stage){
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
        if (!(new File(SetupRunner.geDBDir())).exists()) {
            XMLizer.writeDBDataToXML();
        }

        Loggator.init();

        SetupRunner.InstallKurwanner();

        Engine.startEngine();

        launch(args);
    }

}
