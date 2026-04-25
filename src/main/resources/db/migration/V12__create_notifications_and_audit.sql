CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(40) NOT NULL,
    read_at TIMESTAMP,
    reference_id VARCHAR(36),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    deleted_at TIMESTAMP
);

CREATE TABLE notification_outbox (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipient VARCHAR(150) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    body TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,
    error_message TEXT,
    attempts INT NOT NULL DEFAULT 0
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(36),
    action VARCHAR(30) NOT NULL,
    changed_fields TEXT,
    old_values TEXT,
    new_values TEXT,
    actor VARCHAR(150) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(200)
);

CREATE INDEX idx_notif_user_id ON notifications(user_id);
CREATE INDEX idx_notif_read ON notifications(read_at);
CREATE INDEX idx_audit_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_actor ON audit_logs(actor);
CREATE INDEX idx_audit_timestamp ON audit_logs(timestamp);
