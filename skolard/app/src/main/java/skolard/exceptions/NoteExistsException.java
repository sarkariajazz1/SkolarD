package skolard.exceptions;

/** Thrown when attempting to add or modify a note with invalid data. */
public class NoteExistsException extends RuntimeException {
    public NoteExistsException(String message) {
        super(message);
    }
}