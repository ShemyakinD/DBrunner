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
import org.dcc360.Services.SetupException;
import org.dcc360.Services.SetupRunner;
import org.dcc360.Services.XMLizer;

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
                SetupRunner.dropDbFolder(selectedDatabase, false);
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
                SetupRunner.prepareFolder(newDB);
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

    private boolean checkDBExistence(Database database) {
        for (Database db : databaseTable.getItems()) {
            if (db.equals(database))
                return true;
        }
        return false;
    }

    private void showAlertTestConnection(boolean testResult) {
        if (testResult) {
            getDBViewAlert(Alert.AlertType.INFORMATION,"Проверка соединения с БД","Результат:","Соединение успешно установлено!").showAndWait();
        } else {
            getDBViewAlert(Alert.AlertType.ERROR,"Проверка соединения с БД","Результат:","Ошибка соединения с базой данных").showAndWait();
        }
    }

    private void showAlertSaveDB(boolean result) {
        if (result) {
            getDBViewAlert(Alert.AlertType.INFORMATION,"Создание записи о БД","Результат:","Запись успешно создана!").showAndWait();
        } else {
            getDBViewAlert(Alert.AlertType.WARNING,"Создание записи о БД","Результат:","Данное имя уже используется! Перезаписана существующая запись").showAndWait();
        }
    }

    private void showConfiramationDeleteAlert(Database db, String message){
        Alert alert = getDBViewAlert(Alert.AlertType.CONFIRMATION,"Удаление записи " + db.getName(),message,null);
        ButtonType okButton = new ButtonType("Да", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Нет", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type.getText() == "Да") {
                try {
                    SetupRunner.dropDbFolder(db,true);
                    XMLizer.deleteDB(db);
                    databaseTable.getItems().remove(db);
                }
                catch (SetupException se){
                }
            }
        });
    }

    private Alert getDBViewAlert(Alert.AlertType type, String title, String header, String content){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }
}
