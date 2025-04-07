package main.java.view.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import main.java.security.AuthService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManagerPanel extends VerticalLayout {
    
    private final AuthService authService;
    
    // Sample data classes for demonstration
    private static class Project {
        private String name;
        private String description;
        private String status;
        private int bugs;
        private int developers;
        private int testers;
        
        public Project(String name, String description, String status, int bugs, int developers, int testers) {
            this.name = name;
            this.description = description;
            this.status = status;
            this.bugs = bugs;
            this.developers = developers;
            this.testers = testers;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
        public int getBugs() { return bugs; }
        public int getDevelopers() { return developers; }
        public int getTesters() { return testers; }
    }
    
    private static class TeamMember {
        private String name;
        private String role;
        private String project;
        private int assignedTasks;
        private int completedTasks;
        
        public TeamMember(String name, String role, String project, int assignedTasks, int completedTasks) {
            this.name = name;
            this.role = role;
            this.project = project;
            this.assignedTasks = assignedTasks;
            this.completedTasks = completedTasks;
        }
        
        public String getName() { return name; }
        public String getRole() { return role; }
        public String getProject() { return project; }
        public int getAssignedTasks() { return assignedTasks; }
        public int getCompletedTasks() { return completedTasks; }
    }
    
    public ManagerPanel(AuthService authService) {
        this.authService = authService;
        
        addClassName("manager-panel");
        setSizeFull();
        
        // Welcome message
        H2 welcomeTitle = new H2("Project Manager Dashboard");
        welcomeTitle.getStyle().set("margin-top", "0");
        
        // Create tabs for different manager functions
        Tab projectsTab = new Tab(VaadinIcon.FOLDER.create(), new H3("Projects"));
        Tab teamTab = new Tab(VaadinIcon.USERS.create(), new H3("Team"));
        Tab reportsTab = new Tab(VaadinIcon.CHART.create(), new H3("Reports"));
        
        Tabs tabs = new Tabs(projectsTab, teamTab, reportsTab);
        tabs.setWidthFull();
        
        // Content for each tab
        VerticalLayout projectsContent = createProjectsContent();
        VerticalLayout teamContent = createTeamContent();
        VerticalLayout reportsContent = createReportsContent();
        
        // Initially show projects content
        add(welcomeTitle, tabs, projectsContent);
        
        // Tab change listener
        tabs.addSelectedChangeListener(event -> {
            // Remove all content
            remove(getComponentAt(2));
            
            // Add appropriate content based on selected tab
            if (event.getSelectedTab().equals(projectsTab)) {
                add(projectsContent);
            } else if (event.getSelectedTab().equals(teamTab)) {
                add(teamContent);
            } else if (event.getSelectedTab().equals(reportsTab)) {
                add(reportsContent);
            }
        });
    }
    
    private VerticalLayout createProjectsContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        // Summary cards
        HorizontalLayout summaryLayout = new HorizontalLayout();
        summaryLayout.setWidthFull();
        
        VerticalLayout totalProjects = createSummaryCard("Total Projects", "3", VaadinIcon.FOLDER.create(), "#1676f3");
        VerticalLayout activeProjects = createSummaryCard("Active", "2", VaadinIcon.PLAY.create(), "green");
        VerticalLayout completedProjects = createSummaryCard("Completed", "1", VaadinIcon.CHECK.create(), "gray");
        
        summaryLayout.add(totalProjects, activeProjects, completedProjects);
        summaryLayout.setFlexGrow(1, totalProjects, activeProjects, completedProjects);
        
        // Projects grid
        Grid<Project> projectsGrid = new Grid<>(Project.class);
        projectsGrid.setColumns("name", "description", "status", "bugs", "developers", "testers");
        projectsGrid.getColumnByKey("bugs").setHeader("Bug Count");
        
        // Sample data
        List<Project> projects = new ArrayList<>();
        projects.add(new Project("Website Redesign", "Company website redesign project", "Active", 12, 3, 2));
        projects.add(new Project("Mobile App", "Customer mobile application", "Active", 8, 2, 1));
        projects.add(new Project("API Integration", "Third-party API integration", "Completed", 0, 1, 1));
        
        projectsGrid.setItems(projects);
        projectsGrid.setSizeFull();
        
        // Add action column
        projectsGrid.addComponentColumn(project -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button viewButton = new Button(VaadinIcon.EYE.create());
            viewButton.addThemeVariants();
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addThemeVariants();
            actions.add(viewButton, editButton);
            return actions;
        }).setHeader("Actions").setFlexGrow(0);
        
        // Add project button
        Button addProjectButton = new Button("Add Project", VaadinIcon.PLUS.create());
        addProjectButton.getStyle().set("margin-bottom", "20px");
        
        content.add(summaryLayout, addProjectButton, projectsGrid);
        return content;
    }
    
    private VerticalLayout createSummaryCard(String title, String count, com.vaadin.flow.component.icon.Icon icon, String color) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("summary-card");
        card.setAlignItems(Alignment.CENTER);
        card.getStyle().set("background-color", "white");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        card.getStyle().set("padding", "20px");
        card.getStyle().set("margin", "10px");
        
        icon.setSize("40px");
        icon.setColor(color);
        
        H3 countLabel = new H3(count);
        countLabel.getStyle().set("margin", "10px 0");
        countLabel.getStyle().set("font-size", "2em");
        
        Span titleLabel = new Span(title);
        titleLabel.getStyle().set("color", "var(--lumo-secondary-text-color)");
        
        card.add(icon, countLabel, titleLabel);
        
        return card;
    }
    
    private VerticalLayout createTeamContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        H3 teamTitle = new H3("Team Members");
        
        // Team members grid
        Grid<TeamMember> teamGrid = new Grid<>(TeamMember.class);
        teamGrid.setColumns("name", "role", "project", "assignedTasks", "completedTasks");
        teamGrid.getColumnByKey("assignedTasks").setHeader("Assigned Tasks");
        teamGrid.getColumnByKey("completedTasks").setHeader("Completed Tasks");
        
        // Sample data
        List<TeamMember> teamMembers = new ArrayList<>();
        teamMembers.add(new TeamMember("John Doe", "Developer", "Website Redesign", 5, 2));
        teamMembers.add(new TeamMember("Jane Smith", "Developer", "Mobile App", 3, 1));
        teamMembers.add(new TeamMember("Bob Johnson", "Developer", "API Integration", 0, 4));
        teamMembers.add(new TeamMember("Alice Brown", "Tester", "Website Redesign", 8, 5));
        teamMembers.add(new TeamMember("Charlie Davis", "Tester", "Mobile App", 4, 3));
        
        teamGrid.setItems(teamMembers);
        teamGrid.setSizeFull();
        
        // Add action column
        teamGrid.addComponentColumn(member -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button assignButton = new Button(VaadinIcon.TASKS.create());
            assignButton.addThemeVariants();
            Button messageButton = new Button(VaadinIcon.ENVELOPE.create());
            messageButton.addThemeVariants();
            actions.add(assignButton, messageButton);
            return actions;
        }).setHeader("Actions").setFlexGrow(0);
        
        // Add team member button
        Button addMemberButton = new Button("Add Team Member", VaadinIcon.PLUS.create());
        addMemberButton.getStyle().set("margin-bottom", "20px");
        
        content.add(teamTitle, addMemberButton, teamGrid);
        return content;
    }
    
    private VerticalLayout createReportsContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        H3 reportsTitle = new H3("Reports & Analytics");
        
        // Report types
        HorizontalLayout reportTypes = new HorizontalLayout();
        reportTypes.setWidthFull();
        
        VerticalLayout bugReport = createReportCard("Bug Reports", "View bug statistics and trends");
        VerticalLayout teamReport = createReportCard("Team Performance", "Analyze team productivity and efficiency");
        VerticalLayout projectReport = createReportCard("Project Status", "Track project progress and milestones");
        
        reportTypes.add(bugReport, teamReport, projectReport);
        reportTypes.setFlexGrow(1, bugReport, teamReport, projectReport);
        
        // Chart placeholder
        VerticalLayout chartPlaceholder = new VerticalLayout();
        chartPlaceholder.setHeight("400px");
        chartPlaceholder.setWidthFull();
        chartPlaceholder.getStyle().set("background-color", "#f5f7fa");
        chartPlaceholder.getStyle().set("border-radius", "8px");
        chartPlaceholder.getStyle().set("display", "flex");
        chartPlaceholder.getStyle().set("align-items", "center");
        chartPlaceholder.getStyle().set("justify-content", "center");
        
        Span chartText = new Span("Project Progress Chart Would Be Displayed Here");
        chartPlaceholder.add(chartText);
        
        // Export buttons
        HorizontalLayout exportButtons = new HorizontalLayout();
        Button pdfButton = new Button("Export as PDF", VaadinIcon.DOWNLOAD.create());
        Button csvButton = new Button("Export as CSV", VaadinIcon.DOWNLOAD.create());
        exportButtons.add(pdfButton, csvButton);
        
        content.add(reportsTitle, reportTypes, chartPlaceholder, exportButtons);
        return content;
    }
    
    private VerticalLayout createReportCard(String title, String description) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("report-card");
        card.setAlignItems(Alignment.CENTER);
        card.getStyle().set("background-color", "white");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        card.getStyle().set("padding", "20px");
        card.getStyle().set("margin", "10px");
        card.getStyle().set("cursor", "pointer");
        
        H3 titleLabel = new H3(title);
        titleLabel.getStyle().set("margin-top", "0");
        
        Span descriptionLabel = new Span(description);
        descriptionLabel.getStyle().set("color", "var(--lumo-secondary-text-color)");
        descriptionLabel.getStyle().set("text-align", "center");
        
        Button viewButton = new Button("View Report");
        viewButton.getStyle().set("margin-top", "15px");
        
        card.add(titleLabel, descriptionLabel, viewButton);
        
        return card;
    }
}

