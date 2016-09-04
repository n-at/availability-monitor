package ru.doublebyte.availabilitymonitor.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.doublebyte.availabilitymonitor.entities.Email;

import java.util.List;

public interface EmailRepository extends CrudRepository<Email, Long> {

    List<Email> findAllByOrderByAddress();

}
