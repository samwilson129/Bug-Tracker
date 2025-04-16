package main.java.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import main.java.view.components.NavBar;

@Route("")
@RouteAlias("home")
@PageTitle("BugTracker - Track and Manage Software Bugs")
public class LandingView extends VerticalLayout {

    public LandingView() {
        addClassName("landing-view");
        setPadding(false);
        setSpacing(false);
        setSizeFull();

        // Add the navigation bar
        NavBar navBar = new NavBar();

        // Hero section
        VerticalLayout heroSection = createHeroSection();

        // Features section
        VerticalLayout featuresSection = createFeaturesSection();

        // How it works section
        VerticalLayout howItWorksSection = createHowItWorksSection();

        // Add all sections to the main layout
        add(navBar, heroSection, featuresSection, howItWorksSection);
    }

    private VerticalLayout createHeroSection() {
        VerticalLayout heroSection = new VerticalLayout();
        heroSection.addClassName("hero-section");
        heroSection.setAlignItems(FlexComponent.Alignment.CENTER);
        heroSection.setJustifyContentMode(JustifyContentMode.CENTER);
        heroSection.setWidthFull();
        heroSection.setMinHeight("500px");
        heroSection.setPadding(true);

        // Background styling
        heroSection.getStyle().set("background", "linear-gradient(135deg, #1676f3, #0e4c92)");
        heroSection.getStyle().set("color", "white");
        heroSection.getStyle().set("text-align", "center");

        H1 heroTitle = new H1("Track and Manage Software Bugs Efficiently");
        heroTitle.getStyle().set("margin-top", "0");
        heroTitle.getStyle().set("font-size", "2.5em");

        Paragraph heroSubtitle = new Paragraph(
                "A comprehensive bug tracking system for development teams of all sizes");
        heroSubtitle.getStyle().set("font-size", "1.2em");
        heroSubtitle.getStyle().set("max-width", "800px");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(true);

        Button getStartedButton = new Button("Get Started", VaadinIcon.ARROW_RIGHT.create());
        getStartedButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("signup")));
        getStartedButton.addClassName("hero-button");
        getStartedButton.getStyle().set("background-color", "white");
        getStartedButton.getStyle().set("color", "#1676f3");
        getStartedButton.getStyle().set("font-weight", "bold");
        getStartedButton.getStyle().set("border-radius", "4px");

        Button learnMoreButton = new Button("Learn More");
        learnMoreButton.getStyle().set("background-color", "transparent");
        learnMoreButton.getStyle().set("color", "white");
        learnMoreButton.getStyle().set("border", "2px solid white");
        learnMoreButton.getStyle().set("border-radius", "4px");

        buttonLayout.add(getStartedButton, learnMoreButton);

        heroSection.add(heroTitle, heroSubtitle, buttonLayout);

        return heroSection;
    }

    private VerticalLayout createFeaturesSection() {
        VerticalLayout featuresSection = new VerticalLayout();
        featuresSection.addClassName("features-section");
        featuresSection.setAlignItems(FlexComponent.Alignment.CENTER);
        featuresSection.setWidthFull();
        featuresSection.setPadding(true);
        featuresSection.setSpacing(true);

        H2 featuresTitle = new H2("Key Features");
        featuresTitle.getStyle().set("margin-bottom", "40px");

        HorizontalLayout featuresLayout = new HorizontalLayout();
        featuresLayout.setWidthFull();
        featuresLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        featuresLayout.getStyle().set("flex-wrap", "wrap");

        // Feature cards
        Div feature1 = createFeatureCard(
                VaadinIcon.BUG.create(),
                "Bug Tracking",
                "Easily log, track, and manage bugs throughout the development lifecycle.");

        Div feature2 = createFeatureCard(
                VaadinIcon.USERS.create(),
                "Role-Based Access",
                "Different views for developers, testers, managers, and administrators.");

        Div feature3 = createFeatureCard(
                VaadinIcon.CHART.create(),
                "Reporting",
                "Generate detailed reports to analyze bug trends and team performance.");

        Div feature4 = createFeatureCard(
                VaadinIcon.CONNECT.create(),
                "Integration",
                "Seamlessly integrate with your existing development tools and workflows.");

        featuresLayout.add(feature1, feature2, feature3, feature4);

        featuresSection.add(featuresTitle, featuresLayout);

        return featuresSection;
    }

    private Div createFeatureCard(Icon icon, String title, String description) {
        Div card = new Div();
        card.addClassName("feature-card");
        card.setWidth("250px");
        card.getStyle().set("padding", "20px");
        card.getStyle().set("margin", "10px");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)");
        card.getStyle().set("text-align", "center");

        icon.setSize("50px");
        icon.setColor("#1676f3");

        H2 cardTitle = new H2(title);
        cardTitle.getStyle().set("font-size", "1.5em");
        cardTitle.getStyle().set("margin-top", "10px");

        Paragraph cardDescription = new Paragraph(description);

        card.add(icon, cardTitle, cardDescription);

        return card;
    }

    private VerticalLayout createHowItWorksSection() {
        VerticalLayout howItWorksSection = new VerticalLayout();
        howItWorksSection.addClassName("how-it-works-section");
        howItWorksSection.setAlignItems(FlexComponent.Alignment.CENTER);
        howItWorksSection.setWidthFull();
        howItWorksSection.setPadding(true);
        howItWorksSection.setSpacing(true);
        howItWorksSection.getStyle().set("background-color", "#f5f7fa");

        H2 sectionTitle = new H2("How It Works");
        sectionTitle.getStyle().set("margin-bottom", "40px");

        HorizontalLayout stepsLayout = new HorizontalLayout();
        stepsLayout.setWidthFull();
        stepsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        stepsLayout.getStyle().set("flex-wrap", "wrap");

        // Steps
        Div step1 = createStepCard("1", "Report a Bug", "Testers identify and report bugs with detailed information.");
        Div step2 = createStepCard("2", "Assign & Prioritize",
                "Managers review, prioritize, and assign bugs to developers.");
        Div step3 = createStepCard("3", "Fix & Verify", "Developers fix bugs and testers verify the solutions.");

        stepsLayout.add(step1, step2, step3);

        Button signupButton = new Button("Sign Up Now", e -> getUI().ifPresent(ui -> ui.navigate("signup")));
        signupButton.getStyle().set("margin-top", "40px");
        signupButton.getStyle().set("background-color", "#1676f3");
        signupButton.getStyle().set("color", "white");
        signupButton.getStyle().set("font-weight", "bold");
        signupButton.getStyle().set("border-radius", "4px");

        howItWorksSection.add(sectionTitle, stepsLayout, signupButton);

        return howItWorksSection;
    }

    private Div createStepCard(String number, String title, String description) {
        Div card = new Div();
        card.addClassName("step-card");
        card.setWidth("280px");
        card.getStyle().set("padding", "20px");
        card.getStyle().set("margin", "15px");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("background-color", "white");
        card.getStyle().set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)");
        card.getStyle().set("text-align", "center");

        Div numberCircle = new Div();
        numberCircle.setText(number);
        numberCircle.getStyle().set("width", "40px");
        numberCircle.getStyle().set("height", "40px");
        numberCircle.getStyle().set("border-radius", "50%");
        numberCircle.getStyle().set("background-color", "#1676f3");
        numberCircle.getStyle().set("color", "white");
        numberCircle.getStyle().set("display", "flex");
        numberCircle.getStyle().set("align-items", "center");
        numberCircle.getStyle().set("justify-content", "center");
        numberCircle.getStyle().set("font-weight", "bold");
        numberCircle.getStyle().set("font-size", "1.2em");
        numberCircle.getStyle().set("margin", "0 auto 15px");

        H2 cardTitle = new H2(title);
        cardTitle.getStyle().set("font-size", "1.3em");
        cardTitle.getStyle().set("margin-top", "10px");

        Paragraph cardDescription = new Paragraph(description);

        card.add(numberCircle, cardTitle, cardDescription);

        return card;
    }
}
