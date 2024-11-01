package ar.edu.itba.pod.client.csvParser;

import com.hazelcast.config.ExecutorConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class CSVFileParser<T> {
    private final CSVLineParser<T> lineParser;
    private final BufferedReader reader;
    private String nextLineCache;


    public CSVFileParser(String file, boolean hasHeader, CSVLineParser<T> lineParser){
        try {
            reader = new BufferedReader(new FileReader(file));
            if (hasHeader) {
                reader.readLine();
            }
            nextLineCache = reader.readLine();
        } catch (IOException e){
            throw new IllegalStateException();
        }
        this.lineParser = lineParser;
    }

    private void close(){
        try {
            reader.close();
        } catch(IOException e){
            throw new IllegalStateException();
        }
    }

    private boolean hasNext() {
        return nextLineCache != null;
    }

    private String nextLine() {
        String line = nextLineCache;
        try {
            nextLineCache = reader.readLine();
        } catch (IOException e){
            throw new IllegalStateException();
        }
        return line;
    }


    public void consumeAll(Consumer<T> consumer){
        while(hasNext()){
            lineParser.consume(nextLine(), consumer);
        }
        close();
    }
}
