package be.kdg.integratieproject2.bussiness.exceptions;

/**
 * Created by Tim on 01/03/2018.
 */
public class ObjectNotFoundException extends Exception {
    public ObjectNotFoundException(String id) {
        super("Object with id: "+ id +" either does not exist or user does not have access to it.");
    }

}
