# Hospital Management System Backend

Production-grade Spring Boot backend for a Hospital Management System.

## Tech Stack
- Java 21 (Virtual Threads)
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA (PostgreSQL)
- Redis (Caching)
- Flyway (Migrations)
- MapStruct & Lombok
- Docker & Docker Compose

## Getting Started

### Prerequisites
- Docker & Docker Compose
- Java 21 (for local development)
- Maven 3.9+

### Run with Docker
```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

### API Documentation
Swagger UI is available at: `http://localhost:8080/swagger-ui.html` (only in `dev` profile).

## Core Modules
- **Auth**: JWT-based login, register, and refresh token.
- **Patient**: MRN generation, medical history, and profile management.
- **Doctor & Department**: Scheduling and department assignments.
- **Appointment**: Booking with conflict detection and optimistic locking.
- **Clinical**: Medical records, prescriptions, and lab results.
- **Billing**: Invoicing with idempotency support.
- **Infrastructure**: Audit logging, notification outbox, and rate limiting.

## Project Structure
```text
com.hospital
├── auth/           # Authentication logic
├── user/           # User & Role entities
├── patient/        # Patient management
├── doctor/         # Doctor profiles & availability
├── appointment/    # Booking engine
├── billing/        # Invoicing & payments
├── common/         # Base classes & shared DTOs
├── config/         # Security & Infrastructure configs
└── exception/      # Global error handling
```

## Security
- Role-based access control (RBAC) via `@PreAuthorize`.
- JWT stateless authentication.
- Password hashing with BCrypt.

## Monitoring
- Spring Boot Actuator: `/actuator/health`, `/actuator/prometheus`.
