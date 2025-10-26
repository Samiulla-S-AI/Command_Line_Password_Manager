package datastructures;

import models.Credential;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class CredentialHashMap {
    private final Map<String, Credential> credentials;
    
    public CredentialHashMap() {
        this.credentials = new HashMap<>();
    }
    
    public void put(Credential credential) {
        credentials.put(credential.getPlatformName().toLowerCase(), credential);
    }
    
    public Credential get(String platformName) {
        return credentials.get(platformName.toLowerCase());
    }
    
    public boolean contains(String platformName) {
        return credentials.containsKey(platformName.toLowerCase());
    }
    
    public void remove(String platformName) {
        credentials.remove(platformName.toLowerCase());
    }
    
    public List<Credential> search(String searchTerm) {
        List<Credential> results = new ArrayList<>();
        String lowerSearchTerm = searchTerm.toLowerCase();
        
        for (Credential credential : credentials.values()) {
            if (credential.getPlatformName().toLowerCase().contains(lowerSearchTerm) ||
                (credential.getUsername() != null && credential.getUsername().toLowerCase().contains(lowerSearchTerm)) ||
                (credential.getEmail() != null && credential.getEmail().toLowerCase().contains(lowerSearchTerm))) {
                results.add(credential);
            }
        }
        
        return results;
    }
    
    public List<Credential> getAllSorted() {
        List<Credential> sortedList = new ArrayList<>(credentials.values());
        sortedList.sort(null); // Uses the natural ordering defined in Credential class
        return sortedList;
    }
    
    public int size() {
        return credentials.size();
    }
    
    public void clear() {
        credentials.clear();
    }
}