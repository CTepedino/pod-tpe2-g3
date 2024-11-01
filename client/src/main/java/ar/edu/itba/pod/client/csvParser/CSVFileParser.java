package ar.edu.itba.pod.client.csvParser;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class CSVFileParser<T> implements Iterator<T>, Closeable {
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

    @Override
    public void close(){
        try {
            reader.close();
        } catch(IOException e){
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean hasNext() {
        return nextLineCache != null;
    }

    @Override
    public T next() {
        String line = nextLineCache;
        try {
            nextLineCache = reader.readLine();
        } catch (IOException e){
            throw new IllegalStateException();
        }
        return lineParser.parseLine(line);
    }
}
