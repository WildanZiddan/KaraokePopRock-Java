package karaoke.poprock.connection;

import java.sql.*;

public class DBConnect {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;
    public CallableStatement cstat;

    public DBConnect() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Db_karaoke_poprock;user=Wyzord;password=wyzord2006;encrypt=false;trustServerCertificate=true;";
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
        } catch (Exception e) {
            System.out.println("Error saat connect database: " + e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        DBConnect db = new DBConnect();
//        if(db != null) {
//            System.out.println("Connected to database");
//        }
//        else {
//            System.out.println("Failed to connect to database");
//        }
//    }
}
