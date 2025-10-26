package filehandling;

import models.ActivityLog;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityLogger {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "activity_log.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public ActivityLogger() {
        createLogDirectory();
    }
    
    private void createLogDirectory() {
        try {
            Files.createDirectories(Paths.get(LOG_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create log directory: " + e.getMessage());
        }
    }
    
    public void log(ActivityLog activityLog) {
        String logPath = Paths.get(LOG_DIR, LOG_FILE).toString();
        String logEntry = String.format("[%s] %s: %s%n",
            activityLog.getTimestamp().format(formatter),
            activityLog.getActionType(),
            activityLog.getActionDetails()
        );
        
        try {
            Files.write(
                Paths.get(logPath),
                logEntry.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    public void log(String actionType, String actionDetails) {
        log(new ActivityLog(actionType, actionDetails));
    }
}