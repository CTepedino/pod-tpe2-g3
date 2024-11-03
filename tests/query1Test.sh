#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <database_name> <username> <csv_path>"
    exit 1
fi

DB_NAME="$1"
DB_USER="$2"
CSV_PATH="$3"

rm -f $CSV_PATH/query1Test.csv


psql -U "$DB_USER" -d "$DB_NAME" <<EOF
\copy (SELECT description AS Infraction, t.agency AS Agency, COUNT(*) AS Tickets FROM tickets t JOIN infractions i ON t.infraction_id = i.id JOIN agencies a ON t.agency = a.agency GROUP BY i.description, t.agency HAVING COUNT(*) > 0 ORDER BY Tickets DESC, Infraction, t.agency) TO '$CSV_PATH/query1Test.csv' WITH (FORMAT csv, DELIMITER ';', HEADER);
EOF

if [ -f $CSV_PATH/query1.csv ]; then
  diff $CSV_PATH/query1.csv $CSV_PATH/query1Test.csv > query1Test.diff
fi