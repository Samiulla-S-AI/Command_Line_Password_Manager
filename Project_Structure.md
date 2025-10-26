
═══════════════════════════════════════════════════════════════════════════

PROJECT STRUCTURE:

password-manager/
│
├── src/
│   ├── Main.java
│   │   └── Entry point, handles main menu loop
│   │
│   ├── models/
│   │   ├── Credential.java
│   │   │   └── Data model for credential (platform, username, email, password)
│   │   └── ActivityLog.java
│   │       └── Data model for activity logs
│   │
│   ├── database/
│   │   ├── DatabaseManager.java
│   │   │   └── MySQL connection, CRUD operations
│   │   └── DatabaseConfig.java
│   │       └── Database configuration constants
│   │
│   ├── services/
│   │   ├── AuthenticationService.java
│   │   │   └── Handle login verification
│   │   ├── CredentialService.java
│   │   │   └── Business logic for credential operations
│   │   ├── PasswordGenerator.java
│   │   │   └── Generate strong random passwords
│   │   └── PasswordStrengthChecker.java
│   │       └── Analyze password strength
│   │
│   ├── filehandling/
│   │   ├── FileBackupManager.java
│   │   │   └── Export/Import credentials to/from files
│   │   ├── FileEncryption.java
│   │   │   └── Simple encryption/decryption logic
│   │   └── ActivityLogger.java
│   │       └── Write logs to text file
│   │
│   ├── datastructures/
│   │   ├── CredentialHashMap.java
│   │   │   └── Custom HashMap implementation for credentials
│   │   └── RecentAccessQueue.java
│   │       └── LinkedList-based queue for recent passwords
│   │
│   └── utils/
│       ├── InputValidator.java
│       │   └── Validate user inputs
│       ├── TableFormatter.java
│       │   └── Format credentials in table view
│       └── ConsoleColors.java
│           └── ANSI color codes for better console UI
│
├── resources/
│   ├── database_schema.sql
│   │   └── SQL script to create database and tables
│   └── config.properties
│       └── Database connection details
│
├── backup/
│   └── (Generated backup files stored here)
│
├── logs/
│   └── activity_log.txt
│
└── README.md
    └── Project documentation

═══════════════════════════════════════════════════════════════════════════

TECHNICAL SPECIFICATIONS:

1. Java Version: Java 8 or higher
2. MySQL Connector: mysql-connector-java-8.0.33.jar (or latest)
3. Database Connection:
   - URL: jdbc:mysql://localhost:3306/password_manager_db
   - Driver: com.mysql.cj.jdbc.Driver
   - Use try-with-resources for connection management
   - Implement connection pooling (optional but recommended)

4. Exception Handling:
   - SQLExceptions for database operations
   - IOException for file operations
   - NumberFormatException for input validation
   - Custom exceptions for authentication failures
   - User-friendly error messages in console

5. Security Considerations:
   - Basic password encryption for storage (Base64 or Caesar cipher for demo)
   - PreparedStatement to prevent SQL injection
   - Input sanitization and validation
   - Secure credential display (mask passwords with option to reveal)

6. Code Quality Requirements:
   - Follow Java naming conventions (camelCase, PascalCase)
   - Comprehensive JavaDoc comments for all methods
   - Inline comments explaining algorithm logic
   - Proper indentation and formatting
   - DRY principle (Don't Repeat Yourself)
   - SOLID principles where applicable

═══════════════════════════════════════════════════════════════════════════

SPECIAL IMPLEMENTATION RULES:

1. NULL HANDLING FOR USERNAME/EMAIL:
