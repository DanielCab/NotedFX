module org.group {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens org.group to javafx.fxml;
    exports org.group;
}
