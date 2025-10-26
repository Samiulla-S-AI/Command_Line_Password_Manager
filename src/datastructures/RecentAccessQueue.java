package datastructures;

import models.Credential;
import java.util.LinkedList;

public class RecentAccessQueue {
    private final LinkedList<Credential> recentCredentials;
    private final int maxSize;
    
    public RecentAccessQueue(int maxSize) {
        this.recentCredentials = new LinkedList<>();
        this.maxSize = maxSize;
    }
    
    public void addCredential(Credential credential) {
        // Remove if already exists
        recentCredentials.removeIf(c -> c.getPlatformName().equals(credential.getPlatformName()));
        
        // Add to front
        recentCredentials.addFirst(credential);
        
        // Remove oldest if exceeds max size
        while (recentCredentials.size() > maxSize) {
            recentCredentials.removeLast();
        }
    }
    
    public LinkedList<Credential> getRecentCredentials() {
        return new LinkedList<>(recentCredentials);
    }
    
    public void clear() {
        recentCredentials.clear();
    }
    
    public int size() {
        return recentCredentials.size();
    }
}