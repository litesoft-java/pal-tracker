package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@SuppressWarnings("WeakerAccess")
public class JdbcTimeEntryRepository implements TimeEntryRepository {
    public static final String fID = "id";
    public static final String fPROJECT_ID = "project_id";
    public static final String fUSER_ID = "user_id";
    public static final String fDATE = "date";
    public static final String fHOURS = "hours";
    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);

        timeEntry.setId(generatedKeyHolder.getKey().longValue());
        return timeEntry;
    }

    //    @Override
    public TimeEntry create2(TimeEntry timeEntry) {
        String sql = "INSERT INTO time_entries (" +
//                fID + ", " +
                fPROJECT_ID + ", " + fUSER_ID + // ", " + fDATE + ", " + fHOURS +
                ") VALUES (:" +
//                fID + ", :" +
                fPROJECT_ID + ", :" + fUSER_ID + // ", :" + fDATE + ", :" + fHOURS +
                ")";

        Map<String, Object> params = new Parameters()
//                .add(fID, timeEntry.getId())
                .add(fPROJECT_ID, timeEntry.getProjectId())
                .add(fUSER_ID, timeEntry.getUserId())
//                .add(fDATE, timeEntry.getDate())
//                .add(fHOURS, timeEntry.getHours())
                .get();
        jdbcTemplate.update(sql, params);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return jdbcTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{timeEntryId},
                extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries", mapper);
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE time_entries " +
                        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                timeEntryId);

        timeEntry.setId(timeEntryId);
        return timeEntry;
    }

    @Override
    public void delete(long timeEntryId) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", timeEntryId);
    }

    @SuppressWarnings("WeakerAccess")
    private static class Parameters {
        private final Map<String, Object> mParameters = new HashMap<>();

        public Parameters add(String pFieldRef, Object pFieldValue) {
            mParameters.put(pFieldRef, pFieldValue);
            return this;
        }

        public Map<String, Object> get() {
            return mParameters;
        }
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;

}
