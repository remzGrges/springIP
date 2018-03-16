package be.kdg.integratieproject2.bussiness.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Tim on 01/03/2018.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends Exception {
    public ObjectNotFoundException(String id) {
        super("Object with id: "+ id +" either does not exist or user does not have access to it.");
    }
}
