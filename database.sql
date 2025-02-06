CREATE TABLE swift_codes (
    id SERIAL PRIMARY KEY,
    swift_code VARCHAR(11) NOT NULL UNIQUE, -- Full SWIFT code (11 characters)
    code_type VARCHAR(10) NOT NULL, -- "HEADQUARTERS" or "BRANCH"
    institution_name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    town_name VARCHAR(100) NOT NULL,
    country_iso2 CHAR(2) NOT NULL, -- ISO-2 country code (uppercase)
    country_name VARCHAR(100) NOT NULL, -- Full country name (uppercase)
    time_zone VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    headquarters_id INT REFERENCES swift_codes(id) -- Self-referencing foreign key for branches
);