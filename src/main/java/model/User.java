package main.java.model;

//import java.security.Timestamp;
import java.sql.Timestamp;

public class User {
    public enum UserRole {
        Administrator,
        ProjectManager,
        Developer,
        Tester;

        public static UserRole fromString(String role) {
            // return UserRole.valueOf(role.toUpperCase());
            return UserRole.valueOf(role);
        }

        public String toString() {
            return name();
        }
    }
    

    protected int id;
    protected String name;
    protected String email;
    protected String password;
    protected UserRole userrole;
    protected Timestamp created_date;

    public User() {
        // Default constructor
    }

    public User(int id, String name, String email, String password, UserRole userrole, Timestamp created_date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userrole = userrole;
        this.created_date = created_date;
    }

    public User(int id, String name, String email, String password, UserRole userrole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userrole = userrole;
    }

    public int getter_id() {
        return id;
    }

    public String getter_name() {
        return name;
    }

    public String getter_email() {
        return email;
    }

    public String getter_password() {
        return password;
    }

    public UserRole getter_userrole() {
        return userrole;
    }

    public String getRole() {
        return userrole.toString();
    }

    public Timestamp getter_createdDate() {
        return created_date;
    }

    public void setter_id(int id) {
        this.id = id;
    }

    public void setter_name(String name) {
        this.name = name;
    }

    public void setter_email(String email) {
        this.email = email;
    }

    public void setter_password(String password) {
        this.password = password;
    }

    public void setter_userrole(UserRole userrole) {
        this.userrole = userrole;
    }

    public void setter_createdDate(Timestamp created_date) {
        this.created_date = created_date;
    }

}