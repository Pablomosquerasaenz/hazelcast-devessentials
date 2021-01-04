package essentials.computing;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class EventListenerInterceptorDemo {
    public static void main(String[] args) {
        Config config = new Config();
        MapConfig mapConfig = config.getMapConfig("demo-map");
        EvictionConfig evictionConfig = new EvictionConfig();

        evictionConfig
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE)
                .setSize(2);
        mapConfig
                .setEvictionConfig(evictionConfig);

        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);

        IMap<String, String> map = hz.getMap("demo-map");

        map.addEntryListener(new MyEntryListener(), true);

        for (int i = 0; i < 4; i++) {
            map.put("" + i, "value-" + i);
        }

        map.set("2", "100");
        map.remove("3");

        map.evict("1");
        map.clear();

        hz.shutdown();
    }
}
