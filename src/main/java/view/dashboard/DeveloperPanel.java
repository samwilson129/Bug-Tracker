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

public class DeveloperPanel extends VerticalLayout {
    
    private final AuthService authService;
    
    // Sample data class for demonstration
    public static class Bug {
        private String id;
        private String title;
        private String status;
        private String priority;
        private String project;
        private LocalDate reportedDate;
        
        public Bug(String id, String title, String status, String priority, String project, LocalDate reportedDate) {
            this.id = id;
            this.title = title;
            this.status = status;
            this.priority = priority;
            this.project = project;
            this.reportedDate = reportedDate;
        }
        
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getStatus() { return status; }
        public String getPriority() { return priority; }
        public String getProject() { return project; }
        public LocalDate getReportedDate() { return reportedDate; }
    }
    
    public DeveloperPanel(AuthService authService) {
        this.authService = authService;
        
        addClassName("developer-panel");
        setSizeFull();
        
        // Welcome message
        H2 welcomeTitle = new H2("Developer Dashboard");
        welcomeTitle.getStyle().set("margin-top", "0");
        
        // Create tabs for different developer functions
        Tab assignedBugsTab = new Tab(VaadinIcon.BUG.create(), new H3("Assigned Bugs"));
        Tab projectsTab = new Tab(VaadinIcon.FOLDER.create(), new H3("My Projects"));
        Tab activityTab = new Tab(VaadinIcon.CHART.create(), new H3("Activity"));
        
        Tabs tabs = new Tabs(assignedBugsTab, projectsTab, activityTab);
        tabs.setWidthFull();
        
        // Content for each tab
        VerticalLayout assignedBugsContent = createAssignedBugsContent();
        VerticalLayout projectsContent = createProjectsContent();
        VerticalLayout activityContent = createActivityContent();
        
        // Initially show assigned bugs content
        add(welcomeTitle, tabs, assignedBugsContent);
        
        // Tab change listener
        tabs.addSelectedChangeListener(event -> {
            // Remove all content
            remove(getComponentAt(2));
            
            // Add appropriate content based on selected tab
            if (event.getSelectedTab().equals(assignedBugsTab)) {
                add(assignedBugsContent);
            } else if (event.getSelectedTab().equals(projectsTab)) {
                add(projectsContent);
            } else if (event.getSelectedTab().equals(activityTab)) {
                add(activityContent);
            }
        });
    }
    
    private VerticalLayout createAssignedBugsContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        // Summary cards
        HorizontalLayout summaryLayout = new HorizontalLayout();
        summaryLayout.setWidthFull();
        
        VerticalLayout openBugs = createSummaryCard("Open Bugs", "5", VaadinIcon.BUG.create(), "red");
        VerticalLayout inProgressBugs = createSummaryCard("In Progress", "3", VaadinIcon.HOURGLASS.create(), "orange");
        VerticalLayout resolvedBugs = createSummaryCard("Resolved", "12", VaadinIcon.CHECK.create(), "green");
        
        summaryLayout.add(openBugs, inProgressBugs, resolvedBugs);
        summaryLayout.setFlexGrow(1, openBugs, inProgressBugs, resolvedBugs);
        
        // Bugs grid
        Grid<Bug> bugsGrid = new Grid<>(Bug.class);
        bugsGrid.setColumns("id", "title", "status", "priority", "project", "reportedDate");
        bugsGrid.getColumnByKey("reportedDate").setHeader("Reported Date");
        
        // Sample data
        List<Bug> bugs = new ArrayList<>();
        bugs.add(new Bug("BUG-001", "Login button not working", "Open", "High", "Website Redesign", LocalDate.now().minusDays(5)));
        bugs.add(new Bug("BUG-002", "Form validation error", "In Progress", "Medium", "Website Redesign", LocalDate.now().minusDays(3)));
        bugs.add(new Bug("BUG-003", "API returns 500 error", "Open", "Critical", "API Integration", LocalDate.now().minusDays(1)));
        bugs.add(new Bug("BUG-004", "Incorrect calculation", "In Progress", "High", "Mobile App", LocalDate.now().minusDays(2)));
        bugs.add(new Bug("BUG-005", "UI alignment issue", "Open", "Low", "Website Redesign", LocalDate.now().minusDays(4)));
        
        bugsGrid.setItems(bugs);
        bugsGrid.setSizeFull();
        
        // Add action column
        bugsGrid.addComponentColumn(bug -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button viewButton = new Button(VaadinIcon.EYE.create());
            viewButton.addThemeVariants();
            Button resolveButton = new Button(VaadinIcon.CHECK.create());
            resolveButton.addThemeVariants();
            actions.add(viewButton, resolveButton);
            return actions;
        }).setHeader("Actions").setFlexGrow(0);
        
        content.add(summaryLayout, bugsGrid);
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
    
    private VerticalLayout createProjectsContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        // Projects list
        H3 projectsTitle = new H3("My Projects");
        
        // Sample project cards
        VerticalLayout project1 = createProjectCard("Website Redesign", "5 open bugs", "2 in progress");
        VerticalLayout project2 = createProjectCard("Mobile App", "2 open bugs", "1 in progress");
        VerticalLayout project3 = createProjectCard("API Integration", "1 open bug", "0 in progress");
        
        content.add(projectsTitle, project1, project2, project3);
        
        return content;
    }
    
    private VerticalLayout createProjectCard(String projectName, String openBugs, String inProgressBugs) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("project-card");
        card.setWidthFull();
        card.getStyle().set("background-color", "white");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        card.getStyle().set("padding", "20px");
        card.getStyle().set("margin-bottom", "15px");
        
        H3 projectTitle = new H3(projectName);
        projectTitle.getStyle().set("margin-top", "0");
        
        HorizontalLayout stats = new HorizontalLayout();
        Span openBugsSpan = new Span(openBugs);
        openBugsSpan.getStyle().set("margin-right", "20px");
        Span inProgressBugsSpan = new Span(inProgressBugs);
        stats.add(openBugsSpan, inProgressBugsSpan);
        
        Button viewButton = new Button("View Project", VaadinIcon.ARROW_RIGHT.create());
        viewButton.getStyle().set("margin-top", "10px");
        
        card.add(projectTitle, stats, viewButton);
        
        return card;
    }
    
    private VerticalLayout createActivityContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        // Activity chart placeholder
        H3 activityTitle = new H3("Recent Activity");
        
        // In a real application, you would add a chart component here
        VerticalLayout chartPlaceholder = new VerticalLayout();
        chartPlaceholder.setHeight("300px");
        chartPlaceholder.setWidthFull();
        chartPlaceholder.getStyle().set("background-color", "#f5f7fa");
        chartPlaceholder.getStyle().set("border-radius", "8px");
        chartPlaceholder.getStyle().set("display", "flex");
        chartPlaceholder.getStyle().set("align-items", "center");
        chartPlaceholder.getStyle().set("justify-content", "center");
        
        Span chartText = new Span("Activity Chart Would Be Displayed Here");
        chartPlaceholder.add(chartText);
        
        // Recent activity list
        H3 recentTitle = new H3("Recent Actions");
        
        VerticalLayout activityList = new VerticalLayout();
        activityList.add(createActivityItem("Resolved bug BUG-006", "2 hours ago"));
        activityList.add(createActivityItem("Commented on BUG-003", "4 hours ago"));
        activityList.add(createActivityItem("Started working on BUG-002", "Yesterday"));
        activityList.add(createActivityItem("Resolved bug BUG-005", "2 days ago"));
        
        content.add(activityTitle, chartPlaceholder, recentTitle, activityList);
        
        return content;
    }
    
    private HorizontalLayout createActivityItem(String action, String time) {
        HorizontalLayout item = new HorizontalLayout();
        item.setWidthFull();
        item.setAlignItems(Alignment.CENTER);
        
        Span actionSpan = new Span(action);
        Span timeSpan = new Span(time);
        timeSpan.getStyle().set("color", "var(--lumo-secondary-text-color)");
        timeSpan.getStyle().set("margin-left", "auto");
        
        item.add(actionSpan, timeSpan);
        
        return item;
    }
}

