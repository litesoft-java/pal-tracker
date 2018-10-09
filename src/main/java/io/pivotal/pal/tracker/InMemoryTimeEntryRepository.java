package io.pivotal.pal.tracker;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private final Map<Long, TimeEntry> repo = new TreeMap<>();
    private long lastId;

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(++lastId);
        repo.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return repo.get(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(repo.values());
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        timeEntry.setId(timeEntryId);
        repo.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public void delete(long timeEntryId) {
        repo.remove(timeEntryId);
    }
}
