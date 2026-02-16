# Repository-Specific Copilot Instructions

## Repository Overview

- **Purpose**: REST backend for the Spring PetClinic sample application — provides CRUD APIs and domain logic for clinics, owners, pets, visits and related entities. This repository is a reference/demo implementation intended for learning, testing, and integration examples.

- **Tech Stack**: Java with Spring Boot (parent: spring-boot-starter-parent 4.0.2); Spring MVC (Web), Spring Data JPA and Spring JDBC, Spring Security, springdoc-openapi, MapStruct, Jackson; build and dependency management with Maven; test tooling with JUnit 5 and Mockito; runtime/datastores include H2, HSQLDB, MySQL and PostgreSQL; container tooling via Jib and docker-compose. See `pom.xml` for full dependency list.

- **Architecture**: Layered REST API — controllers (`rest`), service layer (`service`), repositories/data-access (`repository`), domain models (`model`) and DTOs/mappers (`mapper`). API-first approach with OpenAPI spec at `src/main/resources/openapi.yml`.

---

## SPRING BOOT BACKEND
### Naming Conventions
- Classes: `PascalCase`
- Methods/Variables: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Packages: `com.company.project.layer` (lowercase)
- Repositories: `PascalCase` with `Repository` suffix (e.g., `UserRepository`)
- Controllers: `PascalCase` with `Controller` suffix (e.g., `UserController`)

### Code Style

- Use constructor injection (avoid `@Autowired` on fields); prefer constructor parameters for dependencies.
- Apply `@Transactional` on service layer methods where transactional boundaries are required.
- Use Lombok annotations judiciously (e.g., `@Data` for DTOs, avoid `@Data` on JPA entities; prefer `@Getter`/`@Setter`/`@RequiredArgsConstructor` where appropriate).
- Follow SLF4J logging practices and appropriate log levels: `INFO`, `DEBUG`, `ERROR`.
- Use the `final` keyword for immutable dependencies and fields where appropriate.

### Java Package Structure

src/main/java/com/company/project/
- config/  # Configuration classes, Spring beans
- controller/  # REST controllers, API endpoints
- service/  # Business logic layer
- repository/  # Data access layer (JPA repositories)
- model/  # Entities, DTOs, request/response objects
- exception/  # Custom exceptions and global handlers
- security/  # Security configuration, filters
- util/  # Utility classes, helpers

### Spring Testing

- Use JUnit 5 with Mockito for unit tests. Use `@ExtendWith(MockitoExtension.class)` for pure unit tests.
- Test classes: `*Test.java` suffix; test class name pattern: `<ClassName>Test`.
- Use Mockito annotations for unit tests: `@Mock`, `@InjectMocks`. Verify interactions only when meaningful.
- Use `@SpringBootTest` for full integration tests and `@DataJpaTest` for repository tests.
- Mock external dependencies in slice/integration tests with `@MockBean`.
- Test REST controllers with `MockMvc` (verify status codes, error responses, and controller→service interactions).
- Test files must be placed under `src/test/java` mirroring the production package structure; do not create tests outside this folder.
- Unit-test rules: JUnit 5 only, Mockito for mocking, do not use real database connections in unit tests, and keep tests deterministic and readable (happy path, negative cases, edge cases, null/empty inputs).

## API CONVENTIONS

### Request/Response Patterns

- Version all APIs: `/api/v1/resource`
- Use consistent error response format (see below)

### Error Response Format

```json
{
	"timestamp": "2024-01-01T12:00:00Z",
	"status": 404,
	"error": "Not Found",
	"message": "User not found with id: 123",
	"path": "/api/v1/users/123"
}
```
- Validate all inputs with Jakarta Validation annotations
- Return appropriate HTTP status codes (200 OK, 201 Created, 204 No Content)
- Use pagination for list endpoints: `?page=0\&size=20\&sort=name,asc`



## SECURITY GUIDELINES

### DON'Ts

1. Never generate hardcoded secrets, API keys, or passwords in code or documentation.
2. Never generate SQL queries using string concatenation (SQL injection risk).
3. Never generate code that bypasses authentication/authorization checks.
4. Never log sensitive data (passwords, tokens, PII).
5. Never disable CORS or security headers in production code.

### DOs

1. Always include input validation on both frontend and backend (use Jakarta Validation on DTOs).
2. Always include proper error handling with user-friendly messages and avoid leaking internals.
3. Always use HTTPS for all API communications.
4. Always protect state-changing operations with CSRF protection when applicable.
5. Always hash passwords using a secure algorithm (bcrypt or Argon2) and never store plain text passwords.

## DESIGN PATTERNS WE USE

### Backend Patterns

1. Repository Pattern: Use Spring Data JPA repositories for data access and query methods.
2. Service Layer Pattern: Keep business logic in Spring `@Service` classes; controllers should remain thin.
3. DTO Pattern: Separate request/response DTOs from JPA entities; use mappers (MapStruct) to transform between them.
4. Dependency Injection: Prefer constructor injection throughout for testability and immutability.
5. Strategy Pattern: Use strategy interfaces for interchangeable algorithm implementations where applicable.


## DOCUMENTATION REQUIREMENTS

### Code Documentation
1. Document complex business logic with clear comments.
2. Include JavaDoc for Spring Boot public APIs and service-layer classes.
3. Document API endpoints with SpringDoc / OpenAPI annotations so generated specs stay accurate.
4. Keep comments and API docs updated when code changes.

### Project Documentation
1. Update `README.md` when adding new features or changing setup steps.
2. Include clear setup instructions for new developers (local dev DB, profiles, running tests).
3. Document all environment variables and configuration profiles used by the application.
4. Keep API documentation (OpenAPI spec under `src/main/resources/openapi.yml`) in sync with implementation.

### UNIQUE PATTERNS FOR THIS PROJECT
1. Date Handling: All dates stored in UTC but displayed in user's timezone
2. Error Messages: Use translation service (i18n), never hardcoded strings

### WHAT TO AVOID

1. Spring Anti-Patterns - Business logic in controllers (move to services)
2. Spring Anti-Patterns - Entity classes as API request/response objects
3. Spring Anti-Patterns - Ignoring transaction boundaries
4. Spring Anti-Patterns - Circular dependencies between services

You can extend this file with additional project-specific documentation guidance copied from the longer `.github/copilot-instructions-full stack.md` if you want Copilot to follow repository rules when authoring code.
