import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PlannerTugasPintar extends JFrame {
    private JTextField fieldJudul, fieldDeskripsi, fieldDeadline;
    private JComboBox<String> comboPrioritas;
    private JComboBox<String> filterPrioritas, filterStatus;
    private JPanel panelDaftar;
    private TugasDAO dao;

    public PlannerTugasPintar() {
        dao = new TugasDAO();
        dao.cekPengingatDeadline();

        setTitle("Planner Tugas Pintar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(214, 233, 250));

        JLabel labelJudul = new JLabel("Planner Tugas Pintar");
        labelJudul.setFont(new Font("Arial", Font.BOLD, 24));
        labelJudul.setForeground(new Color(0, 102, 204));
        labelJudul.setBounds(320, 10, 300, 30);
        getContentPane().add(labelJudul);

        JLabel label1 = new JLabel("Judul:");
        label1.setBounds(30, 51, 80, 25);
        getContentPane().add(label1);

        fieldJudul = new JTextField();
        fieldJudul.setBounds(180, 51, 200, 25);
        getContentPane().add(fieldJudul);

        JLabel label2 = new JLabel("Deskripsi:");
        label2.setBounds(411, 51, 80, 25);
        getContentPane().add(label2);

        fieldDeskripsi = new JTextField();
        fieldDeskripsi.setBounds(502, 51, 200, 25);
        getContentPane().add(fieldDeskripsi);

        JLabel label3 = new JLabel("Deadline (yyyy-MM-dd):");
        label3.setBounds(30, 100, 160, 25);
        getContentPane().add(label3);

        fieldDeadline = new JTextField();
        fieldDeadline.setBounds(180, 100, 200, 25);
        getContentPane().add(fieldDeadline);

        JLabel label4 = new JLabel("Prioritas:");
        label4.setBounds(411, 100, 70, 25);
        getContentPane().add(label4);

        comboPrioritas = new JComboBox<>(new String[]{"Tinggi", "Sedang", "Rendah"});
        comboPrioritas.setBounds(502, 100, 200, 25);
        getContentPane().add(comboPrioritas);

        JButton btnTambah = new JButton("Tambah");
        btnTambah.setBounds(750, 60, 100, 65);
        getContentPane().add(btnTambah);
        btnTambah.addActionListener(e -> tambahTugas());

        // Filter ComboBox
        JLabel labelFilterPrioritas = new JLabel("Filter Prioritas:");
        labelFilterPrioritas.setBounds(30, 150, 100, 25);
        getContentPane().add(labelFilterPrioritas);

        filterPrioritas = new JComboBox<>(new String[]{"Semua", "Tinggi", "Sedang", "Rendah"});
        filterPrioritas.setBounds(130, 150, 120, 25);
        getContentPane().add(filterPrioritas);

        JLabel labelFilterStatus = new JLabel("Filter Status:");
        labelFilterStatus.setBounds(270, 150, 100, 25);
        getContentPane().add(labelFilterStatus);

        filterStatus = new JComboBox<>(new String[]{"Semua", "Selesai", "Belum Selesai"});
        filterStatus.setBounds(360, 150, 130, 25);
        getContentPane().add(filterStatus);

        filterPrioritas.addActionListener(e -> tampilkanTugas());
        filterStatus.addActionListener(e -> tampilkanTugas());

        // panel daftar tugas
        panelDaftar = new JPanel();
        panelDaftar.setBackground(new Color(214, 233, 250));
        panelDaftar.setLayout(new BoxLayout(panelDaftar, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(panelDaftar);
        scroll.setBounds(30, 190, 820, 400);
        getContentPane().add(scroll);

        tampilkanTugas();
    }

    private void tambahTugas() {
        String judul = fieldJudul.getText();
        String deskripsi = fieldDeskripsi.getText();
        String deadline = fieldDeadline.getText();
        String prioritas = (String) comboPrioritas.getSelectedItem();

        if (judul.isEmpty() || deadline.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Judul dan deadline wajib diisi.");
            return;
        }

        Tugas tugas = new Tugas(judul, deskripsi, deadline, prioritas, false);
        dao.tambahTugas(tugas);
        tampilkanTugas();

        // reset input
        fieldJudul.setText("");
        fieldDeskripsi.setText("");
        fieldDeadline.setText("");
        comboPrioritas.setSelectedIndex(0);
    }

    private void tampilkanTugas() {
        panelDaftar.removeAll();

        String prioritasDipilih = (String) filterPrioritas.getSelectedItem();
        String statusDipilih = (String) filterStatus.getSelectedItem();

        List<Tugas> list = dao.getAllTugas();

        for (Tugas t : list) {
            // filter prioritas
            if (!prioritasDipilih.equals("Semua") && !t.getPrioritas().equalsIgnoreCase(prioritasDipilih)) {
                continue;
            }

            // filter status
            if (statusDipilih.equals("Selesai") && !t.isSelesai()) continue;
            if (statusDipilih.equals("Belum Selesai") && t.isSelesai()) continue;

            JPanel panelItem = new JPanel(new BorderLayout());
            panelItem.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            panelItem.setBackground(t.isSelesai() ? new Color(230, 255, 230) : Color.WHITE);
            panelItem.setMaximumSize(new Dimension(800, 100));

            JTextArea info = new JTextArea(
                "📌 Judul: " + t.getJudul() + "   (Prioritas: " + t.getPrioritas() + ")\n" +
                "📅 Deadline: " + t.getDeadline() + "\n" +
                "📝 " + t.getDeskripsi() + "\n" +
                "Status: " + (t.isSelesai() ? "✅ Sudah Selesai" : "❌ Belum Selesai")
            );
            info.setEditable(false);
            info.setBackground(panelItem.getBackground());
            panelItem.add(info, BorderLayout.CENTER);

            JPanel panelTombol = new JPanel();
            JButton btnSelesai = new JButton("Selesai");
            JButton btnHapus = new JButton("Hapus");

            btnSelesai.setEnabled(!t.isSelesai());

            btnSelesai.addActionListener(e -> {
                dao.updateStatus(t.getId(), true);
                tampilkanTugas();
            });

            btnHapus.addActionListener(e -> {
                dao.hapusTugas(t.getId());
                tampilkanTugas();
            });

            panelTombol.add(btnSelesai);
            panelTombol.add(btnHapus);
            panelItem.add(panelTombol, BorderLayout.SOUTH);

            panelDaftar.add(panelItem);
            panelDaftar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        panelDaftar.revalidate();
        panelDaftar.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlannerTugasPintar().setVisible(true));
    }
}
