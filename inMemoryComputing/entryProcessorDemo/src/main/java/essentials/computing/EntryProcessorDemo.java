package essentials.computing;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;

public class EntryProcessorDemo {
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();

        IMap<Integer, Integer> map = hz.getMap("demo-map");

        for (int i = 0; i < 10; i++) {
            map.put(i, i);
        }

        System.out.println("Map before executing EntryProcessor...");
        System.out.println(map.getName());
        for (Integer i : map.keySet()) {
            System.out.println(i + ": " + map.get(i));
        }

        map.executeOnEntries((EntryProcessor<Integer, Integer, Object>) entry -> {
            Integer value = entry.getValue();
            value += 1;
            entry.setValue(value);
            return null;
        });

        System.out.println();

        System.out.println("Map after executing EntryProcessor...");
        System.out.println(map.getName());
        for (Integer i : map.keySet()) {
            System.out.println(i + ": " + map.get(i));
        }

        hz.shutdown();
    }
}
