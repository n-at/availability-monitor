package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.doublebyte.availabilitymonitor.repositories.TestResultDifferenceRepository;
import ru.doublebyte.availabilitymonitor.types.TestResultDifference;

import java.util.ArrayList;
import java.util.List;

public class TestResultDifferenceManager {

    private static final Logger logger = LoggerFactory.getLogger(TestResultDifferenceManager.class);

    private TestResultDifferenceRepository testResultDifferenceRepository;

    ///////////////////////////////////////////////////////////////////////////

    public TestResultDifferenceManager(
            TestResultDifferenceRepository testResultDifferenceRepository
    ) {
        this.testResultDifferenceRepository = testResultDifferenceRepository;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Add new test result difference
     * @param testResultDifference
     * @return Status of operation
     */
    public boolean add(TestResultDifference testResultDifference) {
        try {
            testResultDifference = testResultDifferenceRepository.save(testResultDifference);
            logger.info("Added test result difference with id {}", testResultDifference.getId());
        } catch (Exception e) {
            logger.error("An error occurred while adding test result difference", e);
            return false;
        }
        return true;
    }

    /**
     * Get result differences for monitoring
     * @param monitoringId
     * @param page
     * @param resultsOnPage
     * @return
     */
    public List<TestResultDifference> getForMonitoringByPage(Long monitoringId, int page, int resultsOnPage) {
        Pageable pageable = new PageRequest(page, resultsOnPage);
        try {
            return testResultDifferenceRepository.findByResultMonitoringIdOrderByResultCreatedAtDesc(
                    monitoringId, pageable);
        } catch (Exception e) {
            logger.error("An error occurred while finding test result differences for monitoring with id " + monitoringId, e);
            return new ArrayList<>();
        }
    }

}
