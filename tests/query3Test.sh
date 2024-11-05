#!/bin/bash

if [ "$#" -ne 6 ]; then
    echo "Usage: $0 <database_name> <username> <csv_path> <n> <from> <to>"
    exit 1
fi

DB_NAME="$1"
DB_USER="$2"
CSV_PATH="$3"
N="$4"
FROM="$5"
TO="$6"

rm -f "$CSV_PATH/query3Test.csv"

psql -U "$DB_USER" -d "$DB_NAME" <<EOF
\copy (WITH repeat_offenders AS (SELECT county, plate, infraction_id FROM tickets WHERE issue_date BETWEEN '$FROM' AND '$TO' GROUP BY county, plate, infraction_id HAVING COUNT(*) >= $N), repeat_offenders_per_county AS (SELECT county, COUNT(DISTINCT plate) as total_repeat_offenders FROM repeat_offenders GROUP BY county), driver_per_county AS (SELECT county, COUNT(DISTINCT plate) AS total_drivers FROM tickets WHERE issue_date BETWEEN '$FROM' AND '$TO' GROUP BY county) SELECT d.county AS "County", CASE WHEN d.total_drivers = 0 THEN '0.00%' WHEN COALESCE(r.total_repeat_offenders, 0) = 0 THEN '0.00%' ELSE TO_CHAR((COALESCE(r.total_repeat_offenders, 0) * 100.0) / NULLIF(d.total_drivers, 0), 'FM999.00') || '%' END AS "Percentage" FROM driver_per_county d LEFT JOIN repeat_offenders_per_county r ON d.county = r.county ORDER BY (COALESCE(r.total_repeat_offenders, 0) * 100.0) / NULLIF(d.total_drivers, 0) DESC) TO '$CSV_PATH/query3Test.csv' WITH (FORMAT csv, DELIMITER ';', HEADER);
EOF

if [ -s "$CSV_PATH/query3Test.csv" ]; then
    echo "Archivo CSV generado correctamente: $CSV_PATH/query3Test.csv"
else
    echo "Error: El archivo CSV no se generó o está vacío."
    exit 1
fi


if [ -f "$CSV_PATH/query3.csv" ]; then
    diff "$CSV_PATH/query3.csv" "$CSV_PATH/query3Test.csv" > query3Test.diff
    if [ -s query3Test.diff ]; then
        echo "Diferencias encontradas entre los archivos."
    else
        echo "Los archivos son idénticos."
    fi
fi
