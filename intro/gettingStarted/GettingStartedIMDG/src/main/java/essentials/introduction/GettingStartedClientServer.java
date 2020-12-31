package essentials.introduction;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class GettingStartedClientServer {
    public static void main(String[] args) {
        ClientConfig cfg = new ClientConfig();

        // Client
        HazelcastInstance hz = HazelcastClient.newHazelcastClient(cfg);

        // Map Data Structure
        IMap<Integer, String> custMap = hz.getMap("customers");
        custMap.put(1, "Sudipto");
        custMap.put(2, "Mahim");
        custMap.put(3, "Aabida");
        custMap.put(4, "Anmol");
        System.out.println("Customer with Key=1: " + custMap.get(1));
        System.out.println("CustomerMap Size: " + custMap.size());

        // Queue Data Structure
        IQueue<String> custQueue = hz.getQueue("customers");
        custQueue.offer("Sudipto");
        custQueue.offer("Mahim");
        custQueue.offer("Aabida");
        custQueue.offer("Anmol");
        System.out.println("First Customer: " + custQueue.poll());
        System.out.println("Second Customer: " + custQueue.peek());
        System.out.println("CustomerQueue Size: " + custQueue.size());
    }
}
