package models;

import java.time.LocalDateTime;

public class ActivityLog {
    private int logId;
    private String actionType;
    private String actionDetails;
    private LocalDateTime timestamp;

    // Constructor
    public ActivityLog(String actionType, String actionDetails) {
        this.actionType = actionType;
        this.actionDetails = actionDetails;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getActionDetails() { return actionDetails; }
    public void setActionDetails(String actionDetails) { this.actionDetails = actionDetails; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s",
            timestamp,
            actionType,
            actionDetails);
    }
}