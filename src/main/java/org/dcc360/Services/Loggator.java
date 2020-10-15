//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360.Services;

import org.dcc360.MainApp;
import org.dcc360.SetupKurwanner;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Loggator {
    static final Logger LOG = Logger.getLogger(MainApp.class.getName());

    private static void init() {
        try {
            LogManager.getLogManager().readConfiguration(MainApp.class.getResourceAsStream("/logging.properties"));
            FileHandler fh = new FileHandler(SetupKurwanner.getInstallDir() + "log.txt");
            fh.setEncoding("UTF-8");
//            fh.setFormatter(new SimpleFormatter());
            LOG.addHandler(fh);
        } catch (IOException ioe) {
            System.err.println("Could not setup logger configuration " + ioe.toString());
        }

    }

    public static void execLog(Level lvl, String dbName, String logText) {
        if (LOG.getHandlers().length == 0)
            init();
        LOG.log(lvl, "БД-" + dbName + ". " + logText);

    }

    public static void commonLog(Level lvl, String logText) {
        if (LOG.getHandlers().length == 0)
            init();
        LOG.log(lvl, logText);
    }

}
