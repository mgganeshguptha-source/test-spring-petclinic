
# Bug Fix Prompt – Spring Petclinic

You are helping to debug and fix issues in the
[Spring Petclinic](https://github.com/spring-projects/spring-petclinic) application.

Follow ALL repository‑level instructions defined in:
`.github/copilot-instructions.md`

Do NOT violate those instructions.

---

## Context

his prompt is used for **small, local bug fixes**, not large refactors.

---

## When I use this prompt

I will provide one or more of:

- The class or method that is failing
- A failing unit test or integration test
- An error / stack trace
- A short description of the expected behaviour

---

## Your Tasks

When I run this prompt on a selected file (and optionally paste a stack trace):

1. **Understand the bug**

   - Read the selected code and any linked tests.
   - If a stack trace or failing test is provided, identify the likely root cause.
   - Summarize the root cause in 2–3 sentences.

2. **Propose a minimal, safe fix**

   - Change as little code as possible.
   - Preserve existing public APIs and business behaviour except where clearly wrong.
   - Prefer fixing logic, null‑handling, and branching before adding new features.
   - Reuse existing utilities, services, and patterns already present in the project.

3. **Update / add tests**

follow instructions in `.github/prompts/unit-testing.prompt.md` for writing good tests.
   - Add tests only if they are needed to verify the fix or prevent regressions.
   - Do NOT add tests for unrelated functionality.

---

## Rules (MUST follow)

- Do NOT introduce new external dependencies.
- Do NOT change database schema, mappings, or configuration unless asked.
- Do NOT change controller URLs or method signatures unless the bug is
  explicitly about the contract.
- Do NOT comment out tests to “make them pass”.

- Prefer:

  - Null and boundary checks where appropriate
  - Clear error messages and logging consistent with the project
  - Keeping changes inside the affected class/service and its tests

---

## Output

- Apply code changes directly to the relevant `.java` and test files.
- At the top of the chat response, include a short section:

  - **Root cause**: <1–3 sentences>
  - **Fix summary**: <1–3 sentences>
  - **Tests**: list of tests added/updated

Do not include long explanations in code comments. Use comments only when the
intent of the fix is not obvious.

---

## Final Instruction

Focus on **analyzing and fixing the bug plus its tests**.  
Do not perform unrelated refactors unless they directly help readability
of the fix.
