package karaoke.poprock.component;

import karaoke.poprock.controller.DashboardCtrl;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import karaoke.poprock.controller.AppCtrl;
import karaoke.poprock.util.Session;

import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static void CreateMenu(VBox menu, AppCtrl appCtrl){
        menu.getChildren().clear();

        List<MenuItem> menus = MenuList.getMenuForRole(Session.getCurrentUser().getRole());
        Button firstButton = null;

        for(MenuItem item : menus){
            Button btn = new Button(item.title());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.TOP_LEFT);
            btn.setStyle("-fx-background-color: #413277; -fx-font-weight: bold;-fx-font-size: 16px;;-fx-padding: 8 0 8 20; -fx-text-fill: #FFFFFF;");
            btn.setCursor(Cursor.HAND);
            btn.setOnAction(e -> {
                if (item.title().equalsIgnoreCase("Logout")) {
                    Session.setCurrentUser(null); // atau Session.clear(); tergantung implementasi kamu
                    appCtrl.loadLoginPage(); // method ini akan ganti scene utama ke login
                } else {
//                    appCtrl.loadPage(item.fxmlPath);
//                    setActiveButton(btn, menu);
                    try {
                        FXMLLoader loader = new FXMLLoader(Menu.class.getResource(item.fxmlPath()));
                        if (item.controller() != null) {
                            loader.setController(item.controller());
                        }
                        Parent page = loader.load();
                        appCtrl.getContentPane().getChildren().clear();
                        appCtrl.getContentPane().getChildren().add(page);
                        setActiveButton(btn, menu);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            if (firstButton == null && item.title().equalsIgnoreCase("Dashboard")) {
                firstButton = btn;
            }
            menu.getChildren().add(btn);
        }
        if (firstButton != null) {
            firstButton.fire();
        }
    }

    private static void setActiveButton(Button activeButton, VBox menu) {
        for (var node : menu.getChildren()) {
            if (node instanceof Button btn) {
                btn.setStyle("-fx-background-color: #413277; -fx-font-weight: bold; -fx-font-size: 16px;-fx-padding: 8 0 8 20; -fx-text-fill: #FFFFFF;");
            }
        }
        activeButton.setStyle("-fx-background-color: #FFFFFF; -fx-font-weight: bold;-fx-font-size: 16px;;-fx-padding: 8 0 8 20;-fx-text-fill: #413277;");
    }

    record MenuItem(String title, String fxmlPath, Object controller) {
        public MenuItem(String title, String fxmlPath) {
            this(title, fxmlPath, null); // default tanpa controller
        }
    }

    class MenuList {
        public static List<MenuItem> getMenuForRole(String role){
            List<MenuItem> list = new ArrayList<>();
            String posisi = Session.getCurrentUser().getRole();
            if(posisi.equals("Admin")){
                posisi = "index";
            }
            System.out.println(posisi.toLowerCase());
            Object controller = switch (posisi.toLowerCase()) {
                case "Manajer" -> new DashboardCtrl.DashboardManagerCtrl();
                case "Kasir" -> new DashboardCtrl.DashboardKasirCtrl(); // contoh, sesuaikan
                default -> new DashboardCtrl();
            };
            list.add(new MenuItem("Beranda",
                    "/karaoke/poprock/views/dashboard/"+posisi.toLowerCase()+".fxml", controller));

            switch (role.toLowerCase()) {
                case "admin" -> {
                    list.add(new MenuItem("Ruangan", "/karaoke/poprock/views/master_ruangan/index.fxml"));
                    list.add(new MenuItem("Menu", "/karaoke/poprock/views/master_ruangan/index.fxml"));
                    list.add(new MenuItem("Member", "/karaoke/poprock/views/master_ruangan/index.fxml"));
                    list.add(new MenuItem("Karyawan", "/karaoke/poprock/views/master_ruangan/index.fxml"));
                }
                case "kasir" -> {
                    list.add(new MenuItem("Ruangan", "/karaoke/poprock/views/master_ruangan/index.fxml"));
                    list.add(new MenuItem("Menu", "/karaoke/poprock/views/master_ruangan/index.fxml"));
                    list.add(new MenuItem("Member", "/karaoke/poprock/views/master_ruangan/index.fxml"));
                    list.add(new MenuItem("Karyawan", "/karaoke/poprock/views/master_ruangan/index.fxml"));
                    list.add(new MenuItem("Transaksi", "/karaoker/poprock/views/transaksi_penyewaan_play_station/index.fxml"));
                }
                case "manajer" -> {
                    list.add(new MenuItem("Laporan Member", "/karaoke/poprock/views/Laporan.fxml"));
                    list.add(new MenuItem("Laporan Pendapatan", "/karaoke/poprock/views/Laporan.fxml"));
                }
            }
            list.add(new MenuItem("Logout", "/karaoke/poprock/views/login/index.fxml"));
            return list;
        }
    }
}
