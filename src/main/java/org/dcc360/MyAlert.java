package org.dcc360;

import javafx.scene.control.Alert;

public class MyAlert extends Alert {

    public MyAlert(Alert.AlertType type, String title, String header, String content){
        super(type,content);
        this.setTitle(title);
        this.setHeaderText(header);
    }

    public static void showMyAlert(Alert.AlertType type, String title, String header, String content){
        new MyAlert(type,title,header,content).showAndWait();
    }
}
