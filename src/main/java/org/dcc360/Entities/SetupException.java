package org.dcc360.Entities;

import org.dcc360.Services.Loggator;

import java.util.logging.Level;

public class SetupException extends Exception {
    public SetupException(String errorMessage){
        super(errorMessage);
        Loggator.commonLog(Level.SEVERE,errorMessage);
    }
    public SetupException(String errorMessage, Level level){
        super(errorMessage);
        Loggator.commonLog(level,errorMessage);
    }
}
