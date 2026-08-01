-- ============================================================
-- Dr. Sanjit Kumar Physiotherapy Clinic — Database Schema
-- Engine: MySQL 8.x
--
-- NOTE: You usually do NOT need to run this manually.
-- Spring Boot (Hibernate, ddl-auto=update) will create/update
-- these tables automatically on first startup, based on the
-- Java entity classes. This file is provided so you can:
--   1) Inspect/understand the structure
--   2) Create the schema manually if you prefer full control
--   3) Recreate the DB on a new hosting provider quickly
-- ============================================================

CREATE DATABASE IF NOT EXISTS physio_clinic
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE physio_clinic;

-- ---------------------------------------------------------
-- Table: diseases  (physiotherapy conditions catalogue)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS diseases (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(150)  NOT NULL,
    short_description   VARCHAR(500),
    treatment_approach  VARCHAR(2000),
    min_recovery_days   INT,
    max_recovery_days   INT,
    sessions_per_week   INT,
    consultation_fee    DOUBLE,
    session_fee         DOUBLE,
    severity_level      VARCHAR(50)
);

-- ---------------------------------------------------------
-- Table: appointments  (consultation booking requests)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS appointments (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_name        VARCHAR(100)  NOT NULL,
    phone_number        VARCHAR(15)   NOT NULL,
    email               VARCHAR(100),
    age                 VARCHAR(10),
    gender              VARCHAR(10),
    preferred_date      DATE,
    preferred_time      VARCHAR(20),
    related_condition   VARCHAR(150),
    message             VARCHAR(1000),
    status              VARCHAR(30) DEFAULT 'PENDING',
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------
-- Table: payments  (fee payment records)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS payments (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id      BIGINT,
    patient_name        VARCHAR(100) NOT NULL,
    phone_number        VARCHAR(15)  NOT NULL,
    amount              DOUBLE NOT NULL,
    payment_purpose     VARCHAR(100),
    payment_mode        VARCHAR(30),
    transaction_ref     VARCHAR(100),
    status              VARCHAR(30) DEFAULT 'INITIATED',
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- ---------------------------------------------------------
-- Table: contact_messages  (contact form submissions)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS contact_messages (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(100) NOT NULL,
    phone_number        VARCHAR(15)  NOT NULL,
    email               VARCHAR(100),
    message             VARCHAR(1500),
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------
-- Handy admin queries
-- ---------------------------------------------------------
-- All appointments, most recent first:
-- SELECT * FROM appointments ORDER BY created_at DESC;

-- Total revenue collected:
-- SELECT SUM(amount) FROM payments WHERE status = 'SUCCESS';

-- Payments for a specific patient by phone number:
-- SELECT * FROM payments WHERE phone_number = '9800000000';
