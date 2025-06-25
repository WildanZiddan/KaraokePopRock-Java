package karaoke.poprock.controller;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
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
import javafx.scene.paint.Color;
import javafx.geometry.Insets;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class RuanganCtrl extends EventListenerIndex {
    @FXML
    private HBox itemHBox;
    @FXML private Label id_lbl, nama_lbl, tipe_lbl, kapasitas_lbl, tarif_lbl, status_lbl;
    @FXML private Button btnEdit, btnHapus;
    @FXML
    TextField tfSearch;
    @FXML
    ComboBox<String> cbFilterStatus, cbFilterTipe;

    @FXML
    public Button btnTambahData;

    @FXML
    ListView<Ruangan> viewRuangan;

//    @FXML
//    private ListView<Ruangan, Integer> ;

    public RuanganCtrl() {}

    public static RuanganSrvcImpl ruanganSrvc = new RuanganSrvcImpl();
    AppCtrl app = AppCtrl.getInstance();
//    private DasHome dashome;

    public void initialize() {
        handleClick();
        setList();
        loadData(null, "Aktif", null, "id_Ruangan", "ASC");
    }

    private void setList() {
        viewRuangan.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Ruangan ruangan, boolean empty) {
                super.updateItem(ruangan, empty);
                if (empty || ruangan == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label idLbl = new Label(String.valueOf(ruangan.getId_ruangan()));
                    idLbl.setFont(Font.font("Impact", 12));
                    idLbl.setTextFill(Color.WHITE);
                    idLbl.setPrefWidth(60);
                    HBox.setMargin(idLbl, new Insets(0, 0, 0, 30));

                    Label namaLbl = new Label(ruangan.getNama_ruangan());
                    namaLbl.setFont(Font.font("Impact", 12));
                    namaLbl.setTextFill(Color.WHITE);
                    namaLbl.setPrefWidth(100);

                    Label tipeLbl = new Label(ruangan.getTipe_ruangan());
                    tipeLbl.setFont(Font.font("Impact", 12));
                    tipeLbl.setTextFill(Color.WHITE);
                    tipeLbl.setPrefWidth(80);

                    Label kapasitasLbl = new Label(ruangan.getKapasitas_ruangan());
                    kapasitasLbl.setFont(Font.font("Impact", 12));
                    kapasitasLbl.setTextFill(Color.WHITE);
                    kapasitasLbl.setPrefWidth(80);
                    HBox.setMargin(kapasitasLbl, new Insets(0, 0, 0, 30));

                    Label tarifLbl = new Label("Rp. " + ruangan.getTarif_ruangan());
                    tarifLbl.setFont(Font.font("Impact", 12));
                    tarifLbl.setTextFill(Color.WHITE);
                    tarifLbl.setPrefWidth(100);
                    HBox.setMargin(tarifLbl, new Insets(0, 0, 0, 30));

                    Label statusLbl = new Label(ruangan.getStatus());
                    statusLbl.setFont(Font.font("Impact", 12));
                    statusLbl.setTextFill(Color.WHITE);
                    statusLbl.setPrefWidth(80);
                    HBox.setMargin(statusLbl, new Insets(0, 0, 0, 60));

                    Button editBtn = new Button("✏");
                    Button hapusBtn = new Button("🗑");

                    HBox.setMargin(editBtn, new Insets(0, 0, 0, 400));

                    HBox row = new HBox(10, idLbl, namaLbl, tipeLbl, kapasitasLbl, tarifLbl, statusLbl, editBtn, hapusBtn);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setStyle("-fx-background-color: #413277; -fx-background-radius: 15; -fx-padding: 10;");

                    setGraphic(row);
                    setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                }
            }
        });
    }

    public void loadData(String search, String status, String tipe, String sortColumn, String sortOrder) {
        List<Ruangan> ruanganList = ruanganSrvc.getAllData(search, status, tipe, sortColumn, sortOrder);
        ObservableList<Ruangan> data = FXCollections.observableArrayList(ruanganList);

        viewRuangan.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                id_lbl.setText(String.valueOf(newVal.getId_ruangan()));
                nama_lbl.setText(newVal.getNama_ruangan());
                tipe_lbl.setText(newVal.getTipe_ruangan());
                kapasitas_lbl.setText(String.valueOf(newVal.getKapasitas_ruangan()));
                tarif_lbl.setText(String.valueOf(newVal.getTarif_ruangan()));
                status_lbl.setText(String.valueOf(newVal.getStatus()));
            }
        });
        id_lbl.setFont(Font.font("Impact", 12));
        nama_lbl.setFont(Font.font("Impact", 12));
        tipe_lbl.setFont(Font.font("Impact", 12));
        kapasitas_lbl.setFont(Font.font("Impact", 12));;
        tarif_lbl.setFont(Font.font("Impact", 12));
        status_lbl.setFont(Font.font("Impact", 12));
        viewRuangan.setItems(data);
        viewRuangan.refresh();
    //        btnEdit.setOnAction(event -> handleUpdate());
    //        btnHapus.setOnAction(event -> handleHapus());
    }

//    private void handleUpdate() {
//        dashome.popUpdateRuangan(ruangan);
//    }

//    private void handleHapus() {
//        dashome.hapusRuangan(ruangan);
//    }

    public void handleClick(){
        btnTambahData.setCursor(Cursor.HAND);
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

//    @Override
    public void handleSearch() {
        String search = tfSearch.getText();
        String status = cbFilterStatus.getValue();
        if (status == null || status.isBlank()) status = null;

        String tipe = cbFilterTipe.getValue();
        if (tipe == null || tipe.isBlank()) tipe = null;
        loadData(search,status,tipe,"id_Ruangan","ASC");
    }

    @Override
    public void handleClear() {
        cbFilterStatus.setValue("");
        cbFilterTipe.setValue("");
        tfSearch.clear();
        loadData(null, null, null, "id_Ruangan", "ASC");
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
