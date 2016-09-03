package ru.doublebyte.availabilitymonitor.factories;

import ru.doublebyte.availabilitymonitor.entities.Monitoring;
import ru.doublebyte.availabilitymonitor.testers.Tester;

public interface TesterFactory {

    Tester createTester(Monitoring monitoring);

}
