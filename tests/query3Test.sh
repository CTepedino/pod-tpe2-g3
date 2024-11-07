#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <database_name> <username> <n>"
    exit 1
fi

DB_NAME="$1"
DB_USER="$2"
CSV_PATH=./resources
N="$3"
FROM=2000-01-01
TO=2021-12-31

cd ../client/target/tpe2-g3-client-1.0-SNAPSHOT-bin/tpe2-g3-client-1.0-SNAPSHOT

sh query3.sh -Daddresses="$ADDRESSES" -Dcity=NYC  -DinPath=../../../../tests/resources/ -DoutPath=../../../../tests/resources/ -Dn="$N" -Dfrom=01/01/2000 -Dto=31/12/2021

cd ../../../../tests

psql -U "$DB_USER" -d "$DB_NAME" <<EOF
\copy (WITH repeat_offenders AS (SELECT county, plate, infraction_id FROM tickets WHERE issue_date BETWEEN '$FROM' AND '$TO' GROUP BY county, plate, infraction_id HAVING COUNT(*) >= $N), repeat_offenders_per_county AS (SELECT county, COUNT(DISTINCT plate) as total_repeat_offenders FROM repeat_offenders GROUP BY county), driver_per_county AS (SELECT county, COUNT(DISTINCT plate) AS total_drivers FROM tickets WHERE issue_date BETWEEN '$FROM' AND '$TO' GROUP BY county) SELECT d.county AS "County", CASE WHEN d.total_drivers = 0 THEN '0.00%' WHEN COALESCE(r.total_repeat_offenders, 0) = 0 THEN '0.00%' ELSE TO_CHAR((COALESCE(r.total_repeat_offenders, 0) * 100.0) / NULLIF(d.total_drivers, 0), 'FM990.00') || '%' END AS "Percentage" FROM driver_per_county d LEFT JOIN repeat_offenders_per_county r ON d.county = r.county ORDER BY (COALESCE(r.total_repeat_offenders, 0) * 100.0) / NULLIF(d.total_drivers, 0) DESC) TO '$CSV_PATH/query3Test.csv' WITH (FORMAT csv, DELIMITER ';', HEADER);
EOF

if [ -f "$CSV_PATH/query3.csv" ]; then
    diff "$CSV_PATH/query3.csv" "$CSV_PATH/query3Test.csv" > query3Test.diff
fi
