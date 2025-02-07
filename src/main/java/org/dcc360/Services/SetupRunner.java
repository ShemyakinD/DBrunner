//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360.Services;

import org.dcc360.Entities.Database;
import org.dcc360.Entities.SetupException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

public class SetupRunner {

    public static Properties getSetupProperties() {
        Properties properties = new Properties();

        try (InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("setup.properties")) {
            properties.load(io);
            for (Object propetry : properties.keySet())
            properties.put(propetry, properties.getProperty(propetry.toString()).replace("{user.home}", System.getProperty("user.home")));
        } catch (IOException ioe) {
            System.out.println("Ошибка считывания свойств " + ioe.getMessage());
            return null;
        }
        return properties;
    }

    public static String getInstallDir() {
        return getSetupProperties().getProperty("app.setup_path");
    }

    public static String geDBDir() {
        return getInstallDir() + "Databases.xml";
    }

    public static void InstallKurwanner() {
        for (Database db : XMLizer.getDBList()){
            prepareFolder(db);
        }
    }

    public static void prepareFolder(Database db) {
        (new File(db.getFolder() + "\\Success")).mkdirs();
        (new File(db.getFolder() + "\\Fail")).mkdirs();
    }

    public static void dropDbFolder(Database db, boolean force) throws SetupException {
        if (checkDbFolderContent(db.getFolder()) || force){
                deleteFolder(db.getFolder());
            Loggator.commonLog(Level.INFO,"Каталог " + db.getFolder().getAbsolutePath() + " успешно удалён");
        }
        else throw new SetupException("Каталог БД {"+ db.getName() +"} содержит внутри себя файлы.\nВы уверены, что хотите удалить каталог?", Level.WARNING);
    }

    private static boolean checkDbFolderContent(File folder){
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                 if (!file.isDirectory() || !checkDbFolderContent(file))
                    return false;
            }
        }
        return true;
    }

    private static void deleteFolder(File folder) throws SetupException{
        if (folder.isDirectory()){
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        if (!folder.delete())
            throw new SetupException("Ошибка удаления каталога: " + folder.getAbsolutePath() + "\n");
    }

}
