package main.java.view.auth;

import com.vaadin.flow.component.UI;
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
import main.java.service.UserService;
import main.java.model.User;
import main.java.view.components.UserTypeSelector;

@Route("signup")
@PageTitle("Sign Up | BugTracker")
public class SignupView extends VerticalLayout {

    private final UserService userService = new UserService(); // Ideally, this should be injected

    public SignupView() {
        addClassName("signup-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 title = new H2("Create an Account");

        TextField fullName = new TextField("Full Name");
        TextField username = new TextField("Username");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        UserTypeSelector userTypeSelector = new UserTypeSelector();

        fullName.setRequired(true);
        username.setRequired(true);
        email.setRequired(true);
        password.setRequired(true);
        confirmPassword.setRequired(true);

        Button signupButton = new Button("Sign Up", event -> {
            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || 
                password.isEmpty() || confirmPassword.isEmpty() || 
                userTypeSelector.getValue() == null) {

                Notification.show("Please fill in all required fields", 3000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (!password.getValue().equals(confirmPassword.getValue())) {
                Notification.show("Passwords do not match", 3000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            //add this later
            // if (userService.isUsernameTaken(username.getValue())) {
            //     Notification.show("Username already exists", 3000, Notification.Position.MIDDLE)
            //                 .addThemeVariants(NotificationVariant.LUMO_ERROR);
            //     return;
            // }

            // User user = new User(
            //     username.getValue(),
            //     password.getValue(),
            //     //fullName.getValue(),
            //     email.getValue(),
            //     userTypeSelector.getValue()
            // );

            // Extract values from the form
            String usernameValue = username.getValue();
            String emailValue = email.getValue();
            String passwordValue = password.getValue();
            String roleValue = userTypeSelector.getValue(); // This should be a string like "Tester", "Developer", etc.

            // Call the UserService method to add user
            boolean success = userService.addUser(usernameValue, emailValue, passwordValue, roleValue);
            System.out.println("Values of user: " + usernameValue + ", " + emailValue + ", " + passwordValue + ", " + roleValue);

            if (success) {
                Notification.show("User registered successfully!", 3000, Notification.Position.MIDDLE);
                UI.getCurrent().navigate("login");
                // You can optionally redirect to login page
            } else {
                Notification.show("Failed to register user. Please check the role and try again.", 3000, Notification.Position.MIDDLE);
            }

            // Notification.show("Account created successfully!", 3000, Notification.Position.MIDDLE)
            //             .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            // UI.getCurrent().navigate("login");
        });

        Paragraph loginText = new Paragraph("Already have an account?");
        RouterLink loginLink = new RouterLink("Log in", LoginView.class);

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

        setMaxWidth("400px");
        setSpacing(true);
    }
}
