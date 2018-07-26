package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;
import ru.doublebyte.availabilitymonitor.entities.TestResult;

/**
 * Controls email notifications
 */
public class NotificationManager {

    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);

    private JavaMailSender javaMailSender;
    private EmailManager emailManager;
    private String notificationFrom;

    ///////////////////////////////////////////////////////////////////////////

    public NotificationManager(
            JavaMailSender javaMailSender,
            EmailManager emailManager,
            String notificationFrom
    ) {
        this.javaMailSender = javaMailSender;
        this.emailManager = emailManager;
        this.notificationFrom = notificationFrom;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Send email notifications of changed test result
     *
     * @param monitoring
     * @param currentTestResult
     * @param prevTestResult
     */
    public void sendNotifications(Monitoring monitoring, TestResult currentTestResult, TestResult prevTestResult) {
        logger.debug("Sending notification for {}", monitoring);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(notificationFrom);
        message.setSubject(getNotificationSubject(monitoring, currentTestResult));
        message.setText(getNotificationBody(monitoring, currentTestResult, prevTestResult));

        emailManager.getAll().forEach(it -> {
            message.setTo(it.getAddress());
            javaMailSender.send(message);
        });
    }

    /**
     * Notification subject for monitoring
     *
     * @param monitoring
     * @return
     */
    private String getNotificationSubject(Monitoring monitoring, TestResult currentTestResult) {
        return String.format("%s: availability status changed, now %s",
                monitoring.getName(), currentTestResult.getResult().toString());
    }

    /**
     * Notification text for monitoring and statuses
     *
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
