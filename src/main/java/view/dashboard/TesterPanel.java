package main.java.view.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import main.java.security.AuthService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TesterPanel extends VerticalLayout {
    
    private final AuthService authService;
    
    // Sample data class for demonstration
    private static class Bug {
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
        Tab testCasesTab = new Tab(VaadinIcon.CHECK_SQUARE.create(), new H3("Test Cases"));
        
        Tabs tabs = new Tabs(reportBugTab, myBugsTab, testCasesTab);
        tabs.setWidthFull();
        
        // Content for each tab
        VerticalLayout reportBugContent = createReportBugContent();
        VerticalLayout myBugsContent = createMyBugsContent();
        VerticalLayout testCasesContent = createTestCasesContent();
        
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
            } else if (event.getSelectedTab().equals(testCasesTab)) {
                add(testCasesContent);
            }
        });
    }
    
    private VerticalLayout createReportBugContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        H3 formTitle = new H3("Report a New Bug");
        
        // Bug report form
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
        
        ComboBox<String> projectField = new ComboBox<>("Project");
        projectField.setItems("Website Redesign", "Mobile App", "API Integration");
        projectField.setWidthFull();
        projectField.setRequired(true);
        
        ComboBox<String> priorityField = new ComboBox<>("Priority");
        priorityField.setItems("Low", "Medium", "High", "Critical");
        priorityField.setWidthFull();
        priorityField.setRequired(true);
        
        TextArea descriptionField = new TextArea("Description");
        descriptionField.setWidthFull();
        descriptionField.setPlaceholder("Describe the bug in detail");
        descriptionField.setMinHeight("150px");
        descriptionField.setRequired(true);
        
        TextArea stepsField = new TextArea("Steps to Reproduce");
        stepsField.setWidthFull();
        stepsField.setPlaceholder("List the steps to reproduce this bug");
        stepsField.setMinHeight("100px");
        stepsField.setRequired(true);
        
        Button submitButton = new Button("Submit Bug Report", VaadinIcon.CHECK.create());
        submitButton.getStyle().set("margin-top", "20px");
        
        form.add(titleField, projectField, priorityField, descriptionField, stepsField, submitButton);
        
        content.add(formTitle, form);
        content.setAlignItems(Alignment.CENTER);
        
        return content;
    }
    
    private VerticalLayout createMyBugsContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        // Summary cards
        HorizontalLayout summaryLayout = new HorizontalLayout();
        summaryLayout.setWidthFull();
        
        VerticalLayout reportedBugs = createSummaryCard("Reported", "15", VaadinIcon.BUG.create(), "#1676f3");
        VerticalLayout openBugs = createSummaryCard("Open", "8", VaadinIcon.HOURGLASS.create(), "orange");
        VerticalLayout resolvedBugs = createSummaryCard("Resolved", "7", VaadinIcon.CHECK.create(), "green");
        
        summaryLayout.add(reportedBugs, openBugs, resolvedBugs);
        summaryLayout.setFlexGrow(1, reportedBugs, openBugs, resolvedBugs);
        
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
        bugs.add(new Bug("BUG-006", "Button color incorrect", "Resolved", "Low", "Mobile App", LocalDate.now().minusDays(10)));
        bugs.add(new Bug("BUG-007", "Typo in error message", "Resolved", "Low", "Website Redesign", LocalDate.now().minusDays(12)));
        
        bugsGrid.setItems(bugs);
        bugsGrid.setSizeFull();
        
        // Add action column
        bugsGrid.addComponentColumn(bug -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button viewButton = new Button(VaadinIcon.EYE.create());
            viewButton.addThemeVariants();
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addThemeVariants();
            actions.add(viewButton, editButton);
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
    
    private VerticalLayout createTestCasesContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        
        H3 testCasesTitle = new H3("Test Cases");
        
        // Action buttons
        HorizontalLayout actionButtons = new HorizontalLayout();
        Button addTestCaseButton = new Button("Add Test Case", VaadinIcon.PLUS.create());
        Button importButton = new Button("Import", VaadinIcon.UPLOAD.create());
        Button exportButton = new Button("Export", VaadinIcon.DOWNLOAD.create());
        actionButtons.add(addTestCaseButton, importButton, exportButton);
        
        // Test cases grid (simplified for example)
        Grid<TestCase> testCasesGrid = new Grid<>();
        testCasesGrid.addColumn(TestCase::getId).setHeader("ID");
        testCasesGrid.addColumn(TestCase::getName).setHeader("Name");
        testCasesGrid.addColumn(TestCase::getProject).setHeader("Project");
        testCasesGrid.addColumn(TestCase::getStatus).setHeader("Status");
        
        // Sample data
        List<TestCase> testCases = new ArrayList<>();
        testCases.add(new TestCase("TC-001", "Login Functionality", "Website Redesign", "Passed"));
        testCases.add(new TestCase("TC-002", "User Registration", "Website Redesign", "Failed"));
        testCases.add(new TestCase("TC-003", "Password Reset", "Website Redesign", "Not Run"));
        testCases.add(new TestCase("TC-004", "Search Functionality", "Mobile App", "Passed"));
        testCases.add(new TestCase("TC-005", "Payment Processing", "Mobile App", "Not Run"));
        
        testCasesGrid.setItems(testCases);
        testCasesGrid.setSizeFull();
        
        // Add action column
        testCasesGrid.addComponentColumn(testCase -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button runButton = new Button(VaadinIcon.PLAY.create());
            runButton.addThemeVariants();
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addThemeVariants();
            actions.add(runButton, editButton);
            return actions;
        }).setHeader("Actions").setFlexGrow(0);
        
        content.add(testCasesTitle, actionButtons, testCasesGrid);
        return content;
    }
    
    // Simple test case class for demonstration
    private static class TestCase {
        private String id;
        private String name;
        private String project;
        private String status;
        
        public TestCase(String id, String name, String project, String status) {
            this.id = id;
            this.name = name;
            this.project = project;
            this.status = status;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getProject() { return project; }
        public String getStatus() { return status; }
    }
}

