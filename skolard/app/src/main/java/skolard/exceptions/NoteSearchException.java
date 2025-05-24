package skolard.exceptions;

/** Thrown when a search operation does not find any matching notes. */
public class NoteSearchException extends RuntimeException {
    public NoteSearchException(String message) {
        super(message);
    }
}
