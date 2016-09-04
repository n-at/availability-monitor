package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.doublebyte.availabilitymonitor.entities.Email;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;
import ru.doublebyte.availabilitymonitor.entities.TestResult;
import ru.doublebyte.availabilitymonitor.repositories.EmailRepository;

import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private static final Logger logger = LoggerFactory.getLogger(EmailManager.class);

    private JavaMailSender javaMailSender;
    private EmailRepository emailRepository;

    private String notificationFrom;

    ///////////////////////////////////////////////////////////////////////////

    public EmailManager(
            JavaMailSender javaMailSender,
            EmailRepository emailRepository,
            String notificationFrom
    ) {
        this.javaMailSender = javaMailSender;
        this.emailRepository = emailRepository;
        this.notificationFrom = notificationFrom;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get all existing emails
     * @return
     */
    public List<Email> getAll() {
        try {
            return emailRepository.findAllByOrderByAddress();
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
            email = emailRepository.save(email);
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
            emailRepository.delete(id);
            logger.info("Email with id {} deleted", id);
        } catch (Exception e) {
            logger.error("An error occurred while removing email with id " + id, e);
            return false;
        }
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Send email notifications of changed test result
     * @param monitoring
     * @param currentTestResult
     * @param prevTestResult
     */
    public void sendNotifications(Monitoring monitoring, TestResult currentTestResult, TestResult prevTestResult) {
        logger.debug("Sending notification for {}", monitoring);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(notificationFrom);
        message.setSubject(getNotificationSubject(monitoring));
        message.setText(getNotificationBody(monitoring, currentTestResult, prevTestResult));

        getAll().forEach(it -> {
            message.setTo(it.getAddress());
            javaMailSender.send(message);
        });
    }

    /**
     * Notification subject for monitoring
     * @param monitoring
     * @return
     */
    private String getNotificationSubject(Monitoring monitoring) {
        return String.format("%s: availability status changed", monitoring.getName());
    }

    /**
     * Notification text for monitoring and statuses
     * @param monitoring
     * @param currentTestResult
     * @param prevTestResult
     * @return
     */
    private String getNotificationBody(Monitoring monitoring, TestResult currentTestResult, TestResult prevTestResult) {
        String currentStatus = currentTestResult.getResult().toString();
        String prevStatus = prevTestResult == null ? "NOT TESTED" : prevTestResult.getResult().toString();

        return String.format(
                "%s (%s) availability status changed\n" +
                "Date: %s\n" +
                "New status: %s\n" +
                "Previous status: %s\n",
                monitoring.getName(), monitoring.getUrl(),
                currentTestResult.getCreatedAtFormatted(),
                currentStatus, prevStatus
        );
    }

}
