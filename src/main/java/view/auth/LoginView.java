package main.java.view.auth;

import org.checkerframework.checker.units.qual.A;

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

import main.java.security.AuthService;
import main.java.service.UserService;

@Route("login")
@PageTitle("Login | BugTracker")
public class LoginView extends VerticalLayout {

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div container = new Div();
        container.addClassName("login-container");
        container.getStyle()
                .set("max-width", "400px")
                .set("padding", "2rem")
                .set("border-radius", "8px")
                .set("background-color", "white")
                .set("box-shadow", "0px 4px 10px rgba(0, 0, 0, 0.1)");

        H2 title = new H2("BugTracker Login");
        title.getStyle().set("text-align", "center");

        TextField email = new TextField("email");
        email.setRequired(true);
        email.setPlaceholder("Enter your email");

        PasswordField password = new PasswordField("Password");
        password.setRequired(true);
        password.setPlaceholder("Enter your password");

        Button loginButton = new Button("Login", event -> {
            if (email.isEmpty() || password.isEmpty()) {
                Notification notification = Notification.show("Please fill in all required fields");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {
                // UserService userService = new UserService();
                AuthService authService = new AuthService();
                // boolean authenticated = userService.login(email.getValue(), password.getValue());
                boolean authenticated = authService.login(email.getValue(), password.getValue());
                System.out.println("Authenticated: " + authenticated);
                if (authenticated) {
                    System.out.println("Login successful");
                    Notification.show("Login successful");
                    // UI.getCurrent().navigate("dashboard");

                    UI ui = UI.getCurrent();
                    ui.access(() -> ui.navigate("dashboard"));
                    System.out.println("Navigating to dashboard");
                } else {
                    Notification notification = Notification.show("Invalid email or password");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        loginButton.addClassName("login-button");
        loginButton.getStyle()
                .set("width", "100%")
                .set("margin-top", "1rem");

        Paragraph signupText = new Paragraph("Don't have an account?");
        RouterLink signupLink = new RouterLink("Sign up", SignupView.class);

        container.add(title, email, password, loginButton, signupText, signupLink);
        add(container);
    }
}
