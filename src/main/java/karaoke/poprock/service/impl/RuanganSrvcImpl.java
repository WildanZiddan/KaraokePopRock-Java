package karaoke.poprock.service.impl;

import karaoke.poprock.connection.DBConnect;
import karaoke.poprock.model.TopMasterData;
import karaoke.poprock.util.SwalAlert;
import karaoke.poprock.util.Validation;
import karaoke.poprock.model.Ruangan;
import karaoke.poprock.service.RuanganSrvc;

import java.sql.*;
import java.util.*;

import static javafx.scene.control.Alert.AlertType.*;

public class RuanganSrvcImpl implements RuanganSrvc {
    DBConnect connect = new DBConnect();
    Validation v = new Validation();
    SwalAlert swal = new SwalAlert();

    @Override
    public Ruangan resultRuangan(ResultSet rs) throws SQLException {
        return new Ruangan(
                v.getInt(rs, "id_Ruangan"),
                v.getString(rs, "nama_Ruangan"),
                v.getString(rs, "tipe_Ruangan"),
                v.getString(rs, "kapasistas"),
                v.getDouble(rs, "tarif_perjam"),
                v.getString(rs, "status")
        );
    }

    @Override
    public List<Ruangan> getAllData() {
        return getAllData(null, null, null, "id_Ruangan", "ASC");
    }

    @Override
    public List<Ruangan> getAllData(String search, String status, String tipe, String sortColumn, String sortOrder) {
        List<Ruangan> ruanganList = new ArrayList<>();
        try {
            String query = "EXEC sp_getListRuangan ?, ?, ?, ?, ?";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setString(1, search);
            connect.pstat.setString(2, status);
            connect.pstat.setString(3, tipe);
            connect.pstat.setString(4, sortColumn);
            connect.pstat.setString(5, sortOrder);

            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                ruanganList.add(resultRuangan(connect.result));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return ruanganList;
    }

    @Override
    public List<TopMasterData> getTop5Ruangan(Integer tahun, Integer bulan) {
        List<TopMasterData> ruanganList = new ArrayList<>();
        try{
            String query = "SELECT * FROM fn_Top5RuanganPalingSeringPakai(?, ?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, tahun);
            connect.pstat.setInt(2, bulan);
            connect.result = connect.pstat.executeQuery();
            while (connect.result.next()) {
                ruanganList.add(new TopMasterData(
                        connect.result.getString("tipe_Ruangan"),
                        connect.result.getInt("jumlah_transaksi"),
                        connect.result.getDouble("persen")
                ));
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return ruanganList;
    }

    @Override
    public Ruangan getDataById(Integer id) {
        try {
            String query = "SELECT * FROM sp_getByIdRuangan(?)";
            connect.pstat = connect.conn.prepareStatement(query);
            connect.pstat.setInt(1, id);
            connect.result = connect.pstat.executeQuery();
            if(connect.result.next()) {
                return resultRuangan(connect.result);
            }
            connect.result.close();
            connect.pstat.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveData(Ruangan ruangan) {
        try{
            String query = "{call sp_createRuangan(?, ?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setString(1, ruangan.getNama_ruangan());
            connect.cstat.setString(2, ruangan.getTipe_ruangan());
            connect.cstat.setString(3, ruangan.getKapasitas_ruangan());
            connect.cstat.setDouble(4, ruangan.getTarif_ruangan());
            connect.cstat.execute();

            connect.cstat.close();
            swal.showAlert(INFORMATION,"Berhasil", "Data berhasil ditambahkan!",false);
            return true;
        }catch (SQLException e){
            swal.showAlert(ERROR,"Gagal", e.getMessage(),false);
            return false;
        }
    }

    @Override
    public boolean updateData(Ruangan ruangan) {
        try{
            String query = "{call sp_updateRuangan(?, ?, ?, ?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setString(1, ruangan.getNama_ruangan());
            connect.cstat.setString(2, ruangan.getTipe_ruangan());
            connect.cstat.setString(3, ruangan.getKapasitas_ruangan());
            connect.cstat.setString(4, ruangan.getStatus());
            connect.cstat.execute();

            connect.cstat.close();
            swal.showAlert(INFORMATION,"Berhasil", "Data berhasil diubah!",false);
            return true;
        }catch (SQLException e){
            System.out.println(e.getMessage()   );
            swal.showAlert(ERROR,"Gagal", e.getMessage(),false);
            return false;
        }
    }

    @Override
    public boolean setStatus(Integer id) {
        try {
            String query = "{call sp_setStatusRuangan(?)}";
            connect.cstat = connect.conn.prepareCall(query);
            connect.cstat.setInt(1, id);
            connect.cstat.executeUpdate();

            connect.cstat.close();
            swal.showAlert(INFORMATION,"Berhasil", "Status berhasil diubah",false);
            return true;
        }catch (SQLException e){
            swal.showAlert(ERROR,"Gagal", e.getMessage(),false);
            return false;
        }
    }
}
