package karaoke.poprock.model;

public class Karyawan {
    private String id;
    private String nama;
    private String noTelepon;
    private String username;
    private String password;
    private String role;
    private String status;

    public Karyawan() {}

    public Karyawan(String id, String nama, String noTelepon, String username, String password, String role, String status) {
        this.id = id;
        this.nama = nama;
        this.noTelepon = noTelepon;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public Karyawan(String id, String nama, String noTelepon, String username, String role, String status) {
        this.id = id;
        this.nama = nama;
        this.noTelepon = noTelepon;
        this.username = username;
        this.role = role;
        this.status = status;
    }

    public Karyawan(String nama, String noTelepon, String username, String password, String role) {
        this.nama = nama;
        this.noTelepon = noTelepon;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Karyawan(String nama, String noTelepon, String username, String password, String posisi, Integer id) {
        this.nama = nama;
        this.noTelepon = noTelepon;
        this.username = username;
        this.password = password;
        this.role = posisi;
        this.id = id.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
