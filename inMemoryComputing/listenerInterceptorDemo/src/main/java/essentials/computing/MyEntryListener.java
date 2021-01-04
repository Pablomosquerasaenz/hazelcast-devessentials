package essentials.computing;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.map.MapEvent;

public class MyEntryListener implements EntryListener<String, String> {

    @Override
    public void entryAdded(EntryEvent<String, String> entryEvent) {
        System.out.println("entryAdded: " + entryEvent);
    }

    @Override
    public void entryEvicted(EntryEvent<String, String> entryEvent) {
        System.out.println("entryEvicted: " + entryEvent);
    }

    @Override
    public void entryExpired(EntryEvent<String, String> entryEvent) {
        System.out.println("entryExpired: " + entryEvent);
    }

    @Override
    public void entryRemoved(EntryEvent<String, String> entryEvent) {
        System.out.println("entryRemoved: " + entryEvent);
    }

    @Override
    public void entryUpdated(EntryEvent<String, String> entryEvent) {
        System.out.println("entryUpdated: " + entryEvent);
    }

    @Override
    public void mapCleared(MapEvent mapEvent) {
        System.out.println("mapCleared: " + mapEvent);
    }

    @Override
    public void mapEvicted(MapEvent mapEvent) {
        System.out.println("mapEvicted: " + mapEvent);
    }
}
