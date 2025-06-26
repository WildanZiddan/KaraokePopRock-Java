package karaoke.poprock.model;

public class Ruangan {
    private int id_ruangan;
    private String nama_ruangan;
    private String tipe_ruangan;
    private String kapasitas_ruangan;
    private Double tarif_ruangan;
    private String status;

    public Ruangan() {

    }

    public Ruangan(int id_rg, String nama_rg, String tipe_rg, String kapasitas_rg, Double tarif_rg, String status) {
        this.id_ruangan = id_rg;
        this.nama_ruangan = nama_rg;
        this.tipe_ruangan = tipe_rg;
        this.kapasitas_ruangan = kapasitas_rg;
        this.tarif_ruangan = tarif_rg;
        this.status = status;
    }

    public Ruangan(String nama_rg, String tipe_rg, String kapasitas_rg, Double tarif_rg) {
        this.nama_ruangan = nama_rg;
        this.tipe_ruangan = tipe_rg;
        this.kapasitas_ruangan = kapasitas_rg;
        this.tarif_ruangan = tarif_rg;
    }

    public Ruangan(int id, String nama, String tipe, String kapasitas, Double trfRuangan) {
        this.id_ruangan = id;
        this.nama_ruangan = nama;
        this.tipe_ruangan = tipe;
        this.kapasitas_ruangan = kapasitas;
        this.tarif_ruangan = trfRuangan;
    }


    public int getId_ruangan() {
        return id_ruangan;
    }

    public void setId_ruangan(int id_ruangan) {
        this.id_ruangan = id_ruangan;
    }

    public String getNama_ruangan() {
        return nama_ruangan;
    }

    public void setNama_ruangan(String nama_ruangan) {
        this.nama_ruangan = nama_ruangan;
    }

    public String getTipe_ruangan() {
        return tipe_ruangan;
    }

    public void setTipe_ruangan(String tipe_ruangan) {
        this.tipe_ruangan = tipe_ruangan;
    }

    public String getKapasitas_ruangan() {
        return kapasitas_ruangan;
    }

    public void setKapasitas_ruangan(String kapasitas_ruangan) {
        this.kapasitas_ruangan = kapasitas_ruangan;
    }

    public Double getTarif_ruangan() {
        return tarif_ruangan;
    }

    public void setTarif_ruangan(Double tarif_ruangan) {
        this.tarif_ruangan = tarif_ruangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
