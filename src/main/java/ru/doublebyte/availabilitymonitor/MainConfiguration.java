package ru.doublebyte.availabilitymonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import ru.doublebyte.availabilitymonitor.factories.TesterFactory;
import ru.doublebyte.availabilitymonitor.factories.UrlTesterFactory;
import ru.doublebyte.availabilitymonitor.managers.*;
import ru.doublebyte.availabilitymonitor.repositories.MonitoringRepository;
import ru.doublebyte.availabilitymonitor.repositories.TestResultDifferenceRepository;
import ru.doublebyte.availabilitymonitor.repositories.TestResultRepository;
import ru.doublebyte.availabilitymonitor.storages.EmailStorage;

@Configuration
@EnableAsync
public class MainConfiguration {

    private final MonitoringRepository monitoringRepository;
    private final TestResultRepository testResultRepository;
    private final TestResultDifferenceRepository testResultDifferenceRepository;

    private final JavaMailSender javaMailSender;

    @Value("${monitor.thread-pool-size}")
    private int threadPoolSize;

    @Value("${monitor.keep-test-results}")
    private int keepTestResults;

    @Value("${spring.mail.from}")
    private String notificationFromAddress;

    @Autowired
    public MainConfiguration(
            JavaMailSender javaMailSender,
            MonitoringRepository monitoringRepository,
            TestResultRepository testResultRepository,
            TestResultDifferenceRepository testResultDifferenceRepository
    ) {
        this.javaMailSender = javaMailSender;
        this.monitoringRepository = monitoringRepository;
        this.testResultRepository = testResultRepository;
        this.testResultDifferenceRepository = testResultDifferenceRepository;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(threadPoolSize);
        return threadPoolTaskScheduler;
    }

    @Bean
    public EmailStorage emailStorage() {
        return new EmailStorage();
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
        return new TestResultManager(testResultRepository, testResultDifferenceManager(),
                notificationManager());
    }

    @Bean
    public SchedulerManager schedulerManager() {
        return new SchedulerManager(taskScheduler(), testerFactory());
    }

    @Bean
    public EmailManager emailManager() {
        return new EmailManager(emailStorage());
    }

    @Bean
    public NotificationManager notificationManager() {
        return new NotificationManager(javaMailSender, emailManager(), notificationFromAddress);
    }

    @Bean
    public TesterFactory testerFactory() {
        return new UrlTesterFactory(testResultManager());
    }

    @Bean
    public MaintenanceManager maintenanceManager() {
        return new MaintenanceManager(taskScheduler(), testResultManager(), keepTestResults);
    }

}
