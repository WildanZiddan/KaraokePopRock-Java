module javafx.karaoke_poprock {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;

    opens karaoke.poprock to javafx.fxml;
    opens karaoke.poprock.controller to javafx.fxml;
    exports karaoke.poprock;
}