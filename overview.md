# SkolarD

**Group Name:** Decoders

## Group Members

- **Joshua Chuwang-Kwa**  
  Email: `chuwangj@myumanitoba.ca`

- **Matthew Yablonski**  
  Email: `yablonsm@myumanitoba.ca`

- **John Aguinaldo**  
  Email: `aguinal1@myumanitoba.ca`

- **Jaskaran Sarkaria**  
  Email: `sarkari2@myumanitoba.ca`

- **Caibaitong(Anton) Wang**  
  Email: `antonwcbt@gmail.com`  
  Alternate Email: `wangc16@myumanitoba.ca`

---

## Project Overview

**SkolarD** is a tutoring management system designed to streamline academic support for both students and tutors. Built as a modular Java Swing desktop application, SkolarD provides a unified interface for scheduling, messaging, rating, and managing tutoring sessions.

The system follows a **three-tier architecture**, cleanly separating:

- **Presentation Layer** – GUI views built with Java Swing
- **Logic Layer** – Handlers and utilities responsible for core behavior
- **Persistence Layer** – SQLite-backed or in-memory stub data storage

This architectural choice supports long-term **maintainability**, **testability**, and **extensibility**.


---

## Project Purpose

SkolarD was created as a final-year capstone project to simulate real-world software engineering practices in designing, implementing, and maintaining a complete desktop application. It aims to:

- Allow **students** to search for tutors, book/unbook sessions, communicate with tutors, and rate them after each session.
- Enable **tutors** to post availability, manage sessions, respond to messages, and receive feedback.
- Support future **administrator features** such as analytics, reports, and user moderation.

---

## Key Features

- Session booking and unbooking with time and rating-based filtering
- Tutor profiles including bios, qualifications, and course expertise
- Post-session rating system with support for per-course tutor ratings
- Messaging system between students and tutors
- SQLite database integration with test and production environments
- Stub support for logic and persistence testing using Java collections
- Clean role-based GUI layouts for students and tutors

---

## Dependencies and Versions

- Java 21  
- Java Swing (GUI)  
- SQLite JDBC: `org.xerial:sqlite-jdbc:3.36.0.3`  
- JUnit 4 for unit and integration testing  
- Gradle (optional, supported for builds and testing)  
- GitLab for version control and documentation  

---

## Setting Up and Running

### Prerequisites

- Java 21 installed on your machine
- SQLite CLI (optional, for direct database access)

### Compiling and Running

#### Using CLI

1. Clone the repository:
   ```bash
   git clone https://gitlab.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders.git
   cd a01-g06-decoders
   ```

2. Compile the source files:
   ```bash
   javac -d out src/skolard/*.java src/skolard/**/*.java
   ```

3. Run the application:
   ```bash
   java -cp out skolard.App
   ```

#### Using Gradle (optional)

To run the application using Gradle:

```bash
./gradlew run
```

---

## Database Management

SkolarD uses a dual-mode persistence design:

- `skolard.db`: the main production SQLite database
- `test.db`: used exclusively for integration testing
- **Stub mode**: simulated database using in-memory Java HashMaps (enabled via `StubFactory`)

The schema is auto-generated on application start. SQL seed files are provided in the `resources/` directory.

### Resetting the Database

#### Delete the database file

On macOS/Linux:
```bash
rm path/to/skolard.db
```

On Windows:
```cmd
del path .\skolard.db
```

#### Reset test database

```bash
rm path/to/test.db
```

Both databases will be re-initialized on the next application run.

---

## Architecture

SkolarD is designed using a strict 3-tier architecture:

1. **Presentation Layer**: Java Swing GUI components organized by features (e.g., `rating/`, `booking/`, `profile/`)
2. **Logic Layer**: Handles business logic, validation, and coordination between UI and persistence
3. **Persistence Layer**: Abstract interfaces with multiple implementations (SQLite and stub)

This structure ensures modularity, low coupling, and high cohesion across components.

---

## Design Patterns Used

The following design patterns were implemented throughout the SkolarD codebase, following principles from *Design Patterns: Elements of Reusable Object-Oriented Software* (Gang of Four):

- **Factory**: Used in `PersistenceFactory` and `HandlerFactory` to encapsulate creation logic of stub vs. SQLite-backed classes
- **Builder**: Used to create complex objects like `Profile`, `Session`, and `SupportTicket` with optional fields
- **Strategy**: Comparators like `GradeComparator`, `TimeComparator`, and `TutorComparator` apply different sorting strategies in the booking logic
- **Observer**: Rating submission indirectly triggers state updates reflected in both student and tutor profiles
- **Singleton**: `ConnectionManager` ensures only one database connection is open at a time
- **Template Method**: Shared workflows across handlers and views (e.g., `ProfileHandler` and `RatingHandler`) with specific steps implemented by subclasses
- **Adapter** (implicit): Stubs mimic the interface of real persistence implementations for seamless interchangeability during testing

---

## Additional Documentation

- [Vision Statement](https://code.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders/-/blob/main/docs/VISION.md?ref_type=heads)
- [Architecture Overview](https://code.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders/-/blob/main/docs/architecture/ARCHITECTURE.md?ref_type=heads)
- [Java Coding Standards](https://code.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders/-/blob/main/docs/JavaCodingStandards.md?ref_type=heads)
- [Team Retrospectives](https://code.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders/-/blob/main/docs/retros/RETROSPECTIVE.md?ref_type=heads)
- [Known Issues](https://code.cs.umanitoba.ca/comp3350-summer2025/a01-g06-decoders/-/blob/main/docs/KnownIssues.md?ref_type=heads)

---

## Summary

SkolarD provides a robust and extensible foundation for tutoring services, emphasizing modular design, user experience, and architectural best practices. Designed and developed by the **Decoders** team, it demonstrates real-world engineering principles including testable code, design patterns, and maintainable layering across functional domains.
