package ru.doublebyte.availabilitymonitor.factories;

import ru.doublebyte.availabilitymonitor.managers.TestResultManager;
import ru.doublebyte.availabilitymonitor.types.Monitoring;
import ru.doublebyte.availabilitymonitor.types.Tester;
import ru.doublebyte.availabilitymonitor.types.UrlTester;

public class UrlTesterFactory implements TesterFactory {

    private TestResultManager testResultManager;

    public UrlTesterFactory(TestResultManager testResultManager) {
        this.testResultManager = testResultManager;
    }

    @Override
    public Tester createTester(Monitoring monitoring) {
        return new UrlTester(testResultManager, monitoring);
    }

}
