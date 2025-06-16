package karaoke.poprock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class MainApp extends Application {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double screenWidth = screenSize.getWidth();
    private double screenHeight = screenSize.getHeight();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/login/index.fxml"));
        Scene splashScene = new Scene(splashLoader.load());
        stage.setScene(splashScene);
        stage.setMaximized(true); // Auto max sesuai layar
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
