package ru.doublebyte.availabilitymonitor.storages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.availabilitymonitor.entities.Email;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EmailStorage {

    private static final Logger logger = LoggerFactory.getLogger(EmailStorage.class);

    private static final Random RANDOM = new Random();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String STORAGE_FILE_NAME = "email.json";

    private Map<Long, Email> emails = new ConcurrentHashMap<>();

    public EmailStorage() {

        load();
    }

    ///////////////////////////////////////////////////////////////////////////

    public List<Email> get() {
        return new ArrayList<>(emails.values());
    }

    public Email save(Email email) {
        Long id = RANDOM.nextLong();
        email.setId(id);

        emails.put(id, email);
        save();

        return email;
    }

    public void delete(Long id) {
        if (!emails.containsKey(id)) {
            throw new IllegalArgumentException(String.format("Email with id %d not found", id));
        }

        emails.remove(id);
        save();
    }

    ///////////////////////////////////////////////////////////////////////////

    private synchronized void load() {
        try {
            TypeReference<HashMap<Long, Email>> typeReference =
                    new TypeReference<HashMap<Long, Email>>() {};

            byte[] contents = Files.readAllBytes(Paths.get(STORAGE_FILE_NAME));
            HashMap<Long, Email> emails = OBJECT_MAPPER.readValue(contents, typeReference);
            this.emails = new ConcurrentHashMap<>(emails);
        } catch (Exception e) {
            logger.error("Emails load error", e);
        }
    }

    private synchronized void save() {
        try (
            OutputStream out = Files.newOutputStream(Paths.get(STORAGE_FILE_NAME))
        ) {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(out, emails);
        } catch (Exception e) {
            logger.error("Emails save error", e);
        }
    }

}
