package ar.edu.itba.pod.client.util;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class QueryTimer {
    private final FileWriter writer;

    private LocalDateTime start;

    public QueryTimer(String outPath) throws IOException{
        writer = new FileWriter(outPath, false);
    }

    private String getTimeString(LocalDateTime time){
        return time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SSSS"));
    }

    private String getElapsedTime(LocalDateTime end){
        long seconds = start.until(end, ChronoUnit.SECONDS);
        long ms = start.until(end, ChronoUnit.MILLIS) % 1000;
        return (seconds!=0? (seconds + "s "):"") + ms + "ms";
    }


    private void start(String msg) throws IOException{
        start = LocalDateTime.now();
        writer.write(getTimeString(start) + " - " + msg + '\n');
    }

    private void end(String msg) throws IOException{
        LocalDateTime end = LocalDateTime.now();
        writer.write(getTimeString(end) + " - " + msg + " -  Tiempo transcurrido: " + getElapsedTime(end) + '\n');
    }

    public void startLoad() throws IOException{
        start("Inicio de la lectura del archivo");
    }

    public void endLoad() throws IOException{
        end("Fin de la lectura del archivo");
    }

    public void startJob() throws IOException{
        start("Inicio del trabajo map/reduce");
    }

    public void endJob()  throws IOException{
        end("Fin del trabajo map/reduce");
    }

    public void endQuery() throws IOException{
        writer.flush();
        writer.close();
    }

}
