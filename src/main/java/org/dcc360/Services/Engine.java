package org.dcc360.Services;

import org.dcc360.Database;
import org.dcc360.XMLizer;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Engine {
    public static ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private static ExecutorService dbService = Executors.newCachedThreadPool();
    private static Set<Database> processingDB = new HashSet<>();

    public static void startEngine(){
        service.scheduleAtFixedRate(() -> {
            CheckRun();
        }, 0L, 1, TimeUnit.MINUTES);
    }

    private static void CheckRun() {
        List<Database> databasesList = XMLizer.getDBList();
        for (Database db: databasesList) {
            if (db.getFolder().listFiles((file) -> file.getName().endsWith(".sql")).length > 0) {
                if (!processingDB.contains(db) && db.getActive()){
                    //runEngine(db);
                    processingDB.add(db);
                    dbService.submit(() -> runEngine(db));
                }
            }
        }
    }

    public static boolean checkConnection(Database db){
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + db.getConnection(), db.getUsername(), db.getPassword())) {
            return true;
        }
        catch (SQLException sqle){
            return false;
        }
    }

    private static void runEngine(Database db) {
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + db.getConnection(), db.getUsername(), db.getPassword())) {
            File[] sqlFiles = db.getFolder().listFiles((file) ->
                    file.isFile() && file.getName().endsWith(".sql")
            );
            for (File sqlFile : sqlFiles) {
                Loggator.execLog(Level.INFO, db.getName(), "Начата обработка файла " + sqlFile.getName());
                runSQL(conn, sqlFile, db);
            }
        } catch (SQLException sqle) {
            Loggator.execLog(Level.WARNING, db.getName(), sqle.getMessage());
            sqle.printStackTrace();
            processingDB.remove(db);
        }
    }

    private static void runSQL(Connection connection, File runFile, Database db) {
        try {
            String scriptText = readSQLfile(runFile.getAbsolutePath());
            CallableStatement stmt = connection.prepareCall(scriptText);
            Scanner s = new Scanner(scriptText);
            s.useDelimiter("(?i)(;((\r)?\n)/((\r)?\n))|(/(\r)?\nexit;)|(;\n*$)");

            while(s.hasNext()) {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/")) {
                    line = line.substring(line.indexOf(32) + 1, line.length() - "*/".length());
                }

                if (line.trim().length() > 0 && !line.toLowerCase().startsWith("exit;".toLowerCase())) {
                    Pattern pat = Pattern.compile("end\\s*\\w*$");
                    Matcher matcher = pat.matcher(line);
                    if (matcher.find()) {
                        stmt.execute(line + ";");
                    } else {
                        stmt.execute(line);
                    }
                }
            }

            stmt.close();

            Loggator.execLog(Level.INFO, db.getName(), "Файл " + runFile.getName() + " успешно обработан.");
            runFile.renameTo(new File(db.getFolder() + "\\Suckess\\" + runFile.getName()));
            runFile.delete();
        }
        catch (SQLException sqle) {
            if (sqle.getErrorCode() != 4021) {
//                Loggator.writeLogs("E", db.getName(), runFile.getName() + "\n\t" + sqle.getMessage());
                Loggator.execLog(Level.WARNING, db.getName(), runFile.getName() + "\n\t" + sqle.getMessage());
                runFile.renameTo(new File(db.getFolder() + "\\Gavnino\\" + runFile.getName()));
                runFile.delete();
            } else {
                Loggator.execLog(Level.WARNING, db.getName(), "Файл " + runFile.getName() + " содержит объект, который сейчас заблокирован.\n\t" + sqle.getMessage());
            }
        }
        finally {
            processingDB.remove(db);
        }
    }

    private static String readSQLfile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try(Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach((s) -> contentBuilder.append(s).append("\n"));
        } catch (Exception streamException) {
            streamException.printStackTrace();
        }
        return contentBuilder.toString();
    }

}
