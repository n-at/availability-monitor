package ru.doublebyte.availabilitymonitor.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.doublebyte.availabilitymonitor.testers.Result;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestResult {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    private Long id;
    private LocalDateTime createdAt;
    private Result result;

    ///////////////////////////////////////////////////////////////////////////

    public TestResult() {

    }

    public TestResult(Long monitoringId, Result result) {
        this(monitoringId, LocalDateTime.now(), result);
    }

    public TestResult(Long monitoringId, LocalDateTime createdAt, Result result) {
        this.createdAt = createdAt;
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("TestResult{id=%d, createdAt=%s, result=%s}",
                id, createdAt, result);
    }

    ///////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @JsonIgnore
    public String getCreatedAtFormatted() {
        return createdAt.format(FORMATTER);
    }
}
