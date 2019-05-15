package model.exceptions;

public class InvalidProgressException extends IllegalArgumentException {

    public InvalidProgressException(String msg) {
        super(msg);
    }
}
