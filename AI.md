# AI assistance

Tool: Cursor Agent, with `AGENTS.md` at the repository root loaded as the project rules.

## Setup

Open this repository in Cursor. The agent reads `AGENTS.md` and follows it: Java only, hexagonal packages, unchanged `TimeDeposit` and `updateBalance`, exactly two endpoints, Testcontainers, and one concern per commit.

## What the agent did

- Planned the package layout and the interest policies, so the existing calculator behavior stayed locked by tests.
- Drafted the persistence port, the OpenAPI contract, the Testcontainers test shape, and the Swagger run instructions.
- Fixed the Spring Boot 4 classpath: JUnit 6, Jackson annotations 2.21, and Boot 4.1.1 modules that springdoc 3.0.0 had pulled as 4.0.0.
- Wrote the commit messages and pushed them to this fork.

The plan rules, controller, service, SQL seed, and test assertions were written in the editor and checked against `mvn test`.
