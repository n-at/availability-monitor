package ru.doublebyte.availabilitymonitor.entities;

import ru.doublebyte.availabilitymonitor.testers.UrlChecker;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "test_result")
public class TestResult {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "monitoring_id", nullable = false)
    private Long monitoringId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "result", nullable = false)
    private UrlChecker.Result result;

    ///////////////////////////////////////////////////////////////////////////

    public TestResult() {

    }

    public TestResult(Long monitoringId, UrlChecker.Result result) {
        this(monitoringId, LocalDateTime.now(), result);
    }

    public TestResult(Long monitoringId, LocalDateTime createdAt, UrlChecker.Result result) {
        this.monitoringId = monitoringId;
        this.createdAt = createdAt;
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("TestResult{id=%d, monitoringId=%d, createdAt=%s, result=%s}",
                id, monitoringId, createdAt, result);
    }

    ///////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public Long getMonitoringId() {
        return monitoringId;
    }

    public void setMonitoringId(Long monitoringId) {
        this.monitoringId = monitoringId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UrlChecker.Result getResult() {
        return result;
    }

    public void setResult(UrlChecker.Result result) {
        this.result = result;
    }

    @Transient
    public String getCreatedAtFormatted() {
        return createdAt.format(FORMATTER);
    }
}
