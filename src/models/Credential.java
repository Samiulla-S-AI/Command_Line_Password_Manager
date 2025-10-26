package models;

import java.time.LocalDateTime;

public class Credential implements Comparable<Credential> {
    private int credentialId;
    private String platformName;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    // Constructor
    public Credential(String platformName, String username, String email, String password) {
        this.platformName = platformName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getCredentialId() { return credentialId; }
    public void setCredentialId(int credentialId) { this.credentialId = credentialId; }

    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }

    @Override
    public int compareTo(Credential other) {
        return this.platformName.compareToIgnoreCase(other.platformName);
    }

    @Override
    public String toString() {
        return String.format("Platform: %s, Username: %s, Email: %s, Created: %s",
            platformName,
            username != null ? username : "-",
            email != null ? email : "-",
            createdDate);
    }
}