package karaoke.poprock.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
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
import karaoke.poprock.model.Karyawan;
import karaoke.poprock.model.Ruangan;
import karaoke.poprock.service.impl.*;
import karaoke.poprock.util.*;

import java.io.IOException;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class KaryawanCtrl extends EventListenerIndex {
    @FXML
    private HBox itemHBox;
    @FXML private Label id_lbl, nama_lbl, notelp_lbl, role_lbl ,username_lbl, status_lbl;
    @FXML private Button btnEdit, btnHapus;
    @FXML
    TextField tfSearch;
    @FXML
    ComboBox<String> cbFilterStatus, cbFilterRole;

    @FXML
    public Button btnTambahData;

    @FXML
    ListView<Karyawan> viewKaryawan;

    public static KaryawanSrvcImpl karyawanSrvc = new KaryawanSrvcImpl();
    AppCtrl app = AppCtrl.getInstance();

    public KaryawanCtrl() {}

    public void initialize() {
        handleClick();
        setList();
        loadData(null,"Aktif", null, "id_karyawan", "ASC");
    }

    private void setList() {
        viewKaryawan.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Karyawan karyawan, boolean empty) {
                super.updateItem(karyawan, empty);
                if (empty || karyawan == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label idLbl = new Label(String.valueOf(karyawan.getId()));
                    idLbl.setFont(Font.font("Impact", 12));
                    idLbl.setTextFill(Color.WHITE);
                    idLbl.setPrefWidth(60);
                    HBox.setMargin(idLbl, new Insets(0, 0, 0, 30));

                    Label namaLbl = new Label(karyawan.getNama());
                    namaLbl.setFont(Font.font("Impact", 12));
                    namaLbl.setTextFill(Color.WHITE);
                    namaLbl.setPrefWidth(100);

                    Label notelpLbl = new Label(karyawan.getNoTelepon());
                    notelpLbl.setFont(Font.font("Impact", 12));
                    notelpLbl.setTextFill(Color.WHITE);
                    notelpLbl.setPrefWidth(80);

                    Label usernameLbl = new Label(karyawan.getUsername());
                    usernameLbl.setFont(Font.font("Impact", 12));
                    usernameLbl.setTextFill(Color.WHITE);
                    usernameLbl.setPrefWidth(80);
                    HBox.setMargin(usernameLbl, new Insets(0, 0, 0, 60));

                    Label roleLbl = new Label(karyawan.getRole());
                    roleLbl.setFont(Font.font("Impact", 12));
                    roleLbl.setTextFill(Color.WHITE);
                    roleLbl.setPrefWidth(80);
                    HBox.setMargin(roleLbl, new Insets(0, 0, 0, 60));

                    Label statusLbl = new Label(karyawan.getStatus());
                    statusLbl.setFont(Font.font("Impact", 12));
                    statusLbl.setTextFill(Color.WHITE);
                    statusLbl.setPrefWidth(80);

                    Button editBtn = new Button("✏");
                    Button hapusBtn = new Button("🗑");

                    String currentStatus = karyawan.getStatus();
                    boolean isAktif = "Aktif".equals(currentStatus);

                    editBtn.setStyle("-fx-background-color: orange;");
                    editBtn.setCursor(Cursor.HAND);
                    hapusBtn.setCursor(Cursor.HAND);
                    hapusBtn.setStyle("-fx-background-color: " + (isAktif ? "red" : "green")+";");
                    editBtn.setOnAction(e -> loadSubPage("edit", karyawan.getId()));
                    if(currentStatus.equals("Tidak Aktif")) {
                        editBtn.setVisible(false);
                        hapusBtn.setAlignment(Pos.CENTER);
                    }
                    hapusBtn.setOnAction(e -> {
                        String actionText = isAktif ? "menonaktifkan" : "mengaktifkan";
                        boolean confirmed = new SwalAlert().showAlert(
                                CONFIRMATION,
                                "Konfirmasi",
                                "Apakah anda yakin ingin " + actionText + " Karyawan dengan nama: " + nama_lbl.getText() + "?",
                                true
                        );
                        if (confirmed) {
                            karyawanSrvc.setStatus(karyawan.getId());
                            //loadData(search, status, sortColumn, sortOrdersearch, status, tipe, sortColumn, sortOrder);
                        }
                    });


                    HBox.setMargin(editBtn, new Insets(0, 0, 0, 400));

                    HBox row = new HBox(10, idLbl, namaLbl, notelpLbl, roleLbl, usernameLbl, statusLbl, editBtn, hapusBtn);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setStyle("-fx-background-color: #413277; -fx-background-radius: 15; -fx-padding: 10;");

                    setGraphic(row);
                    setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                }
            }
        });
    }

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
                loader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/master_karyawan/create.fxml"));
                loader.setController(new KaryawanCreateCtrl());
            } else if ("edit".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/master_karyawan/edit.fxml"));
                KaryawanEditCtrl controller = new KaryawanEditCtrl(); // Buat controller
                controller.setId(id);
                loader.setController(controller);
            } else {
                loader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/master_karyawan/index.fxml"));
            }

            pane = loader.load();
            app.getContentPane().getChildren().clear();
            app.getContentPane().getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void loadData(String search, String status, String role, String sortColumn, String sortOrder) {
        List<Karyawan> karyawanList = karyawanSrvc.getAllData(search, status, role, sortColumn, sortOrder);
        ObservableList<Karyawan> data = FXCollections.observableArrayList(karyawanList);

        viewKaryawan.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                id_lbl.setText(String.valueOf(newVal.getId()));
                nama_lbl.setText(String.valueOf(newVal.getNama()));
                notelp_lbl.setText(String.valueOf(newVal.getNoTelepon()));
                role_lbl.setText(String.valueOf(newVal.getRole()));
                username_lbl.setText(String.valueOf(newVal.getUsername()));
                status_lbl.setText(String.valueOf(newVal.getStatus()));
            }
        });
        id_lbl.setFont(Font.font("Impact", 12));
        nama_lbl.setFont(Font.font("Impact", 12));
        notelp_lbl.setFont(Font.font("Impact", 12));
        role_lbl.setFont(Font.font("Impact", 12));;
        username_lbl.setFont(Font.font("Impact", 12));
        status_lbl.setFont(Font.font("Impact", 12));
        viewKaryawan.setItems(data);
        viewKaryawan.refresh();
    }

//    @Override
    public void handleSearch() {
        String search = tfSearch.getText();
        String status = cbFilterStatus.getSelectionModel().getSelectedItem();
        if (status == null || status.isBlank()) status = null;
        String role = cbFilterRole.getSelectionModel().getSelectedItem();
        if (role == null || role.isBlank()) role = null;
        loadData(search,status,role,"id_karyawan","ASC");
    }

    @Override
    public void handleClear() {
        cbFilterStatus.setValue("");
        cbFilterRole.setValue("");
        tfSearch.clear();
        loadData(null, null, null, "id_karyawan", "ASC");

    }

    public static class KaryawanCreateCtrl extends EventListenerCreate {
        @FXML
        ComboBox<String> cbRole;
        @FXML
        TextField tfNama, tfNoTelp, tfUsername, tfPassword;
        @FXML
        Button btnKembaliR;

        Validation v = new Validation();
        KaryawanCtrl karyawanCtrl = new KaryawanCtrl();

        @FXML
        public void initialize() {
            v.setLetters(tfNama);
            v.setNumbers(tfNoTelp);
            handleClickBack();
        }

        @Override
        public void handleClear() {
        }

        @Override
        public void handleAddData(ActionEvent e) {
            String nama = tfNama.getText();
            String noTelepon = tfNoTelp.getText();
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            String posisi = cbRole.getValue();
            Karyawan kry = new Karyawan(nama, noTelepon, username, password, posisi);
            if(karyawanSrvc.saveData(kry)){
                karyawanCtrl.loadSubPage("index",null);
            }
        }

        @Override
        public void handleBack() {
            super.handleBack();
        }
        public void handleClickBack() {
            btnKembaliR.setOnAction(event ->{
                karyawanCtrl.loadSubPage("index", null);
            });
        }
    }

    public static class KaryawanEditCtrl extends EventListenerUpdate{
        @FXML
        ComboBox<String> cbRole;
        @FXML
        TextField tfNama, tfNoTelp, tfUsername,tfPassword;
        @FXML
                Button btnKembaliR;

        Validation v = new Validation();
        KaryawanCtrl karyawanCtrl = new KaryawanCtrl();
        private Integer id;
        private String currentPassword;

        public void setId(Integer id) {
            this.id = id;
        }

        @FXML
        public void initialize() {
            loadData();
            v.setNumbers(tfNoTelp);
            v.setLetters(tfNama);
            handleClickBack();
        }

        @Override
        public void loadData() {
            if(id!=null){
                Karyawan kry = karyawanSrvc.getDataById(id);
                if(kry!=null){
                    tfNama.setText(kry.getNama());
                    tfNoTelp.setText(kry.getNoTelepon());
                    tfUsername.setText(kry.getUsername());
                    tfPassword.setText(kry.getPassword());
                    cbRole.setValue(kry.getRole());
                }
            }
        }

        @Override
        public void handleClear() {
//            tfNama.setText("");
//            tfNoTelp.setText("");
//            tfUsername.setText("");
//            cbRole.setValue(null);
//            cbRole.setPromptText("Pilih Posisi");
        }

        @Override
        public void handleUpdateData(ActionEvent e) {
            String nama = tfNama.getText();
            String noTelepon = tfNoTelp.getText();
            String username = tfUsername.getText();
            String newPassword = tfPassword.getText();
            String password = newPassword.isEmpty() ? null : newPassword;
            String posisi = cbRole.getValue();
            System.out.println(currentPassword);
            System.out.println(password);
            Karyawan kry = new Karyawan(nama, noTelepon, username, password, posisi, id);
            if(karyawanSrvc.updateData(kry)){
                karyawanCtrl.loadSubPage("index",null);
            }
        }


        @Override
        public void handleBack() {
            super.handleBack();
        }
        public void handleClickBack() {
            btnKembaliR.setOnAction(event ->{
                karyawanCtrl.loadSubPage("index", null);
            });
        }
    }
}
