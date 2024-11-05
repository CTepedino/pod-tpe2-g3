#!/bin/bash

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <database_name> <username> <csv_path> <addresses>"
    exit 1
fi

DB_NAME="$1"
DB_USER="$2"
CSV_PATH="$3"
ADDRESSES="$4"

rm -f $CSV_PATH/query2Test.csv

cd ../client/target/tpe2-g3-client-1.0-SNAPSHOT-bin/tpe2-g3-client-1.0-SNAPSHOT

sh query2.sh -Daddresses="$ADDRESSES" -Dcity=NYC  -DinPath=../../../../tests/resources/ -DoutPath=../../../../tests/resources/

cd ../../../../tests

psql -U "$DB_USER" -d "$DB_NAME" <<EOF
\copy (WITH MonthlyRevenue AS (SELECT a.agency, EXTRACT(YEAR FROM t.issue_date) AS year, EXTRACT(MONTH FROM t.issue_date) AS month, SUM(t.fine) AS monthly_total FROM tickets t JOIN agencies a ON t.agency = a.agency GROUP BY a.agency, EXTRACT(YEAR FROM t.issue_date), EXTRACT(MONTH FROM t.issue_date)), YTDRevenue AS (SELECT agency, year, month, SUM(monthly_total) OVER (PARTITION BY agency, year ORDER BY month ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS ytd_total FROM MonthlyRevenue) SELECT agency, year, month, ytd_total FROM YTDRevenue WHERE ytd_total > 0 ORDER BY agency, year, month) TO '$CSV_PATH/query2Test.csv' WITH (FORMAT csv, DELIMITER ';', HEADER);
EOF

if [ -f $CSV_PATH/query2.csv ]; then
  diff $CSV_PATH/query2.csv $CSV_PATH/query2Test.csv > query2Test.diff
fi