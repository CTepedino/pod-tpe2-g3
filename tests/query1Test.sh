#!/bin/bash

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <database_name> <username> <csv_path> <addresses>"
    exit 1
fi

DB_NAME="$1"
DB_USER="$2"
CSV_PATH="$3"
ADDRESSES="$4"

rm -f $CSV_PATH/query1Test.csv

cd ../client/target/tpe2-g3-client-1.0-SNAPSHOT-bin/tpe2-g3-client-1.0-SNAPSHOT

sh query1.sh -Daddresses="$ADDRESSES" -Dcity=NYC  -DinPath=../../../../tests/resources/ -DoutPath=../../../../tests/resources/

cd ../../../../tests

psql -U "$DB_USER" -d "$DB_NAME" <<EOF
\copy (SELECT description AS Infraction, t.agency AS Agency, COUNT(*) AS Tickets FROM tickets t JOIN infractions i ON t.infraction_id = i.id JOIN agencies a ON t.agency = a.agency GROUP BY i.description, t.agency HAVING COUNT(*) > 0 ORDER BY Tickets DESC, Infraction, t.agency) TO '$CSV_PATH/query1Test.csv' WITH (FORMAT csv, DELIMITER ';', HEADER);
EOF

if [ -f $CSV_PATH/query1.csv ]; then
  diff $CSV_PATH/query1.csv $CSV_PATH/query1Test.csv > query1Test.diff
fi