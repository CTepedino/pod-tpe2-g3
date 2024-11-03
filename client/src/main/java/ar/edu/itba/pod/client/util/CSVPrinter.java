package ar.edu.itba.pod.client.util;

import ar.edu.itba.pod.api.model.CSVPrintable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

public class CSVPrinter {
    private static final Logger logger = LoggerFactory.getLogger(CSVPrinter.class);

    private final String[] columns;
    private final char separator;

    public CSVPrinter(String[] columns, char separator){
        this.columns = columns;
        this.separator = separator;
    }

    public CSVPrinter(String[] columns){
        this(columns, ';');
    }

    public void print(String outPath, Iterable<? extends CSVPrintable> records){
        try (FileWriter writer = new FileWriter(outPath, false)){

            for (int i = 0 ; i < columns.length -1; i++){
                writer.write(columns[i]);
                writer.write(separator);
            }
            writer.write(columns[columns.length-1] + '\n');

            for (CSVPrintable record : records) {
                writer.write(record.printAsCSV(separator));
                writer.write('\n');
            }

            writer.flush();
        } catch (IOException e){
            logger.error("Can't write to file {}", outPath);
            throw new IllegalStateException();
        }
    }
}
