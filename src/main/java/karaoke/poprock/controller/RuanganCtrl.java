package karaoke.poprock.controller;

import javafx.application.Application;
import karaoke.poprock.component.Dropdown;
import karaoke.poprock.controller.event.EventListener.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import karaoke.poprock.model.Ruangan;
import karaoke.poprock.service.impl.*;
import karaoke.poprock.util.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class RuanganCtrl extends EventListenerIndex {
    @FXML
    private HBox itemHBox;

    @FXML
    public Label id_lbl;
    @FXML
    public Label nama_lbl;
    @FXML
    public Label tipe_lbl;
    @FXML
    public Label kapasitas_lbl;
    @FXML
    public Label tarif_lbl;

    @FXML
    private Button btnEdit;

    @FXML
    public Button btnTambahData;

    @FXML
    Button btnHapus;

    @FXML
    ListView<Ruangan> lvRuangan;

//    @FXML
//    private ListView<Ruangan, Integer> ;

    public RuanganCtrl() {}

    public static RuanganSrvcImpl ruanganSrvc = new RuanganSrvcImpl();
    AppCtrl app = AppCtrl.getInstance();
//    private DasHome dashome;

//    public void setData(Ruangan ruangan, DasHome dashome) {
//        this.ruangan = ruangan;
//        this.dashome = dashome;
//
//        id_lbl.setText(String.valueOf(ruangan.getId_ruangan()));
//        nama_lbl.setText(ruangan.getNama_ruangan());
//        tipe_lbl.setText(ruangan.getTipe_ruangan());
//        kapasitas_lbl.setText(ruangan.getKapasitas_ruangan());
//        tarif_lbl.setText(String.valueOf(ruangan.getTarif_ruangan()));
//
//        btnEdit.setOnAction(event -> handleUpdate());
//        btnHapus.setOnAction(event -> handleHapus());
//    }

//    private void handleUpdate() {
//        dashome.popUpdateRuangan(ruangan);
//    }

//    private void handleHapus() {
//        dashome.hapusRuangan(ruangan);
//    }

    public void initialize() {
        handleClick();
    }

    public void handleClick(){
        btnTambahData.setOnAction(event -> {
            loadSubPage("add",null);
        });
    }

    public void loadSubPage(String page, Integer id) {
        try {

            FXMLLoader loader;
            Parent pane;
            if ("add".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/master_ruangan/create.fxml"));
                loader.setController(new RuanganCreateCtrl());
            } else if ("edit".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/master_ruangan/edit.fxml"));
                RuanganEditCtrl controller = new RuanganEditCtrl(); // Buat controller
                controller.setId(id);
                loader.setController(controller);
            } else {
                loader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/master_ruangan/index.fxml"));
            }

            pane = loader.load();
            app.getContentPane().getChildren().clear();
            app.getContentPane().getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void handleSearch() {

    }

    @Override
    public void handleClear() {

    }

    public static class RuanganCreateCtrl extends EventListenerCreate{

        @Override
        public void handleAddData(ActionEvent e) {

        }

        @Override
        public void handleClear() {

        }
    }

    public static class RuanganEditCtrl extends EventListenerUpdate{

        Validation v = new Validation();
        RuanganCtrl ruanganCtrl = new RuanganCtrl();
        private Integer id;

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public void handleUpdateData(ActionEvent e) {

        }

        @Override
        public void loadData() {

        }

        @Override
        public void handleClear() {

        }
    }
}
