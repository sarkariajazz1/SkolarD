package skolard.persistence;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceFactoryTest {

    @Test
    public void testInitializeWithStub() {
        assertDoesNotThrow(() -> PersistenceFactory.initialize(PersistenceType.STUB, false));
    }

    @Test
    public void testInitializeWithTestDb() {
        assertDoesNotThrow(() -> PersistenceFactory.initialize(PersistenceType.TEST, false));
    }

    @Test
    public void testInitializeWithSeededTestDb() {
        assertDoesNotThrow(() -> PersistenceFactory.initialize(PersistenceType.TEST, true));
    }
}
