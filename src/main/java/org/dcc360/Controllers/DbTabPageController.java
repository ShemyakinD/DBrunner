package org.dcc360.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import org.dcc360.Database;
import org.dcc360.Services.Engine;
import org.dcc360.SetupException;
import org.dcc360.SetupKurwanner;
import org.dcc360.XMLizer;

public class DbTabPageController {

    @FXML
    private TableView<Database> databaseTable;
    @FXML
    private TableColumn<Database, String> dbNameColumn;
    @FXML
    private TableColumn<Database, String> dbUsernameColumn;
    @FXML
    private TableColumn<Database, String> selectColumn;

    @FXML
    private TextField databaseName;
    @FXML
    private TextField databaseConnection;
    @FXML
    private TextField databaseUser;
    @FXML
    private PasswordField databasePass;

    @FXML
    private Button testConnectionButton;
    @FXML
    private Button deleteDBButton;
    @FXML
    private Button saveDBButton;

    private ObservableList<Database> databaseData = FXCollections.observableArrayList();

    public void initialize() {
        dbNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        dbUsernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        selectColumn.setCellValueFactory(
                new PropertyValueFactory<>("select")
        );

        databaseTable.setRowFactory(tv -> {
            TableRow<Database> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    Database clickedDB = row.getItem();
                    fillDatabaseData(clickedDB);
                }
            });
            return row;
        });

        refreshDBTable();

        deleteDBButton.setOnAction(e -> {
            Database selectedDatabase = databaseTable.getSelectionModel().getSelectedItem();
            try {
                SetupKurwanner.dropDbFolder(selectedDatabase, false);
                XMLizer.deleteDB(selectedDatabase);
                databaseTable.getItems().remove(selectedDatabase);
            }
            catch (SetupException se){
                showConfiramationDeleteAlert(selectedDatabase, se.getMessage());
            }
        });

        testConnectionButton.setOnAction(e -> {
            Database testDB = new Database("testDB", databaseConnection.getText(), databaseUser.getText(), databasePass.getText(), false);
            showAlertTestConnection(Engine.checkConnection(testDB));
        });

        saveDBButton.setOnAction(e -> {
            Database newDB = new Database(databaseName.getText(), databaseConnection.getText(), databaseUser.getText(), databasePass.getText(), true);
            if (checkDBExistence(newDB)) {
                XMLizer.deleteDB(newDB);
                XMLizer.createDB(newDB);
                refreshDBTable();
                showAlertSaveDB(false);
            } else {
                XMLizer.createDB(newDB);
                SetupKurwanner.prepareFolder(newDB);
                refreshDBTable();
                showAlertSaveDB(true);
            }
        });
    }

    private void fillDatabaseData(Database database) {
        databaseName.setText(database.getName());
        databaseConnection.setText(database.getConnection());
        databaseUser.setText(database.getUsername());
        databasePass.setText(database.getPassword());
    }

    private void refreshDBTable() {
        databaseData.clear();
        for (Database db : XMLizer.getDBList()) {
            databaseData.add(db);
        }
        databaseTable.setItems(databaseData);
    }

    private void showAlertTestConnection(boolean testResult) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Проверка соединения с БД");
        alert.setHeaderText("Результат:");
        if (testResult) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Соединение успешно установлено!");
        } else {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка соединения с базой данных");
        }
        alert.showAndWait();
    }

    private boolean checkDBExistence(Database database) {
        for (Database db : databaseTable.getItems()) {
            if (db.equals(database))
                return true;
        }
        return false;
    }

    private void showAlertSaveDB(boolean result) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Создание записи о БД");
        alert.setHeaderText("Результат:");
        if (result) {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Запись успешно создана!");
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Данное имя уже используется! Перезаписана существующая запись");
        }
        alert.showAndWait();
    }

    private void showConfiramationDeleteAlert(Database db, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи " + db.getName());
        alert.setHeaderText(message);
        ButtonType okButton = new ButtonType("Да", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Нет", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                try {
                    SetupKurwanner.dropDbFolder(db,true);
                }
                catch (SetupException se){
                }
            }
        });
    }
}
