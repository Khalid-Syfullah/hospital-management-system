# Hospital Management System Backend

Spring Boot 3 backend for a hospital management system using Java 21, PostgreSQL, Redis, Flyway, JWT security, method-level RBAC, audit logging, notification outbox, caching, validation, actuator probes, and Docker Compose.

## Run locally

```bash
docker compose up -d postgres redis
mvn spring-boot:run
```

Swagger UI is enabled in the `dev` profile at `/swagger-ui.html`. Actuator health probes are available at `/actuator/health/liveness` and `/actuator/health/readiness`.

## Environment

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `REDIS_HOST`, `REDIS_PORT`
- `JWT_SECRET`, `JWT_ACCESS_MINUTES`, `JWT_REFRESH_DAYS`
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`
- `SPRING_PROFILES_ACTIVE`

## Architecture

The code follows `controller -> service -> repository`. Controllers only validate and delegate. Services own transactions and business rules. Entities extend `BaseEntity` with UUID IDs, auditing fields, and soft-delete timestamp. Controllers return DTO envelopes, never JPA entities.

Core modules are under `com.hospital`: auth, user, patient, doctor, department, appointment, medicalrecord, prescription, lab, billing, pharmacy, ward, notification, and audit.

## Example requests

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@example.com","password":"StrongPass123","fullName":"Admin User","roles":["ADMIN"]}'
```

```bash
curl -X POST http://localhost:8080/api/v1/patients \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Jane Patient","gender":"FEMALE","phone":"+15550101"}'
```

```bash
curl -X POST http://localhost:8080/api/v1/appointments \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Idempotency-Key: booking-123' \
  -H 'Content-Type: application/json' \
  -d '{"patientId":"...","doctorId":"...","startTime":"2026-05-01T10:00:00Z","endTime":"2026-05-01T10:30:00Z","reason":"Consultation"}'
```

## Tests

```bash
mvn test
```
