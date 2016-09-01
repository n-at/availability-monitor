package ru.doublebyte.availabilitymonitor.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.doublebyte.availabilitymonitor.types.Monitoring;

import java.util.List;

public interface MonitoringRepository extends CrudRepository<Monitoring, Long> {

    List<Monitoring> findAllByOrderByUrl();

}
