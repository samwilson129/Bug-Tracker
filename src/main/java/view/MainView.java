//This is a simple Vaadin application that creates a basic UI with a text field and a button.
//Just to check whether vaadin works or not.

package main.java.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.H1;

@Route("") // This makes it the default route at localhost:8080
public class MainView extends VerticalLayout {

    public MainView() {
        H1 heading = new H1("Bug Tracker");

        TextField nameField = new TextField("Enter your name");
        Button greetButton = new Button("Greet Me", e -> {
            Notification.show("Hello, " + nameField.getValue() + "!");
        });

        add(heading, nameField, greetButton);
    }
}
