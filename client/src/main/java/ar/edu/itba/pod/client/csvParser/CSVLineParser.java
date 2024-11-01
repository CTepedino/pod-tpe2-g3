package ar.edu.itba.pod.client.csvParser;


import java.util.List;

public interface CSVLineParser<T>  {

    T parseLine(String line);
}
