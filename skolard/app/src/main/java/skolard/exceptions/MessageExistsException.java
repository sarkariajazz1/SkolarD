package skolard.exceptions;

/** Thrown when attempting to add or modify a note with invalid data. */
public class MessageExistsException extends RuntimeException {
    public MessageExistsException(String message) {
        super(message);
    }
}