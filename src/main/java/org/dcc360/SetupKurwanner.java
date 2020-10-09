//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SetupKurwanner {

    public static Properties getSetupProperties() {
        Properties properties = new Properties();

//        try (InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("")) {
        try (InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("setup.properties")) {
            properties.load(io);
            for (Object propetry : properties.keySet())
            properties.put(propetry, properties.getProperty(propetry.toString()).replace("{user.home}", System.getProperty("user.home")));
        } catch (IOException ioe) {
            System.out.println("Error initialize project properties " + ioe.getMessage());
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
            (new File(db.getFolder() + "\\Suckess")).mkdirs();
            (new File(db.getFolder() + "\\Gavnino")).mkdirs();
        }

    }
}
