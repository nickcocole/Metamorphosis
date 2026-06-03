module com.mycompany.metamorphosis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.mycompany.metamorphosis to javafx.fxml;
    opens com.mycompany.metamorphosis.controller to javafx.fxml;
    opens com.mycompany.metamorphosis.model to javafx.base;

    exports com.mycompany.metamorphosis;
}
