import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class Client2 {

    public static void main(String[] args) {
        // If you are using the cloud to host your cluster, make sure you add the client credentials!
        // Setting up cloud configuration
        ClientConfig config = new ClientConfig();
        // config.setProperty("hazelcast.client.statistics.enabled","true");
        // config.setProperty(ClientProperty.HAZELCAST_CLOUD_DISCOVERY_TOKEN.getName(), "YOUR_CLOUD_DISCOVERY_TOKEN");
        // config.setClusterName("YOUR_CLUSTER_NAME");
        // Create Hazelcast instance which is backed by a client
        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        /**
         * Create a Hazelcast backed map
         */
        IMap<Integer, String> trainingMap = client.getMap("training");

        /**
         * Get key 42 from the map and store the value
         */
        String result = trainingMap.get(42);

        /**
         * Print the result to the console
         */
        if (result != null) {
            System.out.println("Result: " + result);
        }
    }
}
