package ar.edu.itba.pod.client.util;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueryTimer {

    private final String outPath;
    private FileWriter writer;

    public QueryTimer(String outPath){
        this.outPath = outPath;
    }

    private String getTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SSSS"));
    }

    public void startLoad() throws IOException{
        writer = new FileWriter(outPath, false);

        writer.write(getTime() + " - Inicio de la lectura del archivo\n");
    }

    public void endLoad() throws IOException{
        writer.write(getTime() + " - Fin de la lectura del archivo\n");
    }

    public void startJob() throws IOException{
        writer.write(getTime() + " - Inicio del trabajo map/reduce\n");
    }

    public void endJob()  throws IOException{
        writer.write(getTime() + " - Fin del trabajo map/reduce\n");
    }

    public void endQuery() throws IOException{
        writer.flush();
        writer.close();
    }

}
