package main.java.view.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import main.java.security.AuthService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends VerticalLayout {
    
    private final AuthService authService;
    
    // Sample data classes for demonstration
    private static class User {
        private String username;
        private String email;
        private String role;
        private LocalDate created;
        
        public User(String username, String email, String role, LocalDate created) {
            this.username = username;
            this.email = email;
            this.role = role;
            this.created = created;
        }
        
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public LocalDate getCreated() { return created; }
    }
    
    private static class Project {
        private String name;
        private String description;
        private String manager;
        private int bugs;
        
        public Project(String name, String description, String manager, int bugs) {
            this.name = name;
            this.description = description;
            this.manager = manager;
            this.bugs = bugs;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getManager() { return manager; }
        public int getBugs() { return bugs; }
    }
    
    public AdminPanel(AuthService authService) {
        this.authService = authService;
        
        addClassName("admin-panel");
        setSizeFull();
        
        // Welcome message
        H2 welcomeTitle = new H2("Admin Dashboard");
        welcomeTitle.getStyle().set("margin-top", "0");
        
        // Create tabs for different admin functions
        Tab usersTab = new Tab(VaadinIcon.USERS.create(), new H3("Users"));
        Tab projectsTab = new Tab(VaadinIcon.FOLDER.create(), new H3("Projects"));
        Tab settingsTab = new Tab(VaadinIcon.COG.create(), new H3("Settings"));
        
        Tabs tabs = new Tabs(usersTab, projectsTab, settingsTab);
        tabs.setWidthFull();
        
        // Content for each tab
        VerticalLayout usersContent = createUsersContent();
        VerticalLayout projectsContent = createProjectsContent();
        VerticalLayout settingsContent = createSettingsContent();
        
        // Initially show users content
        add(welcomeTitle, tabs, usersContent);
        
        // Tab change listener
        tabs.addSelectedChangeListener(event -> {
            // Remove all content
            remove(getComponentAt(2));
            
            // Add appropriate content based on selected tab
            if (event.getSelectedTab().equals(usersTab)) {
                add(usersContent);
            } else if (event.getSelectedTab().equals(projectsTab)) {
                add(projectsContent);
            } else if (event.getSelectedTab().equals(settingsTab)) {
                add(settingsContent);
            }
        });
    }
    
    private VerticalLayout createUsersContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        // Action buttons
        HorizontalLayout actionButtons = new HorizontalLayout();
        Button addUserButton = new Button("Add User", VaadinIcon.PLUS.create());
        Button exportButton = new Button("Export", VaadinIcon.DOWNLOAD.create());
        actionButtons.add(addUserButton, exportButton);
        
        // Users grid
        Grid<User> usersGrid = new Grid<>(User.class);
        usersGrid.setColumns("username", "email", "role", "created");
        usersGrid.getColumnByKey("created").setHeader("Created Date");
        
        // Sample data
        List<User> users = new ArrayList<>();
        users.add(new User("admin", "admin@example.com", "Administrator", LocalDate.now().minusDays(30)));
        users.add(new User("dev1", "dev1@example.com", "Developer", LocalDate.now().minusDays(25)));
        users.add(new User("tester1", "tester1@example.com", "Tester", LocalDate.now().minusDays(20)));
        users.add(new User("manager1", "manager1@example.com", "Manager", LocalDate.now().minusDays(15)));
        
        usersGrid.setItems(users);
        usersGrid.setSizeFull();
        
        // Add action column
        usersGrid.addComponentColumn(user -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addThemeVariants();
            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants();
            actions.add(editButton, deleteButton);
            return actions;
        }).setHeader("Actions").setFlexGrow(0);
        
        content.add(actionButtons, usersGrid);
        return content;
    }
    
    private VerticalLayout createProjectsContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        // Action buttons
        HorizontalLayout actionButtons = new HorizontalLayout();
        Button addProjectButton = new Button("Add Project", VaadinIcon.PLUS.create());
        Button exportButton = new Button("Export", VaadinIcon.DOWNLOAD.create());
        actionButtons.add(addProjectButton, exportButton);
        
        // Projects grid
        Grid<Project> projectsGrid = new Grid<>(Project.class);
        projectsGrid.setColumns("name", "description", "manager", "bugs");
        projectsGrid.getColumnByKey("bugs").setHeader("Bug Count");
        
        // Sample data
        List<Project> projects = new ArrayList<>();
        projects.add(new Project("Website Redesign", "Company website redesign project", "manager1", 12));
        projects.add(new Project("Mobile App", "Customer mobile application", "manager1", 8));
        projects.add(new Project("API Integration", "Third-party API integration", "manager2", 5));
        
        projectsGrid.setItems(projects);
        projectsGrid.setSizeFull();
        
        // Add action column
        projectsGrid.addComponentColumn(project -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addThemeVariants();
            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants();
            actions.add(editButton, deleteButton);
            return actions;
        }).setHeader("Actions").setFlexGrow(0);
        
        content.add(actionButtons, projectsGrid);
        return content;
    }
    
    private VerticalLayout createSettingsContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        // Settings sections
        H3 generalSettings = new H3("General Settings");
        H3 securitySettings = new H3("Security Settings");
        H3 notificationSettings = new H3("Notification Settings");
        
        // Add some sample settings controls
        Button saveButton = new Button("Save Settings");
        saveButton.getStyle().set("margin-top", "20px");
        
        content.add(generalSettings, securitySettings, notificationSettings, saveButton);
        return content;
    }
}

