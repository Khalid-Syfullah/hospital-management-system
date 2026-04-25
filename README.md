# Hospital Management System - REST API Backend

A production-grade Hospital Management System REST API backend built with Java 21, Spring Boot 3.x, PostgreSQL, and Redis. Features include role-based access control, JWT authentication, appointment scheduling with double-booking prevention, medical records management, billing, and comprehensive audit logging.

## Features

### Core Functionality
- **User Authentication & Authorization**: JWT-based authentication with role-based access control (ADMIN, DOCTOR, NURSE, RECEPTIONIST, PATIENT, PHARMACIST, LAB_TECHNICIAN)
- **Patient Management**: Patient registration with auto-generated MRN, medical history, allergies, insurance information
- **Doctor Management**: Doctor profiles with specializations, availability scheduling, department assignments
- **Appointment Management**: Booking, rescheduling, cancellation with optimistic locking to prevent double-booking
- **Medical Records**: Diagnoses, symptoms, vital signs, visit notes with restricted access
- **Prescriptions**: Medicine management with dosage, frequency, and duration
- **Billing & Invoicing**: Auto-generated invoices with payment tracking and insurance claims
- **Lab Management**: Lab test orders and results tracking
- **Pharmacy**: Medication catalog with stock management and expiry tracking
- **Ward & Bed Management**: Ward occupancy tracking and bed management
- **Notifications**: In-app and email notifications with transactional outbox pattern
- **Audit Trail**: Immutable audit logs tracking who changed what and when

### Technical Highlights
- **Java 21 Virtual Threads**: Project Loom support for high-concurrency async operations
- **Optimistic Locking**: `@Version` on Appointment, Billing, Inventory to prevent race conditions
- **Idempotency**: Idempotency keys on booking and payment endpoints
- **Caching**: Redis for distributed caching + Caffeine for local caching
- **Structured Logging**: SLF4J + Logback with MDC request tracing
- **Security**: Stateless JWT, CORS, rate limiting with Bucket4j
- **Database Migrations**: Flyway for schema versioning
- **API Documentation**: OpenAPI 3 / Swagger UI

## Tech Stack

- **Java 21** (LTS) with virtual threads (Project Loom)
- **Spring Boot 3.3.4**
- **Spring Security** (JWT + OAuth2)
- **Spring Data JPA** (Hibernate ORM)
- **PostgreSQL 16** (primary database)
- **Redis 7** (caching + session store)
- **Flyway** (database migrations)
- **MapStruct** (DTO mapping)
- **Lombok** (boilerplate reduction)
- **JUnit 5** + **Mockito** + **Testcontainers** (testing)
- **Maven** (build tool)
- **Docker** + **Docker Compose**

## Quick Start with Docker

```bash
# Clone repository
git clone https://github.com/khalid-syfullah/hospital-management-system.git
cd hospital-management-system

# Build and start with Docker Compose
docker-compose up -d

# Application will be available at http://localhost:8080
# API: http://localhost:8080/api/v1
# Swagger UI: http://localhost:8080/api/v1/swagger-ui.html
# Health: http://localhost:8080/api/v1/actuator/health
```

## Local Development Setup

### Prerequisites
- Java 21 (JDK)
- Maven 3.9+
- PostgreSQL 16
- Redis 7

### Installation
```bash
# Install dependencies (macOS with Homebrew)
brew install postgresql redis

# Start services
brew services start postgresql
brew services start redis

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

## Environment Variables

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=hospital_db
DB_USER=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT
JWT_SECRET=your-super-secret-key-change-in-production
JWT_EXPIRATION=86400000              # 24 hours in ms
JWT_REFRESH_EXPIRATION=604800000     # 7 days in ms

# Server
SERVER_PORT=8080

# Mail (optional)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login and get JWT token
- `POST /auth/refresh-token` - Refresh access token

### Patients
- `POST /patients` - Create patient
- `GET /patients/{id}` - Get patient by ID
- `GET /patients/mrn/{mrn}` - Get patient by MRN
- `PUT /patients/{id}` - Update patient
- `DELETE /patients/{id}` - Delete patient
- `GET /patients` - List patients (paginated)
- `GET /patients/search?keyword=...` - Search patients

### Doctors
- `POST /doctors` - Create doctor
- `GET /doctors/{id}` - Get doctor
- `PUT /doctors/{id}` - Update doctor
- `DELETE /doctors/{id}` - Delete doctor
- `GET /doctors` - List doctors
- `GET /doctors/search?keyword=...` - Search doctors
- `GET /doctors/department/{departmentId}` - Get doctors by department

### Departments
- `POST /departments` - Create department
- `GET /departments/{id}` - Get department
- `PUT /departments/{id}` - Update department
- `DELETE /departments/{id}` - Delete department
- `GET /departments` - List departments

### Appointments
- `POST /appointments` - Book appointment (with idempotency key)
- `GET /appointments/{id}` - Get appointment
- `PUT /appointments/{id}` - Reschedule appointment
- `DELETE /appointments/{id}` - Cancel appointment
- `POST /appointments/{id}/confirm` - Confirm appointment
- `GET /appointments/patient/{patientId}` - Get patient appointments
- `GET /appointments/doctor/{doctorId}` - Get doctor appointments
- `GET /appointments/status/{status}` - Get appointments by status

## Example API Requests

### Register User
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePass123!",
    "fullName": "John Doe",
    "role": "PATIENT"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123!"
  }'
```

### Create Patient
```bash
curl -X POST http://localhost:8080/api/v1/patients \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "dateOfBirth": "1990-01-15",
    "gender": "F",
    "phoneNumber": "555-0123",
    "email": "jane@example.com",
    "bloodType": "O+",
    "allergies": "Penicillin"
  }'
```

### Book Appointment (with Double-Booking Prevention)
```bash
curl -X POST http://localhost:8080/api/v1/appointments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "patientId": "patient-uuid",
    "doctorId": "doctor-uuid",
    "appointmentDateTime": "2024-05-20T10:30:00",
    "durationMinutes": 30,
    "reason": "General Checkup",
    "idempotencyKey": "unique-key-123"
  }'
```

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthControllerTests

# Run with coverage
mvn clean test jacoco:report
# Open target/site/jacoco/index.html
```

## Project Structure

```
src/main/java/com/hospital
├── config/                  # Security, Redis, Swagger configs
├── common/                  # BaseEntity, ApiResponse, PageResponse
├── exception/               # GlobalExceptionHandler, custom exceptions
├── security/                # JWT filter and utilities
├── auth/                    # Authentication (register, login, refresh)
├── user/                    # User entity and roles
├── patient/                 # Patient management
├── doctor/                  # Doctor management
├── department/              # Department management
├── appointment/             # Appointment booking (with double-booking prevention)
├── medicalrecord/           # Medical records
├── prescription/            # Prescriptions
├── billing/                 # Billing and invoicing
├── lab/                     # Lab management
├── pharmacy/                # Pharmacy and inventory
├── ward/                    # Ward management
└── HospitalApplication.java
```

## Key Architectural Patterns

### 1. Optimistic Locking (Prevents Double-Booking)
Uses `@Version` column for concurrent update detection on Appointments.

### 2. Idempotency Keys
Prevents duplicate operations on retry for critical operations (booking, payment).

### 3. Soft Deletes
Clinical data never hard-deleted; uses `deletedAt` timestamp.

### 4. DTO Mapping
MapStruct for automatic Request/Response DTO conversion from entities.

### 5. Layered Architecture
- Controller → Service → Repository
- No business logic in controllers
- Transactional services with proper exception handling

## Security Features

- **JWT Authentication**: Stateless token-based authentication
- **Role-Based Access Control**: `@PreAuthorize` annotations on endpoints
- **Password Security**: BCrypt hashing with salt
- **Account Lockout**: After 5 failed login attempts
- **CORS**: Configured for trusted origins
- **Rate Limiting**: Bucket4j for API rate limiting
- **Secure Logging**: No sensitive data (passwords, tokens, PII) in logs

## Testing Strategy

- **Unit Tests**: Service layer logic with Mockito
- **Controller Tests**: MockMvc for REST endpoint testing
- **Integration Tests**: Testcontainers with real PostgreSQL
- **Security Tests**: JWT validation and authorization
- **Double-Booking Tests**: Concurrent appointment scenario
- **Billing Tests**: Invoice and payment tracking
- **Target Coverage**: 80%+

## Database

### Migrations
All schema changes via Flyway in `/src/main/resources/db/migration/`.

### Key Tables
- `users` - Authentication and roles
- `patients` - Patient demographics
- `doctors` - Doctor profiles and availability
- `departments` - Hospital departments
- `appointments` - Appointment bookings
- `medical_records` - Clinical records
- `prescriptions` - Medication prescriptions
- `invoices` - Billing records
- `lab_tests` - Lab orders and results
- `medications` - Pharmacy inventory
- `wards` - Ward management

### Indexes
Optimized indexes on frequently queried columns (MRN, appointment date, status, etc.).

## Deployment

### Docker Compose (Recommended)
```bash
docker-compose up -d
```

### Docker Build
```bash
docker build -t hospital-api:1.0.0 .
docker run -d \
  -e DB_HOST=postgres \
  -e DB_PASSWORD=postgres \
  -e JWT_SECRET=your-secret \
  -p 8080:8080 \
  hospital-api:1.0.0
```

### Production Checklist
- [ ] Change JWT_SECRET to strong random value
- [ ] Configure production database credentials
- [ ] Set up Redis with persistence
- [ ] Configure mail server for notifications
- [ ] Enable HTTPS
- [ ] Set up monitoring and alerting
- [ ] Configure automated backups
- [ ] Enable database connection pooling limits

## Performance Optimizations

- Database indexing on key columns
- Pagination on all list endpoints (default 20 items)
- Query optimization with @EntityGraph
- Redis caching for frequently accessed data
- HikariCP connection pooling (max 20 connections)
- JDBC batch processing (batch size 20)
- Virtual threads for async operations

## Monitoring

- Health checks: `/actuator/health`
- Liveness probe: `/actuator/health/liveness`
- Readiness probe: `/actuator/health/readiness`
- Metrics: `/actuator/metrics`
- Docker health checks configured

## Contributing

1. Fork repository
2. Create feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -am 'Add new feature'`)
4. Push branch (`git push origin feature/new-feature`)
5. Create Pull Request

## License

MIT License - see LICENSE for details

## Support

- GitHub Issues: [Report Bugs](https://github.com/khalid-syfullah/hospital-management-system/issues)
- Email: support@hospital-system.com
