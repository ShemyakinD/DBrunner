package org.dcc360.Services;

import org.dcc360.MainApp;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public interface Loggator {
    Logger LOG = Logger.getLogger("CustomLogger");
    static void init() {
        try {
            LogManager.getLogManager().readConfiguration(MainApp.class.getResourceAsStream("/logging.properties"));
            FileHandler fh = new FileHandler(SetupRunner.getInstallDir() + "log.txt");
            fh.setEncoding("UTF-8");
//            fh.setFormatter(new SimpleFormatter());
            LOG.addHandler(fh);
        } catch (IOException ioe) {
            System.err.println("Could not setup logger configuration " + ioe.toString());
        }
    }

    static void execLog(Level lvl, String dbName, String logText) {
        LOG.log(lvl, "БД-" + dbName + ". " + logText);
    }

    static void commonLog(Level lvl, String logText) {
        LOG.log(lvl, logText);
    }
}
