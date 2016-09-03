package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.availabilitymonitor.repositories.TestResultRepository;
import ru.doublebyte.availabilitymonitor.types.TestResult;

public class TestResultManager {

    private static final Logger logger = LoggerFactory.getLogger(TestResultManager.class);

    private TestResultRepository testResultRepository;

    ///////////////////////////////////////////////////////////////////////////

    public TestResultManager(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    ///////////////////////////////////////////////////////////////////////////

    public boolean add(TestResult testResult) {
        try {
            testResult = testResultRepository.save(testResult);
            logger.info("Saved test result with id {} for monitoring {}", //TODO set debug level
                    testResult.getId(), testResult.getMonitoringId());
        } catch (Exception e) {
            logger.error("An error occurred while saving test result for monitoring with id {}",
                    testResult.getMonitoringId());
            return false;
        }

        //TODO send notifications

        return true;
    }

}
