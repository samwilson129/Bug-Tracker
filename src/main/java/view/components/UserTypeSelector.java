package main.java.view.components;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UserTypeSelector extends CustomField<String> {
    
    private final ComboBox<String> userTypeComboBox;
    
    public UserTypeSelector() {
        // Create a container for the component
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        
        // Add a label
        Label label = new Label("User Type");
        
        // Create the combo box with user type options
        userTypeComboBox = new ComboBox<>();
        userTypeComboBox.setItems("Developer", "Tester", "ProjectManager", "Administrator");
        userTypeComboBox.setPlaceholder("Select user type");
        userTypeComboBox.setRequired(true);
        userTypeComboBox.setWidth("100%");
        
        // Add description text
        Div description = new Div();
        description.setText("Your user type determines your permissions in the system");
        description.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        description.getStyle().set("color", "var(--lumo-secondary-text-color)");
        
        // Add components to layout
        layout.add(label, userTypeComboBox, description);
        
        // Add the layout to this custom field
        add(layout);
    }
    
    @Override
    protected String generateModelValue() {
        return userTypeComboBox.getValue();
    }
    
    @Override
    protected void setPresentationValue(String userType) {
        userTypeComboBox.setValue(userType);
    }
}

