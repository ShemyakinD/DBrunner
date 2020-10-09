//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.dcc360.Services;

import org.dcc360.MainApp;
import org.dcc360.SetupKurwanner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Loggator {
    static final Logger LOG = Logger.getLogger(MainApp.class.getName());
    private static List<String> swearList = Arrays.asList("Кажется ты опять обосрался, кожанный ублюдок!", "Блять, да сколько уже можно!?", "Что же ты творишь, денегеративная личинка?", "Ну ты и дибил!", "Опять за тобой прибираться, кожанный мудозвон.");
    private static List<String> congratList = Arrays.asList("Ты молодчинка! <3", "Ты такой клёвый!", "Чувак, это было просто божественно!", "Лукас этому сударю.", "Пресвятые ебобошеньки, ну ты и царь!", "Ты заставил меня уважать тебя.", "Видимо ты знатно напряг своего Саурона.");
    private static List<String> delayList = Arrays.asList("Сцуко!", "Сорян, босс, у меня не получилось.", "Не присунул!", "Впендюринг failed!", "В этот раз тебе повезло, обосрался Я.", "Блядь! Она мне не дала!", "Не судьба, чел.");

    public Loggator() {
    }

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

//                LOG.log(Level.WARNING, "БД-" + dbName + ". " + getSwear(1) + "\n\t" + logText);
                LOG.log(lvl, "БД-" + dbName + ". " + logText);

    }

    public static void commonLog(Level lvl, String logText) {
        if (LOG.getHandlers().length == 0)
            init();
        LOG.log(lvl, logText);
    }

    private static String getSwear(int mode) {
        Random random = new Random();
        if (mode == 1) {
            return swearList.get(random.nextInt(swearList.size()));
        } else if (mode == 2) {
            return congratList.get(random.nextInt(congratList.size()));
        } else {
            return mode == 3 ? delayList.get(random.nextInt(delayList.size())) : null;
        }
    }
}
