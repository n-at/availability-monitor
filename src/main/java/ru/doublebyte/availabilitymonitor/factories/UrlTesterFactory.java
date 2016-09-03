package ru.doublebyte.availabilitymonitor.factories;

import ru.doublebyte.availabilitymonitor.managers.TestResultManager;
import ru.doublebyte.availabilitymonitor.entities.Monitoring;
import ru.doublebyte.availabilitymonitor.testers.Tester;
import ru.doublebyte.availabilitymonitor.testers.UrlTester;

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
