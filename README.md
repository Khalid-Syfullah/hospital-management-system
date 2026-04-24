# Hospital Management System Backend

Production-oriented Spring Boot 3 backend for hospital operations: auth, users, patients, doctors, departments, appointments, EMR, prescriptions, lab, billing, pharmacy, wards, notifications, and audit logs.

## Stack

Java 21, Spring Boot 3.4, Spring Web, Spring Data JPA, Spring Security JWT, OAuth2 resource server dependency, Validation, Cache, Redis, Caffeine, Mail, Actuator, Retry, PostgreSQL, Flyway, MapStruct, Lombok dependency, Bucket4j, Springdoc OpenAPI, Maven, JUnit 5, Mockito, and Testcontainers.

## Run Locally

```bash
docker compose up --build
```

Swagger UI is enabled in the `dev` profile at:

```text
http://localhost:8080/swagger-ui.html
```

Health probes:

```text
GET /actuator/health
GET /actuator/health/liveness
GET /actuator/health/readiness
```

## Environment Variables

```text
SPRING_PROFILES_ACTIVE=dev
DB_URL=jdbc:postgresql://localhost:5432/hospital_db
DB_USERNAME=hospital
DB_PASSWORD=hospital
REDIS_HOST=localhost
REDIS_PORT=6379
JWT_SECRET=<base64-encoded-256-bit-secret>
JWT_ACCESS_EXPIRATION=900000
JWT_REFRESH_EXPIRATION=604800000
ALLOWED_ORIGINS=http://localhost:3000
MAIL_HOST=localhost
MAIL_PORT=1025
```

## Architecture

The code follows controller -> service -> repository layering. Controllers return DTO response envelopes only. Services own transactions and business rules. Entities extend `BaseEntity` with UUID IDs, audit fields, and soft-delete support. Flyway owns schema changes. Security is stateless JWT with BCrypt passwords, refresh-token rotation, method-level RBAC, request tracing, and rate limiting on auth endpoints.

## Example Requests

Register:

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@example.com","password":"password123","fullName":"Admin User","roles":["ADMIN"]}'
```

Login:

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@example.com","password":"password123"}'
```

Create patient:

```bash
curl -X POST http://localhost:8080/api/v1/patients \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Jane Patient","dateOfBirth":"1990-01-01","phone":"555-1000"}'
```

Book appointment:

```bash
curl -X POST http://localhost:8080/api/v1/appointments \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Idempotency-Key: appointment-123' \
  -H 'Content-Type: application/json' \
  -d '{"patientId":"<uuid>","doctorId":"<uuid>","startTime":"2026-05-01T10:00:00","endTime":"2026-05-01T10:30:00"}'
```

## Tests

```bash
mvn test
```
