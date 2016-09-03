package ru.doublebyte.availabilitymonitor.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlChecker {

    private static Logger logger = LoggerFactory.getLogger(UrlChecker.class);

    private String url;
    private int respondTimeout;

    ///////////////////////////////////////////////////////////////////////////

    public UrlChecker(String url, int respondTimeout) {
        this.url = url;
        this.respondTimeout = respondTimeout;
    }

    public Result check() {
        return Result.SUCCESS; //TODO perform check
    }

    ///////////////////////////////////////////////////////////////////////////

    public static enum Result {
        SUCCESS(true),
        NO_CONNECTION(false),
        TIMEOUT(false),
        BAD_STATUS(false);

        private boolean success;

        Result(boolean success) {
            this.success = success;
        }
    }

}
