package ar.edu.itba.pod.client.util;

import ar.edu.itba.pod.client.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class QueryPropertiesFactory {
    private int n = 0;
    private boolean dateRange = false;
    private boolean agency = false;

    public QueryPropertiesFactory useN(int n){
        this.n = n;
        return this;
    }

    public QueryPropertiesFactory useDateRange(){
        dateRange = true;
        return this;
    }

    public QueryPropertiesFactory useAgency(){
        agency = true;
        return this;
    }

    public QueryProperties build(){
        return new QueryProperties(n, dateRange, agency);
    }

    public static class QueryProperties{
        private static final Logger logger = LoggerFactory.getLogger(QueryProperties.class);

        private static final String IPV4_PATTERN = "^(localhost|(((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)))(:\\d{1,5})?$";

        private final List<String> addresses;
        private final City city;
        private final String inPath;
        private final String outPath;
        private int n;
        private LocalDate from;
        private LocalDate to;
        private String agency;


        private QueryProperties(int minN, boolean useDateRange, boolean useAgency){
            addresses = parseAddresses();
            city = parseCity();
            inPath = parsePath("inPath");
            outPath = parsePath("outPath");
            if (minN != 0){
                n = parseN(minN);
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
                if (!address.matches(IPV4_PATTERN)){
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
            String pathString = System.getProperty(property);
            if (pathString == null){
                logger.error("Path not specified");
                throw new IllegalStateException();
            }
            if (pathString.startsWith("~")) {
                pathString = System.getProperty("user.home") + pathString.substring(1);
            }
            Path path = Paths.get(pathString);
            if (!Files.isDirectory(path)){
                logger.error("Invalid path: {}", path);
                throw new IllegalStateException();
            }
            return pathString;
        }

        private int parseN(int minN){
            String numberString = System.getProperty("n");
            if (numberString == null){
                logger.error("No number specified");
                throw new IllegalStateException();
            }
            try {
                int n = Integer.parseInt(numberString);
                if (n < minN){
                    logger.error("N must be at least {}", minN);
                    throw new IllegalStateException();
                }
                return n;
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
