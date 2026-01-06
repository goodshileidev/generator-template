-- Operator (User) Table
-- This table stores user account information including authentication, profile, and verification status

CREATE TABLE t_operator (
	operator_id VARCHAR(36) DEFAULT gen_random_uuid() NOT NULL, -- User ID (Primary Key)
	company_id VARCHAR(36) NULL, -- Company ID (Foreign Key)
	username VARCHAR(300) NULL, -- Username (for login)
	operator_name VARCHAR(300) NULL, -- Operator display name
	language VARCHAR(300) NULL, -- Preferred language
	session_auto_logout_wait_time_current_value VARCHAR(300) NULL, -- Session auto-logout current value
	associated_mobile_verification_status VARCHAR(300) NULL, -- Mobile verification status (yes/no)
	associated_email VARCHAR(300) NULL, -- Associated email address
	session_auto_logout_waiting_time VARCHAR(300) NULL, -- Session auto-logout waiting time setting
	mobile VARCHAR(300) NULL, -- Mobile phone number
	associated_email_verification_status VARCHAR(300) NULL, -- Email verification status (yes/no)
	login_welcome_message_setting_status VARCHAR(300) NULL, -- Login welcome message status
	usage_status VARCHAR(300) DEFAULT 'enabled' NULL, -- Usage status (enabled/disable)
	company_name VARCHAR(300) NULL, -- Company name (denormalized)
	description VARCHAR(300) NULL, -- User description/notes
	email VARCHAR(300) NULL, -- Primary email address
	old_password VARCHAR(300) NULL, -- Old password (for password change)
	new_password VARCHAR(300) NULL, -- New password (for password change)
	password VARCHAR(300) NULL, -- Password (BCrypt hashed)
	login_welcome_message VARCHAR(300) NULL, -- Custom login welcome message
	is_deleted INT DEFAULT 0 NULL, -- Soft delete flag (0=active, 1=deleted)
	created_at timestamp NULL, -- Record creation timestamp
	updated_at timestamp NULL, -- Record last update timestamp
	created_by VARCHAR(300) NULL, -- Creator user ID
	updated_by VARCHAR(300) NULL, -- Last updater user ID
	user_role VARCHAR(20) NULL, -- User role (admin/maintainer/normal)
	last_name VARCHAR(100) NULL, -- User last name
	first_name VARCHAR(100) NULL, -- User first name
	position_title VARCHAR(100) NULL, -- Job position/title
	power_station_id VARCHAR(1000) NULL, -- Power station ID (deprecated, use power_station_ids)
	power_station_ids VARCHAR(1000) NULL, -- Comma-separated power station IDs
	mobile_verification_code VARCHAR(100) NULL, -- Mobile verification code (6-digit)
	mobile_verification_code_send_time timestamp NULL, -- Mobile code send timestamp
	mobile_verification_code_verified_time timestamp NULL, -- Mobile code verification timestamp
	email_verification_code VARCHAR(100) NULL, -- Email verification code (6-digit)
	email_verification_code_send_time timestamp NULL, -- Email code send timestamp
	email_verification_code_verified_time timestamp NULL, -- Email code verification timestamp
	language VARCHAR(100) DEFAULT 'en-US' NULL, -- User interface language (en-US, ja-JP, zh-CN)
	CONSTRAINT t_operator_pkey PRIMARY KEY (operator_id)
);

-- Create indexes for frequently queried columns
CREATE INDEX idx_operator_username ON t_operator(username);
CREATE INDEX idx_operator_email ON t_operator(email);
CREATE INDEX idx_operator_company_id ON t_operator(company_id);
CREATE INDEX idx_operator_is_deleted ON t_operator(is_deleted);

-- Add comments
COMMENT ON TABLE t_operator IS 'User/Operator account table with authentication and profile information';
COMMENT ON COLUMN t_operator.password IS 'BCrypt hashed password (frontend sends SHA256, backend applies BCrypt)';
COMMENT ON COLUMN t_operator.email_verification_code IS 'Email verification code valid for 24 hours';
COMMENT ON COLUMN t_operator.user_role IS 'User role: admin (full access), maintainer (device management), normal (view only)';
