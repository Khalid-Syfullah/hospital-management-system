CREATE TABLE medical_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id UUID NOT NULL REFERENCES patients(id),
    doctor_id UUID NOT NULL REFERENCES doctors(id),
    diagnosis TEXT,
    icd10_code VARCHAR(20),
    symptoms TEXT,
    visit_notes TEXT,
    treatment_plan TEXT,
    attachment_paths TEXT,
    blood_pressure_systolic INT,
    blood_pressure_diastolic INT,
    heart_rate INT,
    temperature DECIMAL(5,2),
    weight_kg DECIMAL(6,2),
    height_cm DECIMAL(6,2),
    oxygen_saturation INT,
    respiratory_rate INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);

CREATE TABLE vital_sign_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    medical_record_id UUID NOT NULL REFERENCES medical_records(id) ON DELETE CASCADE,
    blood_pressure_systolic INT,
    blood_pressure_diastolic INT,
    heart_rate INT,
    temperature DECIMAL(5,2),
    weight_kg DECIMAL(6,2),
    height_cm DECIMAL(6,2),
    oxygen_saturation INT,
    respiratory_rate INT,
    notes VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);

CREATE INDEX idx_mr_patient_id ON medical_records(patient_id);
CREATE INDEX idx_mr_doctor_id ON medical_records(doctor_id);
