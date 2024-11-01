package ar.edu.itba.pod.client.csvParser;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class CSVLineParser<T>  {

    public abstract T parseLine(String line);

    public <R> R parseAs(String line, Function<T, R> toR){
        return toR.apply(parseLine(line));
    }

    public void consume(String line, Consumer<T> consumer){
        consumer.accept(parseLine(line));
    }
}
