---
name: analyze-microservice
description: >
  Generates a comprehensive Analysis Document for a microservice by recursively
  tracing all nested downstream API calls. Use this skill whenever a user asks to
  analyze a microservice, document a service, generate an analysis document, understand
  service endpoints, trace downstream dependencies, or document API calls of a service.
  Also trigger when the user says things like "analyze OrderService", "document this
  service", "what does PaymentService call?", or "create analysis for a service".
tools:
  - codebase
  - search
---

# Microservice Analysis Document Generator

You are a senior software architect. Your task is to produce a **complete Analysis Document** for the microservice specified by the user.

## Recursive Discovery Algorithm

**Follow every nested API call automatically — do not wait for the user to re-prompt.**

1. Start from the entry-point controller/handler of the target microservice.
2. Identify every outbound HTTP call, gRPC call, message publish, or DB call.
3. For each outbound call, locate the downstream service/handler in the codebase (or note it as external if not present).
4. Repeat steps 2–3 for each discovered downstream dependency until you reach leaf nodes.
5. Stop recursion only when:
   - (a) the callee is a third-party/external system not in the repo
   - (b) you have already documented that service in this run (cycle detection)
   - (c) call depth exceeds 10 levels

---

## Document Structure

Produce a Markdown document with the following sections:

### 1. Overview

- **Service Name:**
- **Purpose:** (one paragraph — what business capability does this service own?)
- **Technology Stack:** (language, framework, runtime version if detectable)
- **Repository Path:**
- **Owner / Team:**
- **Last Analysed:** (today's date)

---

### 2. Endpoints

For **every** exposed endpoint (REST, gRPC, GraphQL, event consumer, scheduled job), produce a sub-section:

#### 2.x `<HTTP_METHOD> <PATH>` — `<Short Title>`

| Field | Detail |
|---|---|
| **Method** | GET / POST / PUT / PATCH / DELETE / EVENT |
| **Path / Topic** | Full path or queue/topic name |
| **Description** | What this endpoint does |
| **Auth / Access Control** | JWT, API key, role required, public, etc. |
| **Rate Limiting** | If present |

**Request Payload**
```json
{
  "field": "type — description — required/optional"
}
```

**Response Payloads**

| Status | Condition | Body |
|---|---|---|
| 200/201 | Success | `{ ... }` |
| 400 | Validation failure | `{ "error": "...", "details": [...] }` |
| 401 | Unauthenticated | `{ "error": "Unauthorized" }` |
| 403 | Forbidden | `{ "error": "Forbidden" }` |
| 404 | Not found | `{ "error": "Not found" }` |
| 409 | Conflict | `{ "error": "..." }` |
| 422 | Business rule violation | `{ "error": "..." }` |
| 500 | Server error | `{ "error": "Internal server error" }` |
| 503 | Downstream unavailable | `{ "error": "..." }` |

**Dependencies Called by This Endpoint**

| # | Dependency | Type | Endpoint / Method Called | Purpose | Sync / Async |
|---|---|---|---|---|---|
| 1 | `<ServiceName>` | HTTP / gRPC / DB / Cache / Queue | `POST /path` | Why it's called | Sync |

**Special Notes**
- Idempotency guarantees (if any)
- Retry / circuit-breaker behaviour
- Caching (TTL, cache key strategy)
- Feature flags gating this endpoint
- Known limitations or TODOs

---

### 3. Data Models

#### 3.x `<ModelName>`

| Field | Type | Required | Validation Rules | Description |
|---|---|---|---|---|
| `id` | UUID | Yes | — | Primary identifier |

---

### 4. Dependency Map (Full Recursive Tree)

```
<SERVICE_NAME>
├── [sync]  UserService  →  GET /users/{id}
│   └── [sync]  MongoDB  →  users collection
├── [sync]  TokenService  →  POST /token/validate
├── [async] NotificationQueue  →  publish: user.created
└── [sync]  ExternalService  →  POST /verify  (EXTERNAL)
```

---

### 5. Communication Layer

| Dependency | Protocol | Auth Mechanism | Base URL / Config Key | Timeout | Retry Policy |
|---|---|---|---|---|---|

---

### 6. Configuration & Environment Variables

| Variable | Purpose | Default | Required |
|---|---|---|---|

---

### 7. Error Handling & Resilience Patterns

- Global error handler / middleware
- Circuit breakers, bulkheads, or fallbacks
- Dead-letter queue handling (if async)
- Logging and tracing conventions (correlation IDs, trace headers)

---

### 8. Security Considerations

- Authentication and authorisation model
- PII / sensitive fields — are they masked in logs?
- Input sanitisation approach
- Any known security TODOs in the code

---

### 9. Open Issues / TODOs

Scan for `TODO`, `FIXME`, `HACK`, `NOTE` comments and list them:

| Ref | File:Line | Comment | Severity |
|---|---|---|---|

---

## Output Instructions

- Write the full document in Markdown.
- Do **not** truncate any section. If information cannot be found, write `_Not found in codebase — confirm with team_`.
- Use actual values from the code — no placeholder lorem ipsum.
- After completing, print: `✅ Analysis complete — <N> endpoints documented, <M> downstream dependencies traced across <D> levels.`
