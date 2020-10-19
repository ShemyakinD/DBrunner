package org.dcc360;

import javafx.scene.control.CheckBox;
import org.dcc360.Services.SetupRunner;
import org.dcc360.Services.XMLizer;

import java.io.File;
import java.util.Objects;

public class Database {
    private File folder;
    private String connection;
    private String username;
    private String password;
    private Boolean isActive;

    private CheckBox select = new CheckBox();

    public Database(File folder, String connection, String username, String password, Boolean isActive) {
        this.folder = folder;
        this.connection = connection;
        this.username = username;
        this.password = password;
        this.isActive = isActive;

        addSelectListener(this, select);
    }

    public Database(String name, String connection, String username, String password, Boolean isActive) {
        this(new File(SetupRunner.getInstallDir() + name),connection,username,password,isActive);
    }

    private void addSelectListener(Database database, CheckBox activeBox){
        activeBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            aBoolean = !aBoolean;
            XMLizer.setIsActiveAttribute(database, aBoolean.toString());
        });
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public String getName(){
        return folder.getName();
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public CheckBox getSelect() {
        if (getActive())
            select.setSelected(true);
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return getName().equals(database.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(connection, username);
    }

}
