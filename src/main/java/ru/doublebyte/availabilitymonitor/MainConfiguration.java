package ru.doublebyte.availabilitymonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import ru.doublebyte.availabilitymonitor.factories.TesterFactory;
import ru.doublebyte.availabilitymonitor.factories.UrlTesterFactory;
import ru.doublebyte.availabilitymonitor.managers.*;
import ru.doublebyte.availabilitymonitor.repositories.EmailRepository;
import ru.doublebyte.availabilitymonitor.repositories.MonitoringRepository;
import ru.doublebyte.availabilitymonitor.repositories.TestResultDifferenceRepository;
import ru.doublebyte.availabilitymonitor.repositories.TestResultRepository;

@Configuration
public class MainConfiguration {

    private final MonitoringRepository monitoringRepository;
    private final TestResultRepository testResultRepository;
    private final TestResultDifferenceRepository testResultDifferenceRepository;
    private final EmailRepository emailRepository;

    @Autowired
    public MainConfiguration(
            MonitoringRepository monitoringRepository,
            TestResultRepository testResultRepository,
            TestResultDifferenceRepository testResultDifferenceRepository,
            EmailRepository emailRepository
    ) {
        this.monitoringRepository = monitoringRepository;
        this.testResultRepository = testResultRepository;
        this.testResultDifferenceRepository = testResultDifferenceRepository;
        this.emailRepository = emailRepository;
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
    public TestResultDifferenceManager testResultDifferenceManager() {
        return new TestResultDifferenceManager(testResultDifferenceRepository);
    }

    @Bean
    public TestResultManager testResultManager() {
        return new TestResultManager(testResultRepository, testResultDifferenceManager());
    }

    @Bean
    public SchedulerManager schedulerManager() {
        return new SchedulerManager(taskScheduler(), testerFactory());
    }

    @Bean
    public EmailManager emailManager() {
        return new EmailManager(emailRepository);
    }

    @Bean
    public TesterFactory testerFactory() {
        return new UrlTesterFactory(testResultManager());
    }

}
