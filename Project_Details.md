PROJECT NAME: Command-Line Personal Password Manager Console Application

═══════════════════════════════════════════════════════════════════════════

MANDATORY REQUIREMENTS:
✓ Pure Java Console-Based Application (NO GUI)
✓ Data Structures and Algorithms Implementation (must be prominently demonstrated)
✓ MySQL Database Connectivity using JDBC
✓ File Handling (backup, export, import functionality)
✓ Professional code structure suitable for academic project submission

═══════════════════════════════════════════════════════════════════════════

PROJECT OVERVIEW:
Build a secure console-based password manager that helps users store and manage their login credentials (platform names, usernames/emails, and passwords) for various websites and applications. Users can generate strong passwords, store them securely in a MySQL database, and retrieve them when needed after proper authentication.

═══════════════════════════════════════════════════════════════════════════

AUTHENTICATION SYSTEM:
- Fixed Admin Credentials for Authorization:
  * Username: Sahan
  * Password: Sahan@123
  
- Login Flow:
  1. Display welcome screen
  2. Prompt for username and password
  3. Verify credentials against fixed values
  4. Grant access only if credentials match exactly
  5. Show error message and retry option if authentication fails
  6. Maximum 3 login attempts, then exit application

═══════════════════════════════════════════════════════════════════════════

CORE FEATURES & FUNCTIONALITY:

1. ADD NEW CREDENTIAL
   - Prompt for:
     * Platform Name (e.g., "Gmail", "Facebook", "LinkedIn") - REQUIRED
     * Username OR Email (user chooses one, other can be NULL) - At least one REQUIRED
     * Password (user can enter custom OR generate strong password) - REQUIRED
   - Validation:
     * Platform name cannot be empty
     * At least one of username/email must be provided
     * Password cannot be empty
   - Store in MySQL database with timestamp
   - Confirm successful addition

2. VIEW ALL CREDENTIALS
   - Retrieve all stored credentials from MySQL database
   - Display in formatted table structure:
     ```
     ╔═══════════════════════════════════════════════════════════════════════╗
     ║  PLATFORM  |  USERNAME/EMAIL  |  PASSWORD  |  CREATED DATE           ║
     ╠═══════════════════════════════════════════════════════════════════════╣
     ║  Gmail     |  user@gmail.com  |  Pass@123  |  2025-10-25 10:30:00   ║
     ║  Facebook  |  john_doe        |  Fb#2024!  |  2025-10-24 15:20:00   ║
     ╚═══════════════════════════════════════════════════════════════════════╝
     ```
   - Important Display Rules:
     * If username is NULL, show only email
     * If email is NULL, show only username
     * Never display "null" text in the table
     * Show "-" or empty space for NULL fields
   - Include total credential count
   - Option to sort by platform name or date

3. UPDATE EXISTING CREDENTIAL
   - Search by platform name
   - Display current details
   - Allow updating:
     * Username/Email (can change from one to another)
     * Password (enter new or generate)
   - Update timestamp in database
   - Confirm successful update

4. DELETE CREDENTIAL
   - Search by platform name
   - Display credential details for confirmation
   - Ask for confirmation (Y/N)
   - Delete from MySQL database
   - Confirm successful deletion

5. SEARCH CREDENTIAL
   - Search by platform name (partial match supported)
   - Display matching credentials in table format
   - Show "No results found" if no match

6. GENERATE STRONG PASSWORD
   - Ask for desired password length (minimum 8, maximum 32)
   - Generate password with:
     * Uppercase letters (A-Z)
     * Lowercase letters (a-z)
     * Numbers (0-9)
     * Special characters (!@#$%^&*)
   - Display generated password
   - Option to use this password for a credential immediately
   - Algorithm must be clearly implemented (show randomization logic)

7. PASSWORD STRENGTH CHECKER
   - Analyze any password (stored or new)
   - Display strength metrics:
     * Length check (weak: <8, medium: 8-12, strong: >12)
     * Character variety (uppercase, lowercase, numbers, special chars)
     * Overall strength score (Weak/Medium/Strong/Very Strong)
   - Provide suggestions for improvement

8. FILE HANDLING FEATURES
   - EXPORT BACKUP:
     * Export all credentials to encrypted text file
     * File format: passwords_backup_[DATE].txt
     * Include encryption (use simple Base64 or Caesar cipher for demonstration)
     * Save to designated backup folder
     * Confirm successful export with file path
   
   - IMPORT BACKUP:
     * Read from encrypted backup file
     * Decrypt and parse credentials
     * Insert into MySQL database (avoid duplicates)
     * Show import summary (X credentials imported)
   
   - AUTO-SAVE LOG:
     * Maintain activity log file (activity_log.txt)
     * Log all operations with timestamp
     * Format: [TIMESTAMP] [ACTION] [DETAILS]

═══════════════════════════════════════════════════════════════════════════

DATA STRUCTURES TO IMPLEMENT & DEMONSTRATE:

1. HashMap / HashTable:
   - Use for fast platform name lookup
   - Store credentials with platform name as key
   - Demonstrate O(1) search complexity

2. ArrayList:
   - Store list of credentials retrieved from database
   - Use for sorting and filtering operations

3. LinkedList:
   - Implement for recently accessed passwords queue
   - Maintain last 5 accessed credentials

4. TreeMap (Optional Advanced):
   - Use for sorted platform name display
   - Demonstrate automatic alphabetical ordering

5. Custom Data Structures:
   - Create Credential class with proper encapsulation
   - Implement comparable interface for sorting

6. Algorithms to Demonstrate:
   - Binary Search (for sorted credential search)
   - Sorting Algorithm (QuickSort/MergeSort for platform names)
   - String Matching Algorithm (for search functionality)
   - Random Number Generation Algorithm (for password generation)
   - Hash Function (for password encryption demonstration)

═══════════════════════════════════════════════════════════════════════════

MYSQL DATABASE SCHEMA:

Database Name: password_manager_db

Table: credentials
╔════════════════════════════════════════════════════════════════════╗
║  COLUMN NAME      │  DATA TYPE        │  CONSTRAINTS              ║
╠════════════════════════════════════════════════════════════════════╣
║  credential_id    │  INT              │  PRIMARY KEY, AUTO_INCREMENT
║  platform_name    │  VARCHAR(100)     │  NOT NULL                 ║
║  username         │  VARCHAR(100)     │  NULL (optional)          ║
║  email            │  VARCHAR(100)     │  NULL (optional)          ║
║  password         │  VARCHAR(255)     │  NOT NULL                 ║
║  created_date     │  TIMESTAMP        │  DEFAULT CURRENT_TIMESTAMP║
║  modified_date    │  TIMESTAMP        │  NULL                     ║
╚════════════════════════════════════════════════════════════════════╝

Table: activity_logs
╔════════════════════════════════════════════════════════════════════╗
║  log_id           │  INT              │  PRIMARY KEY, AUTO_INCREMENT
║  action_type      │  VARCHAR(50)      │  NOT NULL                 ║
║  action_details   │  VARCHAR(255)     │  NULL                     ║
║  timestamp        │  TIMESTAMP        │  DEFAULT CURRENT_TIMESTAMP║
╚════════════════════════════════════════════════════════════════════╝

Constraints:
- At least one of (username OR email) must be provided (check in Java code)
- Platform name must be unique per user
- Cascade delete for related records

═══════════════════════════════════════════════════════════════════════════

CONSOLE MENU STRUCTURE:

Main Menu (After Successful Login):
