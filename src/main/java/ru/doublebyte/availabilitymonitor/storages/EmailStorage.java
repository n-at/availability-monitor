package ru.doublebyte.availabilitymonitor.storages;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.availabilitymonitor.entities.Email;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EmailStorage extends AbstractStorage<Email> {

    private static final Logger logger = LoggerFactory.getLogger(EmailStorage.class);
    private static final Random RANDOM = new Random();

    private Map<Long, Email> emails = new ConcurrentHashMap<>();

    public EmailStorage() {
        try {
            setFileName("email.json");
            this.emails = load(new TypeReference<Map<Long, Email>>() {});
        } catch (Exception e) {
            logger.error("Emails load error", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Email get(Long id) {
        if (!emails.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Email with id %d not found", id));
        }
        return emails.get(id);
    }

    @Override
    public List<Email> getAll() {
        List<Email> emailList = new ArrayList<>(emails.values());
        emailList.sort(Comparator.comparing(Email::getAddress));
        return emailList;
    }

    @Override
    public Email save(Email email) {
        Long id = RANDOM.nextLong();

        if (email.getId() != null) {
            id = email.getId();
        } else {
            email.setId(id);
        }

        emails.put(id, email);
        save(emails);

        return email;
    }

    @Override
    public void delete(Long id) {
        if (!emails.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Email with id %d not found", id));
        }

        emails.remove(id);
        save(emails);
    }

    @Override
    public void delete(Email email) {
        delete(email.getId());
    }

}
