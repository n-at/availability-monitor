package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;
import ru.doublebyte.availabilitymonitor.entities.TestResult;
import ru.doublebyte.availabilitymonitor.storages.TestResultStorage;
import ru.doublebyte.availabilitymonitor.testers.Result;

import java.time.LocalDateTime;

public class TestResultManager {

    private static final Logger logger = LoggerFactory.getLogger(TestResultManager.class);

    private TestResultStorage testResultStorage;
    private NotificationManager notificationManager;

    ///////////////////////////////////////////////////////////////////////////

    public TestResultManager(
            TestResultStorage testResultStorage,
            NotificationManager notificationManager
    ) {
        this.testResultStorage = testResultStorage;
        this.notificationManager = notificationManager;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get test result by monitoring id
     * @param id
     * @return
     */
    public TestResult getById(Long id) {
        try {
            return testResultStorage.get(id);
        } catch (Exception e) {
            logger.error("An error occurred while getting test result by id " + id, e);
            return null;
        }
    }

    /**
     * Save test result
     * @param monitoring
     * @param testResult
     * @return
     */
    public boolean add(Monitoring monitoring, TestResult testResult) {
        TestResult latestTestResult;
        try {
            latestTestResult = testResultStorage.get(monitoring.getId());
        } catch (Exception e) {
            logger.error("An error occurred while finding latest result for monitoring with id " + monitoring.getId(), e);

            latestTestResult = new TestResult();
            latestTestResult.setId(monitoring.getId());
            latestTestResult.setCreatedAt(LocalDateTime.MIN);
            latestTestResult.setResult(Result.NO_CONNECTION);
        }

        try {
            testResult.setId(monitoring.getId());
            testResult = testResultStorage.save(testResult);
            logger.debug("Saved test result for monitoring {}", testResult.getId());
        } catch (Exception e) {
            logger.error("An error occurred while saving test result for monitoring with id {}",
                    monitoring.getId());
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

        notificationManager.sendNotifications(monitoring, currentTestResult, latestTestResult);
    }

}
