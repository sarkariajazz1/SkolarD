# Java Project Coding Standards

## 1. File & Project Structure
- **Naming conventions for files and folders:** Use clear, descriptive names.
- **Package organization:** Group files into logical packages (e.g., `ui`, `logic`, `model`, `persistence`).
- **Test files vs. source files:** Place test files in a separate directory, typically `src/test/java` for unit tests and `src/main/java` for production code.

## 2. Naming Conventions
- **Class names:** Use PascalCase (e.g., `UserProfile`, `GameEngine`).
- **Variable and method names:** Use camelCase (e.g., `userScore`, `calculateTotal`).
- **Constants:** Use ALL_CAPS_WITH_UNDERSCORES (e.g., `MAX_PLAYER_COUNT`, `DEFAULT_TIMEOUT`).
- **Clear naming vs. abbreviations:** Avoid abbreviations unless they are universally understood (e.g., `URL`, `ID`).

## 3. Formatting & Style
- **Indentation rules:** Use 4 spaces per indentation level, avoid tabs.
- **Line length limits:** 100-120 characters per line for readability.
- **Braces:** Use the same line for opening braces (K&R style), e.g., `public void method() {`.
- **Blank lines for readability:** Use blank lines between methods and before return statements.

## 4. Commenting Practices
- **Required class/method headers:** Include Javadoc comments for all public classes and methods.
- **Inline comments:** Use sparingly for complex logic, prefer self-explanatory code.
- **TODO and FIXME:** Use these tags for work-in-progress or known issues.

## 5. Code Organization
- **Order of class members:** 
  - Constants
  - Fields
  - Constructors
  - Public methods
  - Private methods
- **Static vs. instance variable placement:** Group static variables together, followed by instance variables.
- **Visibility:** Avoid public variables, use private with getters/setters as needed.

## 6. Error Handling
- **try-catch blocks:** Use only where meaningful recovery is possible.
- **Exceptions:** Throw specific exceptions, catch broader ones only when necessary.
- **Custom exceptions:** Use if the application has domain-specific error cases.

## 7. Version Control Practices
- **Branch naming conventions:** Use descriptive names like `feature/login`, `bugfix/typo`.
- **Commit message format:** Use the form `[Category] Short summary` (e.g., `[Feature] Add user authentication`).
- **Frequency of pushes:** Push small, atomic changes frequently.
- **Code review:** Expect reviews for all non-trivial changes.

## 8. Testing Standards
- **Unit test coverage:** Aim for at least 80% coverage.
- **Naming tests:** Use descriptive names (e.g., `testCalculateTotal_withValidData`).
- **Test structure:** Mirror the production code structure, e.g., `src/test/java`.
- **Mock data:** Use test doubles where needed to isolate components.

## 9. Dependency Management
- **Allowed libraries:** Use only approved libraries like JUnit 5, Mockito, and Lombok (if allowed).
- **External APIs:** Follow guidelines for integrating with third-party services.
- **Version management:** Use a dependency manager like Maven or Gradle.

## 10. Collaboration & Workflow Norms
- **Pair programming:** Clearly communicate when pair programming is expected or encouraged.
- **Pull request etiquette:** Provide meaningful descriptions and link related tickets or issues.
- **Decision documentation:** Use a shared changelog or decision log for major project choices.
