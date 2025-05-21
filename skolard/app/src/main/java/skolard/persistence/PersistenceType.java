package skolard.persistence;

/**
 * Enum to specify the type of persistence layer being used.
 * STUB = in-memory data (default for Iteration 1)
 * TEST = temporary database used for unit/integration testing
 * PROD = full production database (future)
 */
public enum PersistenceType {
    STUB, TEST, PROD
}
