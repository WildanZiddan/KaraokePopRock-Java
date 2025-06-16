package karaoke.poprock.controller;

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
import karaoke.poprock.service.impl.*;
import karaoke.poprock.util.*;

import java.io.IOException;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class KaryawanCtrl extends EventListenerIndex {
    @FXML
    Button btnTambahData;
    @FXML
    Label lbActiveUser;
    @FXML
    TextField tfSearch;
    @FXML
    ComboBox<String> cbFilterStatus, cbFilterPosisi;
    @FXML
    TableView<Karyawan> tbKaryawan;
    @FXML
    TableColumn<Karyawan, Integer> clNo;
    @FXML
    TableColumn<Karyawan, Void> clAksi;
    @FXML
    TableColumn<Karyawan, String> clNama, clPosisi, clNoTelepon, clEmail, clUsername, clStatus;

    public static KaryawanSrvcImpl karyawanSrvc = new KaryawanSrvcImpl();
    AppCtrl app = AppCtrl.getInstance();

    public KaryawanCtrl() {}

    public void initialize() {
        lbActiveUser.setText(Session.getCurrentUser().getRole());
        handleClick();
        loadData(null,"Aktif", null, "id_karyawan", "ASC");
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
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_karyawan/create.fxml"));
                loader.setController(new KaryawanCreateCtrl());
            } else if ("edit".equals(page)) {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_karyawan/edit.fxml"));
                KaryawanEditCtrl controller = new KaryawanEditCtrl(); // Buat controller
                controller.setId(id);
                loader.setController(controller);
            } else {
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/master_karyawan/index.fxml"));
            }

            pane = loader.load();
            app.getContentPane().getChildren().clear();
            app.getContentPane().getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void loadData(String search, String status, String posisi, String sortColumn, String sortOrder){
        List<Karyawan> karyawanList = karyawanSrvc.getAllData(search,status, posisi, sortColumn, sortOrder);
        ObservableList<Karyawan> data = FXCollections.observableArrayList(karyawanList);

        clNo.setCellValueFactory(col -> new ReadOnlyObjectWrapper<>(tbKaryawan.getItems().indexOf(col.getValue()) + 1));
        clNama.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));
        clNoTelepon.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoTelepon()));
        clUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        clPosisi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        clStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        clAksi.setCellFactory(param -> new TableCell<>() {
            final Button btnEdit = new Button("Edit");
            final Button btnDelete = new Button("Hapus");
            final HBox pane = new HBox(btnEdit, btnDelete);
            {
                pane.setSpacing(10);
                btnEdit.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
                btnDelete.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                btnEdit.setCursor(Cursor.HAND);
                btnDelete.setCursor(Cursor.HAND);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                Karyawan karyawan = getTableView().getItems().get(getIndex());
                String nama = karyawan.getNama();
                String currentStatus = karyawan.getStatus();
                boolean isAktif = "Aktif".equals(currentStatus);
                btnDelete.setText(isAktif ? "Hapus" : "Pulihkan");
                btnDelete.setStyle("-fx-background-color: " + (isAktif ? "red" : "green") + "; -fx-text-fill: white;");
//                btnEdit.setOnAction(e -> loadSubPage("edit", karyawan.getId()));
                btnDelete.setOnAction(e -> {
                    String actionText = isAktif ? "menonaktifkan" : "mengaktifkan";
                    boolean confirmed = new SwalAlert().showAlert(
                        CONFIRMATION,
                    "Konfirmasi",
                "Apakah anda yakin ingin " + actionText + " karyawan dengan nama: " + nama + "?",
            true
                    );
                    if (confirmed) {
//                        karyawanSrvc.setStatus(karyawan.getId());
                        loadData(search,status, posisi, sortColumn, sortOrder);
                    }
                });
                setGraphic(pane);
            }
        });
        tbKaryawan.setItems(data);
    }

//    @Override
    public void handleSearch() {
        String search = tfSearch.getText();
        String status = cbFilterStatus.getSelectionModel().getSelectedItem();
        String posisi = cbFilterPosisi.getSelectionModel().getSelectedItem();
        loadData(search,status,posisi,"kry_id","ASC");
    }

    @Override
    public void handleClear() {
        cbFilterStatus.setValue("");
        cbFilterPosisi.setValue("");
    }

    public static class KaryawanCreateCtrl extends EventListenerCreate {
        @FXML
        ComboBox<String> cbPosisi;
        @FXML
        Label lbActiveUser;
        @FXML
        TextArea taAlamat;
        @FXML
        PasswordField tfPassword;
        @FXML
        TextField tfNama, tfNoTelepon, tfEmail, tfUsername;

        Validation v = new Validation();
        KaryawanCtrl karyawanCtrl = new KaryawanCtrl();

        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getRole());
            Dropdown.setDropdown(cbPosisi, List.of("Admin", "Manajer", "Kasir"));
            v.setNumbers(tfNoTelepon);
            v.setLetters(tfNama);
        }

        @Override
        public void handleClear() {
            tfNama.setText("");
            tfNoTelepon.setText("");
            tfEmail.setText("");
            tfUsername.setText("");
            tfPassword.setText("");
            taAlamat.setText("");
            cbPosisi.setValue(null);
            cbPosisi.setPromptText("Pilih Posisi");
        }

        @Override
        public void handleAddData(ActionEvent e) {
            String nama = tfNama.getText();
            String noTelepon = tfNoTelepon.getText();
            String email = tfEmail.getText();
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            String alamat = taAlamat.getText();
            String posisi = cbPosisi.getValue();
            String createdBy = Session.getCurrentUser().getNama();
            Karyawan kry = new Karyawan(nama, noTelepon, username, password, posisi);
            if(karyawanSrvc.saveData(kry)){
                karyawanCtrl.loadSubPage("index",null);
            }
        }

        @Override
        public void handleBack() {
            super.handleBack();
        }
    }

    public static class KaryawanEditCtrl extends EventListenerUpdate{
        @FXML
        ComboBox<String> cbPosisi;
        @FXML
        Label lbActiveUser;
        @FXML
        TextArea taAlamat;
        @FXML
        PasswordField tfPassword;
        @FXML
        TextField tfNama, tfNoTelepon, tfEmail, tfUsername;

        Validation v = new Validation();
        KaryawanCtrl karyawanCtrl = new KaryawanCtrl();
        private Integer id;
        private String currentPassword;

        public void setId(Integer id) {
            this.id = id;
        }

        @FXML
        public void initialize() {
            lbActiveUser.setText(Session.getCurrentUser().getRole());
            loadData();
            Dropdown.setDropdown(cbPosisi, List.of("Admin", "Manajer", "Kasir"));
            v.setNumbers(tfNoTelepon);
            v.setLetters(tfNama);
        }

        @Override
        public void loadData() {
            if(id!=null){
                Karyawan kry = karyawanSrvc.getDataById(id);
                if(kry!=null){
                    tfNama.setText(kry.getNama());
                    tfNoTelepon.setText(kry.getNoTelepon());
                    tfUsername.setText(kry.getUsername());
                    cbPosisi.setValue(kry.getRole());
                }
            }
        }

        @Override
        public void handleClear() {
            tfNama.setText("");
            tfNoTelepon.setText("");
            tfEmail.setText("");
            tfUsername.setText("");
            taAlamat.setText("");
            cbPosisi.setValue(null);
            cbPosisi.setPromptText("Pilih Posisi");
        }

        @Override
        public void handleUpdateData(ActionEvent e) {
            String nama = tfNama.getText();
            String noTelepon = tfNoTelepon.getText();
            String email = tfEmail.getText();
            String username = tfUsername.getText();
            String newPassword = tfPassword.getText();
            String password = newPassword.isEmpty() ? null : newPassword;
            String alamat = taAlamat.getText();
            String posisi = cbPosisi.getValue();
            String updatedBy = Session.getCurrentUser().getNama();
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
    }
}
