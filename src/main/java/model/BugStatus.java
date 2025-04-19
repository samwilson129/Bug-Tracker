package main.java.model;

public enum BugStatus {
    reported,
    in_progress,
    fixed,
    verified,
    closed;

    public static BugStatus fromString(String status) {
        System.out.println("Converting string to BugStatus: " + status);
        return BugStatus.valueOf(status.toLowerCase());
    }
}