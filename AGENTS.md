# Agent instructions

Take-home for XA Bank time deposits. Work only in this fork. Do not open a pull request to ikigai-digital.

## Stack

- Java 17, Spring Boot, one Maven module under `java/`. Leave `kotlin/`, `c#/`, `typescript/`, and `python/` unchanged
- Hexagonal packages: `domain`, `application`, `adapter.web`, `adapter.persistence`
- OpenAPI contract in `openapi.yaml`; Swagger UI serves that file
- PostgreSQL via Testcontainers for integration tests
- Domain code must not depend on Spring, JPA, or HTTP

## Do not change

- `TimeDeposit` and `TimeDepositCalculator` stay in package `org.ikigaidigital`
- `TimeDeposit` public API: constructor, fields, getters, `setBalance`
- `TimeDepositCalculator.updateBalance(List<TimeDeposit>)` signature and observable results
- Rounding stays `BigDecimal` scale 2, `HALF_UP`, applied once per deposit
- Monthly interest is the annual rate divided by 12, because that is what `updateBalance` already does

## Interest

- One `InterestPolicy` per plan. The 30-day rule lives inside Basic, Student, and Premium, not as a global gate.
- Basic: 1% / 12 after 30 days
- Student: 3% / 12 after 30 days and before 366 days
- Premium: 5% / 12 only after 45 days
- `updateBalance` does not increment `days`
- Withdrawals are not part of `TimeDeposit` and do not affect interest

## API

Exactly two endpoints:

- `GET /time-deposits` returns `id`, `planType`, `balance`, `days`, `withdrawals`
- `POST /time-deposits/balance-updates` loads all deposits, calls `updateBalance`, saves balances

No other endpoints. Do not handle invalid input. There is no create endpoint: seed rows in the database and explain that in a code comment.

## Persistence

- Table `timeDeposits`: `id` integer primary key, `planType` string required, `days` integer required, `balance` decimal required
- Table `withdrawals`: `id` integer primary key, `timeDepositId` integer foreign key required, `amount` decimal required, `date` date required
- Database `balance` and `amount` are decimal. Map to `Double` only at the edge, inside the persistence adapter

## Commits

One concern per commit: agent rules, characterization tests, interest refactor, schema, OpenAPI, each endpoint, Swagger instructions, AI assistance report.

## AI assistance report

Keep a short `AI.md` in the repo root. Record the tool (Cursor Agent), how to reproduce this setup (`AGENTS.md` at the repo root), and which parts the agent wrote (refactor, tests, API, docs) and why.

## Assumptions

When a requirement is ambiguous, pick the simplest reading that keeps `updateBalance` behavior and write the reason in a code comment.
