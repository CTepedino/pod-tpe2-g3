#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <database_name> <username>"
    exit 1
fi

DB_NAME="$1"
DB_USER="$2"

psql -U "$DB_USER" -d "$DB_NAME" <<EOF
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS agencies;
DROP TABLE IF EXISTS infractions;
EOF
