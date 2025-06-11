## README

# SkolarD

SkolarD is an all-in-one tutoring management system designed to streamline academic support for both students and tutors. Built as a desktop Java Swing application, it combines session booking, messaging, payment tracking, and post-session rating in a unified interface.

---

## 💡 Project Purpose

The goal of SkolarD is to provide a professional and seamless tutoring experience. It enables:

- **Students** to discover tutors by course, view tutor qualifications, book and unbook sessions, communicate with tutors, and rate sessions after completion.
- **Tutors** to create and manage available sessions, respond to messages, and monitor feedback.
- **Administrators** to later integrate analytics and oversee platform engagement (future scope).

This project was designed as part of a final-year university capstone and reflects real-world system design, usability principles, and code organization practices.

---

## 💎 Why SkolarD Is Valuable

- **Efficient Scheduling**: Students can easily search and book sessions by course, tutor rating, or availability.
- **Two-Way Communication**: Built-in messaging allows asynchronous coordination between tutors and students.
- **Feedback Loop**: Rating requests are automatically generated and tracked, ensuring quality and transparency.
- **Session Integrity**: Booked sessions can be managed with refund logic, and unbooked sessions automatically skip feedback collection.
- **User-Centric Design**: Simple GUI layouts tailored to the user's role (student or tutor).
- **Scalable Architecture**: Modular persistence and logic layers make future expansion (e.g., admin panel or reporting tools) possible.

---

## ✨ Features

- ✅ Session booking, unbooking, and creation
- ✅ Time and rating-based session filtering
- ✅ Per-course tutor ratings
- ✅ Tutor bios and qualifications
- ✅ Rating system with optional feedback
- ✅ Built-in messaging system between tutors and students
- ✅ Role-based access and GUI views
- ✅ SQLite database with seeding support

---

## 🧪 Technologies Used

- **Java 17**
- **Java Swing** for the GUI
- **SQLite** for persistence
- **JUnit & Mockito** for testing
- **Maven (optional)** for dependency management
- **Layered architecture** (Presentation, Logic, Persistence, Objects)

---

## 🚀 Getting Started

### ✅ Prerequisites
- Java 17+ installed
- SQLite CLI (optional for DB inspection)

### 🔧 Running the App

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/skolard.git
   cd skolard

2. **Compile the Source**
    ```bash
    javac -d out src/skolard/*.java src/skolard/**/*.java

3. **Run the Application**
    ```bash
    java -cp out skolard.App

## Vision Statement
https://code.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders/-/blob/dev/docs/VISION.md?ref_type=heads

## Architecture: 
https://code.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders/-/blob/dev/docs/architecture/ARCHITECTURE.md?ref_type=heads

## Coding Standards
https://code.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders/-/blob/main/docs/JavaCodingStandards.md?ref_type=heads

## Retrospectives
https://code.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders/-/tree/main/docs/retros?ref_type=heads

