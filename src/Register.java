import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.awt.*;

public class Register extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField fieldUsername;
    private JTextField fieldPassword;
    private JTextField fieldConfirm;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Register frame = new Register();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Register() {
        setTitle("Register");
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

        JLabel lblTitle = new JLabel("Register");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(330, 23, 150, 30);
        panel.add(lblTitle);

        JLabel lblUsername = new JLabel("Nama Pengguna:");
        lblUsername.setBounds(192, 86, 100, 32);
        panel.add(lblUsername);

        JLabel lblPassword = new JLabel("Kata Sandi:");
        lblPassword.setBounds(192, 164, 80, 32);
        panel.add(lblPassword);

        JLabel lblConfirm = new JLabel("Konfirmasi Sandi:");
        lblConfirm.setBounds(192, 238, 120, 32);
        panel.add(lblConfirm);

        fieldUsername = new JTextField();
        fieldUsername.setBounds(465, 89, 178, 26);
        panel.add(fieldUsername);
        fieldUsername.setColumns(10);

        fieldPassword = new JTextField();
        fieldPassword.setBounds(465, 167, 178, 26);
        panel.add(fieldPassword);
        fieldPassword.setColumns(10);

        fieldConfirm = new JTextField();
        fieldConfirm.setBounds(465, 241, 178, 26);
        panel.add(fieldConfirm);
        fieldConfirm.setColumns(10);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(325, 312, 100, 30);
        panel.add(btnRegister);

     // aksi ketika tombol regis ditekan
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = fieldUsername.getText();
                String password = fieldPassword.getText();
                String confirmPassword = fieldConfirm.getText();

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Konfirmasi sandi tidak cocok.");
                } else {
                    boolean success = UserDatabase.register(username, password); //panggil method regis dari userdatabase
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Registrasi berhasil! Silakan login.");
                        new Login().setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Nama pengguna sudah terdaftar.");
                    }
                }
            }
        });
        
        JButton btnToLogin = new JButton("Sudah punya akun? Login");
        btnToLogin.setBounds(261, 353, 226, 25);
        panel.add(btnToLogin);

        btnToLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Login().setVisible(true);
                dispose(); // tutup halaman register
            }
        });

    }
}
