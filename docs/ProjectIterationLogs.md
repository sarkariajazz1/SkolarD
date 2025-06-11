# Project Iteration Log
This document tracks all changes and progress across project iterations.

## Iteration 0 – Updates
- Converted the Tutoring Session Confirmation feature into a user story and linked it to the Booking feature.
- Updated the Real-time Search feature to Student Preference Matching, restructured it as a user story, and linked it to Student Tutor Matching.
- Transformed the FAQ feature into a user story and linked it to the Help and Support feature.
- Implemented descriptive user story issue titles and ensured all user stories are properly documented with complete descriptions.

## Iteration 1 – Updates
- Closed the Student and Tutor Profile feature issue, including all associated user stories.
- Closed the Student Tutor Matching feature issue, including all associated user stories.
- Moved the Booking feature issue, along with its user stories, to Iteration 2.

# Iteration 1 Feedback Implementation Summary

This document outlines the key changes made based on feedback provided during Iteration 1.

📎 **Feedback Source**: [i1 Feedback Spreadsheet](i1 Feedback.xlsx)

---

## Summary of Changes

### 1. Logic Refactoring

- Eliminated unused or template code remnants from imported project structure.

- Ensured objects were only deleted if they existed and added validations for duplication.

- Removed mutable returns in `getItem()` and protected internal lists using unmodifiable views.

- Extracted abstract responsibilities into proper abstract classes where applicable.

- Introduced clear subclass responsibilities for sorting logic with improved `PriorityList` hierarchy.


### 2. Improved Sorting Strategy

- Replaced manual sort logic (e.g., bubble sort) with `Collections.sort()` and comparators.

- Ensured all sortable objects implement consistent comparator logic.


### 3. Object Model Adjustments

- Refactored class hierarchies for `Tutor`, `Student`, and `User` to clarify responsibility boundaries.

- Avoided tight coupling and enhanced modularity in profile and session management.


### 4. Persistence Enhancements

- Removed redundant or shallow database references.

- Standardized ID and email uniqueness validations.

- Ensured accurate handling of optional fields (e.g., `OptionalDouble`).


### 5. UI and Miscellaneous Fixes

- Reduced hardcoded strings by introducing a centralized config file.

- Improved message feedback and error propagation.

---

These changes were implemented collaboratively and tested to ensure alignment with the project objectives and code quality guidelines.
