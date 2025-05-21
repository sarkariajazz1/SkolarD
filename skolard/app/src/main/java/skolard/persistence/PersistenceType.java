package skolard.persistence;

/**
 * Enumeration of supported persistence types in the SkolarD system.
 * This allows the application to dynamically select the desired
 * data storage backend (e.g., stubbed memory, test data, or production database).
 *
 * This enum is used by {@link PersistenceFactory} to determine which
 * implementation of the persistence interfaces to instantiate.
 */
public enum PersistenceType {
    
    /** 
     * In-memory stub persistence for development and unit testing.
     * No permanent data storage; resets between runs.
     */
    STUB,

    /**
     * Placeholder for a controlled test environment.
     * May use temporary files or mock database instances.
     * (Not yet implemented.)
     */
    TEST,

    /**
     * Production mode for persistent storage (e.g., database).
     * Intended for deployment environments.
     * (Not yet implemented.)
     */
    PROD
}





