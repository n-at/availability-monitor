package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Maintenance tasks
 */
public class MaintenanceManager implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceManager.class);

    /**
     * Delay between maintenance tasks (6 hours)
     */
    private static final long MAINTENANCE_DELAY = 6 * 60 * 60 * 1000;

    private TaskScheduler taskScheduler;
    private TestResultManager testResultManager;
    private int keepTestResults;

    ///////////////////////////////////////////////////////////////////////////

    public MaintenanceManager(
            TaskScheduler taskScheduler,
            TestResultManager testResultManager,
            int keepTestResults
    ) {
        this.taskScheduler = taskScheduler;
        this.testResultManager = testResultManager;
        this.keepTestResults = keepTestResults;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        taskScheduler.scheduleWithFixedDelay(getTestResultsCleaner(), MAINTENANCE_DELAY);
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Old test results removal
     * @return
     */
    private Runnable getTestResultsCleaner() {
        logger.info("Removing test results older than {} days", keepTestResults);
        return () -> {
            LocalDateTime deleteOlderDate = LocalDateTime.now().minus(keepTestResults, ChronoUnit.DAYS);
            testResultManager.deleteOlderThan(deleteOlderDate);
        };
    }
}
