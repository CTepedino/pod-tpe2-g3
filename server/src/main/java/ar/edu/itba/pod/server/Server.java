package ar.edu.itba.pod.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;

public class Server {

    private static final String GROUP_NAME = "g3";
    private static final String GROUP_PASS = GROUP_NAME + "-pass";
    private static final String EXECUTOR_NAME = GROUP_NAME + "-executor";

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info(" Server Starting ...");

        String addressesString = System.getProperty("addresses");
        List<String> addresses;
        if (addressesString == null){
            addresses = Collections.singletonList("127.0.0.*");
        } else {
            StringTokenizer tokenizer = new StringTokenizer(addressesString, ";");
            addresses = new ArrayList<>();
            while (tokenizer.hasMoreTokens()){
                addresses.add(tokenizer.nextToken());
            }
        }

        int port;
        String portString = System.getProperty("port");
        if (portString == null){
            port = 5701;
        } else {
            try {
                port = Integer.parseInt(portString);
            } catch (NumberFormatException e){
                logger.error("Invalid port: {}", portString);
                throw new IllegalStateException(e.getMessage());
            }
        }


        Config config = new Config();

        GroupConfig groupConfig = new GroupConfig().setName(GROUP_NAME).setPassword(GROUP_PASS);
        config.setGroupConfig(groupConfig);

        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);

        InterfacesConfig interfacesConfig = new InterfacesConfig().setInterfaces(addresses).setEnabled(true);

        NetworkConfig networkConfig = new NetworkConfig().setInterfaces(interfacesConfig).setJoin(joinConfig);
        networkConfig.setPort(port);
        config.setNetworkConfig(networkConfig);

        Hazelcast.newHazelcastInstance(config);
    }

}