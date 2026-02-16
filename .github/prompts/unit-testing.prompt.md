

# Unit Testing Prompt – Spring PetClinic REST

You are generating unit tests for the Spring PetClinic REST project.

Follow ALL repository-level instructions defined in:
.github/copilot-instructions.md

Do NOT violate those instructions.

---
Provide the class name/packagename to generate unit test cases : ${input:className:provide your class name with package name}


## Context

- Project: Spring PetClinic REST
- Language: Java
- Framework: Spring Boot
- Testing Framework: **JUnit 5 ONLY**
- Mocking Framework: **Mockito**
- Architecture: Controller → Service → Repository
- Persistence: Spring Data JPA

---

## Objective

Generate high-quality unit tests for the selected class that:
- Follow existing project conventions
- Are readable, maintainable, and deterministic
- Increase meaningful test coverage

---

## Rules for Unit Test Generation (Mandatory)

### Test Framework & Structure
- Use **JUnit 5 annotations only**
    - `@Test`
    - `@ExtendWith(MockitoExtension.class)`
- Use Mockito for mocking dependencies
- Name test classes as:
  `<ClassName>Test`
- Place tests under the appropriate test package

❌ Do NOT:
- Use JUnit 4
- Introduce new test libraries
- Use real database connections

---

### What to Test

For **Service classes**:
- Business logic
- Positive scenarios
- Negative scenarios
- Edge cases
- Exception handling

For **Controller classes**:
- HTTP status codes
- Request validation behavior
- Error responses
- Interaction with service layer

---

### What NOT to Test

Do NOT write unit tests for:
- Spring framework behavior
- Configuration classes
- Simple getters/setters
- JPA entity mappings (unless custom logic exists)

---

## Mocking Rules

- Mock repositories and external dependencies
- Use `@Mock` and `@InjectMocks`
- Verify interactions only when meaningful
- Avoid over-mocking

---

## Assertions

- Use clear and explicit assertions
- Assert both:
    - Expected results
    - Expected exceptions
- Prefer readable assertions over complex logic

---

---
## Test file location and naming rules

- Always create or update test files under this folder:

  `src/test/java`

- Mirror the package / folder structure of the source code.
- For a source file:
    - `src/main/java/com/example/service/MyService.java`
    - The test file **must** be:
    - `src/test/java/com/example/service/MyServiceTest.java`
- Never create test files in any other folder.
- Never invent new folders for tests.

---

---

## Coverage Expectations

When generating unit tests:
- Identify all public methods.
- Identify conditional logic (if/else, null checks, collections).
- Logic Validation: Do not simply mirror the current implementation. If the source code logic contradicts standard business rules (e.g., allowing negative ages, processing null names without validation), write the test to expect the correct logical behavior.

Generate tests for:
- Happy path
- Null input
- Empty collections
- Invalid or boundary values

- Intent-Based Testing: If a test fails because the source code is logically flawed, the test is considered correct; the source code must be fixed to pass the test.

---

## Error & Edge Case Coverage

Include tests for:
- Invalid input
- Empty or null responses
- Repository returning no data
- Expected runtime exceptions

---

## Output Expectations

- Generated tests must compile
- Tests must be executable without modification
- Code must follow existing formatting and naming conventions
- Add brief comments only where intent is not obvious

---

## Final Instruction

Generate ONLY the unit test code.
Do not include explanations unless explicitly requested.
    
