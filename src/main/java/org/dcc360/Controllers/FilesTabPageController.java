package org.dcc360.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.dcc360.Entities.MyAlert;
import org.dcc360.Services.Loggator;
import org.dcc360.Services.SetupRunner;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

public class FilesTabPageController {

    @FXML
    private TreeView<String> filesTreeView;

    @FXML
    private Button refreshFilesListButton;

    @FXML
    private Button openDbFileButton;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public void initialize() {
        filesTreeView.setShowRoot(false);
        setFilesDBTree();

        new Thread(() -> setupWathcer()).start();

        refreshFilesListButton.setOnAction(e -> {
            setFilesDBTree();
        });

        openDbFileButton.setOnAction(e -> {
            try {
                Desktop.getDesktop().open(new File(getSelectedFilePath(filesTreeView.getSelectionModel().getSelectedItem())));
            }
            catch (IOException ioe){
                Loggator.commonLog(Level.WARNING,"Ошибка открытия файла " + filesTreeView.getSelectionModel().getSelectedItem().getValue());
                MyAlert.showMyAlert(Alert.AlertType.ERROR,"Ошибка открытия файла!","Не получилось открыть файл!",null);
            }
            catch (NullPointerException npe){
                try {
                    Desktop.getDesktop().open(new File(SetupRunner.getInstallDir()));
                }
                catch (IOException ioe){
                    Loggator.commonLog(Level.WARNING,"Ошибка открытия каталога " + SetupRunner.getInstallDir());
                    MyAlert.showMyAlert(Alert.AlertType.ERROR,"Ошибка открытия корневого каталога!","Не получилось открыть корневой каталог!",null);
                }
//                MyAlert.showMyAlert(Alert.AlertType.ERROR,"Ошибка открытия файла!","Не получилось открыть файл!\nПроверьте, что он выбран в списке.",null);
            }
        });
    }

    public void setFilesDBTree() {
        filesTreeView.setRoot(getDBDirectories(new File(SetupRunner.getInstallDir())));
    }

    private static TreeItem<String> getDBDirectories(File rootDirectory) {
        TreeItem<String> root = new TreeItem(rootDirectory.getName());
        for (File dir : rootDirectory.listFiles()) {
            if (dir.isDirectory()) {
                root.getChildren().add(getFilesForDirectory(dir));
            }
        }
        return root;
    }

    private static TreeItem<String> getFilesForDirectory(File directory) {
        TreeItem<String> root = new TreeItem(directory.getName());
        for (File f : directory.listFiles()) {
            if (f.isDirectory()) {
                root.getChildren().add(getFilesForDirectory(f));
            } else {
                root.getChildren().add(new TreeItem(f.getName() + "\t\t" + dateFormat.format(f.lastModified())));
            }
        }
        return root;
    }

    private String getSelectedFilePath(TreeItem selectedItem){
        StringBuilder resultPath = new StringBuilder();
        for( TreeItem ti = selectedItem; ti.getParent() != null; ti = ti.getParent()){
            resultPath.insert(0,"\\"+ti.getValue().toString().split("\t\t\\d*")[0]);
        }
        return SetupRunner.getInstallDir() + resultPath.deleteCharAt(0).toString();
    }

    public void setupWathcer() {
        try {
            WatchService watchService
                    = FileSystems.getDefault().newWatchService();

            Path path = Paths.get(SetupRunner.getInstallDir());

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Platform.runLater(() -> setFilesDBTree());
                }
                key.reset();
            }
        } catch (Exception e) {
            Loggator.commonLog(Level.WARNING, "Ошибка наблюдателя каталогов! Пора чинить.\n" + e.getMessage());
        }
    }
}
