# Known Issues

This document lists known issues in the current version of the SkolarD project. These are either confirmed bugs, incomplete features, or limitations observed during development and testing.

---

## 1. Integration Tests Fail When Run Together
- **Issue:** Integration tests (e.g., `MessageHandlerIntegrationTest`, `BookingHandlerIntegrationTest`) fail when executed together.
- **Cause:** The database is not being properly cleaned up or re-initialized between tests.
- **Impact:** Test results may be inconsistent or produce false negatives.
- **Workaround:** Run integration tests **individually** using Gradle test filters:
  ```bash
  ./gradlew test --tests "skolard.logic.message.MessageHandlerIntegrationTest"
