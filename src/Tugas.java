import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class Tugas {
    private int id;
    private String judul, deskripsi, deadline, prioritas;
    private boolean selesai;

    // konstruktor untuk menambahkan tugas baru
    public Tugas(String judul, String deskripsi, String deadline, String prioritas, boolean selesai) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.deadline = deadline;
        this.prioritas = prioritas;
        this.selesai = selesai;
    }

 // konstruktor untuk data tugas yang sudah ada (diambil dari database)
    public Tugas(int id, String judul, String deskripsi, String deadline, String prioritas, boolean selesai) {
        this(judul, deskripsi, deadline, prioritas, selesai); // panggil konstruktor sebelumnya
        this.id = id;
    }

    // mengambil nilai dari masing-masing atribut
    public int getId() { return id; }
    public String getJudul() { return judul; }
    public String getDeskripsi() { return deskripsi; }
    public String getDeadline() { return deadline; }
    public String getPrioritas() { return prioritas; }
    public boolean isSelesai() { return selesai; }

    // mengubah nilai atribut tertentu
    public void setId(int id) { this.id = id; }
    public void setSelesai(boolean selesai) { this.selesai = selesai; }
}

// Data Access Object (DAO) untuk akses database

class TugasDAO {
    private Connection conn;

    // konstruktor untuk membuka koneksi ke database
    public TugasDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = Database.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // mengambil semua data tugas milik user yang sedang login
    public List<Tugas> getAllTugas() {
        List<Tugas> list = new ArrayList<>();
        String query = "SELECT * FROM tugas WHERE id_user = ? ORDER BY deadline ASC";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, Session.getCurrentUserId()); // ambil tugas user yang login
            ResultSet rs = ps.executeQuery();

            // hasil query ditambahkan ke list
            while (rs.next()) {
                Tugas tugas = new Tugas(
                    rs.getInt("id_tugas"),
                    rs.getString("judul"),
                    rs.getString("deskripsi"),
                    rs.getString("deadline"),
                    rs.getString("prioritas"),
                    rs.getBoolean("selesai")
                );
                list.add(tugas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // menambahkan tugas baru ke database
    public void tambahTugas(Tugas tugas) {
        String query = "INSERT INTO tugas (judul, deskripsi, deadline, prioritas, selesai, id_user) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, tugas.getJudul());
            ps.setString(2, tugas.getDeskripsi());
            ps.setString(3, tugas.getDeadline());
            ps.setString(4, tugas.getPrioritas());
            ps.setBoolean(5, tugas.isSelesai());
            ps.setInt(6, Session.getCurrentUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // menghapus tugas berdasarkan id
    public void hapusTugas(int id) {
        String query = "DELETE FROM tugas WHERE id_tugas = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // memperbarui status selesai atau belum selesai
    public void updateStatus(int id, boolean selesai) {
        String query = "UPDATE tugas SET selesai = ? WHERE id_tugas = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBoolean(1, selesai);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method pengingat tugas mendekati deadline
    public void cekPengingatDeadline() {
        String query = "SELECT judul, deadline FROM tugas WHERE id_user = ? AND selesai = 0";

        //menjalankan query untuk mengambil data tugas milik user yang login
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, Session.getCurrentUserId());
            ResultSet rs = ps.executeQuery();

            LocalDate hariIni = LocalDate.now();

            // membaca setiap tugas dari hasil query
            while (rs.next()) {
                String judul = rs.getString("judul");
                LocalDate deadline = LocalDate.parse(rs.getString("deadline")); 

                long sisaHari = ChronoUnit.DAYS.between(hariIni, deadline);

                // jika deadline tinggal 1 hari lagi, tampilkan peringatan
                if (sisaHari == 1) {
                    JOptionPane.showMessageDialog(null, "Tugas \"" + judul + "\" jatuh tempo BESOK!",
                        "Peringatan Deadline", JOptionPane.WARNING_MESSAGE);
                }
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
