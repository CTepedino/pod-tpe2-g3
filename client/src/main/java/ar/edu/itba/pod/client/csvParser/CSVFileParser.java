package ar.edu.itba.pod.client.csvParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CSVFileParser<T> {
    private static final Logger logger = LoggerFactory.getLogger(CSVFileParser.class);

    private final CSVLineParser<T> lineParser;
    private final Stream<String> lines;

    public CSVFileParser(String file, boolean hasHeader, CSVLineParser<T> lineParser){
        try {
            lines = Files.lines(Paths.get(file)).skip(hasHeader? 1: 0);
        } catch (IOException e){
            logger.error("Cant open csv file {}", file);
            throw new IllegalStateException();
        }
        this.lineParser = lineParser;
    }

    private void close(){
        lines.close();
    }

    public void consumeAll(Consumer<T> consumer) {
        lines.parallel().forEach(line -> lineParser.consume(line, consumer));
    }
}
