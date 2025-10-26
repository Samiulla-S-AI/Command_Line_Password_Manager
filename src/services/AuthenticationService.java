package services;

public class AuthenticationService {
    private static final String ADMIN_USERNAME = "Sahan";
    private static final String ADMIN_PASSWORD = "Sahan@123";

    public boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
}