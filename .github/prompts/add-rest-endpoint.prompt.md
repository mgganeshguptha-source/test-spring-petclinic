---
name: add-rest-endpoint
description: Add a REST endpoint following Spring Petclinic conventions
agent: agent
argument-hint: 'Describe the endpoint you want, e.g. GET /api/v1/ping'
tools: ['search', 'read', 'edit']
---

# Add REST Endpoint – Spring PetClinic REST

You are adding a new REST endpoint to the Spring PetClinic REST API.

Follow ALL repository-level instructions defined in:
`.github/copilot-instructions.md`

---

## Gather Requirements

Ask me for:
- **HTTP method and path** (must include versioning): e.g., `GET /api/v1/ping`
- **Purpose** of the endpoint and expected response schema
- **Request body/parameters** (if applicable)
- **Validation rules** and constraints (Jakarta Validation)
- **Error cases** and expected HTTP status codes
- **Authentication/Authorization** requirements (if any)

---

## Standards to Follow

### Controller Layer
- Place in `org.springframework.samples.petclinic.rest.controller` package
- Use `@RestController` and `@RequestMapping("/api/v1")`
- Use appropriate mapping annotations: `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- Keep controllers **thin** — no business logic
- Use **constructor injection** for dependencies (no `@Autowired` on fields)

### Service Layer
- Place business logic in `@Service` classes under `service` package
- Apply `@Transactional` where transactional boundaries are required
- Use `final` keyword for immutable dependencies

### DTOs & Validation
- Use separate **request/response DTOs** (not JPA entities)
- Validate inputs with **Jakarta Validation** annotations (`@NotNull`, `@Size`, `@Valid`, etc.)
- Use **MapStruct** mappers if transforming between entities and DTOs

### Response Format
- Return consistent JSON structure
- Use standard HTTP status codes: `200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `404 Not Found`
- Follow error response format:
```json
{
  "timestamp": "2026-02-25T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found with id: 123",
  "path": "/api/v1/resource/123"
}
```

### Documentation
- Add **SpringDoc/OpenAPI annotations**: `@Operation`, `@ApiResponse`, `@Tag`, `@Parameter`
- Include **JavaDoc** comments for public methods

### Security
- ❌ Never hardcode secrets or API keys
- ❌ Never bypass authentication/authorization
- ✅ Add endpoint to security configuration if authentication is required

---

## Implementation Steps

1. **Identify or create controller** in `rest.controller` package
2. **Create DTO classes** (if request/response needed) in `rest.dto` package
3. **Implement service method** (if business logic required) in `service` package
4. **Add OpenAPI annotations** for API documentation
5. **Add/update unit tests** using JUnit 5 + Mockito
   - Controller tests with `MockMvc`
   - Service tests with mocked repositories
6. **Update security configuration** if endpoint requires specific access rules
7. **Show diff and explain changes**

---

## Test Requirements

- Place tests in `src/test/java` mirroring production package structure
- Use **JUnit 5** only (`@Test`, `@ExtendWith(MockitoExtension.class)`)
- Use **Mockito** for mocking (`@Mock`, `@InjectMocks`)
- Test with **MockMvc** for controller tests
- Cover: happy path, validation errors, not found, edge cases

---

## Output Expectations

- Generated code must compile and follow existing project conventions
- Include all necessary imports
- Add brief comments only where intent is not obvious
- Show complete file paths for all created/modified files