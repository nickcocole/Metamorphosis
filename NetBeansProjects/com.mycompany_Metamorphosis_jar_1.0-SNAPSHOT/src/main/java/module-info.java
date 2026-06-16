module com.mycompany.metamorphosis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    
    opens com.mycompany.metamorphosis to javafx.fxml;
    opens com.mycompany.metamorphosis.controller to javafx.fxml;
    exports com.mycompany.metamorphosis;
}
