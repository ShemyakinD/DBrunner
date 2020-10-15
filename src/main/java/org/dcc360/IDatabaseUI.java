package org.dcc360;

import javafx.scene.control.CheckBox;

public interface IDatabaseUI {

    default void addSelectListener(Database database, CheckBox activeBox){
        activeBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            aBoolean = !aBoolean;
            XMLizer.setIsActiveAttribute(database, aBoolean.toString());
        });
    }

     CheckBox getSelect();

     void setSelect(CheckBox select);

}
