# Hospital Management System

A production-grade Hospital Management System backend built with Java Spring Boot, featuring full role-based access control, audit logging, and production-ready infrastructure.

## Tech Stack

- **Java 25** with virtual threads (Project Loom)
- **Spring Boot 3.2.5**
  - Spring Web (REST APIs)
  - Spring Data JPA (Hibernate ORM)
  - Spring Security (JWT + OAuth2 Resource Server)
  - Spring Validation
  - Spring Cache (Caffeine + Redis)
  - Spring Mail
  - Spring Actuator
  - Spring Retry
- **PostgreSQL** (primary database)
- **Redis** (caching + session store)
- **Flyway** (database migrations)
- **MapStruct** (DTO mapping)
- **Lombok** (boilerplate reduction)
- **Bucket4j** (API rate limiting)
- **OpenAPI 3 / Springdoc** (Swagger UI)
- **Docker + Docker Compose**
- **JUnit 5 + Mockito**

## Architecture

The project follows a layered architecture:
- **Controller** → **Service** → **Repository**
- No business logic in controllers
- Services are @Transactional at method level
- Repositories use Spring Data JPA

## Project Structure

```
com.hospital
├── config/           # Security, Redis, Swagger, Async, Cache, CORS configs
├── common/           # BaseEntity, ApiResponse, PageResponse
├── exception/        # GlobalExceptionHandler, custom exceptions
├── security/        # JWT filter, JWT utils, UserDetailsService
├── auth/             # Register, login, refresh token
├── user/             # User entity, roles
├── patient/          # Patient profile, medical history
├── doctor/           # Doctor profile, specializations
├── appointment/      # Booking with idempotency, double-booking protection
├── medicalrecord/    # Diagnoses, symptoms, vitals
├── prescription/     # Prescriptions, medications
├── billing/          # Invoices, payments
├── department/       # Departments
├── lab/              # Lab orders, results
├── pharmacy/         # Medication catalog, stock
├── ward/             # Beds, admissions
├── notification/    # In-app + email with outbox
├── audit/            # Audit trail
```

## Roles

- `ADMIN` - Full system access
- `DOCTOR` - Medical records, prescriptions
- `NURSE` - Patient care
- `RECEPTIONIST` - Appointments, patient registration
- `PATIENT` - Own records, appointments
- `PHARMACIST` - Medication dispensing
- `LAB_TECHNICIAN` - Lab test management

## API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/refresh` - Refresh token
- `POST /api/v1/auth/logout` - Logout

### Patients
- `POST /api/v1/patients` - Register patient
- `GET /api/v1/patients/{id}` - Get patient
- `GET /api/v1/patients/mrn/{mrn}` - Get by MRN
- `GET /api/v1/patients` - List all
- `PUT /api/v1/patients/{id}` - Update patient

### Doctors
- `POST /api/v1/doctors` - Create doctor
- `GET /api/v1/doctors/{id}` - Get doctor
- `GET /api/v1/doctors` - List all

### Appointments
- `POST /api/v1/appointments` - Book appointment
- `GET /api/v1/appointments/{id}` - Get appointment
- `PUT /api/v1/appointments/{id}/confirm` - Confirm
- `PUT /api/v1/appointments/{id}/cancel` - Cancel

### Medical Records
- `POST /api/v1/medical-records` - Create record
- `GET /api/v1/medical-records/patient/{id}` - Patient history

### Billing
- `POST /api/v1/billing` - Create invoice
- `GET /api/v1/billing/{id}` - Get invoice
- `POST /api/v1/billing/{id}/payment` - Process payment

### Pharmacy
- `POST /api/v1/pharmacy` - Add medication
- `GET /api/v1/pharmacy/low-stock` - Low stock alert
- `POST /api/v1/pharmacy/{id}/dispense` - Dispense

### Wards
- `POST /api/v1/wards` - Create ward
- `GET /api/v1/wards` - List wards
- `POST /api/v1/wards/beds/{id}/admit` - Admit patient
- `POST /api/v1/wards/beds/{id}/discharge` - Discharge

## Getting Started

### Prerequisites
- Java 25
- Maven
- Docker Desktop

### Running Locally

```bash
# Start infrastructure
docker-compose up -d

# Run the application
mvn spring-boot:run
```

### Running Tests

```bash
mvn test
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| SERVER_PORT | Server port | 8080 |
| DB_URL | PostgreSQL URL | jdbc:postgresql://localhost:5432/hospital |
| DB_USERNAME | Database username | hospital |
| DB_PASSWORD | Database password | hospital |
| REDIS_HOST | Redis host | localhost |
| REDIS_PORT | Redis port | 6379 |
| JWT_SECRET | JWT secret key | (required) |

## API Documentation

Swagger UI is available at `/swagger-ui.html` in dev profile.

## Health Endpoints

- `/actuator/health` - Health check
- `/actuator/info` - Application info
- `/actuator/prometheus` - Prometheus metrics

## Security Features

- JWT-based stateless authentication
- Refresh token rotation
- Account lockout after 5 failed login attempts
- Role-based access control
- Method-level security with @PreAuthorize
- Rate limiting via Bucket4j
- Input validation with Jakarta Bean Validation

## Database Schema

All schema changes are managed via Flyway migrations in `resources/db/migration/`.

## License

Proprietary - All rights reserved