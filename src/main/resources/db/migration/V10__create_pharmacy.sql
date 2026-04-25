CREATE TABLE medications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    generic_name VARCHAR(100),
    brand VARCHAR(100),
    dosage_form VARCHAR(100),
    strength VARCHAR(50),
    unit_price DECIMAL(10,2),
    stock_quantity INT NOT NULL DEFAULT 0,
    reorder_level INT NOT NULL DEFAULT 10,
    expiry_date DATE,
    batch_number VARCHAR(50),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);

CREATE TABLE dispensing_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    prescription_id UUID REFERENCES prescriptions(id),
    patient_id UUID NOT NULL REFERENCES patients(id),
    medication_id UUID NOT NULL REFERENCES medications(id),
    quantity_dispensed INT NOT NULL,
    dispensed_at TIMESTAMP NOT NULL,
    dispensed_by VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);
