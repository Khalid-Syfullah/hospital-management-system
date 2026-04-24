# Graph Report - hospital-management-system  (2026-04-25)

## Corpus Check
- 115 files · ~9,076 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 555 nodes · 661 edges · 59 communities detected
- Extraction: 76% EXTRACTED · 24% INFERRED · 0% AMBIGUOUS · INFERRED: 161 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]
- [[_COMMUNITY_Community 6|Community 6]]
- [[_COMMUNITY_Community 7|Community 7]]
- [[_COMMUNITY_Community 8|Community 8]]
- [[_COMMUNITY_Community 9|Community 9]]
- [[_COMMUNITY_Community 10|Community 10]]
- [[_COMMUNITY_Community 11|Community 11]]
- [[_COMMUNITY_Community 12|Community 12]]
- [[_COMMUNITY_Community 13|Community 13]]
- [[_COMMUNITY_Community 14|Community 14]]
- [[_COMMUNITY_Community 15|Community 15]]
- [[_COMMUNITY_Community 16|Community 16]]
- [[_COMMUNITY_Community 17|Community 17]]
- [[_COMMUNITY_Community 18|Community 18]]
- [[_COMMUNITY_Community 19|Community 19]]
- [[_COMMUNITY_Community 20|Community 20]]
- [[_COMMUNITY_Community 21|Community 21]]
- [[_COMMUNITY_Community 22|Community 22]]
- [[_COMMUNITY_Community 23|Community 23]]
- [[_COMMUNITY_Community 24|Community 24]]
- [[_COMMUNITY_Community 25|Community 25]]
- [[_COMMUNITY_Community 26|Community 26]]
- [[_COMMUNITY_Community 27|Community 27]]
- [[_COMMUNITY_Community 28|Community 28]]
- [[_COMMUNITY_Community 29|Community 29]]
- [[_COMMUNITY_Community 30|Community 30]]
- [[_COMMUNITY_Community 31|Community 31]]
- [[_COMMUNITY_Community 32|Community 32]]
- [[_COMMUNITY_Community 33|Community 33]]
- [[_COMMUNITY_Community 34|Community 34]]
- [[_COMMUNITY_Community 35|Community 35]]
- [[_COMMUNITY_Community 36|Community 36]]
- [[_COMMUNITY_Community 37|Community 37]]
- [[_COMMUNITY_Community 38|Community 38]]
- [[_COMMUNITY_Community 39|Community 39]]
- [[_COMMUNITY_Community 40|Community 40]]
- [[_COMMUNITY_Community 41|Community 41]]
- [[_COMMUNITY_Community 42|Community 42]]
- [[_COMMUNITY_Community 43|Community 43]]
- [[_COMMUNITY_Community 44|Community 44]]
- [[_COMMUNITY_Community 45|Community 45]]
- [[_COMMUNITY_Community 46|Community 46]]
- [[_COMMUNITY_Community 47|Community 47]]
- [[_COMMUNITY_Community 48|Community 48]]
- [[_COMMUNITY_Community 49|Community 49]]
- [[_COMMUNITY_Community 50|Community 50]]
- [[_COMMUNITY_Community 51|Community 51]]
- [[_COMMUNITY_Community 52|Community 52]]
- [[_COMMUNITY_Community 53|Community 53]]
- [[_COMMUNITY_Community 54|Community 54]]
- [[_COMMUNITY_Community 55|Community 55]]
- [[_COMMUNITY_Community 56|Community 56]]
- [[_COMMUNITY_Community 57|Community 57]]
- [[_COMMUNITY_Community 58|Community 58]]

## God Nodes (most connected - your core abstractions)
1. `success()` - 26 edges
2. `from()` - 15 edges
3. `User` - 14 edges
4. `Patient` - 14 edges
5. `Appointment` - 13 edges
6. `Invoice` - 13 edges
7. `Medication` - 10 edges
8. `Prescription` - 10 edges
9. `BaseEntity` - 10 edges
10. `MedicalRecord` - 10 edges

## Surprising Connections (you probably didn't know these)
- `Medication` --extends--> `BaseEntity`  [EXTRACTED]
  src/main/java/com/hospital/pharmacy/Medication.java →   _Bridges community 12 → community 2_
- `Appointment` --extends--> `BaseEntity`  [EXTRACTED]
  src/main/java/com/hospital/appointment/Appointment.java →   _Bridges community 2 → community 5_
- `OutboxEvent` --extends--> `BaseEntity`  [EXTRACTED]
  src/main/java/com/hospital/notification/OutboxEvent.java →   _Bridges community 2 → community 8_
- `Prescription` --extends--> `BaseEntity`  [EXTRACTED]
  src/main/java/com/hospital/prescription/Prescription.java →   _Bridges community 2 → community 13_
- `User` --extends--> `BaseEntity`  [EXTRACTED]
  src/main/java/com/hospital/user/User.java →   _Bridges community 2 → community 3_

## Communities

### Community 0 - "Community 0"
Cohesion: 0.04
Nodes (14): success(), AppointmentController, AuthController, DepartmentController, DoctorController, LabController, MedicalRecordController, NotificationController (+6 more)

### Community 1 - "Community 1"
Cohesion: 0.05
Nodes (13): AuditService, BillingMapper, DepartmentService, DoctorRepository, LabService, MedicalRecordService, PatientNotFoundException, PatientService (+5 more)

### Community 2 - "Community 2"
Cohesion: 0.04
Nodes (8): BaseEntity, Bed, Department, Doctor, LabOrder, MedicalRecord, Notification, Ward

### Community 3 - "Community 3"
Cohesion: 0.09
Nodes (6): AuthService, AuthServiceTest, CustomUserDetailsService, User, UserDetailsService, UserRepository

### Community 4 - "Community 4"
Cohesion: 0.07
Nodes (4): BillingItem, BillingService, Invoice, InvoiceRepository

### Community 5 - "Community 5"
Cohesion: 0.1
Nodes (5): Appointment, JwtAuthenticationFilter, OncePerRequestFilter, RateLimitingFilter, RequestTracingFilter

### Community 6 - "Community 6"
Cohesion: 0.14
Nodes (4): AppointmentRepository, AppointmentService, AppointmentServiceTest, NotificationService

### Community 7 - "Community 7"
Cohesion: 0.11
Nodes (2): DoctorService, Patient

### Community 8 - "Community 8"
Cohesion: 0.12
Nodes (3): JwtService, OutboxEvent, SecurityIntegrationTest

### Community 9 - "Community 9"
Cohesion: 0.29
Nodes (2): failure(), GlobalExceptionHandler

### Community 10 - "Community 10"
Cohesion: 0.18
Nodes (3): BillingController, BillingServiceTest, PatientServiceTest

### Community 11 - "Community 11"
Cohesion: 0.18
Nodes (1): BaseEntity

### Community 12 - "Community 12"
Cohesion: 0.2
Nodes (1): Medication

### Community 13 - "Community 13"
Cohesion: 0.2
Nodes (1): Prescription

### Community 14 - "Community 14"
Cohesion: 0.2
Nodes (1): AuditLog

### Community 15 - "Community 15"
Cohesion: 0.2
Nodes (4): InsufficientStockException, ResourceNotFoundException, RuntimeException, SlotUnavailableException

### Community 16 - "Community 16"
Cohesion: 0.29
Nodes (1): SecurityConfig

### Community 17 - "Community 17"
Cohesion: 0.29
Nodes (2): AuditController, AuditLogRepository

### Community 18 - "Community 18"
Cohesion: 0.4
Nodes (1): PatientRepository

### Community 19 - "Community 19"
Cohesion: 0.67
Nodes (1): HospitalApplication

### Community 20 - "Community 20"
Cohesion: 0.67
Nodes (1): PharmacyDtos

### Community 21 - "Community 21"
Cohesion: 0.67
Nodes (1): MedicationMapper

### Community 22 - "Community 22"
Cohesion: 0.67
Nodes (1): AppointmentMapper

### Community 23 - "Community 23"
Cohesion: 0.67
Nodes (1): AppointmentDtos

### Community 24 - "Community 24"
Cohesion: 0.67
Nodes (1): AuditConfig

### Community 25 - "Community 25"
Cohesion: 0.67
Nodes (1): OpenApiConfig

### Community 26 - "Community 26"
Cohesion: 0.67
Nodes (1): AsyncConfig

### Community 27 - "Community 27"
Cohesion: 0.67
Nodes (1): CacheConfig

### Community 28 - "Community 28"
Cohesion: 0.67
Nodes (1): AuthDtos

### Community 29 - "Community 29"
Cohesion: 0.67
Nodes (1): NotificationMapper

### Community 30 - "Community 30"
Cohesion: 0.67
Nodes (1): OutboxEventRepository

### Community 31 - "Community 31"
Cohesion: 0.67
Nodes (1): NotificationDtos

### Community 32 - "Community 32"
Cohesion: 0.67
Nodes (1): DoctorMapper

### Community 33 - "Community 33"
Cohesion: 0.67
Nodes (1): DoctorDtos

### Community 34 - "Community 34"
Cohesion: 0.67
Nodes (1): LabDtos

### Community 35 - "Community 35"
Cohesion: 0.67
Nodes (1): LabOrderMapper

### Community 36 - "Community 36"
Cohesion: 0.67
Nodes (1): PrescriptionMapper

### Community 37 - "Community 37"
Cohesion: 0.67
Nodes (1): PrescriptionDtos

### Community 38 - "Community 38"
Cohesion: 0.67
Nodes (1): UserDtos

### Community 39 - "Community 39"
Cohesion: 0.67
Nodes (1): UserMapper

### Community 40 - "Community 40"
Cohesion: 0.67
Nodes (1): DepartmentDtos

### Community 41 - "Community 41"
Cohesion: 0.67
Nodes (1): DepartmentMapper

### Community 42 - "Community 42"
Cohesion: 0.67
Nodes (1): AuditDtos

### Community 43 - "Community 43"
Cohesion: 0.67
Nodes (1): AuditMapper

### Community 44 - "Community 44"
Cohesion: 0.67
Nodes (1): PatientMapper

### Community 45 - "Community 45"
Cohesion: 0.67
Nodes (1): PatientDtos

### Community 46 - "Community 46"
Cohesion: 0.67
Nodes (1): WardDtos

### Community 47 - "Community 47"
Cohesion: 0.67
Nodes (1): WardMapper

### Community 48 - "Community 48"
Cohesion: 0.67
Nodes (1): MedicalRecordMapper

### Community 49 - "Community 49"
Cohesion: 0.67
Nodes (1): MedicalRecordDtos

### Community 50 - "Community 50"
Cohesion: 0.67
Nodes (1): BillingDtos

### Community 51 - "Community 51"
Cohesion: 1.0
Nodes (1): MedicationRepository

### Community 52 - "Community 52"
Cohesion: 1.0
Nodes (1): NotificationRepository

### Community 53 - "Community 53"
Cohesion: 1.0
Nodes (1): LabOrderRepository

### Community 54 - "Community 54"
Cohesion: 1.0
Nodes (1): PrescriptionRepository

### Community 55 - "Community 55"
Cohesion: 1.0
Nodes (1): DepartmentRepository

### Community 56 - "Community 56"
Cohesion: 1.0
Nodes (1): BedRepository

### Community 57 - "Community 57"
Cohesion: 1.0
Nodes (1): WardRepository

### Community 58 - "Community 58"
Cohesion: 1.0
Nodes (1): MedicalRecordRepository

## Knowledge Gaps
- **8 isolated node(s):** `MedicationRepository`, `NotificationRepository`, `LabOrderRepository`, `PrescriptionRepository`, `DepartmentRepository` (+3 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **Thin community `Community 7`** (20 nodes): `DoctorService`, `.create()`, `.DoctorService()`, `.list()`, `.resolveDepartment()`, `DoctorService.java`, `Patient.java`, `Patient`, `.getAllergies()`, `.getChronicConditions()`, `.getDateOfBirth()`, `.getEmergencyContact()`, `.getFullName()`, `.getInsurancePolicyNumber()`, `.getInsuranceProvider()`, `.getMedicalHistory()`, `.getMrn()`, `.getPhone()`, `.Patient()`, `.update()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 9`** (13 nodes): `failure()`, `GlobalExceptionHandler`, `.conflict()`, `.constraint()`, `.forbidden()`, `.formatFieldError()`, `.generic()`, `.notFound()`, `.unauthorized()`, `.validation()`, `ApiResponse.java`, `GlobalExceptionHandler.java`, `.getMessage()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 11`** (11 nodes): `BaseEntity`, `.getCreatedAt()`, `.getCreatedBy()`, `.getDeletedAt()`, `.getId()`, `.getUpdatedAt()`, `.getUpdatedBy()`, `.prePersist()`, `.preUpdate()`, `.softDelete()`, `BaseEntity.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 12`** (10 nodes): `Medication.java`, `Medication`, `.dispense()`, `.getExpiryDate()`, `.getForm()`, `.getLowStockThreshold()`, `.getName()`, `.getStockQuantity()`, `.getVersion()`, `.Medication()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 13`** (10 nodes): `Prescription.java`, `Prescription`, `.getDoctor()`, `.getDosage()`, `.getDuration()`, `.getFrequency()`, `.getInstructions()`, `.getMedicineName()`, `.getPatient()`, `.Prescription()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 14`** (10 nodes): `AuditLog`, `.AuditLog()`, `.getAction()`, `.getActor()`, `.getChangedFields()`, `.getEntityId()`, `.getEntityType()`, `.getId()`, `.getTimestamp()`, `AuditLog.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 16`** (7 nodes): `SecurityConfig.java`, `SecurityConfig`, `.authenticationManager()`, `.authenticationProvider()`, `.corsConfigurationSource()`, `.passwordEncoder()`, `.securityFilterChain()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 17`** (7 nodes): `AuditController`, `.AuditController()`, `.list()`, `AuditLogRepository`, `.findByEntityTypeIgnoreCase()`, `AuditController.java`, `AuditLogRepository.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 18`** (5 nodes): `PatientRepository.java`, `PatientRepository`, `.findByDeletedAtIsNull()`, `.findByMrnAndDeletedAtIsNull()`, `.list()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 19`** (3 nodes): `HospitalApplication`, `.main()`, `HospitalApplication.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 20`** (3 nodes): `PharmacyDtos.java`, `PharmacyDtos`, `.PharmacyDtos()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 21`** (3 nodes): `MedicationMapper.java`, `MedicationMapper`, `.toResponse()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 22`** (3 nodes): `AppointmentMapper`, `.toResponse()`, `AppointmentMapper.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 23`** (3 nodes): `AppointmentDtos`, `.AppointmentDtos()`, `AppointmentDtos.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 24`** (3 nodes): `AuditConfig`, `.auditorAware()`, `AuditConfig.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 25`** (3 nodes): `OpenApiConfig.java`, `OpenApiConfig`, `.hospitalOpenApi()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 26`** (3 nodes): `AsyncConfig`, `.applicationTaskExecutor()`, `AsyncConfig.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 27`** (3 nodes): `CacheConfig`, `.cacheManager()`, `CacheConfig.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 28`** (3 nodes): `AuthDtos`, `.AuthDtos()`, `AuthDtos.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 29`** (3 nodes): `NotificationMapper.java`, `NotificationMapper`, `.toResponse()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 30`** (3 nodes): `OutboxEventRepository.java`, `OutboxEventRepository`, `.findTop50ByProcessedAtIsNullOrderByCreatedAtAsc()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 31`** (3 nodes): `NotificationDtos.java`, `NotificationDtos`, `.NotificationDtos()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 32`** (3 nodes): `DoctorMapper`, `.toResponse()`, `DoctorMapper.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 33`** (3 nodes): `DoctorDtos`, `.DoctorDtos()`, `DoctorDtos.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 34`** (3 nodes): `LabDtos`, `.LabDtos()`, `LabDtos.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 35`** (3 nodes): `LabOrderMapper`, `.toResponse()`, `LabOrderMapper.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 36`** (3 nodes): `PrescriptionMapper.java`, `PrescriptionMapper`, `.toResponse()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 37`** (3 nodes): `PrescriptionDtos.java`, `PrescriptionDtos`, `.PrescriptionDtos()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 38`** (3 nodes): `UserDtos.java`, `UserDtos`, `.UserDtos()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 39`** (3 nodes): `UserMapper.java`, `UserMapper`, `.toResponse()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 40`** (3 nodes): `DepartmentDtos`, `.DepartmentDtos()`, `DepartmentDtos.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 41`** (3 nodes): `DepartmentMapper`, `.toResponse()`, `DepartmentMapper.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 42`** (3 nodes): `AuditDtos`, `.AuditDtos()`, `AuditDtos.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 43`** (3 nodes): `AuditMapper`, `.toResponse()`, `AuditMapper.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 44`** (3 nodes): `PatientMapper.java`, `PatientMapper`, `.toResponse()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 45`** (3 nodes): `PatientDtos.java`, `PatientDtos`, `.PatientDtos()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 46`** (3 nodes): `WardDtos.java`, `WardDtos`, `.WardDtos()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 47`** (3 nodes): `WardMapper.java`, `WardMapper`, `.toResponse()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 48`** (3 nodes): `MedicalRecordMapper.java`, `MedicalRecordMapper`, `.toResponse()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 49`** (3 nodes): `MedicalRecordDtos.java`, `MedicalRecordDtos`, `.MedicalRecordDtos()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 50`** (3 nodes): `BillingDtos`, `.BillingDtos()`, `BillingDtos.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 51`** (2 nodes): `MedicationRepository.java`, `MedicationRepository`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 52`** (2 nodes): `NotificationRepository.java`, `NotificationRepository`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 53`** (2 nodes): `LabOrderRepository`, `LabOrderRepository.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 54`** (2 nodes): `PrescriptionRepository.java`, `PrescriptionRepository`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 55`** (2 nodes): `DepartmentRepository`, `DepartmentRepository.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 56`** (2 nodes): `BedRepository`, `BedRepository.java`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 57`** (2 nodes): `WardRepository.java`, `WardRepository`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.
- **Thin community `Community 58`** (2 nodes): `MedicalRecordRepository.java`, `MedicalRecordRepository`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `success()` connect `Community 0` to `Community 8`, `Community 9`, `Community 10`, `Community 4`?**
  _High betweenness centrality (0.117) - this node is a cross-community bridge._
- **Why does `from()` connect `Community 0` to `Community 17`, `Community 10`, `Community 3`?**
  _High betweenness centrality (0.080) - this node is a cross-community bridge._
- **Why does `User` connect `Community 3` to `Community 2`?**
  _High betweenness centrality (0.079) - this node is a cross-community bridge._
- **Are the 25 inferred relationships involving `success()` (e.g. with `.create()` and `.dispense()`) actually correct?**
  _`success()` has 25 INFERRED edges - model-reasoned connections that need verification._
- **Are the 14 inferred relationships involving `from()` (e.g. with `.list()` and `.list()`) actually correct?**
  _`from()` has 14 INFERRED edges - model-reasoned connections that need verification._
- **What connects `MedicationRepository`, `NotificationRepository`, `LabOrderRepository` to the rest of the system?**
  _8 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.04 - nodes in this community are weakly interconnected._