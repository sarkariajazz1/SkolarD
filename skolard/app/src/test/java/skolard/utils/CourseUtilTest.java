package skolard.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseUtilTest {

    @Test
    void testNormalizeCourseCode() {
        assertEquals("COMP1010", CourseUtil.normalizeCourseCode(" comp 1010 "));
        assertEquals("MATH1234", CourseUtil.normalizeCourseCode("math1234"));
        assertNull(CourseUtil.normalizeCourseCode(null));
    }

    @Test
    void testIsValidCourseCode() {
        assertTrue(CourseUtil.isValidCourseCode("COMP1010"));
        assertTrue(CourseUtil.isValidCourseCode(" comp 1010 "));
        assertFalse(CourseUtil.isValidCourseCode("math12"));
        assertFalse(CourseUtil.isValidCourseCode(null));
    }

    @Test
    void testAreSameCourse() {
        assertTrue(CourseUtil.areSameCourse(" comp1010 ", "COMP1010"));
        assertFalse(CourseUtil.areSameCourse("MATH1234", "COMP1234"));
    }
}
