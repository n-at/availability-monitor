package ru.doublebyte.availabilitymonitor.managers;

import org.junit.Before;
import org.junit.Test;
import ru.doublebyte.availabilitymonitor.repositories.MonitoringRepository;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MonitoringManagerTest {

    private MonitoringManager monitoringManager;

    @Before
    public void setUp() {
        monitoringManager = new MonitoringManager(monitoringRepository(), schedulerManager());
    }

    @Test
    public void validate() throws Exception {
        Monitoring monitoring = null;
        List<String> errors = null;

        monitoring = new Monitoring("http://example.com", "Test", 1, 1);
        errors = monitoringManager.validate(monitoring);
        assertNotNull(errors);
        assertEquals(0, errors.size());

        monitoring = new Monitoring("", "Test", 1, 1);
        errors = monitoringManager.validate(monitoring);
        assertEquals(1, errors.size());

        monitoring = new Monitoring(null, "Test", 1, 1);
        errors = monitoringManager.validate(monitoring);
        assertEquals(1, errors.size());

        monitoring = new Monitoring("http://example.com", "", 1, 1);
        errors = monitoringManager.validate(monitoring);
        assertEquals(1, errors.size());

        monitoring = new Monitoring("http://example.com", null, 1, 1);
        errors = monitoringManager.validate(monitoring);
        assertEquals(1, errors.size());

        monitoring = new Monitoring("http://example.com", "Test", 0, 1);
        errors = monitoringManager.validate(monitoring);
        assertEquals(1, errors.size());

        monitoring = new Monitoring("http://example.com", "Test", 1, 0);
        errors = monitoringManager.validate(monitoring);
        assertEquals(1, errors.size());

        monitoring = new Monitoring(null, null, -1, -1);
        errors = monitoringManager.validate(monitoring);
        assertEquals(4, errors.size());
    }

    ///////////////////////////////////////////////////////////////////////////

    private SchedulerManager schedulerManager() {
        SchedulerManager schedulerManager = mock(SchedulerManager.class);

        return schedulerManager;
    }

    private MonitoringRepository monitoringRepository() {
        MonitoringRepository monitoringRepository = mock(MonitoringRepository.class);

        return monitoringRepository;
    }

}