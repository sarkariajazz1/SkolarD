package skolard.exceptions;

/** Thrown when an operation tries to find a note that does not exist. */
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(String message) {
        super(message);
    }
    
}
