package karaoke.poprock.component;

import karaoke.poprock.controller.DashboardCtrl;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import karaoke.poprock.controller.AppCtrl;
import karaoke.poprock.util.Session;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    public static void CreateMenu(VBox menu, AppCtrl appCtrl){
        menu.getChildren().clear();

        ImageView userIcon = new ImageView(new Image(Menu.class.getResource("/karaoke/poprock/assets/icons/user.png").toExternalForm()));
        userIcon.setFitHeight(36);
        userIcon.setFitWidth(36);

        Label nameLabel = new Label(Session.getCurrentUser().getNama());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label roleLabel = new Label(Session.getCurrentUser().getRole());
        roleLabel.setStyle("-fx-text-fill: #CCCCCC; -fx-font-size: 13px;");

        VBox textBox = new VBox(2, nameLabel, roleLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox userInfo = new HBox(10, userIcon, textBox);
        userInfo.setAlignment(Pos.CENTER_LEFT);
        userInfo.setStyle("-fx-padding: 20 0 20 20;");
        menu.getChildren().add(userInfo);

        List<MenuItem> menus = MenuList.getMenuForRole(Session.getCurrentUser().getRole());
        Button firstButton = null;

        for(MenuItem item : menus){
            String iconName = item.title().toLowerCase().replace(" ", "_") + ".png";
            ImageView icon;
            try {
                Image iconImage = new Image(Menu.class.getResource("/karaoke/poprock/assets/icons/" + iconName).toExternalForm());
                icon = new ImageView(iconImage);
                icon.setFitHeight(100);
                icon.setFitWidth(100);
            } catch (Exception e) {
                icon = new ImageView();
            }

            Button btn = new Button("  " + item.title(), icon);
            btn.setGraphicTextGap(10);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-font-size: 15px; -fx-padding: 10 0 10 20; -fx-text-fill: #FFFFFF;");
            btn.setCursor(Cursor.HAND);

            btn.addEventHandler(MouseEvent.MOUSE_ENTERED, ev -> btn.setStyle("-fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 15px; -fx-padding: 10 0 10 20; -fx-text-fill: #413277;"));
            btn.addEventHandler(MouseEvent.MOUSE_EXITED, ev -> {
                if (!btn.getStyle().contains("#FFFFFF; -fx-background-color: #FFFFFF;")) {
                    btn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-font-size: 15px; -fx-padding: 10 0 10 20; -fx-text-fill: #FFFFFF;");
                }
            });

            btn.setOnAction(e -> {
                if (item.title().equalsIgnoreCase("Logout")) {
                    Session.setCurrentUser(null);
                    appCtrl.loadLoginPage();
                } else {
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
                btn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-font-size: 15px; -fx-padding: 10 0 10 20; -fx-text-fill: #FFFFFF;");
            }
        }
        activeButton.setStyle("-fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 15px; -fx-padding: 10 0 10 20; -fx-text-fill: #413277;");
    }

    record MenuItem(String title, String fxmlPath, Object controller) {
        public MenuItem(String title, String fxmlPath) {
            this(title, fxmlPath, null);
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
                case "Kasir" -> new DashboardCtrl.DashboardKasirCtrl();
                default -> new DashboardCtrl();
            };
            list.add(new MenuItem("Beranda", "/karaoke/poprock/views/dashboard/"+posisi.toLowerCase()+".fxml", controller));

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
