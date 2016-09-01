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

    @Column(name = "check_interval", nullable = false)
    private int checkInterval;

    @Column(name = "respond_interval", nullable = false)
    private int respondInterval;

    ///////////////////////////////////////////////////////////////////////////

    public Monitoring() {

    }

    public Monitoring(String url, int checkInterval, int respondInterval) {
        this.url = url;
        this.checkInterval = checkInterval;
        this.respondInterval = respondInterval;
    }

    @Override
    public String toString() {
        return "Monitoring{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", checkInterval=" + checkInterval +
                ", respondInterval=" + respondInterval +
                '}';
    }

    ///////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public int getRespondInterval() {
        return respondInterval;
    }
}
