package skolard.exceptions;

/** Thrown when attempting to add or modify a note with invalid data. */
public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}