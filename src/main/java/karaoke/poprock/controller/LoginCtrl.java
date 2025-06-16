package karaoke.poprock.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import karaoke.poprock.component.Menu;
import karaoke.poprock.model.Karyawan;
import karaoke.poprock.service.impl.*;
import karaoke.poprock.util.Session;
import karaoke.poprock.util.SwalAlert;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class LoginCtrl {
    @FXML
    private AnchorPane login_container;
    @FXML
     TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    PasswordField pfPassword;
    @FXML
    private FontIcon eyeIcon;

    private boolean show;
    SwalAlert alert = new SwalAlert();
    KaryawanSrvcImpl karyawanSrvc = new KaryawanSrvcImpl();

//    @FXML
//    public void initialize() {
//        tfPassword.textProperty().bindBidirectional(pfPassword.textProperty());
//    }

    @FXML
    public void showPw() {
        show = !show;
        tfPassword.textProperty().bindBidirectional(pfPassword.textProperty());
        tfPassword.setVisible(show);
        tfPassword.setManaged(show);

        pfPassword.setVisible(!show);
        pfPassword.setManaged(!show);
        eyeIcon.setIconLiteral(show ? "fas-eye-slash" : "fas-eye");
    }

    public void handleSubmit(ActionEvent e){
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        if(username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()){
            alert.showAlert(AlertType.WARNING, "WARNING", "Inputan tidak boleh ada yang kosong", false);
        }
        try {
            Karyawan kry = karyawanSrvc.auth(username,password);
            if(kry != null){
                Session.setCurrentUser(kry);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/main.fxml"));
            Parent root = loader.load();

            AppCtrl app = loader.getController();

            Menu.CreateMenu(app.vbMenu, app);

            login_container.getChildren().removeAll();
            login_container.getChildren().setAll(root);
        }catch (RuntimeException ex){
            //alert.showAlert(AlertType.ERROR, "ERROR", ex.getMessage(), false);
        } catch (IOException ex) {
            //throw new RuntimeException(ex);
        }
    }

    public void handleExit(ActionEvent e){
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }
}
