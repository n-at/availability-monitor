package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;
import ru.doublebyte.availabilitymonitor.repositories.TestResultRepository;
import ru.doublebyte.availabilitymonitor.entities.TestResult;
import ru.doublebyte.availabilitymonitor.entities.TestResultDifference;
import ru.doublebyte.availabilitymonitor.testers.Result;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestResultManager {

    private static final Logger logger = LoggerFactory.getLogger(TestResultManager.class);

    private TestResultRepository testResultRepository;
    private TestResultDifferenceManager testResultDifferenceManager;
    private NotificationManager notificationManager;

    ///////////////////////////////////////////////////////////////////////////

    public TestResultManager(
            TestResultRepository testResultRepository,
            TestResultDifferenceManager testResultDifferenceManager,
            NotificationManager notificationManager
    ) {
        this.testResultRepository = testResultRepository;
        this.testResultDifferenceManager = testResultDifferenceManager;
        this.notificationManager = notificationManager;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get latest test result for monitoring with given id
     * @param monitoringId
     * @return
     */
    public TestResult getLatestForMonitoring(Long monitoringId) {
        try {
            return testResultRepository.findFirstByMonitoringIdOrderByCreatedAtDesc(monitoringId);
        } catch (Exception e) {
            logger.error("An error occurred while finding latest result for monitoring with id " + monitoringId, e);
            return null;
        }
    }

    /**
     * Get test results for monitoring with given id
     * @param monitoringId
     * @param page
     * @return
     */
    public List<TestResult> getForMonitoringByPage(Long monitoringId, int page, int resultsOnPage) {
        Pageable pageable = new PageRequest(page, resultsOnPage);
        try {
            return testResultRepository.findByMonitoringIdOrderByCreatedAtDesc(monitoringId, pageable);
        } catch (Exception e) {
            logger.error("An error occurred while requesting test results for monitoring with id " + monitoringId);
            return new ArrayList<>();
        }
    }

    /**
     * Save test result
     * @param monitoring
     * @param testResult
     * @return
     */
    public boolean add(Monitoring monitoring, TestResult testResult) {
        TestResult latestTestResult = getLatestForMonitoring(testResult.getMonitoringId());

        try {
            testResult = testResultRepository.save(testResult);
            logger.debug("Saved test result with id {} for monitoring {}",
                    testResult.getId(), testResult.getMonitoringId());
        } catch (Exception e) {
            logger.error("An error occurred while saving test result for monitoring with id {}",
                    testResult.getMonitoringId());
            return false;
        }

        checkDifferences(monitoring, testResult, latestTestResult);

        return true;
    }

    /**
     * Process result differences
     * @param monitoring
     * @param currentTestResult
     * @param latestTestResult
     */
    private void checkDifferences(Monitoring monitoring, TestResult currentTestResult, TestResult latestTestResult) {
        Result latestResult = latestTestResult == null ? null : latestTestResult.getResult();
        Result currentResult = currentTestResult.getResult();

        if (currentResult == latestResult) {
            return;
        }

        TestResultDifference testResultDifference =
                new TestResultDifference(currentTestResult, latestTestResult);
        testResultDifferenceManager.add(testResultDifference);

        notificationManager.sendNotifications(monitoring, currentTestResult, latestTestResult);
    }

    /**
     * Delete test results older than desired date
     * @param date
     * @return
     */
    public boolean deleteOlderThan(LocalDateTime date) {
        logger.info("Removing test results older than {}", date);

        try {
            int count = testResultRepository.deleteOlder(date);
            logger.info("Removed {} test results", count);
        } catch (Exception e) {
            logger.error("An error occurred while removing test results", e);
            return false;
        }

        return true;
    }

}
