package karaoke.poprock.service;

import karaoke.poprock.model.Karyawan;
import karaoke.poprock.model.Ruangan;
import karaoke.poprock.model.TopMasterData;

import java.sql.*;
import java.util.List;

public interface RuanganSrvc {
    Ruangan resultRuangan(ResultSet rs) throws SQLException;
    List<Ruangan> getAllData();
    List<Ruangan> getAllData(String search, String status, String tipe, String sortColumn, String sortOrder);
    List<TopMasterData> getTop5Ruangan(Integer tahun, Integer bulan);
    Ruangan getDataById(Integer id);
    boolean saveData(Ruangan ruangan);
    boolean updateData(Ruangan ruangan);
    boolean setStatus(Integer id);
}
