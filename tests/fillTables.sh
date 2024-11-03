#!/bin/bash

#Espera el formato de csv de NYC

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <database_name> <username> <csv_path>"
    exit 1
fi

DB_NAME="$1"
DB_USER="$2"
CSV_PATH="$3"

psql -U "$DB_USER" -d "$DB_NAME" <<EOF
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS agencies;
DROP TABLE IF EXISTS infractions;

CREATE TABLE IF NOT EXISTS tickets (
                                       plate VARCHAR(10),
    infraction_id INT,
    fine INT,
    agency VARCHAR(255),
    issue_date DATE,
    county VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS infractions (
                                           id INT PRIMARY KEY,
                                           description VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS agencies (
                                        agency VARCHAR(255) PRIMARY KEY
    );

\copy agencies (agency) FROM '$CSV_PATH/agenciesNYC.csv' WITH (FORMAT csv, DELIMITER ';', HEADER);

\copy infractions (id, description) FROM '$CSV_PATH/infractionsNYC.csv' WITH (FORMAT csv, DELIMITER ';', HEADER);

CREATE TABLE IF NOT EXISTS tickets_staging (
                                               plate VARCHAR(10),
    infraction_id INT,
    fine NUMERIC,
    agency VARCHAR(255),
    issue_date DATE,
    county VARCHAR(255)
    );

\copy tickets_staging (plate, infraction_id, fine, agency, issue_date, county) FROM '$CSV_PATH/ticketsNYC.csv' WITH (FORMAT csv, DELIMITER ';', HEADER);

INSERT INTO tickets (plate, infraction_id, fine, agency, issue_date, county)
SELECT plate, infraction_id, ROUND(fine), agency, issue_date, county
FROM tickets_staging;

DROP TABLE tickets_staging;
EOF