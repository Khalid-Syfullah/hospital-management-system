CREATE TABLE lab_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patient_id UUID NOT NULL REFERENCES patients(id),
    doctor_id UUID NOT NULL REFERENCES doctors(id),
    test_name VARCHAR(200) NOT NULL,
    test_code VARCHAR(100),
    clinical_notes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'REQUESTED',
    priority VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);

CREATE TABLE lab_results (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lab_order_id UUID NOT NULL UNIQUE REFERENCES lab_orders(id) ON DELETE CASCADE,
    findings TEXT,
    reference_range TEXT,
    interpretation VARCHAR(50),
    result_date_time TIMESTAMP,
    performed_by VARCHAR(100),
    attachment_path TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);

CREATE INDEX idx_lab_patient_id ON lab_orders(patient_id);
CREATE INDEX idx_lab_doctor_id ON lab_orders(doctor_id);
