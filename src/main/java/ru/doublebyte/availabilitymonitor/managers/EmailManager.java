package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.availabilitymonitor.entities.Email;
import ru.doublebyte.availabilitymonitor.storages.EmailStorage;

import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private static final Logger logger = LoggerFactory.getLogger(EmailManager.class);

    private EmailStorage emailStorage;

    ///////////////////////////////////////////////////////////////////////////

    public EmailManager(EmailStorage emailStorage) {
        this.emailStorage = emailStorage;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get all existing emails
     * @return
     */
    public List<Email> getAll() {
        try {
            return emailStorage.get();
        } catch (Exception e) {
            logger.error("An error occurred while getting all emails", e);
            return new ArrayList<>();
        }
    }

    /**
     * Add new email
     * @param email
     * @return
     */
    public boolean add(Email email) {
        if (email.getAddress() == null || email.getAddress().isEmpty()) {
            logger.debug("Trying to save email with empty address");
            return false;
        }

        try {
            email = emailStorage.save(email);
            logger.info("Saved new email with id {}", email.getId());
        } catch (Exception e) {
            logger.error("An error occurred while saving new email", e);
            return false;
        }

        return true;
    }

    /**
     * Delete email
     * @param id
     * @return
     */
    public boolean delete(Long id) {
        try {
            emailStorage.delete(id);
            logger.info("Email with id {} deleted", id);
        } catch (Exception e) {
            logger.error("An error occurred while removing email with id " + id, e);
            return false;
        }
        return true;
    }

}
