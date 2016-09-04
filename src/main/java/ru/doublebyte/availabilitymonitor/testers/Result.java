package ru.doublebyte.availabilitymonitor.testers;

/**
 * URL check result
 */
public enum Result {
    SUCCESS(true, "SUCCESS"),
    NO_CONNECTION(false, "NO CONNECTION"),
    TIMEOUT(false, "TIMEOUT"),
    BAD_STATUS(false, "BAD STATUS"),;

    private boolean success;
    private String display;

    Result(boolean success, String display) {
        this.success = success;
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getDisplay() {
        return display;
    }
}
