package org.dcc360.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.dcc360.Services.Loggator;
import org.dcc360.Services.SetupRunner;

import java.io.File;
import java.nio.file.*;
import java.util.logging.Level;

public class FilesTabPageController {

    @FXML
    private TreeView<String> filesTreeView;

    @FXML
    private Button refreshFilesList;

    public void initialize() {
        filesTreeView.setShowRoot(false);
        setFilesDBTree();

        new Thread(() -> setupWathcer()).start();

        refreshFilesList.setOnAction(e -> {
            setFilesDBTree();
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
                root.getChildren().add(new TreeItem(f.getName()));
            }
        }
        return root;
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
