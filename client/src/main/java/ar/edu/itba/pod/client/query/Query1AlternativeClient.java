package ar.edu.itba.pod.client.query;

import ar.edu.itba.pod.api.model.dto.InfractionAgency;
import ar.edu.itba.pod.api.model.dto.InfractionAgencyTicketCount;
import ar.edu.itba.pod.api.query1.TotalTicketsByInfractionAndAgencyCollator;
import ar.edu.itba.pod.api.query1.TotalTicketsByInfractionAndAgencyCombinerFactory;
import ar.edu.itba.pod.api.query1.alternative.TotalTicketsByInfractionAndAgencyMapperAlternative;
import ar.edu.itba.pod.api.query1.TotalTicketsByInfractionAndAgencyReducerFactory;
import ar.edu.itba.pod.client.util.QueryPropertiesFactory;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ISet;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.IOException;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;

/***
 * Alternativa para la query1 guardando las colecciones de agencias e infracciones en hazelcast.
 * Con los datasets de ejemplo, es mas lenta, pero serviría en caso de que estas colecciones tengan muchos mas registros
 * Se puede aplicar el mismo cambio a todas las otras queries, solo dejamos esta de ejemplo
 */
@SuppressWarnings("deprecation")
public class Query1AlternativeClient extends QueryClient<InfractionAgency> {
    private static final String JOB_TRACKER_NAME = GROUP_NAME + "-ticket-count-a";
    private static final String[] OUT_CSV_HEADERS = {"Infraction", "Agency", "Tickets"};
    private static final String OUT_CSV_FILENAME = "/query1a.csv";
    private static final String OUT_TIME_FILENAME = "/time1a.txt";

    protected static final String AGENCY_SET = GROUP_NAME + "-agencies";
    protected static final String INFRACTION_MAP = GROUP_NAME + "-infractions";

    public Query1AlternativeClient() {
        super(new QueryPropertiesFactory().build(), OUT_TIME_FILENAME);
    }

    void fillAgencySet(){
        ISet<String> agencies = hazelcastInstance.getSet(AGENCY_SET);
        agencies.clear();
        csvParserFactory.getAgencyFileParser().consumeAll(agencies::add);
    }

    void fillInfractionsMap(){
        IMap<String, String> infractions = hazelcastInstance.getMap(INFRACTION_MAP);
        infractions.clear();
        csvParserFactory.getInfractionFileParser().consumeAll(i ->
            infractions.put(i.getId(), i.getDefinition())
        );
    }

    @Override
    KeyValueSource<Long, InfractionAgency> loadData(){
        fillAgencySet();
        fillInfractionsMap();
        return loadTicketData(t -> new InfractionAgency(t.getIssuingAgency(), t.getInfractionId()));
    }

    @Override
    void mapReduceJob(KeyValueSource<Long, InfractionAgency> keyValueSource) throws ExecutionException, InterruptedException, IOException {

        JobTracker jobTracker = hazelcastInstance.getJobTracker(JOB_TRACKER_NAME);
        Job<Long, InfractionAgency> job = jobTracker.newJob(keyValueSource);
        ICompletableFuture<SortedSet<InfractionAgencyTicketCount>> future = job
                .mapper(new TotalTicketsByInfractionAndAgencyMapperAlternative(INFRACTION_MAP, AGENCY_SET))
                .combiner(new TotalTicketsByInfractionAndAgencyCombinerFactory())
                .reducer(new TotalTicketsByInfractionAndAgencyReducerFactory())
                .submit(new TotalTicketsByInfractionAndAgencyCollator(hazelcastInstance.getMap(INFRACTION_MAP)));

        printResults(OUT_CSV_HEADERS, OUT_CSV_FILENAME, future.get());
    }

    public static void main(String[] args){
        new Query1AlternativeClient().executeQuery();
    }
}