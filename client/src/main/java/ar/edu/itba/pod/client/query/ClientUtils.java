package ar.edu.itba.pod.client.query;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;


import java.util.List;

public abstract class ClientUtils {

    public static HazelcastInstance startHazelcast(List<String> addresses){
        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g3").setPassword("g3-pass");

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.setAddresses(addresses);

        // Client Config
        ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig).setNetworkConfig(clientNetworkConfig);

        // Node Client
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
