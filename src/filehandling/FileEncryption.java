package filehandling;

import java.util.Base64;

public class FileEncryption {
    // Simple encryption using Base64 (for demonstration purposes)
    public static String encrypt(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
    
    // Simple decryption using Base64 (for demonstration purposes)
    public static String decrypt(String encryptedData) {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        return new String(decodedBytes);
    }
}