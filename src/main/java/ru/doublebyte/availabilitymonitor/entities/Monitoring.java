package ru.doublebyte.availabilitymonitor.entities;

public class Monitoring {

    private Long id;
    private String url;
    private String name;
    private Boolean active;
    private Integer checkInterval;
    private Integer respondInterval;
    private TestResult latestTestResult;

    ///////////////////////////////////////////////////////////////////////////

    public Monitoring() {

    }

    public Monitoring(String url, String name, int checkInterval, int respondInterval) {
        this.url = url;
        this.name = name;
        this.checkInterval = checkInterval;
        this.respondInterval = respondInterval;
    }

    @Override
    public String toString() {
        return String.format("Monitoring{id=%d, url='%s', name='%s'}", id, url, name);
    }

    ///////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(Integer checkInterval) {
        this.checkInterval = checkInterval;
    }

    public Integer getRespondInterval() {
        return respondInterval;
    }

    public void setRespondInterval(Integer respondInterval) {
        this.respondInterval = respondInterval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public TestResult getLatestTestResult() {
        return latestTestResult;
    }

    public void setLatestTestResult(TestResult latestTestResult) {
        this.latestTestResult = latestTestResult;
    }
}
