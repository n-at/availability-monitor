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
import ru.doublebyte.availabilitymonitor.storages.EmailStorage;
import ru.doublebyte.availabilitymonitor.storages.MonitoringStorage;
import ru.doublebyte.availabilitymonitor.storages.TestResultStorage;

@Configuration
@EnableAsync
public class MainConfiguration {

    private final JavaMailSender javaMailSender;

    @Value("${monitor.thread-pool-size}")
    private int threadPoolSize;

    @Value("${monitor.keep-test-results}")
    private int keepTestResults;

    @Value("${spring.mail.from}")
    private String notificationFromAddress;

    @Autowired
    public MainConfiguration(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
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
    public MonitoringStorage monitoringStorage() {
        return new MonitoringStorage();
    }

    @Bean
    public TestResultStorage testResultStorage() {
        return new TestResultStorage();
    }

    @Bean
    public MonitoringManager monitoringManager() {
        return new MonitoringManager(monitoringStorage(), schedulerManager());
    }

    @Bean
    public TestResultManager testResultManager() {
        return new TestResultManager(testResultStorage(), notificationManager());
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

}
