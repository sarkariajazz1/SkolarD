
# Technical Debt Note: RatingList Implementation

## Current Simplification

Currently, the **`RatingList`** class sorts sessions based on the **tutor's overall course grade** for simplicity. This is a temporary approach and does not fully capture the intended rating logic for the application.

## Intended Future Logic

In the final version, the **`RatingList`** should sort sessions based on the **average rating** each session received from students who took that specific course, rather than just the tutor's self-reported grades.

## Current Implementation Strategy

- **Temporary Approach:** Using the **`getGradeForCourse()`** method from the **`Tutor`** class to fetch the tutor's overall grade for the specified course.
- **Planned Upgrade:** Migrate to a system where each **`Session`** stores individual ratings, and the average is calculated dynamically based on real student feedback.

## Next Steps

- Extend the **`Session`** class to store individual student ratings.
- Add methods to **`Student`** for submitting session ratings.
- Update the **`RatingList`** class to consider these ratings instead of the static tutor grades.
- Consider adding weighting to ratings based on student experience or course difficulty.

---

### Example Future Method Signature

```java
// Future method for sorting sessions by average student ratings
public List<Session> sortByAverageStudentRating(String course) {
    // Placeholder logic for future implementation
}
```

---

**Author:** Anton Wang  
**Date:** 2025-05-18
