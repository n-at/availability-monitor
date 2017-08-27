package ru.doublebyte.availabilitymonitor.storages;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class MonitoringStorage extends AbstractStorage {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringStorage.class);
    private static final Random RANDOM = new Random();
    private static final String STORAGE_FILE_NAME = "monitoring.json";

    private Map<Long, Monitoring> monitorings = new ConcurrentHashMap<>();

    public MonitoringStorage() {
        try {
            setFileName("monitoring.json");
            this.monitorings = load(new TypeReference<Map<Long, Monitoring>>() {});
        } catch (Exception e) {
            logger.error("Monitorings load error", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public List<Monitoring> getAll() {
        return new ArrayList<>(monitorings.values());
    }

    public Monitoring get(Long id) {
        if (!monitorings.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Monitoring with ud %d not found", id));
        }
        return monitorings.get(id);
    }

    public Monitoring save(Monitoring monitoring) {
        Long id = RANDOM.nextLong();

        if (monitoring.getId() != null) {
            id = monitoring.getId();
        } else {
            monitoring.setId(id);
        }

        monitorings.put(id, monitoring);
        save(monitorings);

        return monitoring;
    }

    public void delete(Long id) {
        if (!monitorings.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Monitoring with ud %d not found", id));
        }

        monitorings.remove(id);
        save(monitorings);
    }

    public void delete(Monitoring monitoring) {
        delete(monitoring.getId());
    }

}
