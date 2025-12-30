# construo-shop

Planned work is tracked in [`Jira Board`](https://marouaneboukedir.atlassian.net/jira/software/projects/CI/boards/3?sprintStarted=true).

## Progress log
- Time invested so far: ~4 hours.
- Assistance: AI was used to accelerate a couple of tasks (code generation and refining).
- Remaining scope: security (Keycloak), Flyway migrations, Docker packaging (app + DB + Keycloak), and tests.

## How to run
- Start app: `mvn spring-boot:run`
- Postman: import [`postman collection`](postman/Construo-Shop.postman_collection.json) and set `baseUrl` (default `http://localhost:8080`).
