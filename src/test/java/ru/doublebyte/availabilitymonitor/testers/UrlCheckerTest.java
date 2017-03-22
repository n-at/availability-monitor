package ru.doublebyte.availabilitymonitor.testers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.doublebyte.availabilitymonitor.TestController;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {
        TestController.class,
    },
    properties = {
        "logging.level.ru.doublebyte.availabilitymonitor=DEBUG"
    }
)
@SpringBootApplication(
    exclude = {
        FlywayAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class,
    }
)
public class UrlCheckerTest {

    private static final int CHECK_TIMEOUT = 500;
    private static final int BAD_TIMEOUT = 1000;

    @LocalServerPort
    private int serverPort;

    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void check() throws Exception {
        testSuccess();
        testTimeout();
        testBad4XX();
        testBad5XX();
        testNoConnection();
    }

    private void testSuccess() {
        UrlChecker checker = new UrlChecker(successUrl(), CHECK_TIMEOUT);
        for (int i = 0; i < 10; i++) {
            Result result = checker.check();
            assertEquals(Result.SUCCESS, result);
        }
    }

    private void testTimeout() {
        UrlChecker checker = new UrlChecker(timeoutUrl(), CHECK_TIMEOUT);
        Result result = checker.check();
        assertEquals(Result.TIMEOUT, result);
    }

    private void testBad4XX() {
        UrlChecker checker = new UrlChecker(code4XXUrl(), CHECK_TIMEOUT);
        Result result = checker.check();
        assertEquals(Result.BAD_STATUS, result);
    }

    private void testBad5XX() {
        UrlChecker checker = new UrlChecker(code5XXUrl(), CHECK_TIMEOUT);
        Result result = checker.check();
        assertEquals(Result.BAD_STATUS, result);
    }

    private void testNoConnection() {
        UrlChecker checker = new UrlChecker(incorrectUrl(), CHECK_TIMEOUT);
        Result result = checker.check();
        assertEquals(Result.NO_CONNECTION, result);
    }

    ///////////////////////////////////////////////////////////////////////////

    private String successUrl() {
        return String.format("http://localhost:%d/test/status/200", serverPort);
    }

    private String timeoutUrl() {
        return String.format("http://localhost:%d/test/timeout/%d", serverPort, BAD_TIMEOUT);
    }

    private String code4XXUrl() {
        return String.format("http://localhost:%d/test/status/404", serverPort);
    }

    private String code5XXUrl() {
        return String.format("http://localhost:%d/test/status/502", serverPort);
    }

    private String incorrectUrl() {
        int incorrectServerPort = serverPort + 1;
        return String.format("http://localhost:%d/test/incorrect", incorrectServerPort);
    }

}