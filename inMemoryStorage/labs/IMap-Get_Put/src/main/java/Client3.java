import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class Client3 {
    public static void main(String[] args) {
        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        IMap<Integer, Integer> trainingMap = client.getMap("training-overwrite");
        Integer value;
        value = 1;
        trainingMap.put(1, value);
        System.out.println("Put " + value);
        value = trainingMap.get(1);
        System.out.println("Got " + value);
        value += 1;
        trainingMap.set(1, value);
        System.out.println("Incremented entry");
        value = trainingMap.get(1);
        System.out.println("Got " + value);
    }
}
