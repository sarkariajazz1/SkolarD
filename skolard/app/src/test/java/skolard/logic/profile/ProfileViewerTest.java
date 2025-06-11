package skolard.logic.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProfileViewerTest {

    private SessionHandler mockSessionHandler;
    private ProfileFormatter mockFormatter;
    private ProfileViewer viewer;

    @BeforeEach
    void setup() {
        mockSessionHandler = mock(SessionHandler.class);
        mockFormatter = mock(ProfileFormatter.class);
        viewer = new ProfileViewer(mockFormatter, mockSessionHandler);
    }

    @Test
    void testViewBasicProfile_NullReturnsEmpty() {
        String result = viewer.viewBasicProfile(null);
        assertEquals("", result);
    }

    @Test
    void testViewBasicProfile_Valid() {
        Student s = new Student("S", "s@skolard.ca", "pass");
        when(mockFormatter.basic(s)).thenReturn("OK");
        assertEquals("OK", viewer.viewBasicProfile(s));
    }

    @Test
    void testViewFullProfile_Student() {
        Student s = new Student("S", "s@skolard.ca", "pass");
        when(mockFormatter.full(s)).thenReturn("S");
        viewer.viewFullProfile(s);
        verify(mockSessionHandler).setStudentSessionLists(s);
    }

    @Test
    void testViewFullProfile_Tutor() {
        Tutor t = new Tutor("T", "t@skolard.ca", "pass", "Bio", null);
        when(mockFormatter.full(t)).thenReturn("T");
        viewer.viewFullProfile(t);
        verify(mockSessionHandler).setTutorSessionLists(t);
    }
}
