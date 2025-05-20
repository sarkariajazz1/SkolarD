package summernotes.exceptions;

/** Thrown when attempting to add or modify a note with invalid data. */
public class InvalidNoteException extends RuntimeException {
    public InvalidNoteException(String message) {
        super(message);
    }
}