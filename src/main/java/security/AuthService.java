package main.java.security;

import com.vaadin.flow.server.VaadinSession;

import main.java.DAO.UserDAO;
import main.java.model.User;
import main.java.service.UserService;

public class AuthService {
    private static final String SESSION_USER_KEY = "authenticatedUser";

    private final UserService userService;
    private UserDAO userDAO; 

    public AuthService() {
        this.userService = new UserService();
        this.userDAO = new UserDAO(); // Initialize the UserDAO here
    }

    // Authenticate and store in session if successful
    public boolean login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            setCurrentUser(user);
            return true;
        }
        return false;
    }

    // Store logged-in user in session
    public void setCurrentUser(User user) {
        System.out.println("Setting session user: " + user.getter_name());
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute(SESSION_USER_KEY, user);

        // ✅ Store individual attributes for convenience
        session.setAttribute("userId", user.getter_id());
        session.setAttribute("email", user.getter_email());
        session.setAttribute("role", user.getter_userrole()); // Changed getRole() to getter_userrole()
    }

    // Get current user from session
    public User getCurrentUser() {
        System.out.println("Getting current user "+ VaadinSession.getCurrent().getAttribute(SESSION_USER_KEY));
        return (User) VaadinSession.getCurrent().getAttribute(SESSION_USER_KEY);
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        System.out.println("Checking if user is logged in "+ getCurrentUser());
        return getCurrentUser() != null;
    }

    // Get username of current user
    public String getCurrentUsername() {
        User user = getCurrentUser();

        return user != null ? user.getter_name() : null;
    }

    // Get role of current user
    public String getCurrentUserRole() {
        User user = getCurrentUser();
        return user != null ? user.getter_userrole().toString() : null; // Changed getRole() to getter_userrole().toString()
    }

    // Logout and clear session
    public void logout() {
        VaadinSession session = VaadinSession.getCurrent();
        session.setAttribute(SESSION_USER_KEY, null);
        session.setAttribute("userId", null);
        session.setAttribute("email", null);
        session.setAttribute("role", null);
        
        session.getSession().invalidate();
        session.close();
    }
}
