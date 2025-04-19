package main.java.view.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.server.VaadinSession;

import main.java.DAO.BugDAO;
import main.java.DAO.ProjectDAO;
import main.java.DAO.UserDAO;
import main.java.model.Bug;
import main.java.model.BugStatus;
import main.java.model.Project;
import main.java.model.User;
import main.java.security.AuthService;
import main.java.service.BugService;
import main.java.service.ProjectService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DeveloperPanel extends VerticalLayout {

    private final AuthService authService;
    private final BugService bugService;
    private final ProjectService projectService;
    private final UserDAO userDAO;
    private final Grid<Bug> bugsGrid;

    public DeveloperPanel(AuthService authService) {
        this.authService = authService;
        this.bugService = new BugService();
        this.projectService = new ProjectService();
        this.userDAO = new UserDAO();
        this.bugsGrid = new Grid<>(Bug.class, false);

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

        // Get current developer's ID
        Integer currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            content.add(new H3("You need to be logged in to view your assigned bugs"));
            return content;
        }

        // Debug statement to check the current user ID
        System.out.println("Current developer ID: " + currentUserId);

        // Fetch bugs assigned to this developer - comparing with Integer instead of
        // String
        List<Bug> assignedBugs = bugService.getAllBugs().stream()
                .filter(b -> {
                    // Debug statement to check each bug's assigned_to value
                    String assignedTo = b.getter_assignedTo();
                    System.out.println("Bug ID: " + b.getter_id() + ", Title: " + b.getter_title() + ", Assigned To: "
                            + assignedTo);

                    // Handle potential null values and compare
                    if (assignedTo == null)
                        return false;

                    try {
                        int assignedToId = Integer.parseInt(assignedTo);
                        return assignedToId == currentUserId;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        System.out.println("Total assigned bugs found: " + assignedBugs.size());

        // Count bugs by status
        long openBugsCount = assignedBugs.stream()
                .filter(b -> b.getter_bugstatus() == BugStatus.reported)
                .count();

        long inProgressBugsCount = assignedBugs.stream()
                .filter(b -> b.getter_bugstatus() == BugStatus.in_progress)
                .count();

        long resolvedBugsCount = assignedBugs.stream()
                .filter(b -> b.getter_bugstatus() == BugStatus.fixed ||
                        b.getter_bugstatus() == BugStatus.verified ||
                        b.getter_bugstatus() == BugStatus.closed)
                .count();

        // Summary cards
        HorizontalLayout summaryLayout = new HorizontalLayout();
        summaryLayout.setWidthFull();

        VerticalLayout openBugs = createSummaryCard("Open Bugs", String.valueOf(openBugsCount), VaadinIcon.BUG.create(),
                "red");
        VerticalLayout inProgressBugs = createSummaryCard("In Progress", String.valueOf(inProgressBugsCount),
                VaadinIcon.HOURGLASS.create(), "orange");
        VerticalLayout resolvedBugs = createSummaryCard("Resolved", String.valueOf(resolvedBugsCount),
                VaadinIcon.CHECK.create(), "green");

        summaryLayout.add(openBugs, inProgressBugs, resolvedBugs);
        summaryLayout.setFlexGrow(1, openBugs, inProgressBugs, resolvedBugs);

        // Configure bugs grid
        bugsGrid.addColumn(Bug::getter_id).setHeader("ID").setAutoWidth(true);
        bugsGrid.addColumn(Bug::getter_title).setHeader("Title").setFlexGrow(1);
        bugsGrid.addColumn(b -> b.getter_bugstatus().name()).setHeader("Status").setAutoWidth(true);
        bugsGrid.addColumn(b -> b.getter_priority().name()).setHeader("Priority").setAutoWidth(true);

        // Add project name column
        ProjectDAO projectDAO = new ProjectDAO();
        bugsGrid.addColumn(bug -> {
            Project project = projectDAO.getProjectById(bug.getter_projectId());
            return project != null ? project.getter_name() : "Unknown Project";
        }).setHeader("Project").setAutoWidth(true);

        // Add date column
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        bugsGrid.addColumn(b -> b.getter_createdAt().format(formatter))
                .setHeader("Reported Date")
                .setAutoWidth(true);

        bugsGrid.setItems(assignedBugs);
        bugsGrid.setSizeFull();

        // Add action column
        bugsGrid.addComponentColumn(bug -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setSpacing(true);
            actions.setPadding(false);
            actions.setJustifyContentMode(JustifyContentMode.CENTER);

            Button viewButton = new Button(VaadinIcon.EYE.create());
            viewButton.addThemeVariants();
            viewButton.getElement().setAttribute("aria-label", "View bug details");
            viewButton.addClickListener(e -> Notification.show("Viewing bug: " + bug.getter_title(),
                    3000, Position.MIDDLE));

            Button resolveButton = new Button(VaadinIcon.CHECK.create());
            resolveButton.addThemeVariants();
            resolveButton.getElement().setAttribute("aria-label", "Resolve bug");

            // Only enable resolve button for in-progress bugs
            resolveButton.setEnabled(bug.getter_bugstatus() == BugStatus.in_progress);

            resolveButton.addClickListener(e -> {
                if (bug.getter_bugstatus() == BugStatus.in_progress) {
                    bug.setter_bugstatus(BugStatus.fixed);
                    bug.setter_updatedAt(java.time.LocalDateTime.now());

                    if (bugService.updateBugStatus(bug.getter_id(),BugStatus.fixed)) {
                        Notification.show("Bug marked as fixed",
                                3000, Position.MIDDLE);
                        refreshBugsGrid();
                    } else {
                        Notification.show("Failed to update bug status",
                                3000, Position.MIDDLE);
                    }
                }
            });

            actions.add(viewButton, resolveButton);
            return actions;
        }).setHeader("Actions")
                .setWidth("150px")
                .setFlexGrow(0)
                .setTextAlign(ColumnTextAlign.CENTER);

        content.add(summaryLayout, bugsGrid);
        return content;
    }

    private Integer getCurrentUserId() {
        // Get the current user ID from session
        Object userIdObj = VaadinSession.getCurrent().getAttribute("userId");
        System.out.println("User ID from session: " + userIdObj);

        if (userIdObj != null) {
            try {
                return Integer.parseInt(userIdObj.toString());
            } catch (NumberFormatException e) {
                System.err.println("Error parsing user ID: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    private void refreshBugsGrid() {
        Integer currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            List<Bug> assignedBugs = bugService.getAllBugs().stream()
                    .filter(b -> {
                        String assignedTo = b.getter_assignedTo();
                        if (assignedTo == null)
                            return false;

                        try {
                            int assignedToId = Integer.parseInt(assignedTo);
                            return assignedToId == currentUserId;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
            bugsGrid.setItems(assignedBugs);
        }
    }

    private VerticalLayout createSummaryCard(String title, String count, com.vaadin.flow.component.icon.Icon icon,
            String color) {
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

        // Get current developer's ID
        Integer currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            content.add(new H3("You need to be logged in to view your projects"));
            return content;
        }

        // Projects list
        H3 projectsTitle = new H3("My Projects");
        content.add(projectsTitle);

        // Get projects assigned to this developer
        List<Project> developerProjects = projectService.getProjectsByDeveloperId(currentUserId);

        if (developerProjects.isEmpty()) {
            content.add(new Span("You are not assigned to any projects yet."));
            return content;
        }

        // Create a card for each project
        BugDAO bugDAO = new BugDAO();
        for (Project project : developerProjects) {
            // Get bugs for this project
            List<Bug> projectBugs = bugDAO.getAllBugs().stream()
                    .filter(b -> b.getter_projectId() == project.getter_id())
                    .collect(Collectors.toList());

            long openBugs = projectBugs.stream()
                    .filter(b -> b.getter_bugstatus() ==BugStatus.reported)
                    .count();

            long inProgressBugs = projectBugs.stream()
                    .filter(b -> b.getter_bugstatus() ==BugStatus.in_progress)
                    .count();

            VerticalLayout projectCard = createProjectCard(
                    project.getter_name(),
                    openBugs + " open bugs",
                    inProgressBugs + " in progress");
            content.add(projectCard);
        }

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

        // Get current developer's ID
        Integer currentUserId = getCurrentUserId();
        VerticalLayout activityList = new VerticalLayout();

        if (currentUserId != null) {
            // Get recent bug updates
            List<Bug> recentlyUpdatedBugs = bugService.getAllBugs().stream()
                    .filter(b -> String.valueOf(currentUserId).equals(b.getter_assignedTo()))
                    .sorted((b1, b2) -> b2.getter_updatedAt().compareTo(b1.getter_updatedAt()))
                    .limit(5)
                    .collect(Collectors.toList());

            for (Bug bug : recentlyUpdatedBugs) {
                String action = formatBugAction(bug);
                String time = formatTimeAgo(bug.getter_updatedAt());
                activityList.add(createActivityItem(action, time));
            }
        }

        if (activityList.getChildren().count() == 0) {
            activityList.add(new Span("No recent activity found."));
        }

        content.add(activityTitle, chartPlaceholder, recentTitle, activityList);

        return content;
    }

    private String formatBugAction(Bug bug) {
        switch (bug.getter_bugstatus()) {
            case in_progress:
                return "Started working on " + bug.getter_title();
            case fixed:
                return "Resolved bug " + bug.getter_title();
            case verified:
                return "Verified bug " + bug.getter_title();
            case closed:
                return "Closed bug " + bug.getter_title();
            default:
                return "Updated bug " + bug.getter_title();
        }
    }

    private String formatTimeAgo(java.time.LocalDateTime dateTime) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.Duration duration = java.time.Duration.between(dateTime, now);

        if (duration.toDays() > 1) {
            return duration.toDays() + " days ago";
        } else if (duration.toDays() == 1) {
            return "Yesterday";
        } else if (duration.toHours() > 0) {
            return duration.toHours() + " hours ago";
        } else if (duration.toMinutes() > 0) {
            return duration.toMinutes() + " minutes ago";
        } else {
            return "Just now";
        }
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
