package main.java.view.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;

import main.java.DAO.BugDAO;
import main.java.DAO.ProjectDAO;
import main.java.DAO.UserDAO;
import main.java.model.Project;
import main.java.security.AuthService;
import main.java.service.ProjectService;
import main.java.model.Bug;
import main.java.model.BugStatus;
import main.java.model.Priority;
import main.java.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class TesterPanel extends VerticalLayout {

    private final AuthService authService;
    private final ProjectDAO projectDAO = new ProjectDAO();
    private List<Project> projects;
    private List<Bug> bug;
    // private User user;

    // Sample data class for demonstration
    // public static class Bug {
    // private String id;
    // private String title;
    // private String status;
    // private String priority;
    // private String project;
    // private LocalDate reportedDate;

    // public Bug(String id, String title, String status, String priority, String
    // project, LocalDate reportedDate) {
    // this.id = id;
    // this.title = title;
    // this.status = status;
    // this.priority = priority;
    // this.project = project;
    // this.reportedDate = reportedDate;
    // }

    // public String getId() { return id; }
    // public String getTitle() { return title; }
    // public String getStatus() { return status; }
    // public String getPriority() { return priority; }
    // public String getProject() { return project; }
    // public LocalDate getReportedDate() { return reportedDate; }
    // }

    // Sample Project class (adjust based on your actual model)
    // public class Project {
    // private int id;
    // private String title;
    // // private String description;

    // public Project(int id, String title) {
    // this.id = id;
    // this.title = title;
    // // this.description = description;
    // }

    // public int getId() { return id; }
    // public String getTitle() { return title; }
    // // public String getDescription() { return description; }

    // @Override
    // public String toString() {
    // return title; // for display in ComboBox
    // }
    // }

    public TesterPanel(AuthService authService) {
        this.authService = authService;

        addClassName("tester-panel");
        setSizeFull();

        // Welcome message
        H2 welcomeTitle = new H2("Tester Dashboard");
        welcomeTitle.getStyle().set("margin-top", "0");

        // Create tabs for different tester functions
        Tab reportBugTab = new Tab(VaadinIcon.PLUS.create(), new H3("Report Bug"));
        Tab myBugsTab = new Tab(VaadinIcon.BUG.create(), new H3("My Bugs"));
        // Tab testCasesTab = new Tab(VaadinIcon.CHECK_SQUARE.create(), new H3("Test
        // Cases"));

        // Tabs tabs = new Tabs(reportBugTab, myBugsTab, testCasesTab);
        Tabs tabs = new Tabs(reportBugTab, myBugsTab);
        tabs.setWidthFull();

        // Content for each tab
        Component reportBugContent = createReportBugContent(); // Look into this later
        Component myBugsContent = createMyBugsContent();
        // VerticalLayout testCasesContent = createTestCasesContent();

        // Initially show report bug content
        add(welcomeTitle, tabs, reportBugContent);

        // Tab change listener
        tabs.addSelectedChangeListener(event -> {
            // Remove all content
            remove(getComponentAt(2));

            // Add appropriate content based on selected tab
            if (event.getSelectedTab().equals(reportBugTab)) {
                add(reportBugContent);
            } else if (event.getSelectedTab().equals(myBugsTab)) {
                add(myBugsContent);
            }
            // else if (event.getSelectedTab().equals(testCasesTab)) {
            // add(testCasesContent);
            // }
        });
    }

    // private VerticalLayout createReportBugContent() {
    // VerticalLayout content = new VerticalLayout();
    // content.setSizeFull();

    // H3 formTitle = new H3("Report a New Bug");

    // // Bug report form
    // VerticalLayout form = new VerticalLayout();
    // form.setMaxWidth("800px");
    // form.setPadding(true);
    // form.getStyle().set("background-color", "white");
    // form.getStyle().set("border-radius", "8px");
    // form.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");

    // TextField titleField = new TextField("Bug Title");
    // titleField.setWidthFull();
    // titleField.setPlaceholder("Enter a descriptive title");
    // titleField.setRequired(true);

    // ComboBox<Project> projectField = new ComboBox<>("Project");
    // projectField.setWidthFull();
    // projectField.setRequired(true);

    // // Fetch project list from DB
    // List<Project> projects = new ProjectService().getAllProjects();
    // projectField.setItems(projects);

    // ComboBox<String> priorityField = new ComboBox<>("Priority");
    // priorityField.setItems("Low", "Medium", "High", "Critical");
    // priorityField.setWidthFull();
    // priorityField.setRequired(true);

    // TextArea descriptionField = new TextArea("Description");
    // descriptionField.setWidthFull();
    // descriptionField.setPlaceholder("Describe the bug in detail");
    // descriptionField.setMinHeight("150px");
    // descriptionField.setRequired(true);

    // TextArea stepsField = new TextArea("Steps to Reproduce");
    // stepsField.setWidthFull();
    // stepsField.setPlaceholder("List the steps to reproduce this bug");
    // stepsField.setMinHeight("100px");
    // stepsField.setRequired(true);

    // Button submitButton = new Button("Submit Bug Report",
    // VaadinIcon.CHECK.create());
    // submitButton.getStyle().set("margin-top", "20px");

    // form.add(titleField, projectField, priorityField, descriptionField,
    // stepsField, submitButton);

    // content.add(formTitle, form);
    // content.setAlignItems(Alignment.CENTER);

    // return content;
    // }

    private Component createReportBugContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        H3 formTitle = new H3("Report a New Bug");

        VerticalLayout form = new VerticalLayout();
        form.setMaxWidth("800px");
        form.setPadding(true);
        form.getStyle().set("background-color", "white");
        form.getStyle().set("border-radius", "8px");
        form.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");

        TextField titleField = new TextField("Bug Title");
        titleField.setWidthFull();
        titleField.setPlaceholder("Enter a descriptive title");
        titleField.setRequired(true);

        ComboBox<Project> projectField = new ComboBox<>("Project");
        projectField.setWidthFull();
        projectField.setRequired(true);
        List<Project> projects = new ProjectService().getAllProjects();
        projectField.setItems(projects);
        projectField.setItemLabelGenerator(Project::getter_name);

        ComboBox<Priority> priorityField = new ComboBox<>("Priority");
        priorityField.setItems(Priority.values());
        priorityField.setWidthFull();
        priorityField.setRequired(true);

        TextArea descriptionField = new TextArea("Description");
        descriptionField.setWidthFull();
        descriptionField.setPlaceholder("Describe the bug in detail and include steps to reproduce it");
        descriptionField.setMinHeight("200px");
        descriptionField.setRequired(true);

        Button submitButton = new Button("Submit Bug Report", VaadinIcon.CHECK.create());
        submitButton.getStyle().set("margin-top", "20px");

        submitButton.addClickListener(event -> {
            if (titleField.isEmpty() || projectField.isEmpty() || priorityField.isEmpty()
                    || descriptionField.isEmpty()) {
                Notification.show("Please fill in all the fields.", 3000, Notification.Position.MIDDLE);
                return;
            }

            String currentUserId = VaadinSession.getCurrent().getAttribute("userId").toString();
            System.out.println(currentUserId);
            Bug newBug = new Bug(0, "", "", BugStatus.reported, Priority.Low, LocalDateTime.now(),
                    LocalDateTime.now(), null, null, 0);

            newBug.setter_title(titleField.getValue());
            newBug.setter_description(descriptionField.getValue());
            newBug.setter_bugstatus(BugStatus.reported);
            newBug.setter_priority(priorityField.getValue());
            newBug.setter_createdAt(LocalDateTime.now());
            newBug.setter_updatedAt(LocalDateTime.now());
            newBug.setter_assignedTo(null); // Unassigned initially
            newBug.setter_reportedBy(currentUserId); // Replace with actual current user retrieval
            newBug.setter_projectId(projectField.getValue().getter_id());

            new BugDAO().addBug(newBug);

            Notification.show("Bug reported successfully!", 3000, Notification.Position.BOTTOM_START);

            titleField.clear();
            projectField.clear();
            priorityField.clear();
            descriptionField.clear();
        });

        form.add(titleField, projectField, priorityField, descriptionField, submitButton);
        content.add(formTitle, form);
        content.setAlignItems(Alignment.CENTER);

        return content;
    }

    private Component createMyBugsContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        // Get current username from session
        String currentUser = (String) VaadinSession.getCurrent().getAttribute("email");
        if (currentUser == null) {
            content.add(new H3("User not logged in."));
            return content;
        }

        // Fetch tester's ID using UserDAO
        User currentTester = new UserDAO().getUserByEmail(currentUser);
        if (currentTester == null) {
            content.add(new H3("User not found in the database."));
            return content;
        }

        int testerId = currentTester.getter_id();

        // Fetch bugs reported by this tester
        List<Bug> bugs = new BugDAO().getBugByReportedId(String.valueOf(testerId));

        // Count summary stats
        long totalReported = bugs.size();
        long totalOpen = bugs.stream()
                .filter(b -> b.getter_bugstatus() == BugStatus.reported
                        || b.getter_bugstatus() == BugStatus.in_progress)
                .count();
        long totalResolved = bugs.stream()
                .filter(b -> b.getter_bugstatus() == BugStatus.fixed
                        || b.getter_bugstatus() == BugStatus.closed)
                .count();

        // Summary cards
        HorizontalLayout summaryLayout = new HorizontalLayout();
        summaryLayout.setWidthFull();

        VerticalLayout reportedCard = createSummaryCard("Reported", String.valueOf(totalReported),
                VaadinIcon.BUG.create(), "#1676f3");
        VerticalLayout openCard = createSummaryCard("Open", String.valueOf(totalOpen), VaadinIcon.HOURGLASS.create(),
                "orange");
        VerticalLayout resolvedCard = createSummaryCard("Resolved", String.valueOf(totalResolved),
                VaadinIcon.CHECK.create(), "green");

        summaryLayout.add(reportedCard, openCard, resolvedCard);
        summaryLayout.setFlexGrow(1, reportedCard, openCard, resolvedCard);

        // Bugs Grid
        Grid<Bug> bugsGrid = new Grid<>(Bug.class, false);
        bugsGrid.addColumn(Bug::getter_id).setHeader("ID");
        bugsGrid.addColumn(Bug::getter_title).setHeader("Title");
        bugsGrid.addColumn(b -> b.getter_bugstatus().name()).setHeader("Status");
        bugsGrid.addColumn(Bug::getter_priority).setHeader("Priority");
        // Fetch project name using lambda and ProjectDAO
        ProjectDAO projectDAO = new ProjectDAO();
        bugsGrid.addColumn(bug -> {
            Project project = projectDAO.getProjectById(bug.getter_projectId());
            return project != null ? project.getter_name() : "Unknown Project";
        }).setHeader("Project");
        bugsGrid.addColumn(b -> b.getter_createdAt().toLocalDate()).setHeader("Reported Date");

        bugsGrid.setItems(bugs);
        bugsGrid.setSizeFull();

        // Action buttons
        bugsGrid.addComponentColumn(bug -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button viewButton = new Button(VaadinIcon.EYE.create());
            viewButton.addClickListener(e -> Notification.show("Viewing bug: " + bug.getter_id()));

            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addClickListener(e -> Notification.show("Editing bug: " + bug.getter_id()));

            actions.add(viewButton, editButton);
            return actions;
        }).setHeader("Actions").setFlexGrow(0);

        content.add(summaryLayout, bugsGrid);
        return content;
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

    // private VerticalLayout createTestCasesContent() {
    // VerticalLayout content = new VerticalLayout();
    // content.setSizeFull();

    // H3 testCasesTitle = new H3("Test Cases");

    // // Action buttons
    // HorizontalLayout actionButtons = new HorizontalLayout();
    // Button addTestCaseButton = new Button("Add Test Case",
    // VaadinIcon.PLUS.create());
    // Button importButton = new Button("Import", VaadinIcon.UPLOAD.create());
    // Button exportButton = new Button("Export", VaadinIcon.DOWNLOAD.create());
    // actionButtons.add(addTestCaseButton, importButton, exportButton);

    // // Test cases grid (simplified for example)
    // Grid<TestCase> testCasesGrid = new Grid<>();
    // testCasesGrid.addColumn(TestCase::getId).setHeader("ID");
    // testCasesGrid.addColumn(TestCase::getName).setHeader("Name");
    // testCasesGrid.addColumn(TestCase::getProject).setHeader("Project");
    // testCasesGrid.addColumn(TestCase::getStatus).setHeader("Status");

    // // Sample data
    // List<TestCase> testCases = new ArrayList<>();
    // testCases.add(new TestCase("TC-001", "Login Functionality", "Website
    // Redesign", "Passed"));
    // testCases.add(new TestCase("TC-002", "User Registration", "Website Redesign",
    // "Failed"));
    // testCases.add(new TestCase("TC-003", "Password Reset", "Website Redesign",
    // "Not Run"));
    // testCases.add(new TestCase("TC-004", "Search Functionality", "Mobile App",
    // "Passed"));
    // testCases.add(new TestCase("TC-005", "Payment Processing", "Mobile App", "Not
    // Run"));

    // testCasesGrid.setItems(testCases);
    // testCasesGrid.setSizeFull();

    // // Add action column
    // testCasesGrid.addComponentColumn(testCase -> {
    // HorizontalLayout actions = new HorizontalLayout();
    // Button runButton = new Button(VaadinIcon.PLAY.create());
    // runButton.addThemeVariants();
    // Button editButton = new Button(VaadinIcon.EDIT.create());
    // editButton.addThemeVariants();
    // actions.add(runButton, editButton);
    // return actions;
    // }).setHeader("Actions").setFlexGrow(0);

    // content.add(testCasesTitle, actionButtons, testCasesGrid);
    // return content;
    // }

    // // Simple test case class for demonstration
    // private static class TestCase {
    // private String id;
    // private String name;
    // private String project;
    // private String status;

    // public TestCase(String id, String name, String project, String status) {
    // this.id = id;
    // this.name = name;
    // this.project = project;
    // this.status = status;
    // }

    // public String getId() { return id; }
    // public String getName() { return name; }
    // public String getProject() { return project; }
    // public String getStatus() { return status; }
    // }
}
