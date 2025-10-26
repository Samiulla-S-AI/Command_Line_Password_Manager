package filehandling;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import models.Credential;
import services.CredentialService;

public class FileBackupManager {
    private static final String BACKUP_DIR = "backup";
    private final CredentialService credentialService;
    
    public FileBackupManager(CredentialService credentialService) {
        this.credentialService = credentialService;
        createBackupDirectory();
    }
    
    private void createBackupDirectory() {
        try {
            Files.createDirectories(Paths.get(BACKUP_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create backup directory: " + e.getMessage());
        }
    }
    
    public String exportBackup() throws Exception {
        List<Credential> credentials = credentialService.getAllCredentials();
        if (credentials.isEmpty()) {
            throw new Exception("No credentials to export");
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("%s/passwords_backup_%s.txt", BACKUP_DIR, timestamp);
        Path filePath = Paths.get(fileName);
        
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Credential credential : credentials) {
                if (credential.getPlatformName() == null || credential.getPassword() == null) {
                    continue; // Skip invalid credentials
                }
                String line = String.format("%s|%s|%s|%s",
                    credential.getPlatformName().trim(),
                    credential.getUsername() != null ? credential.getUsername().trim() : "",
                    credential.getEmail() != null ? credential.getEmail().trim() : "",
                    credential.getPassword()
                );
                String encryptedLine = FileEncryption.encrypt(line);
                writer.write(encryptedLine);
                writer.newLine();
            }
            System.out.println("Backup created successfully at: " + fileName);
            return fileName;
        }
    }
    
    public int importBackup(String filePath) throws Exception {
        if (!Files.exists(Paths.get(filePath))) {
            throw new FileNotFoundException("Backup file not found: " + filePath);
        }

        int importedCount = 0;
        int lineNumber = 0;
        List<String> existingPlatforms = new ArrayList<>();
        
        // Get list of existing platform names to avoid duplicates
        for (Credential cred : credentialService.getAllCredentials()) {
            existingPlatforms.add(cred.getPlatformName().toLowerCase());
        }
        
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String encryptedLine;
            while ((encryptedLine = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String line = FileEncryption.decrypt(encryptedLine);
                    String[] parts = line.split("\\|");
                    
                    if (parts.length != 4) {
                        System.out.printf("Line %d: Invalid format, skipping%n", lineNumber);
                        continue;
                    }

                    String platformName = parts[0].trim();
                    String username = parts[1].isEmpty() ? null : parts[1].trim();
                    String email = parts[2].isEmpty() ? null : parts[2].trim();
                    String password = parts[3];

                    // Basic validation
                    if (platformName.isEmpty() || password.isEmpty()) {
                        System.out.printf("Line %d: Missing required fields, skipping%n", lineNumber);
                        continue;
                    }

                    // Check for duplicates
                    if (existingPlatforms.contains(platformName.toLowerCase())) {
                        System.out.printf("Line %d: Platform '%s' already exists, skipping%n", 
                            lineNumber, platformName);
                        continue;
                    }

                    Credential credential = new Credential(platformName, username, email, password);
                    if (credentialService.addCredential(credential)) {
                        importedCount++;
                        existingPlatforms.add(platformName.toLowerCase());
                        System.out.printf("Imported credentials for: %s%n", platformName);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.printf("Line %d: Invalid data format - %s%n", lineNumber, e.getMessage());
                } catch (Exception e) {
                    System.out.printf("Line %d: Error processing line - %s%n", lineNumber, e.getMessage());
                }
            }
        }
        
        if (importedCount == 0) {
            System.out.println("No credentials were imported. Please check the file format and content.");
        } else {
            System.out.printf("Successfully imported %d credential(s)%n", importedCount);
        }
        
        return importedCount;
    }
}