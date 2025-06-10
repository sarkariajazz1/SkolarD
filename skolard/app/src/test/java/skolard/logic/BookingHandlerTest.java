package skolard.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import skolard.logic.booking.BookingHandler;
import skolard.persistence.PersistenceFactory;
import skolard.persistence.PersistenceType;
import skolard.persistence.SessionPersistence;

public class BookingHandlerTest {
    private BookingHandler handler;
    private SessionPersistence sp;


    @Before
    public void setup(){
        sp = mock(SessionPersistence.class);
        handler = new BookingHandler(sp);

    }

    @Test
    public void testGetAvailableSessions_TimeComparator(){

    }
}
