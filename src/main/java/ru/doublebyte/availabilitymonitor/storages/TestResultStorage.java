package ru.doublebyte.availabilitymonitor.storages;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.availabilitymonitor.entities.TestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class TestResultStorage extends AbstractStorage<TestResult> {

    private static final Logger logger = LoggerFactory.getLogger(TestResultStorage.class);
    private static final Random RANDOM = new Random();

    private Map<Long, TestResult> testResults = new ConcurrentHashMap<>();

    public TestResultStorage() {
        try {
            setFileName("test_result.json");
            testResults = load(new TypeReference<Map<Long, TestResult>>() {});
        } catch (Exception e) {
            logger.error("TestResults load error", e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    @Override
    public List<TestResult> getAll() {
        return new ArrayList<>(testResults.values());
    }

    @Override
    public TestResult get(Long id) {
        if (!testResults.containsKey(id)) {
            throw new IllegalArgumentException(String.format("TestResult with id %d not found", id));
        }
        return testResults.get(id);
    }

    @Override
    public TestResult save(TestResult testResult) {
        Long id = RANDOM.nextLong();

        if (testResult.getId() != null) {
            id = testResult.getId();
        } else {
            testResult.setId(id);
        }

        testResults.put(id, testResult);
        save(testResults);

        return testResult;
    }

    @Override
    public void delete(Long id) {
        if (!testResults.containsKey(id)) {
            throw new IllegalArgumentException(String.format("TestResult with id %d not found", id));
        }

        testResults.remove(id);
        save(testResults);
    }

    @Override
    public void delete(TestResult value) {
        delete(value.getId());
    }

}
