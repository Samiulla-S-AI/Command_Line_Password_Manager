package services;

import database.DatabaseManager;
import models.Credential;
import models.ActivityLog;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CredentialService {
    private final DatabaseManager dbManager;

    public CredentialService() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public boolean addCredential(Credential credential) throws SQLException {
        String sql = "INSERT INTO credentials (platform_name, username, email, password, created_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, credential.getPlatformName());
            pstmt.setString(2, credential.getUsername());
            pstmt.setString(3, credential.getEmail());
            pstmt.setString(4, credential.getPassword());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        credential.setCredentialId(rs.getInt(1));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Credential> getAllCredentials() throws SQLException {
        List<Credential> credentials = new ArrayList<>();
        String sql = "SELECT * FROM credentials ORDER BY platform_name";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Credential credential = new Credential(
                    rs.getString("platform_name"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                credential.setCredentialId(rs.getInt("credential_id"));
                credential.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                
                Timestamp modifiedDate = rs.getTimestamp("modified_date");
                if (modifiedDate != null) {
                    credential.setModifiedDate(modifiedDate.toLocalDateTime());
                }
                
                credentials.add(credential);
            }
        }
        return credentials;
    }

    public Credential getCredentialByPlatform(String platformName) throws SQLException {
        String sql = "SELECT * FROM credentials WHERE platform_name = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, platformName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Credential credential = new Credential(
                        rs.getString("platform_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                    credential.setCredentialId(rs.getInt("credential_id"));
                    credential.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                    
                    Timestamp modifiedDate = rs.getTimestamp("modified_date");
                    if (modifiedDate != null) {
                        credential.setModifiedDate(modifiedDate.toLocalDateTime());
                    }
                    
                    return credential;
                }
            }
        }
        return null;
    }

    public List<Credential> searchCredentials(String searchTerm) throws SQLException {
        List<Credential> credentials = new ArrayList<>();
        String sql = "SELECT * FROM credentials WHERE platform_name LIKE ? OR username LIKE ? OR email LIKE ?";
        String searchPattern = "%" + searchTerm + "%";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Credential credential = new Credential(
                        rs.getString("platform_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                    credential.setCredentialId(rs.getInt("credential_id"));
                    credential.setCreatedDate(rs.getTimestamp("created_date").toLocalDateTime());
                    
                    Timestamp modifiedDate = rs.getTimestamp("modified_date");
                    if (modifiedDate != null) {
                        credential.setModifiedDate(modifiedDate.toLocalDateTime());
                    }
                    
                    credentials.add(credential);
                }
            }
        }
        return credentials;
    }

    public boolean updateCredential(Credential credential) throws SQLException {
        String sql = "UPDATE credentials SET username = ?, email = ?, password = ?, modified_date = ? WHERE credential_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, credential.getUsername());
            pstmt.setString(2, credential.getEmail());
            pstmt.setString(3, credential.getPassword());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(5, credential.getCredentialId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCredential(int credentialId) throws SQLException {
        String sql = "DELETE FROM credentials WHERE credential_id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, credentialId);
            
            return pstmt.executeUpdate() > 0;
        }
    }
}