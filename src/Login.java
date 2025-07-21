import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.*;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField fieldUsername;
    private JTextField fieldPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Login() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 803, 490);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(214, 233, 250));
        panel.setBounds(10, 25, 769, 417);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblTitle = new JLabel("Login");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(350, 23, 150, 30);
        panel.add(lblTitle);

        JLabel lblUsername = new JLabel("Nama Pengguna:");
        lblUsername.setBounds(230, 107, 100, 32);
        panel.add(lblUsername);

        JLabel lblPassword = new JLabel("Kata Sandi:");
        lblPassword.setBounds(234, 206, 80, 32);
        panel.add(lblPassword);

        fieldUsername = new JTextField();
        fieldUsername.setBounds(441, 110, 178, 26);
        panel.add(fieldUsername);
        fieldUsername.setColumns(10);

        fieldPassword = new JTextField();
        fieldPassword.setBounds(441, 209, 178, 26);
        panel.add(fieldPassword);
        fieldPassword.setColumns(10);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(334, 322, 100, 30);
        panel.add(btnLogin);

        JButton btnRegister = new JButton("Belum punya akun? Register");
        btnRegister.setBounds(261, 375, 226, 25);
        panel.add(btnRegister); 

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Register().setVisible(true);
                dispose(); // tutup halaman login
            }
        });
        
        // aksi ketika tombol login ditekan
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = fieldPassword.getText();

                boolean success = UserDatabase.login(username, password);
                if (success) {
                    // ambil id user dari username
                    int userId = UserDatabase.getUserId(username);
                    
                    // set session user
                    Session.setCurrentUserId(userId);

                    JOptionPane.showMessageDialog(null, "Login berhasil. Selamat datang, " + username + "!");
                    
                    // buka planner
                    new PlannerTugasPintar().setVisible(true);
                    dispose(); // tutup jendela login
                } else {
                    JOptionPane.showMessageDialog(null, "Username atau password salah.");
                }
            }
        });
    }
}
