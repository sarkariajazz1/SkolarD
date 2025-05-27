package skolard.exceptions;

/** Thrown when attempting to add or modify a note with invalid data. */
public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }
}