module se233.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;
    requires javafx.swing;


    opens se233.project to javafx.fxml;
    exports se233.project;
}