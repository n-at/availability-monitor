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

public class MonitoringStorage extends AbstractStorage<Monitoring> {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringStorage.class);
    private static final Random RANDOM = new Random();

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

    @Override
    public List<Monitoring> getAll() {
        return new ArrayList<>(monitorings.values());
    }

    @Override
    public Monitoring get(Long id) {
        if (!monitorings.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Monitoring with ud %d not found", id));
        }
        return monitorings.get(id);
    }

    @Override
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

    @Override
    public void delete(Long id) {
        if (!monitorings.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Monitoring with ud %d not found", id));
        }

        monitorings.remove(id);
        save(monitorings);
    }

    @Override
    public void delete(Monitoring monitoring) {
        delete(monitoring.getId());
    }

}
