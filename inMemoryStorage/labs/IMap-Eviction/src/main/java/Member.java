import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;

public class Member {

    public static void main(String[] args) {
        Config config = new Config();

        MapConfig mapConfig = config.getMapConfig("training-eviction");
        EvictionConfig evictionConfig = new EvictionConfig();

        /*
         * Set the eviction config
         */
        evictionConfig
                .setEvictionPolicy(EvictionPolicy.LFU)
                .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE)
                .setSize(1);

        mapConfig.setEvictionConfig(evictionConfig);

        Hazelcast.newHazelcastInstance(config);
    }
}
