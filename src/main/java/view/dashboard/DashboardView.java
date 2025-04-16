package main.java.view.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import main.java.security.AuthService;
import main.java.view.components.NavBar;

@Route("dashboard")
@PageTitle("Dashboard | BugTracker")
public class DashboardView extends VerticalLayout implements BeforeEnterObserver {
    
    private final AuthService authService;
    
    public DashboardView() {
        this.authService = new AuthService();
        
        addClassName("dashboard-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Check if user is logged in
        if (!authService.isLoggedIn()) {
            event.forwardTo("login");
            return; 
        }
        
        // Get user role and load appropriate panel
        String userRole = authService.getCurrentUserRole();
        
        // Add the navigation bar with logged-in state
        NavBar navBar = new NavBar(authService, true);
        
        // Create the appropriate panel based on user role
        Component panel;
        System.out.println("hi");
        System.out.println("Role:"+userRole);
        
        switch (userRole) {
            case "Administrator":
                panel = new AdminPanel(authService);
                break;
            case "Developer":
                panel = new DeveloperPanel(authService);
                break;
            case "Tester":
                panel = new TesterPanel(authService);
                break;
            case "ProjectManager":
                panel = new ManagerPanel(authService);
                break;
            default:
                // If role is unknown, redirect to login
                UI.getCurrent().navigate("login");
                return;
        }
        
        // Add components to the layout
        add(navBar, panel);
    }
}

