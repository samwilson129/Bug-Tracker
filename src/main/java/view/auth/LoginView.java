package main.java.view.auth;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.html.Div;

@Route("login")
@PageTitle("Login | BugTracker")
public class LoginView extends VerticalLayout {

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Create a centered container
        Div container = new Div();
        container.addClassName("login-container");
        container.getStyle()
                .set("max-width", "400px") // Set max width
                .set("padding", "2rem")    // Add padding
                .set("border-radius", "8px")
                .set("background-color", "white")
                .set("box-shadow", "0px 4px 10px rgba(0, 0, 0, 0.1)");

        // Create form components
        H2 title = new H2("BugTracker Login");
        title.getStyle().set("text-align", "center");

        TextField username = new TextField("Username");
        username.setRequired(true);
        username.setPlaceholder("Enter your username");
        
        PasswordField password = new PasswordField("Password");
        password.setRequired(true);
        password.setPlaceholder("Enter your password");
        
        Button loginButton = new Button("Login", event -> {
            if (username.isEmpty() || password.isEmpty()) {
                Notification notification = Notification.show("Please fill in all required fields");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {
                Notification.show("Login functionality will be implemented here");
                // UI.getCurrent().navigate("dashboard"); // Uncomment when dashboard exists
            }
        });
        loginButton.addClassName("login-button");
        loginButton.getStyle()
                .set("width", "100%")
                .set("margin-top", "1rem");

        Paragraph signupText = new Paragraph("Don't have an account?");
        RouterLink signupLink = new RouterLink("Sign up", SignupView.class);
        
        // Add components to container
        container.add(
            title,
            username,
            password,
            loginButton,
            signupText,
            signupLink
        );

        // Add container to the main layout
        add(container);
    }
}
