package org.dcc360;

import org.dcc360.Services.Loggator;

import java.util.logging.Level;

public class SetupException extends Exception {
    SetupException(String errorMessage){
        super(errorMessage);
        Loggator.commonLog(Level.SEVERE,errorMessage);
    }
}
