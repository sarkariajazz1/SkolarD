package skolard.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GradeUtilTest {

    @Test
    void testLetterGrades() {
        assertEquals(90.0, GradeUtil.toNumeric("A+"));
        assertEquals(80.0, GradeUtil.toNumeric("a"));
        assertEquals(75.0, GradeUtil.toNumeric("B+"));
        assertEquals(70.0, GradeUtil.toNumeric("b"));
        assertEquals(65.0, GradeUtil.toNumeric("C+"));
        assertEquals(60.0, GradeUtil.toNumeric("C"));
        assertEquals(50.0, GradeUtil.toNumeric("d"));
        assertEquals(40.0, GradeUtil.toNumeric("F"));
    }

    @Test
    void testNumericStringGrade() {
        assertEquals(87.5, GradeUtil.toNumeric("87.5"));
    }

    @Test
    void testInvalidGradeString() {
        assertEquals(-1.0, GradeUtil.toNumeric("Pass"));
    }

    @Test
    void testNullInput() {
        assertEquals(-1.0, GradeUtil.toNumeric(null));
    }
}
