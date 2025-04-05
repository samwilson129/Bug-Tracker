package main.java.view.auth;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import main.java.view.components.UserTypeSelector;

@Route("signup")
@PageTitle("Sign Up | BugTracker")
public class SignupView extends VerticalLayout {

    public SignupView() {
        addClassName("signup-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Create form components
        H2 title = new H2("Create an Account");
        
        TextField fullName = new TextField("Full Name");
        fullName.setRequired(true);
        fullName.setPlaceholder("Enter your full name");
        
        TextField username = new TextField("Username");
        username.setRequired(true);
        username.setPlaceholder("Choose a username");
        
        EmailField email = new EmailField("Email");
        email.setRequired(true);
        email.setPlaceholder("Enter your email");
        
        PasswordField password = new PasswordField("Password");
        password.setRequired(true);
        password.setPlaceholder("Create a password");
        
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        confirmPassword.setRequired(true);
        confirmPassword.setPlaceholder("Confirm your password");
        
        // User type selector component
        UserTypeSelector userTypeSelector = new UserTypeSelector();
        
        Button signupButton = new Button("Sign Up", event -> {
            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || 
                password.isEmpty() || confirmPassword.isEmpty() || 
                userTypeSelector.getValue() == null) {
                
                Notification notification = Notification.show("Please fill in all required fields");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            
            if (!password.getValue().equals(confirmPassword.getValue())) {
                Notification notification = Notification.show("Passwords do not match");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            
            // Here you would call your authentication service to register the user
            // For example: authService.register(username.getValue(), password.getValue(), email.getValue(), userTypeSelector.getValue());
            Notification.show("Registration functionality will be implemented here");
            
            // Navigate to login view after successful registration
            // UI.getCurrent().navigate("login");
        });
        signupButton.addClassName("signup-button");
        
        Paragraph loginText = new Paragraph("Already have an account?");
        RouterLink loginLink = new RouterLink("Log in", LoginView.class);
        
        // Add components to layout
        add(
            title,
            fullName,
            username,
            email,
            password,
            confirmPassword,
            userTypeSelector,
            signupButton,
            loginText,
            loginLink
        );
        
        // Set max width for better appearance
        setMaxWidth("400px");
        
        // Add some spacing between components
        setSpacing(true);
    }
}

