// package main.java.security;

// import com.vaadin.flow.server.VaadinSession;
// import java.util.Optional;

// /**
//  * Service for handling authentication-related operations.
//  * This is a simplified example. In a real application, you would:
//  * - Use proper password hashing (e.g., BCrypt)
//  * - Implement proper session management
//  * - Use Spring Security or similar framework
//  * - Store user data in a database
//  */
// public class AuthService {
    
//     private static final String SESSION_USERNAME_KEY = "current_user";
//     private static final String SESSION_ROLE_KEY = "current_role";
    
//     /**
//      * Authenticates a user with the provided credentials.
//      * 
//      * @param username The username
//      * @param password The password
//      * @return Optional containing the user if authentication is successful, empty otherwise
//      */
//     public Optional<User> authenticate(String username, String password) {
//         // In a real application, you would:
//         // 1. Look up the user in your database
//         // 2. Verify the password using a secure hashing algorithm
//         // 3. Return the user if authentication is successful
        
//         // This is just a placeholder implementation
//         if ("admin".equals(username) && "password".equals(password)) {
//             User user = new User();
//             user.setUsername(username);
//             user.setUserType("Administrator");
            
//             // Store user info in session
//             setCurrentUsername(username);
//             setCurrentUserRole("Administrator");
            
//             return Optional.of(user);
//         } else if ("dev".equals(username) && "password".equals(password)) {
//             User user = new User();
//             user.setUsername(username);
//             user.setUserType("Developer");
            
//             setCurrentUsername(username);
//             setCurrentUserRole("Developer");
            
//             return Optional.of(user);
//         } else if ("tester".equals(username) && "password".equals(password)) {
//             User user = new User();
//             user.setUsername(username);
//             user.setUserType("Tester");
            
//             setCurrentUsername(username);
//             setCurrentUserRole("Tester");
            
//             return Optional.of(user);
//         } else if ("manager".equals(username) && "password".equals(password)) {
//             User user = new User();
//             user.setUsername(username);
//             user.setUserType("Manager");
            
//             setCurrentUsername(username);
//             setCurrentUserRole("Manager");
            
//             return Optional.of(user);
//         }
        
//         return Optional.empty();
//     }
    
//     /**
//      * Registers a new user.
//      * 
//      * @param username The username
//      * @param password The password
//      * @param email The email
//      * @param userType The user type
//      * @return The newly created user
//      */
//     public User register(String username, String password, String email, String userType) {
//         // In a real application, you would:
//         // 1. Validate the input
//         // 2. Check if the username or email is already taken
//         // 3. Hash the password
//         // 4. Store the user in your database
//         // 5. Return the created user
        
//         // This is just a placeholder implementation
//         User user = new User();
//         user.setUsername(username);
//         user.setEmail(email);
//         user.setUserType(userType);
        
//         return user;
//     }
    
//     /**
//      * Sets the current user's username in the session.
//      * 
//      * @param username The username to set
//      */
//     public void setCurrentUsername(String username) {
//         VaadinSession.getCurrent().setAttribute(SESSION_USERNAME_KEY, username);
//     }
    
//     /**
//      * Gets the current user's username from the session.
//      * 
//      * @return The current username, or null if not set
//      */
//     public String getCurrentUsername() {
//         return (String) VaadinSession.getCurrent().getAttribute(SESSION_USERNAME_KEY);
//     }
    
//     /**
//      * Sets the current user's role in the session.
//      * 
//      * @param role The role to set
//      */
//     public void setCurrentUserRole(String role) {
//         VaadinSession.getCurrent().setAttribute(SESSION_ROLE_KEY, role);
//     }
    
//     /**
//      * Gets the current user's role from the session.
//      * 
//      * @return The current role, or null if not set
//      */
//     public String getCurrentUserRole() {
//         return (String) VaadinSession.getCurrent().getAttribute(SESSION_ROLE_KEY);
//     }
    
//     /**
//      * Checks if a user is currently logged in.
//      * 
//      * @return true if a user is logged in, false otherwise
//      */
//     public boolean isLoggedIn() {
//         return getCurrentUsername() != null && getCurrentUserRole() != null;
//     }
    
//     /**
//      * Logs out the current user by clearing the session.
//      */
//     public void logout() {
//         VaadinSession.getCurrent().getSession().invalidate();
//         VaadinSession.getCurrent().close();
//     }
    
//     /**
//      * Simple user class for demonstration purposes.
//      */
//     public static class User {
//         private String username;
//         private String email;
//         private String userType;
        
//         public String getUsername() {
//             return username;
//         }
        
//         public void setUsername(String username) {
//             this.username = username;
//         }
        
//         public String getEmail() {
//             return email;
//         }
        
//         public void setEmail(String email) {
//             this.email = email;
//         }
        
//         public String getUserType() {
//             return userType;
//         }
        
//         public void setUserType(String userType) {
//             this.userType = userType;
//         }
//     }
// }


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
        session.setAttribute("role", user.getRole());
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

        return user != null ? user.getRole() : null;
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
