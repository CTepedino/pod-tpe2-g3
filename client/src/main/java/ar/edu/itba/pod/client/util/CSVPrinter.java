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
            for (String column : columns){
                writer.write(column);
                writer.write(separator);
            }
            writer.write('\n');
            for (CSVPrintable record : records) {
                writer.write(record.printAsCSV(separator));
                writer.write('\n');
            }
            writer.close();
        } catch (IOException e){
            logger.error("Can't write to file {}", outPath);
            throw new IllegalStateException();
        }
    }
}
