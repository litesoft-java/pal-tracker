package io.pivotal.pal.tracker;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {

        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping
//    public ResponseEntity<String> create(HttpRequest pRequest) {
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        return buildResponseCreated(timeEntryRepository.create(timeEntryToCreate));
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        return buildResponseReadOne(timeEntryRepository.find(timeEntryId));
    }

    @GetMapping()
    public ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntryToUpdate) {
        return buildResponseReadOne(timeEntryRepository.update(timeEntryId, timeEntryToUpdate));
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity<TimeEntry> buildResponseReadOne(TimeEntry timeEntry) {
        return (timeEntry == null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(timeEntry);
    }

    private ResponseEntity<TimeEntry> buildResponseCreated(TimeEntry timeEntry) {
        return (timeEntry == null) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(timeEntry, HttpStatus.CREATED);
    }
}
