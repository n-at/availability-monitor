package ru.doublebyte.availabilitymonitor.factories;

import ru.doublebyte.availabilitymonitor.types.Monitoring;
import ru.doublebyte.availabilitymonitor.types.Tester;

public interface TesterFactory {

    Tester createTester(Monitoring monitoring);

}
