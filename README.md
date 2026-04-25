# Hospital Management System

A production-grade REST API backend for a hospital system built with Java 21 + Spring Boot 3.

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 21 (Project Loom virtual threads) |
| Framework | Spring Boot 3.2 |
| Database | PostgreSQL 16 |
| Cache | Caffeine + Redis |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA / Hibernate |
| Migrations | Flyway |
| Mapping | MapStruct |
| API Docs | OpenAPI 3 / Springdoc |
| Build | Maven |
| Containerization | Docker + Docker Compose |
| Testing | JUnit 5, Mockito, Testcontainers |

## Architecture

```
com.hospital
├── config/           # Security, Cache, Swagger, Async, CORS
├── common/           # BaseEntity, ApiResponse, PageResponse
├── exception/        # GlobalExceptionHandler, custom exceptions
├── security/         # JWT filter, JwtUtils, UserDetailsService
├── auth/             # Login, register, refresh token
├── user/             # User entity, roles, CRUD
├── patient/          # Patient profile, MRN, allergies, insurance
├── doctor/           # Doctor profile, availability, specialization
├── appointment/      # Booking, conflict detection, status flow
├── medicalrecord/    # EMR, diagnoses, vitals, ICD-10
├── prescription/     # Prescriptions, medications, dosage
├── billing/          # Invoices, payments, idempotency
├── department/       # Department CRUD, head tracking
├── lab/              # Lab orders, results, status tracking
├── pharmacy/         # Medication catalog, dispensing, stock
├── ward/             # Wards, beds, admissions, discharge
├── notification/     # In-app + email, transactional outbox
└── audit/            # Immutable audit trail
```

## API Endpoints Summary

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register new account |
| POST | `/api/v1/auth/login` | Login, get JWT |
| POST | `/api/v1/auth/refresh` | Refresh access token |
| POST | `/api/v1/auth/logout` | Revoke refresh token |

### Core Modules
| Module | Base Path |
|--------|-----------|
| Users | `/api/v1/users` |
| Patients | `/api/v1/patients` |
| Doctors | `/api/v1/doctors` |
| Departments | `/api/v1/departments` |
| Appointments | `/api/v1/appointments` |
| Medical Records | `/api/v1/medical-records` |
| Prescriptions | `/api/v1/prescriptions` |
| Lab | `/api/v1/lab` |
| Billing | `/api/v1/billing` |
| Pharmacy | `/api/v1/pharmacy` |
| Wards | `/api/v1/wards` |
| Notifications | `/api/v1/notifications` |
| Audit | `/api/v1/audit` |

## Setup

### Prerequisites
- Docker and Docker Compose
- Java 21 (for local development)
- Maven 3.9+

### Quick Start with Docker

```bash
# Clone the repository
git clone https://github.com/your-org/hospital-management-system.git
cd hospital-management-system

# Copy and configure environment variables
cp .env.example .env
# Edit .env with your settings

# Start all services
docker-compose up -d

# Application will be available at:
# API:      http://localhost:8080
# Swagger:  http://localhost:8080/swagger-ui.html
# MailHog:  http://localhost:8025
```

### Local Development

```bash
# Start infrastructure only
docker-compose up -d postgres redis mailhog

# Run the application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Run tests
./mvnw test
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `APP_PROFILE` | Active profile (dev/test/prod) | `dev` |
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/hospital_db` |
| `DB_USERNAME` | Database username | `hospital_user` |
| `DB_PASSWORD` | Database password | `hospital_pass` |
| `REDIS_HOST` | Redis hostname | `localhost` |
| `REDIS_PORT` | Redis port | `6379` |
| `JWT_SECRET` | Base64-encoded JWT signing key (256-bit min) | *(dev default)* |
| `JWT_EXPIRATION_MS` | Access token TTL in ms | `900000` (15 min) |
| `JWT_REFRESH_EXPIRATION_MS` | Refresh token TTL in ms | `604800000` (7 days) |
| `MAIL_HOST` | SMTP server hostname | `smtp.gmail.com` |
| `MAIL_PORT` | SMTP port | `587` |
| `MAIL_USERNAME` | SMTP username | — |
| `MAIL_PASSWORD` | SMTP password/app-password | — |
| `CORS_ALLOWED_ORIGINS` | Comma-separated allowed origins | `http://localhost:3000` |
| `SWAGGER_ENABLED` | Show Swagger UI | `true` (disabled in prod) |

## Roles

| Role | Description |
|------|-------------|
| `ADMIN` | Full system access |
| `DOCTOR` | Clinical access, medical records, prescriptions |
| `NURSE` | Patient care, vitals, ward management |
| `RECEPTIONIST` | Appointments, patient registration, billing |
| `PATIENT` | Own data, own appointments |
| `PHARMACIST` | Pharmacy catalog, dispensing |
| `LAB_TECHNICIAN` | Lab orders, results |

## Example API Requests

### Register & Login

```bash
# Register
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@hospital.com","password":"Admin@123!","firstName":"Admin","lastName":"User"}'

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@hospital.com","password":"Admin@123!"}'
```

### Book an Appointment

```bash
curl -X POST http://localhost:8080/api/v1/appointments \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "<patient-uuid>",
    "doctorId": "<doctor-uuid>",
    "startTime": "2026-05-01T10:00:00",
    "endTime": "2026-05-01T10:30:00",
    "reason": "Annual checkup",
    "idempotencyKey": "booking-001"
  }'
```

### Create Invoice

```bash
curl -X POST http://localhost:8080/api/v1/billing/invoices \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "<patient-uuid>",
    "invoiceDate": "2026-04-26",
    "items": [
      {"description":"Consultation","itemType":"CONSULTATION","quantity":1,"unitPrice":150.00},
      {"description":"Blood Test","itemType":"LAB_TEST","quantity":1,"unitPrice":50.00}
    ]
  }'
```

## Key Design Decisions

- **Soft deletes** on all clinical data — `deletedAt` timestamp, never hard deleted
- **Optimistic locking** (`@Version`) on Appointment, Invoice, and Medication to prevent race conditions
- **Idempotency keys** on appointment booking and payment endpoints
- **Transactional outbox** for reliable email delivery via scheduled processor
- **Virtual threads** (Project Loom) via `spring.threads.virtual.enabled=true` for high throughput
- **JWT + refresh token rotation** — each refresh issues a new refresh token and revokes the old one
- **MDC request tracing** — every log line includes `requestId`, `userId`, and `role`
- **Audit trail** is append-only and uses `REQUIRES_NEW` propagation to survive parent transaction rollbacks

## Health & Monitoring

```
GET /actuator/health          # Overall health
GET /actuator/health/liveness # Kubernetes liveness probe
GET /actuator/health/readiness # Kubernetes readiness probe
GET /actuator/info            # Application info
GET /actuator/metrics         # Metrics
```
