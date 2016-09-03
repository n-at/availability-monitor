package ru.doublebyte.availabilitymonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import ru.doublebyte.availabilitymonitor.factories.TesterFactory;
import ru.doublebyte.availabilitymonitor.factories.UrlTesterFactory;
import ru.doublebyte.availabilitymonitor.managers.MonitoringManager;
import ru.doublebyte.availabilitymonitor.managers.SchedulerManager;
import ru.doublebyte.availabilitymonitor.managers.TestResultManager;
import ru.doublebyte.availabilitymonitor.repositories.MonitoringRepository;
import ru.doublebyte.availabilitymonitor.repositories.TestResultRepository;

@Configuration
public class MainConfiguration {

    private final MonitoringRepository monitoringRepository;
    private final TestResultRepository testResultRepository;

    @Autowired
    public MainConfiguration(MonitoringRepository monitoringRepository, TestResultRepository testResultRepository) {
        this.monitoringRepository = monitoringRepository;
        this.testResultRepository = testResultRepository;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    public MonitoringManager monitoringManager() {
        return new MonitoringManager(monitoringRepository, schedulerManager());
    }

    public TestResultManager testResultManager() {
        return new TestResultManager(testResultRepository);
    }

    @Bean
    public SchedulerManager schedulerManager() {
        return new SchedulerManager(taskScheduler(), testerFactory());
    }

    @Bean
    public TesterFactory testerFactory() {
        return new UrlTesterFactory(testResultManager());
    }

}
