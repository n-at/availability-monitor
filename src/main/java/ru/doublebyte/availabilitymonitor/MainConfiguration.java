package ru.doublebyte.availabilitymonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.doublebyte.availabilitymonitor.managers.MonitoringManager;
import ru.doublebyte.availabilitymonitor.repositories.MonitoringRepository;

@Configuration
public class MainConfiguration {

    private final MonitoringRepository monitoringRepository;

    @Autowired
    public MainConfiguration(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    @Bean
    public MonitoringManager monitoringManager() {
        return new MonitoringManager(monitoringRepository);
    }

}
