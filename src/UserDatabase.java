import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDatabase {

    // untuk registrasi user baru
    public static boolean register(String username, String password) {
        try (Connection conn = Database.getConnection()) {
            // cek apakah username sudah ada
            String checkSql = "SELECT * FROM user WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) return false; // username sudah dipakai

            // kalau belum dipakai, insert user baru
            String insertSql = "INSERT INTO user (username, password) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // untuk login
    public static boolean login(String username, String password) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // true kalau user ditemukan
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ambil id_user dari username (untuk session)
    public static int getUserId(String username) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT id_user FROM user WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_user");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // return 0 kalau user tidak ditemukan
    }

    //menampilkan nama pengguna berdasarkan id_user
    public static String getUsernameById(int id) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT username FROM user WHERE id_user = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
