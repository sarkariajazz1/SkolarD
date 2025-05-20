
# Technical Debt Note: RatingList Implementation

## Current Simplification

Currently, the **`RatingList`** class sorts sessions based on the **tutor's overall course grade** for simplicity due to the lack of rating system. This is a temporary approach and does not fully capture the intended rating logic for the application.

## Intended Future Logic

In the final version, the **`RatingList`** should sort tutors based on the **average rating** of teaching the specified course given by students. This should not consider sessions since it is specifically meant to show students the highest rated tutors first that may or may not have available sessions within a student's preferred time range.

## Current Implementation Strategy

- **Temporary Approach:** Using the **`getGradeForCourse()`** method from the **`Tutor`** class to fetch the tutor's overall grade for the specified course. This should be a String that is parseable as a double (e.g 1.0,2.5,4.5) to help make it easier to integrate future plans. Any String value that is not parseable is by default 1.0.
- **Planned Upgrade:** Migrate to a system where each **`Tutors`** stores individual ratings, and the average is calculated dynamically based on real student feedback.

## Next Steps

- Implement the rating system feature.
- Extend the **`Tutor`** class to store individual student ratings.
- Add methods to **`Student`** for submitting session ratings.
- Update the **`RatingList`** class to consider these ratings instead of the static tutor grades.
- Consider adding weight to ratings based on student experience or course difficulty.

---

### Example Future Method Signature

```java
// Future method for sorting sessions by average student ratings
public List<Tutor> sortByAverageStudentRating(String course) {
    // Placeholder logic for future implementation
}
```

---

**Author:** Anton Wang, John Aguinaldo
**Date:** 2025-05-20
