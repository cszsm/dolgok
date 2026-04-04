# Development Guidelines

## 1. Build & Configuration

This is a multi-module Android project developed with Kotlin and Jetpack Compose.

### Requirements
- JDK 11 (configured via `allprojects` in root `build.gradle.kts`)
- Android Studio Ladybug or newer recommended.

### Build Instructions
The project uses a custom `buildSrc` to manage build logic and dependencies.
- To build from command line: `./gradlew assembleDebug`
- To clean the project: `./gradlew clean`

### Module Structure
- `app`: Main application module.
- `core`: Common utilities, components, and domain models.
- `forecast`: Weather forecast feature.
- `animation`: Animation and progress indicator experiments.
- `home`: Main entry screen.
- `pointandshoot`: Camera feature.
- `localization`: Shared string resources.
- `buildSrc`: Centralized Gradle configuration and plugin logic.

---

## 2. Testing

### Configuration & Execution
The project uses **JUnit 5 (Jupiter)** for unit testing and **MockK** for mocking.

- **Run all unit tests**: `./gradlew test`
- **Run tests for a specific module**: `./gradlew :core:test`
- **Run a specific test class**: `./gradlew :core:test --tests "cszsm.dolgok.core.util.DateTimeUtilTest"`

### Adding New Tests
- Place unit tests in `src/test/kotlin`.
- Use `org.junit.jupiter.api.Test` annotation.
- Use `io.mockk` for mocking dependencies.
- For `ViewModel` or Coroutine testing, use `kotlinx-coroutines-test` and set the Main dispatcher.

### Test Example
Below is a simple unit test demonstrating the use of JUnit 5 and testing a utility function.

```kotlin
package cszsm.dolgok.core.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DateTimeUtilTest {

    @Test
    fun `roundDownToHour should set minutes and seconds to zero`() {
        // Given
        val dateTime = LocalDateTime(2023, Month.OCTOBER, 27, 14, 30, 45, 123)

        // When
        val rounded = dateTime.roundDownToHour()

        // Then
        val expected = LocalDateTime(2023, Month.OCTOBER, 27, 14, 0, 0, 0)
        assertEquals(expected, rounded)
    }
}
```

---

## 3. Additional Development Information

### Code Style
- **Kotlin-first**: Strictly use Kotlin for all new code.
- **Jetpack Compose**: All UI should be built using Compose.
- **Dependency Injection**: Koin is used for DI. Modules are typically defined in a `di` package within each module (e.g., `CoreModule.kt`).
- **Clean Architecture**: The project follows Clean Architecture principles (Domain, Data, Presentation layers).

### Dependency Management
- Dependencies are managed in `gradle/libs.versions.toml`.
- Common dependencies and plugins are applied via `buildSrc` in `CommonDependencyConfig.kt` and `CommonPluginsConfig.kt`.

### Localization
- String resources are centralized in the `localization` module.
- Hungarian (`values-hu`) and default (English) locales are supported.
