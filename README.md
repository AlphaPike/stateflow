# Workflow Platform

A microservice skeleton for authenticated workflow transitions: JWT at the gateway, a persisted state machine, a Redis lock released after commit, and a delayed retry queue.

Stateflow is a small microservice layout: gateway, auth, and a system service that owns the workflow.

## Tech Stack

- Java 8, Spring Boot 2.7, Spring Cloud 2021, Spring Cloud Alibaba
- Nacos, Spring Cloud Gateway, Sentinel
- MySQL, MyBatis, Redis
- Spring State Machine 2.0
- JWT

## Key Features & Architecture

- **Gateway auth.** `AuthFilter` checks the JWT. `IdempotentFilter` uses Redis `SETNX` so a repeated write with the same `Idempotent` header is rejected. `GlobalExceptionHandler` maps permission and business failures to a single response shape.
- **Login.** Password login looks the user up by phone, limits retries in Redis, and rejects blocked IPs. Tokens are issued by `stateflow-auth` and stored in Redis.
- **State machine.** Each task has its own machine. Legal path: `CREATED -> SUBMITTED -> APPROVED -> DONE`. `CANCEL` and `REJECT` are terminal side exits. The current state is restored from `wf_task` and written back in the same transaction.
- **Concurrency.** `ActionLock` takes a Redis lock for the task id and releases it in `afterCompletion`, so a second transition cannot run between the state change and the commit.
- **Delayed completion.** Approving a task enqueues it on a Redis sorted set. `RetryWatchScheduler` claims due ids and sends `FINISH`. A rejected attempt is scheduled again with backoff.

## Quick Start

Requirements: JDK 8+, Maven, MySQL 8, Redis, Nacos.

1. Create a database and import the framework tables plus the workflow table:

```bash
mysql -uroot -p your_database < sql/base_schema.sql
mysql -uroot -p your_database < sql/schema.sql
```

2. Point Nacos, Redis, and MySQL at local services. Default passwords in this repo are `your_secret_here`. Nacos in the sample config listens on `127.0.0.1:8848`.

3. Build and start auth, gateway, and the system module:

```bash
mvn -pl stateflow-auth,stateflow-gateway,stateflow-modules/stateflow-system -am package -DskipTests
```

The framework seed user phone is `15888888888` and the password is `admin123`.

Create a task on the system service (port `9201`) after login:

```bash
curl -X POST http://127.0.0.1:9201/workflow/tasks
curl -X POST http://127.0.0.1:9201/workflow/tasks/{id}/events -H "Content-Type: application/json" -d "{\"event\":\"SUBMIT\"}"
```

`APPROVE` schedules `FINISH`. `DONE` appears after the retry worker runs. A second event that the machine does not allow returns a business error instead of changing the row.
