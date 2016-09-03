package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import ru.doublebyte.availabilitymonitor.repositories.MonitoringRepository;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage monitoring instances
 */
public class MonitoringManager implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringManager.class);

    private MonitoringRepository monitoringRepository;
    private SchedulerManager schedulerManager;

    ///////////////////////////////////////////////////////////////////////////

    public MonitoringManager(
            MonitoringRepository monitoringRepository,
            SchedulerManager schedulerManager
    ) {
        this.monitoringRepository = monitoringRepository;
        this.schedulerManager = schedulerManager;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get all monitorings
     * @return Monitoring list
     */
    public List<Monitoring> getAll() {
        return monitoringRepository.findAllByOrderByName();
    }

    /**
     * Get monitoring with id
     * @param id Monitoring id
     * @return Monitoring or null
     */
    public Monitoring get(Long id) {
        try {
            logger.debug("Finding monitoring with id {}", id);
            return monitoringRepository.findOne(id);
        } catch (Exception e) {
            logger.error("An error occurred while getting monitoring with id " + id, e);
            return null;
        }
    }

    /**
     * Add new monitoring
     * @param monitoring New monitoring
     * @return Status of operation
     */
    public boolean add(Monitoring monitoring) {
        try {
            monitoring = monitoringRepository.save(monitoring);
            logger.info("Added new monitoring with id {}", monitoring.getId());
        } catch (Exception e) {
            logger.error("An error occurred while adding new monitoring", e);
            return false;
        }

        schedulerManager.addMonitoring(monitoring);

        return true;
    }

    /**
     * Update monitoring
     * @param monitoring Updated monitoring data
     * @return Status of operation
     */
    public boolean update(Monitoring monitoring) {
        try {
            monitoringRepository.save(monitoring);
            logger.info("Monitoring with id {} updated", monitoring.getId());
        } catch (Exception e) {
            logger.error("An error occurred while updating monitoring with id {}" + monitoring.getId(), e);
        }

        schedulerManager.updateMonitoring(monitoring);

        return true;
    }

    /**
     * Delete monitoring
     * @param id Monitoring identifier
     * @return Status of operation
     */
    public boolean delete(Long id) {
        Monitoring monitoring = get(id);
        if (monitoring == null) {
            return true;
        }

        try {
            monitoringRepository.delete(monitoring);
            logger.info("Monitoring with id {} removed", monitoring.getId());
        } catch (Exception e) {
            logger.error("An error occurred while deleting monitoring", e);
            return false;
        }

        schedulerManager.deleteMonitoring(monitoring);

        return true;
    }

    /**
     * Check monitoring for validity
     * @param monitoring Monitoring to check
     * @return Check errors
     */
    public List<String> validate(Monitoring monitoring) {
        List<String> errors = new ArrayList<>();

        if (monitoring.getName() == null || monitoring.getName().isEmpty()) {
            errors.add("Name should not be empty");
        }

        if (monitoring.getUrl() == null || monitoring.getUrl().isEmpty()) {
            errors.add("Url should not be empty");
        }

        if (monitoring.getCheckInterval() <= 0) {
            errors.add("Check interval should be greater than zero");
        }

        if (monitoring.getRespondInterval() <= 0) {
            errors.add("Respond interval should be greater than zero");
        }

        return errors;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Add all monitorings to scheduler on startup
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        getAll().forEach(it -> schedulerManager.addMonitoring(it));
    }

}
