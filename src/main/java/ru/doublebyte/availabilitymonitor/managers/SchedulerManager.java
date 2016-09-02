package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import ru.doublebyte.availabilitymonitor.types.Monitoring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class SchedulerManager {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerManager.class);

    private TaskScheduler taskScheduler;

    private Map<Long, ScheduledFuture> tasks = new HashMap<>();

    ///////////////////////////////////////////////////////////////////////////

    public SchedulerManager(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
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
        ScheduledFuture scheduledFuture = taskScheduler.scheduleWithFixedDelay(createTask(monitoring), delay);

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

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Create runnable task for monitoring
     * @param monitoring
     * @return Task
     */
    private Runnable createTask(Monitoring monitoring) {
        return () -> {
            logger.info("TODO monitoring task");
        };
    }

}
