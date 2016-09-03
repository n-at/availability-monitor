package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import ru.doublebyte.availabilitymonitor.factories.TesterFactory;
import ru.doublebyte.availabilitymonitor.types.Monitoring;
import ru.doublebyte.availabilitymonitor.types.Tester;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class SchedulerManager {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerManager.class);

    private TaskScheduler taskScheduler;
    private TesterFactory testerFactory;

    private Map<Long, ScheduledFuture> tasks = new HashMap<>();

    ///////////////////////////////////////////////////////////////////////////

    public SchedulerManager(
            TaskScheduler taskScheduler,
            TesterFactory testerFactory
    ) {
        this.taskScheduler = taskScheduler;
        this.testerFactory = testerFactory;
    }

    /**
     * Add monitoring to scheduler
     * @param monitoring Monitoring to add
     */
    public void addMonitoring(Monitoring monitoring) {
        logger.info("Adding monitoring with id {} to scheduler", monitoring.getId());

        if (!monitoring.isActive()) {
            logger.info("Monitoring with id {} is inactive", monitoring.getId());
            return;
        }

        long delay = monitoring.getCheckInterval() * 1000;
        Tester tester = testerFactory.createTester(monitoring);

        ScheduledFuture scheduledFuture = taskScheduler.scheduleWithFixedDelay(tester, delay);

        tasks.put(monitoring.getId(), scheduledFuture);
    }

    /**
     * Update monitoring on scheduler
     * @param monitoring Monitoring to update
     */
    public void updateMonitoring(Monitoring monitoring) {
        logger.info("Updating monitoring with id {} in scheduler", monitoring.getId());
        deleteMonitoring(monitoring);
        addMonitoring(monitoring);
    }

    /**
     * Delete monitoring from scheduler
     * @param monitoring Monitoring to delete
     */
    public void deleteMonitoring(Monitoring monitoring) {
        logger.info("Removing monitoring with id {} from scheduler", monitoring.getId());

        ScheduledFuture scheduledFuture = tasks.get(monitoring.getId());
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

}
