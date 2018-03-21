package be.kdg.integratieproject2.bussiness.exceptions;

import java.util.Date;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String username) {
        super("This token is not valid for user: " + username);
    }

    public InvalidTokenException(Date date)
    {
        super("This token expired on: " +  date.toString());
    }
}
