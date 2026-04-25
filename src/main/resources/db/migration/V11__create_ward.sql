CREATE TABLE wards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    ward_type VARCHAR(50),
    floor VARCHAR(10),
    description TEXT,
    department_id UUID REFERENCES departments(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);

CREATE TABLE beds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ward_id UUID NOT NULL REFERENCES wards(id) ON DELETE CASCADE,
    bed_number VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    bed_type VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);

CREATE TABLE admissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id UUID NOT NULL REFERENCES patients(id),
    bed_id UUID NOT NULL REFERENCES beds(id),
    doctor_id UUID REFERENCES doctors(id),
    admission_time TIMESTAMP NOT NULL,
    discharge_time TIMESTAMP,
    admission_reason TEXT,
    discharge_notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);

CREATE INDEX idx_beds_ward_id ON beds(ward_id);
CREATE INDEX idx_beds_status ON beds(status);
CREATE INDEX idx_admission_patient_id ON admissions(patient_id);
CREATE INDEX idx_admission_bed_id ON admissions(bed_id);
