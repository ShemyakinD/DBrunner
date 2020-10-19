package org.dcc360.Services;

import java.util.logging.Level;

public class SetupException extends Exception {
    SetupException(String errorMessage){
        super(errorMessage);
        Loggator.commonLog(Level.SEVERE,errorMessage);
    }
    SetupException(String errorMessage, Level level){
        super(errorMessage);
        Loggator.commonLog(level,errorMessage);
    }
}
