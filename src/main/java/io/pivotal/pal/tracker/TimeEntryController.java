package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private final TimeEntryRepository timeEntryRepository;
    private final CounterService counter;
    private final GaugeService gauge;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, CounterService counter, GaugeService gauge) {

        this.timeEntryRepository = timeEntryRepository;
        this.counter = counter;
        this.gauge = gauge;
    }

    @PostMapping
//    public ResponseEntity<String> create(HttpRequest pRequest) {
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);

        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntryRepository.count());

        return buildResponseCreated(timeEntry);
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        if (timeEntry != null) {
            counter.increment("TimeEntry.read");
        }
        return buildResponseReadOne(timeEntry);
    }

    @GetMapping()
    public ResponseEntity<List<TimeEntry>> list() {
        counter.increment("TimeEntry.listed");
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntryToUpdate) {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(timeEntryId, timeEntryToUpdate);
        if (updatedTimeEntry != null) {
            counter.increment("TimeEntry.updated");
        }
        return buildResponseReadOne(updatedTimeEntry);
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);

        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", timeEntryRepository.count());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<TimeEntry> buildResponseReadOne(TimeEntry timeEntry) {
        return (timeEntry == null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(timeEntry);
    }

    private ResponseEntity<TimeEntry> buildResponseCreated(TimeEntry timeEntry) {
        return (timeEntry == null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(timeEntry, HttpStatus.CREATED);
    }
}
