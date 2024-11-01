package ar.edu.itba.pod.client.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class QueryPropertiesParserFactory {
    private boolean n = false;
    private boolean dateRange = false;
    private boolean agency = false;

    public void useN(){
        n = true;
    }

    public void useDateRange(){
        dateRange = true;
    }

    public void useAgency(){
        agency = true;
    }

    public QueryPropertiesParser build(){
        return new QueryPropertiesParser(n, dateRange, agency);
    }

    public static class QueryPropertiesParser{
        private static final Logger logger = LoggerFactory.getLogger(QueryPropertiesParser.class);

        private static final String IPV4_PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        private final List<String> addresses;
        private final City city;
        private final String inPath;
        private final String outPath;
        private int n;
        private LocalDate from;
        private LocalDate to;
        private String agency;


        private QueryPropertiesParser(boolean useN, boolean useDateRange, boolean useAgency){
            addresses = parseAddresses();
            city = parseCity();
            inPath = parsePath("inPath");//TODO: verificar que existan los csv en el directorio de entrada
            outPath = parsePath("outPath");
            if (useN){
                n = parseN();
            }
            if (useDateRange){
                to = parseDate("to");
                from = parseDate("from");
                if (from.isAfter(to)){
                    logger.error("Invalid date range");
                    throw new IllegalStateException();
                }
            }
            if (useAgency){
                agency = parseAgency();
            }
        }

        private List<String> parseAddresses(){
            String addressesString = System.getProperty("addresses");
            if (addressesString == null){
                logger.error("No addresses specified");
                throw new IllegalStateException();
            }

            List<String> addresses = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(addressesString, ";");
            while(tokenizer.hasMoreTokens()){
                String address = tokenizer.nextToken();
                if (!(address.equals("localhost") || address.matches(IPV4_PATTERN))){
                    logger.error("Invalid address: {}", address);
                    throw new IllegalStateException();
                }
                addresses.add(address);
            }

            return addresses;
        }

        private City parseCity(){
            String cityString = System.getProperty("city");
            if (cityString == null){
                logger.error("City not specified");
                throw new IllegalStateException();
            }

            City city = City.fromCode(cityString);
            if (city == null){
                logger.error("Invalid city code: {}", cityString);
                throw new IllegalStateException();
            }
            return city;
        }

        private String parsePath(String property){

            String path = System.getProperty(property);
            if (path == null){
                logger.error("Path not specified");
                throw new IllegalStateException();
            }

            if (!Files.isDirectory(java.nio.file.Path.of(path))){
                logger.error("Invalid path: {}", path);
                throw new IllegalStateException();
            }
            return path;
        }

        private int parseN(){
            String numberString = System.getProperty("n");
            if (numberString == null){
                logger.error("No number specified");
                throw new IllegalStateException();
            }
            try {
                return Integer.parseInt(numberString);
            } catch (NumberFormatException e){
                logger.error("Invalid number {}", numberString);
                throw new IllegalStateException();
            }
        }

        private LocalDate parseDate(String property){
            String dateString = System.getProperty(property);
            if (dateString == null){
                logger.error("No date especified");
                throw new IllegalStateException();
            }

            try {
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                logger.error("Invalid date {}. Valid format is DD/MM/YYYY", dateString);
                throw new IllegalStateException();
            }

        }

        private String parseAgency(){
            String agency = System.getProperty("agency");
            if (agency == null){
                logger.error("No agency especified");
                throw new IllegalStateException();
            }
            return agency.replace('_', ' ');
        }

        public List<String> getAddresses() {
            return addresses;
        }

        public City getCity() {
            return city;
        }

        public String getInPath() {
            return inPath;
        }

        public String getOutPath() {
            return outPath;
        }

        public int getN() {
            return n;
        }

        public LocalDate getFrom() {
            return from;
        }

        public LocalDate getTo() {
            return to;
        }

        public String getAgency() {
            return agency;
        }
    }
}
