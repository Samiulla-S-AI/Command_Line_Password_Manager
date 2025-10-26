package services;

public class PasswordStrengthChecker {
    public static class PasswordStrength {
        public final String level;
        public final String[] suggestions;
        
        public PasswordStrength(String level, String[] suggestions) {
            this.level = level;
            this.suggestions = suggestions;
        }
    }
    
    public PasswordStrength checkStrength(String password) {
        int score = 0;
        StringBuilder suggestionBuilder = new StringBuilder();
        
        // Check length
        if (password.length() < 8) {
            suggestionBuilder.append("Make the password at least 8 characters long. ");
        } else if (password.length() >= 12) {
            score += 2;
        } else {
            score += 1;
        }
        
        // Check for numbers
        if (password.matches(".*\\d.*")) {
            score += 1;
        } else {
            suggestionBuilder.append("Add numbers. ");
        }
        
        // Check for lowercase letters
        if (password.matches(".*[a-z].*")) {
            score += 1;
        } else {
            suggestionBuilder.append("Add lowercase letters. ");
        }
        
        // Check for uppercase letters
        if (password.matches(".*[A-Z].*")) {
            score += 1;
        } else {
            suggestionBuilder.append("Add uppercase letters. ");
        }
        
        // Check for special characters
        if (password.matches(".*[!@#$%^&*].*")) {
            score += 1;
        } else {
            suggestionBuilder.append("Add special characters (!@#$%^&*). ");
        }
        
        String level;
        if (score < 2) {
            level = "Weak";
        } else if (score < 3) {
            level = "Medium";
        } else if (score < 4) {
            level = "Strong";
        } else {
            level = "Very Strong";
        }
        
        String[] suggestions = suggestionBuilder.length() > 0 
            ? suggestionBuilder.toString().trim().split("\\. ")
            : new String[0];
            
        return new PasswordStrength(level, suggestions);
    }
}