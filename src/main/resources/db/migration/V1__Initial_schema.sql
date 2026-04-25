create table if not exists users (
    id uuid primary key,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    created_by varchar(255),
    updated_by varchar(255),
    deleted_at timestamptz,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    full_name varchar(255) not null,
    phone varchar(255),
    enabled boolean not null,
    account_locked boolean not null,
    failed_login_attempts integer not null,
    locked_until timestamptz
);
create table if not exists user_roles (user_id uuid not null references users(id), role varchar(64) not null);
create table if not exists refresh_tokens (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, token varchar(600) not null unique, user_id uuid not null references users(id), expires_at timestamptz not null, revoked boolean not null);
create table if not exists departments (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, name varchar(255) not null unique, description varchar(1000), head_doctor_id uuid);
create table if not exists doctors (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, full_name varchar(255) not null, license_number varchar(255) not null unique, credentials varchar(255), availability varchar(2000), department_id uuid references departments(id), user_id uuid references users(id));
alter table departments add constraint fk_department_head_doctor foreign key (head_doctor_id) references doctors(id);
create table if not exists doctor_specializations (doctor_id uuid not null references doctors(id), specializations varchar(255));
create table if not exists patients (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, mrn varchar(255) not null unique, full_name varchar(255) not null, gender varchar(32) not null, date_of_birth date, phone varchar(255), email varchar(255), address varchar(2000), medical_history varchar(2000), allergies varchar(1000), chronic_conditions varchar(1000), emergency_contact_name varchar(255), emergency_contact_phone varchar(255), insurance_provider varchar(255), insurance_policy_number varchar(255), user_id uuid references users(id));
create table if not exists patient_vitals (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, patient_id uuid not null references patients(id), blood_pressure varchar(255), heart_rate integer, temperature_celsius numeric(8,2), weight_kg numeric(8,2), height_cm numeric(8,2), measured_at timestamptz);
create table if not exists appointments (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, patient_id uuid not null references patients(id), doctor_id uuid not null references doctors(id), start_time timestamptz not null, end_time timestamptz not null, status varchar(32) not null, idempotency_key varchar(255) not null unique, reason varchar(255), version bigint not null);
create table if not exists medical_records (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, patient_id uuid not null references patients(id), doctor_id uuid not null references doctors(id), icd10_code varchar(255), diagnoses varchar(2000), symptoms varchar(2000), visit_notes varchar(4000), attachment_path varchar(2000));
create table if not exists prescriptions (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, patient_id uuid not null references patients(id), doctor_id uuid not null references doctors(id), medicine_name varchar(255) not null, dosage varchar(255) not null, frequency varchar(255) not null, duration varchar(255) not null, instructions varchar(2000), status varchar(32) not null);
create table if not exists lab_orders (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, patient_id uuid not null references patients(id), test_name varchar(255) not null, status varchar(32) not null, result varchar(4000));
create table if not exists invoices (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, patient_id uuid not null references patients(id), total numeric(12,2) not null, status varchar(32) not null, insurance_claim_number varchar(255), payment_idempotency_key varchar(255) unique, version bigint not null);
create table if not exists invoice_items (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, invoice_id uuid not null references invoices(id), description varchar(255) not null, unit_price numeric(12,2) not null, quantity integer not null, line_total numeric(12,2) not null);
create table if not exists medications (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, name varchar(255) not null, strength varchar(255), form varchar(255), stock_quantity integer not null, low_stock_threshold integer not null, expiry_date date, version bigint not null);
create table if not exists wards (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, name varchar(255) not null, floor varchar(255));
create table if not exists beds (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, ward_id uuid not null references wards(id), bed_number varchar(255) not null, status varchar(32) not null, patient_id uuid references patients(id));
create table if not exists notifications (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, type varchar(32) not null, recipient varchar(255) not null, subject varchar(255) not null, body varchar(4000) not null, read_at timestamptz);
create table if not exists notification_outbox (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, event_type varchar(255) not null, subject varchar(255) not null, payload varchar(4000) not null, processed boolean not null, processed_at timestamptz);
create table if not exists audit_logs (id uuid primary key, created_at timestamptz not null, updated_at timestamptz not null, created_by varchar(255), updated_by varchar(255), deleted_at timestamptz, entity_type varchar(255) not null, entity_id varchar(255) not null, action varchar(255) not null, changed_fields varchar(4000), actor varchar(255) not null, timestamp timestamptz not null);
create index if not exists idx_patient_mrn on patients(mrn);
create index if not exists idx_appointment_date on appointments(start_time);
create index if not exists idx_appointment_doctor_start on appointments(doctor_id, start_time);
create index if not exists idx_billing_status on invoices(status);
create index if not exists idx_audit_entity on audit_logs(entity_type, entity_id);
