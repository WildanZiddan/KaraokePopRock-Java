package karaoke.poprock.controller;

import karaoke.poprock.component.Chart;
import karaoke.poprock.connection.DBConnect;
import karaoke.poprock.model.SummaryTabel;
import karaoke.poprock.model.TopMasterData;
import karaoke.poprock.service.impl.*;
import karaoke.poprock.util.Session;
import karaoke.poprock.util.SwalAlert;
import karaoke.poprock.util.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardCtrl {
    @FXML
    Text txtTotalRuangan, txtTotalMenu, txtTotalMember;

    DBConnect connect = new DBConnect();

    AppCtrl app = AppCtrl.getInstance();

    public void initialize(){
//        List<SummaryTabel> summaryTabelList = getSummaryTabel();
//
//        txtTotalRuangan.setText(summaryTabelList.get(0).getTotal().toString());
//        txtTotalMenu.setText(summaryTabelList.get(1).getTotal().toString());
//        txtTotalMember.setText(summaryTabelList.get(2).getTotal().toString());
//
//        System.out.println(summaryTabelList.get(3).getTotal().toString());
    }

    public void loadSubPage() {
        try {
            FXMLLoader loader = null;
            Parent pane;
            String role = Session.getCurrentUser().getRole();
            System.out.println(role);
            if ("Kasir".equals(role)) {
                System.out.println("KASIR");
                loader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/dashboard/kasir.fxml"));
                DashboardKasirCtrl controller = new DashboardKasirCtrl();
//                loader.setController(new KaryawanCtrl.KaryawanCreateCtrl());
                loader.setController(controller);
            }
            else if ("Manajer".equals(role)) {
                System.out.println("MANAGER");
                loader = new FXMLLoader(getClass().getResource("/himma/pendidikan/views/dashboard/manajer.fxml"));
                DashboardManagerCtrl controller = new DashboardManagerCtrl(); // Buat controller
                loader.setController(controller);
            }
            else{
                System.out.println("ADMIN");
                loader = new FXMLLoader(getClass().getResource("/karaoke/poprock/views/dashboard/index.fxml"));
            }
            pane = loader.load();
            app.getContentPane().getChildren().clear();
            app.getContentPane().getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception lainnya: " + e.getMessage());
        }
    }

    public List<SummaryTabel> getSummaryTabel() {
        List<SummaryTabel> summaryTabelList = new ArrayList<>();
        try{
            String query = "SELECT * FROM fn_GetDataMasterSummary()";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                summaryTabelList.add(new SummaryTabel(
                        connect.result.getString("Kategori"),
                        connect.result.getInt("JumlahAktif"),
                        connect.result.getInt("JumlahTidakAktif"),
                        connect.result.getInt("Total"),
                        connect.result.getDouble("PersenAktif"),
                        connect.result.getDouble("PersenTidakAktif")
                ));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return summaryTabelList;
    }

    public static class DashboardManagerCtrl{
        @FXML
        PieChart pcTopRuangan, pcTopMenu;
        @FXML
        VBox vbTopRuangan, vbTopMenu;
        @FXML
        BarChart<String, Double> bcPendapatan, bcTransaksi;
        @FXML
        DatePicker dpManager;
        @FXML
        Text txtTotalPendapatan, txtTotalPenyewaan;

        DBConnect connect = new DBConnect();

        KaryawanSrvcImpl karyawanSrvc = new KaryawanSrvcImpl();
        SwalAlert alert = new SwalAlert();

        @FXML
        public void initialize() {
            LocalDate today = LocalDate.now();
            int tahun = today.getYear();
            int bulan = today.getMonthValue();

            //PenyewaanPlaystation.RekapTransaksiBulanan rekapTransaksi = getRekapPenyewaanBulanan(tahun, bulan);

//            List<TopMasterData> top5Ps = playStationSrvc.getTop5PlayStation(tahun, bulan);
//            Chart.pieChart(pcTopPs, top5Ps, "Top 5 Playstation", vbTopPs);
//            List<TopMasterData> jenisPs = jenisPlayStationSrvc.getTop5JenisPlayStation(tahun, bulan);
//            Chart.pieChart(pcJenisPs, jenisPs, "Top 5 Jenis Playstation", vbJenisPs);
//            List<TopMasterData> metodePembayaran = metodePembayaranSrvc.getTop5MetodePembayaran(tahun, bulan);
//            Chart.pieChart(pcPembayaran, metodePembayaran, "Top 5 Metode Pembayaran", vbPembayaran);
//            List<TopMasterData> topKaryawan = karyawanSrvc.getTop5Karyawan(tahun, bulan);
//            Chart.pieChart(pcKaryawan, topKaryawan, "Top 5 Karyawan", vbKaryawan);
//            txtTotalPenyewaan.setText(String.valueOf(rekapTransaksi.getTotalPenyewaan()));
//            txtTotalPendapatan.setText(String.valueOf(rekapTransaksi.getTotalPendapatan()));

            loadBarChart1(tahun, bulan);
            loadBarChart2(tahun, bulan);
        }

//        public PenyewaanPlaystation.RekapTransaksiBulanan getRekapPenyewaanBulanan(Integer tahun, Integer bulan) {
//            PenyewaanPlaystation.RekapTransaksiBulanan rekapPenyewaan = new PenyewaanPlaystation.RekapTransaksiBulanan();
//            try{
//                String query = "SELECT * FROM fn_RekapPenyewaanBulanan(?, ?)";
//                connect.pstat = connect.conn.prepareStatement(query);
//                connect.pstat.setInt(1, tahun);
//                connect.pstat.setInt(2, bulan);
//                connect.result = connect.pstat.executeQuery();
//                if(connect.result.next()) {
//                    return new  PenyewaanPlaystation.RekapTransaksiBulanan(
//                            connect.result.getInt("total_penyewaan"),
//                            connect.result.getDouble("total_pendapatan")
//                    );
//                }
//                connect.result.close();
//                connect.pstat.close();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//            return rekapPenyewaan;
//        }

        public void loadBarChart1(Integer tahun, Integer bulan) {
            CategoryAxis xAxis = (CategoryAxis) bcPendapatan.getXAxis();
            xAxis.setLabel("Tanggal Transaksi");

            XYChart.Series<String, Double> pendapatan = new XYChart.Series<>();
//            List<PenyewaanPlaystation.RekapPendapatanHarian> pendapatanHarian = getRekapPendapatan(tahun, bulan);
//            for (PenyewaanPlaystation.RekapPendapatanHarian re : pendapatanHarian) {
//                String hariKe = String.valueOf(re.getTanggal());
//                pendapatan.getData().add(new XYChart.Data<>(hariKe, re.getTotalPendapatan()));
//            }

            bcPendapatan.setTitle("Grafik Pendapatan Harian");
            bcPendapatan.getData().add(pendapatan);
        }

//        public List<PenyewaanPlaystation.RekapPendapatanHarian> getRekapPendapatan(Integer tahun, Integer bulan) {
//            List<PenyewaanPlaystation.RekapPendapatanHarian> rekapPendapatan = new ArrayList<>();
//            try{
//                String query = "SELECT * FROM fn_PendapatanHarian(?, ?)";
//                connect.pstat = connect.conn.prepareStatement(query);
//                connect.pstat.setInt(1, tahun);
//                connect.pstat.setInt(2, bulan);
//                connect.result = connect.pstat.executeQuery();
//                while (connect.result.next()) {
//                    rekapPendapatan.add(new PenyewaanPlaystation.RekapPendapatanHarian(
//                            connect.result.getInt("tanggal"),
//                            connect.result.getDouble("total_pendapatan")
//                    ));
//                }
//                connect.result.close();
//                connect.pstat.close();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//            return rekapPendapatan;
//        }

        public void loadBarChart2(Integer tahun, Integer bulan) {
            CategoryAxis xAxis = (CategoryAxis) bcTransaksi.getXAxis();
            xAxis.setLabel("Tanggal Transaksi");

            XYChart.Series<String, Double> transaksi = new XYChart.Series<>();
//            List<PenyewaanPlaystation.RekapTransaksiHarian> transaksiHarian = getRekapTransaksi(tahun, bulan);
//            for (PenyewaanPlaystation.RekapTransaksiHarian re : transaksiHarian) {
//                String hariKe = String.valueOf(re.getTanggal());
//                transaksi.getData().add(new XYChart.Data<>(hariKe, re.getTotalTransaksi()));
//            }

            bcTransaksi.setTitle("Grafik Transaksi Harian");
            bcTransaksi.getData().add(transaksi);
        }

//        public List<PenyewaanPlaystation.RekapTransaksiHarian> getRekapTransaksi(Integer tahun, Integer bulan) {
//            List<PenyewaanPlaystation.RekapTransaksiHarian> rekapTransaksi = new ArrayList<>();
//            try{
//                String query = "SELECT * FROM fn_TotalTransaksiHarian(?, ?)";
//                connect.pstat = connect.conn.prepareStatement(query);
//                connect.pstat.setInt(1, tahun);
//                connect.pstat.setInt(2, bulan);
//                connect.result = connect.pstat.executeQuery();
//                while (connect.result.next()) {
//                    rekapTransaksi.add(new PenyewaanPlaystation.RekapTransaksiHarian(
//                            connect.result.getInt("tanggal"),
//                            connect.result.getDouble("total_transaksi")
//                    ));
//                }
//                connect.result.close();
//                connect.pstat.close();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//            return rekapTransaksi;
//        }

        public void apply(){
            if (dpManager.getValue() == null) {
                alert.showAlert(Alert.AlertType.WARNING, "WARNING", "Tanggal harus diisi!", false);
            } else {
//                pcTopPs.getData().clear();
//                vbTopPs.getChildren().clear();
//                pcJenisPs.getData().clear();
//                vbJenisPs.getChildren().clear();
//                pcPembayaran.getData().clear();
//                vbPembayaran.getChildren().clear();
//                pcKaryawan.getData().clear();
//                vbKaryawan.getChildren().clear();
//                bcPendapatan.getData().clear();
//                bcTransaksi.getData().clear();

                LocalDate selectedDate = dpManager.getValue();
                int tahun = selectedDate.getYear();
                int bulan = selectedDate.getMonthValue();

//                PenyewaanPlaystation.RekapTransaksiBulanan rekapTransaksi = getRekapPenyewaanBulanan(tahun, bulan);
//
//                List<TopMasterData> top5Ps = playStationSrvc.getTop5PlayStation(tahun,bulan);
//                Chart.pieChart(pcTopPs, top5Ps, "Top 5 Playstation", vbTopPs);
//                List<TopMasterData> jenisPs = jenisPlayStationSrvc.getTop5JenisPlayStation(tahun,bulan);
//                Chart.pieChart(pcJenisPs, jenisPs, "Top 5 Jenis Playstation", vbJenisPs);
//                List<TopMasterData> metodePembayaran = metodePembayaranSrvc.getTop5MetodePembayaran(tahun,bulan);
//                Chart.pieChart(pcPembayaran, metodePembayaran, "Top 5 Metode Pembayaran", vbPembayaran);
                List<TopMasterData> topKaryawan = karyawanSrvc.getTop5Karyawan(tahun,bulan);
//                Chart.pieChart(pcKaryawan, topKaryawan, "Top 5 Karyawan", vbKaryawan);
//
//                txtTotalPenyewaan.setText(String.valueOf(rekapTransaksi.getTotalPenyewaan()));
//                txtTotalPendapatan.setText(String.valueOf(rekapTransaksi.getTotalPendapatan()));

                loadBarChart1(tahun, bulan);
                loadBarChart2(tahun, bulan);
            }
        }

        public void reset(ActionEvent e){
            dpManager.setValue(null);
//            pcTopPs.getData().clear();
//            vbTopPs.getChildren().clear();
//            pcJenisPs.getData().clear();
//            vbJenisPs.getChildren().clear();
//            pcPembayaran.getData().clear();
//            vbPembayaran.getChildren().clear();
//            pcKaryawan.getData().clear();
//            vbKaryawan.getChildren().clear();
            bcPendapatan.getData().clear();
            bcTransaksi.getData().clear();

            LocalDate today = LocalDate.now();
            int tahun = today.getYear();
            int bulan = today.getMonthValue();

            //PenyewaanPlaystation.RekapTransaksiBulanan rekapTransaksi = getRekapPenyewaanBulanan(tahun, bulan);
//
//            List<TopMasterData> top5Ps = playStationSrvc.getTop5PlayStation(tahun, bulan);
//            Chart.pieChart(pcTopPs, top5Ps, "Top 5 Playstation", vbTopPs);
//
//            List<TopMasterData> jenisPs = jenisPlayStationSrvc.getTop5JenisPlayStation(tahun, bulan);
//            Chart.pieChart(pcJenisPs, jenisPs, "Top 5 Jenis Playstation", vbJenisPs);
//
//            List<TopMasterData> metodePembayaran = metodePembayaranSrvc.getTop5MetodePembayaran(tahun, bulan);
//            Chart.pieChart(pcPembayaran, metodePembayaran, "Top 5 Metode Pembayaran", vbPembayaran);

            List<TopMasterData> topKaryawan = karyawanSrvc.getTop5Karyawan(tahun, bulan);
//            Chart.pieChart(pcKaryawan, topKaryawan, "Top 5 Karyawan", vbKaryawan);
//
//            txtTotalPenyewaan.setText(String.valueOf(rekapTransaksi.getTotalPenyewaan()));
//            txtTotalPendapatan.setText(String.valueOf(rekapTransaksi.getTotalPendapatan()));

            loadBarChart1(tahun, bulan);
            loadBarChart2(tahun, bulan);
        }
    }

    public static class DashboardKasirCtrl{
        @FXML
        PieChart pcTopPs, pcPembayaran;
        @FXML
        VBox vbTopPs, vbPembayaran;
        @FXML
        Text txtTotalPendapatan, txtTotalPenyewaan;
        @FXML
        DatePicker dpKasir;

        DBConnect connect = new DBConnect();
        //PlayStationSrvcImpl playStationSrvc = new PlayStationSrvcImpl();
        //MetodePembayaranSrvcImpl metodePembayaranSrvc = new MetodePembayaranSrvcImpl();
        SwalAlert alert = new SwalAlert();

        @FXML
        public void initialize() {
            LocalDate today = LocalDate.now();
            int tahun = today.getYear();
            int bulan = today.getMonthValue();

            //PenyewaanPlaystation.RekapTransaksiBulanan rekapTransaksi = getRekapPenyewaanBulanan(tahun, bulan);
            System.out.println(tahun);
            System.out.println(bulan);

            //List<TopMasterData> top5Ps = playStationSrvc.getTop5PlayStation(tahun, bulan);
//            Chart.pieChart(pcTopPs, top5Ps, "Top 5 Playstation", vbTopPs);
//            List<TopMasterData> metodePembayaran = metodePembayaranSrvc.getTop5MetodePembayaran(tahun, bulan);
//            Chart.pieChart(pcPembayaran, metodePembayaran, "Top 5 Metode Pembayaran", vbPembayaran);
//
//            txtTotalPenyewaan.setText(String.valueOf(rekapTransaksi.getTotalPenyewaan()));
//            txtTotalPendapatan.setText(String.valueOf(rekapTransaksi.getTotalPendapatan()));
        }

//        public PenyewaanPlaystation.RekapTransaksiBulanan getRekapPenyewaanBulanan(Integer tahun, Integer bulan) {
//            PenyewaanPlaystation.RekapTransaksiBulanan rekapPenyewaan = new PenyewaanPlaystation.RekapTransaksiBulanan();
//            try{
//                String query = "SELECT * FROM fn_RekapPenyewaanBulanan(?, ?)";
//                connect.pstat = connect.conn.prepareStatement(query);
//                connect.pstat.setInt(1, tahun);
//                connect.pstat.setInt(2, bulan);
//                connect.result = connect.pstat.executeQuery();
//                if(connect.result.next()) {
//                    return new  PenyewaanPlaystation.RekapTransaksiBulanan(
//                            connect.result.getInt("total_penyewaan"),
//                            connect.result.getDouble("total_pendapatan")
//                    );
//                }
//                connect.result.close();
//                connect.pstat.close();
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//            return rekapPenyewaan;
//        }

        public void apply(){
            if (dpKasir.getValue() == null) {
                alert.showAlert(Alert.AlertType.WARNING, "WARNING", "Tanggal harus diisi!", false);
            } else {
                pcTopPs.getData().clear();
                vbTopPs.getChildren().clear();
                pcPembayaran.getData().clear();
                vbPembayaran.getChildren().clear();
                LocalDate selectedDate = dpKasir.getValue();
                int tahun = selectedDate.getYear();
                int bulan = selectedDate.getMonthValue();

//                PenyewaanPlaystation.RekapTransaksiBulanan rekapTransaksi = getRekapPenyewaanBulanan(tahun, bulan);
//
//                List<TopMasterData> top5Ps = playStationSrvc.getTop5PlayStation(tahun, bulan);
//                Chart.pieChart(pcTopPs, top5Ps, "Top 5 Playstation", vbTopPs);
//                List<TopMasterData> metodePembayaran = metodePembayaranSrvc.getTop5MetodePembayaran(tahun, bulan);
//                Chart.pieChart(pcPembayaran, metodePembayaran, "Top 5 Metode Pembayaran", vbPembayaran);
//
//                txtTotalPenyewaan.setText(String.valueOf(rekapTransaksi.getTotalPenyewaan()));
//                txtTotalPendapatan.setText(String.valueOf(rekapTransaksi.getTotalPendapatan()));
            }
        }

//        @FXML
//        public void reset(){
//            dpKasir.setValue(null);
//            pcTopPs.getData().clear();
//            vbTopPs.getChildren().clear();
//            pcPembayaran.getData().clear();
//            vbPembayaran.getChildren().clear();
//
//            LocalDate today = LocalDate.now();
//            int tahun = today.getYear();
//            int bulan = today.getMonthValue();
//
//            PenyewaanPlaystation.RekapTransaksiBulanan rekapTransaksi = getRekapPenyewaanBulanan(tahun, bulan);
//
//            List<TopMasterData> top5Ps = playStationSrvc.getTop5PlayStation(tahun, bulan);
//            Chart.pieChart(pcTopPs, top5Ps, "Top 5 Playstation", vbTopPs);
//            List<TopMasterData> metodePembayaran = metodePembayaranSrvc.getTop5MetodePembayaran(tahun, bulan);
//            Chart.pieChart(pcPembayaran, metodePembayaran, "Top 5 Metode Pembayaran", vbPembayaran);
//
//            txtTotalPenyewaan.setText(String.valueOf(rekapTransaksi.getTotalPenyewaan()));
//            txtTotalPendapatan.setText(String.valueOf(rekapTransaksi.getTotalPendapatan()));
//        }
    }
}