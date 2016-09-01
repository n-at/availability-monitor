package ru.doublebyte.availabilitymonitor.managers;

import ru.doublebyte.availabilitymonitor.repositories.MonitoringRepository;
import ru.doublebyte.availabilitymonitor.types.Monitoring;

import java.util.List;

/**
 * Manage monitoring instances
 */
public class MonitoringManager {

    private MonitoringRepository monitoringRepository;

    ///////////////////////////////////////////////////////////////////////////

    public MonitoringManager(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get all monitorings
     * @return Monitoring list
     */
    public List<Monitoring> getAll() {
        return monitoringRepository.findAllByOrderByUrl();
    }

}
