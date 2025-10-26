import datastructures.*;
import filehandling.*;
import java.util.List;
import java.util.Scanner;
import models.*;
import services.*;
import utils.ConsoleColors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthenticationService authService = new AuthenticationService();
    private static final CredentialService credentialService = new CredentialService();
    private static final PasswordGenerator passwordGenerator = new PasswordGenerator();
    private static final PasswordStrengthChecker strengthChecker = new PasswordStrengthChecker();
    private static final FileBackupManager backupManager = new FileBackupManager(credentialService);
    private static final ActivityLogger activityLogger = new ActivityLogger();
    private static final CredentialHashMap credentialMap = new CredentialHashMap();
    private static final RecentAccessQueue recentAccess = new RecentAccessQueue(5);

    public static void main(String[] args) {
        System.out.println(ConsoleColors.BLUE_BOLD + "╔════════════════════════════════════════════╗");
        System.out.println("║      Welcome to Password Manager Console     ║");
        System.out.println("╚════════════════════════════════════════════╝" + ConsoleColors.RESET);

        if (login()) {
            showMainMenu();
        } else {
            System.out.println(ConsoleColors.RED + "Maximum login attempts exceeded. Exiting..." + ConsoleColors.RESET);
        }
    }

    private static boolean login() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print(ConsoleColors.YELLOW + "Username: " + ConsoleColors.RESET);
            String username = scanner.nextLine();
            System.out.print(ConsoleColors.YELLOW + "Password: " + ConsoleColors.RESET);
            String password = scanner.nextLine();

            if (authService.authenticate(username, password)) {
                System.out.println(ConsoleColors.GREEN + "Login successful!" + ConsoleColors.RESET);
                activityLogger.log("LOGIN", "User logged in successfully");
                return true;
            }

            attempts++;
            System.out.println(ConsoleColors.RED + "Invalid credentials! Attempts remaining: " + 
                (MAX_ATTEMPTS - attempts) + ConsoleColors.RESET);
            activityLogger.log("LOGIN_FAILED", "Login attempt failed");
        }
        return false;
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n" + ConsoleColors.BLUE_BOLD + "╔════════════════════════════════════════════╗");
            System.out.println("║              MAIN MENU                   ║");
            System.out.println("╠════════════════════════════════════════════╣");
            System.out.println("║ 1. Add New Credential                      ║");
            System.out.println("║ 2. View All Credentials                    ║");
            System.out.println("║ 3. Update Existing Credential             ║");
            System.out.println("║ 4. Delete Credential                      ║");
            System.out.println("║ 5. Search Credential                      ║");
            System.out.println("║ 6. Generate Strong Password               ║");
            System.out.println("║ 7. Check Password Strength                ║");
            System.out.println("║ 8. Export Backup                          ║");
            System.out.println("║ 9. Import Backup                          ║");
            System.out.println("║ 0. Exit                                   ║");
            System.out.println("╚════════════════════════════════════════════╝" + ConsoleColors.RESET);

            System.out.print(ConsoleColors.YELLOW + "Enter your choice: " + ConsoleColors.RESET);
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addNewCredential();
                        break;
                    case "2":
                        viewAllCredentials();
                        break;
                    case "3":
                        updateCredential();
                        break;
                    case "4":
                        deleteCredential();
                        break;
                    case "5":
                        searchCredentials();
                        break;
                    case "6":
                        generatePassword();
                        break;
                    case "7":
                        checkPasswordStrength();
                        break;
                    case "8":
                        exportBackup();
                        break;
                    case "9":
                        importBackup();
                        break;
                    case "0":
                        System.out.println(ConsoleColors.GREEN + "Thank you for using Password Manager. Goodbye!" + ConsoleColors.RESET);
                        activityLogger.log("LOGOUT", "User logged out");
                        return;
                    default:
                        System.out.println(ConsoleColors.RED + "Invalid choice! Please try again." + ConsoleColors.RESET);
                }
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
                activityLogger.log("ERROR", e.getMessage());
            }
        }
    }

    private static void addNewCredential() throws Exception {
        System.out.println(ConsoleColors.BLUE_BOLD + "\n=== Add New Credential ===" + ConsoleColors.RESET);
        
        System.out.print("Enter platform name: ");
        String platformName = scanner.nextLine();
        
        System.out.print("Enter username (press Enter to skip): ");
        String username = scanner.nextLine();
        username = username.isEmpty() ? null : username;
        
        System.out.print("Enter email (press Enter to skip): ");
        String email = scanner.nextLine();
        email = email.isEmpty() ? null : email;
        
        if (username == null && email == null) {
            System.out.println(ConsoleColors.RED + "Error: At least one of username or email must be provided!" + ConsoleColors.RESET);
            return;
        }
        
        System.out.print("Do you want to (1) enter password manually or (2) generate one? ");
        String choice = scanner.nextLine();
        
        String password;
        if (choice.equals("1")) {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
        } else {
            password = generatePassword();
        }
        
        Credential credential = new Credential(platformName, username, email, password);
        if (credentialService.addCredential(credential)) {
            System.out.println(ConsoleColors.GREEN + "Credential added successfully!" + ConsoleColors.RESET);
            activityLogger.log("ADD_CREDENTIAL", "Added credential for platform: " + platformName);
            credentialMap.put(credential);
        }
    }

    private static void viewAllCredentials() throws Exception {
        System.out.println(ConsoleColors.BLUE_BOLD + "\n=== All Credentials ===" + ConsoleColors.RESET);
        
        List<Credential> credentials = credentialService.getAllCredentials();
        if (credentials.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "No credentials found." + ConsoleColors.RESET);
            return;
        }
        
        printCredentialTable(credentials);
        activityLogger.log("VIEW_CREDENTIALS", "Viewed all credentials");

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. View password for a specific credential");
            System.out.println("0. Back to main menu");
            
            System.out.print(ConsoleColors.YELLOW + "Enter your choice: " + ConsoleColors.RESET);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter credential ID to view password: ");
                    try {
                        int credentialId = Integer.parseInt(scanner.nextLine());
                        boolean found = false;
                        
                        for (Credential cred : credentials) {
                            if (cred.getCredentialId() == credentialId) {
                                System.out.println(ConsoleColors.GREEN + "\nCredential Details:" + ConsoleColors.RESET);
                                System.out.println("Platform: " + cred.getPlatformName());
                                System.out.println("Username: " + (cred.getUsername() != null ? cred.getUsername() : "-"));
                                System.out.println("Email: " + (cred.getEmail() != null ? cred.getEmail() : "-"));
                                System.out.println("Password: " + ConsoleColors.YELLOW + cred.getPassword() + ConsoleColors.RESET);
                                
                                activityLogger.log("VIEW_PASSWORD", "Viewed password for platform: " + cred.getPlatformName());
                                found = true;
                                break;
                            }
                        }
                        
                        if (!found) {
                            System.out.println(ConsoleColors.RED + "Invalid credential ID!" + ConsoleColors.RESET);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(ConsoleColors.RED + "Please enter a valid number!" + ConsoleColors.RESET);
                    }
                    break;
                    
                case "0":
                    return;
                    
                default:
                    System.out.println(ConsoleColors.RED + "Invalid choice! Please try again." + ConsoleColors.RESET);
            }
        }
    }

    private static void updateCredential() throws Exception {
        System.out.println(ConsoleColors.BLUE_BOLD + "\n=== Update Credential ===" + ConsoleColors.RESET);
        
        System.out.print("Enter platform name to update: ");
        String platformName = scanner.nextLine();
        
        Credential credential = credentialService.getCredentialByPlatform(platformName);
        if (credential == null) {
            System.out.println(ConsoleColors.RED + "Credential not found!" + ConsoleColors.RESET);
            return;
        }
        
        System.out.println("Current details:");
        printCredentialTable(List.of(credential));
        
        System.out.print("Enter new username (press Enter to skip): ");
        String username = scanner.nextLine();
        if (!username.isEmpty()) {
            credential.setUsername(username);
        }
        
        System.out.print("Enter new email (press Enter to skip): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            credential.setEmail(email);
        }
        
        System.out.print("Enter new password (press Enter to skip): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            credential.setPassword(password);
        }
        
        if (credentialService.updateCredential(credential)) {
            System.out.println(ConsoleColors.GREEN + "Credential updated successfully!" + ConsoleColors.RESET);
            activityLogger.log("UPDATE_CREDENTIAL", "Updated credential for platform: " + platformName);
            credentialMap.put(credential);
        }
    }

    private static void deleteCredential() throws Exception {
        System.out.println(ConsoleColors.BLUE_BOLD + "\n=== Delete Credential ===" + ConsoleColors.RESET);
        
        System.out.print("Enter platform name to delete: ");
        String platformName = scanner.nextLine();
        
        Credential credential = credentialService.getCredentialByPlatform(platformName);
        if (credential == null) {
            System.out.println(ConsoleColors.RED + "Credential not found!" + ConsoleColors.RESET);
            return;
        }
        
        System.out.println("Are you sure you want to delete this credential? (y/n)");
        printCredentialTable(List.of(credential));
        
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            if (credentialService.deleteCredential(credential.getCredentialId())) {
                System.out.println(ConsoleColors.GREEN + "Credential deleted successfully!" + ConsoleColors.RESET);
                activityLogger.log("DELETE_CREDENTIAL", "Deleted credential for platform: " + platformName);
                credentialMap.remove(platformName);
            }
        }
    }

    private static void searchCredentials() throws Exception {
        System.out.println(ConsoleColors.BLUE_BOLD + "\n=== Search Credentials ===" + ConsoleColors.RESET);
        
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();
        
        List<Credential> results = credentialService.searchCredentials(searchTerm);
        if (results.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "No matching credentials found." + ConsoleColors.RESET);
        } else {
            printCredentialTable(results);
        }
        
        activityLogger.log("SEARCH_CREDENTIALS", "Searched for: " + searchTerm);
    }

    private static String generatePassword() {
        System.out.print("Enter desired password length (8-32): ");
        int length = Integer.parseInt(scanner.nextLine());
        
        String password = passwordGenerator.generatePassword(length);
        System.out.println(ConsoleColors.GREEN + "Generated password: " + password + ConsoleColors.RESET);
        
        activityLogger.log("GENERATE_PASSWORD", "Generated new password");
        return password;
    }

    private static void checkPasswordStrength() {
        System.out.print("Enter password to check: ");
        String password = scanner.nextLine();
        
        PasswordStrengthChecker.PasswordStrength strength = strengthChecker.checkStrength(password);
        
        System.out.println(ConsoleColors.BLUE + "\nPassword Strength: " + strength.level + ConsoleColors.RESET);
        
        if (strength.suggestions.length > 0) {
            System.out.println(ConsoleColors.YELLOW + "\nSuggestions for improvement:" + ConsoleColors.RESET);
            for (String suggestion : strength.suggestions) {
                System.out.println("- " + suggestion);
            }
        }
        
        activityLogger.log("CHECK_PASSWORD", "Checked password strength");
    }

    private static void exportBackup() throws Exception {
        String backupFile = backupManager.exportBackup();
        System.out.println(ConsoleColors.GREEN + "Backup exported successfully to: " + backupFile + ConsoleColors.RESET);
        activityLogger.log("EXPORT_BACKUP", "Exported backup to: " + backupFile);
    }

    private static void importBackup() throws Exception {
        System.out.print("Enter backup file path: ");
        String filePath = scanner.nextLine();
        
        int importedCount = backupManager.importBackup(filePath);
        System.out.println(ConsoleColors.GREEN + "Successfully imported " + importedCount + " credentials!" + ConsoleColors.RESET);
        activityLogger.log("IMPORT_BACKUP", "Imported " + importedCount + " credentials from: " + filePath);
    }

    private static void printCredentialTable(List<Credential> credentials) {
        System.out.println("╔═════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║  ID  |  PLATFORM  |  USERNAME/EMAIL  |  PASSWORD  |  CREATED DATE               ║");
        System.out.println("╠═════════════════════════════════════════════════════════════════════════════════╣");
        
        for (Credential cred : credentials) {
            String userOrEmail = cred.getUsername() != null ? cred.getUsername() : 
                               (cred.getEmail() != null ? cred.getEmail() : "-");
            
            System.out.printf("║  %-3d |  %-9s |  %-14s |  %-9s |  %-25s ║%n",
                cred.getCredentialId(),
                cred.getPlatformName(),
                userOrEmail,
                "*".repeat(cred.getPassword().length()),
                cred.getCreatedDate().toString()
            );
        }
        
        System.out.println("╚═════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("Total credentials: " + credentials.size());
    }
}