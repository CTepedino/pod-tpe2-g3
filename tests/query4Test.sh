#!/bin/bash

if [ "$#" -ne 6 ]; then
    echo "Usage: $0 <database_name> <username> <csv_path> <addresses> <n> <agency>"
    exit 1
fi

DB_NAME="$1"
DB_USER="$2"
CSV_PATH="$3"
ADDRESSES="$4"
N="$5"
AGENCY="$6"

rm -f $CSV_PATH/query4Test.csv

cd ../client/target/tpe2-g3-client-1.0-SNAPSHOT-bin/tpe2-g3-client-1.0-SNAPSHOT

sh query4.sh -Daddresses="$ADDRESSES" -Dcity=NYC  -DinPath=../../../../tests/resources/ -DoutPath=../../../../tests/resources/ -Dn="$N" -Dagency="$AGENCY"

cd ../../../../tests

psql -U "$DB_USER" -d "$DB_NAME" <<EOF
\copy (SELECT i.description AS infraction, MIN(t.fine) AS min_fine, MAX(t.fine) AS max_fine, (MAX(t.fine) - MIN(t.fine)) AS fine_difference FROM tickets t JOIN infractions i ON t.infraction_id = i.id WHERE t.agency = REPLACE('$AGENCY', '_', ' ') GROUP BY t.infraction_id, i.description ORDER BY fine_difference DESC, infraction ASC LIMIT $N) TO '$CSV_PATH/query4Test.csv' WITH (FORMAT csv, DELIMITER ';', HEADER);
EOF

if [ -f $CSV_PATH/query4.csv ]; then
  diff $CSV_PATH/query4.csv $CSV_PATH/query4Test.csv > query4Test.diff
fi