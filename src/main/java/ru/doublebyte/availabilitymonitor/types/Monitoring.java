package ru.doublebyte.availabilitymonitor.types;

import javax.persistence.*;

@Entity
@Table(name = "monitoring")
public class Monitoring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url", nullable = false, length = 250)
    private String url;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "check_interval", nullable = false)
    private Integer checkInterval;

    @Column(name = "respond_interval", nullable = false)
    private Integer respondInterval;

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
}
