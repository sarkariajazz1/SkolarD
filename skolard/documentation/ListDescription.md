# Priority List Hierarchy
## **PriorityList.java**
**Description:**  
The base class for managing a list of generic items. It provides basic list operations and can be extended by more specialized list types like **RatingList**, **TimeList**, and **TutorList**.
### **Key Methods:**
- **`addItem(T item)`** - Adds an item to the list.
- **`removeItem(T item)`** - Removes an item from the list.
- **`getAllItems()`** - Returns an unmodifiable view of the entire list.
- **`clear()`** - Clears the entire list.
---
## **RatingList.java**
**Description:**  
Manages a list of **Session** objects, sorted by the rating of the tutors for a specific course. Useful for presenting the highest-rated tutors first.
### **Key Methods:**
- **`getSessionsByRating(String courseName)`** - Returns sessions sorted by the average rating of the tutors for a specific course.
---
## **TimeList.java**
**Description:**  
Manages a list of **Session** objects, sorted by their proximity to a given target time. Useful for finding the closest available sessions.
### **Key Methods:**
- **`getSessionsNearTime(LocalDateTime targetTime)`** - Returns sessions sorted by proximity to a target time.
---
## **TutorList.java**
**Description:**  
Manages a list of **Session** objects, sorted by the overall average ratings of the tutors. This view focuses on tutors rather than specific sessions.
### **Key Methods:**
- **`getSessionsByTutor(String courseName)`** - Returns sessions for a specific course, sorted by the overall average rating of the tutors.
- **`getSessionsForCourse(String courseName)`** - Returns sessions for a specific course without additional sorting.