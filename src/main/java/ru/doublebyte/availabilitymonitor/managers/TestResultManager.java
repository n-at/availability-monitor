package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;
import ru.doublebyte.availabilitymonitor.repositories.TestResultRepository;
import ru.doublebyte.availabilitymonitor.entities.TestResult;
import ru.doublebyte.availabilitymonitor.entities.TestResultDifference;
import ru.doublebyte.availabilitymonitor.testers.UrlChecker;

import java.util.ArrayList;
import java.util.List;

public class TestResultManager {

    private static final Logger logger = LoggerFactory.getLogger(TestResultManager.class);

    private TestResultRepository testResultRepository;
    private TestResultDifferenceManager testResultDifferenceManager;
    private EmailManager emailManager;

    ///////////////////////////////////////////////////////////////////////////

    public TestResultManager(
            TestResultRepository testResultRepository,
            TestResultDifferenceManager testResultDifferenceManager,
            EmailManager emailManager
    ) {
        this.testResultRepository = testResultRepository;
        this.testResultDifferenceManager = testResultDifferenceManager;
        this.emailManager = emailManager;
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
        UrlChecker.Result latestResult = latestTestResult == null ? null : latestTestResult.getResult();
        UrlChecker.Result currentResult = currentTestResult.getResult();

        if (currentResult == latestResult) {
            return;
        }

        TestResultDifference testResultDifference =
                new TestResultDifference(currentTestResult, latestTestResult);
        testResultDifferenceManager.add(testResultDifference);

        emailManager.sendNotifications(monitoring, currentTestResult, latestTestResult);
    }

}
