package ru.doublebyte.availabilitymonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import ru.doublebyte.availabilitymonitor.managers.MonitoringManager;
import ru.doublebyte.availabilitymonitor.managers.SchedulerManager;
import ru.doublebyte.availabilitymonitor.repositories.MonitoringRepository;

@Configuration
public class MainConfiguration {

    private final MonitoringRepository monitoringRepository;

    @Autowired
    public MainConfiguration(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    public MonitoringManager monitoringManager() {
        return new MonitoringManager(monitoringRepository, schedulerManager());
    }

    @Bean
    public SchedulerManager schedulerManager() {
        return new SchedulerManager(taskScheduler());
    }

}
