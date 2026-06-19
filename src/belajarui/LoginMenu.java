/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package belajarui;

/**
 *
 * @author ASUS
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginMenu extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(LoginMenu.class.getName());

    private void setupCustomUI() {
        MainCanvas.setLayout(new GridBagLayout());
        MainCanvas.setBackground(new Color(236, 240, 241));

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // --- BAGIAN LOGO STORE YANG BARU ---
        JLabel lblLogo;
        java.net.URL logoURL = getClass().getResource("/belajarui/images/logo.png");

        if (logoURL != null) {
            ImageIcon iconOriginal = new ImageIcon(logoURL);
            Image imgResized = iconOriginal.getImage().getScaledInstance(200, 60, Image.SCALE_SMOOTH);
            lblLogo = new JLabel(new ImageIcon(imgResized), SwingConstants.CENTER);
        } else {
            lblLogo = new JLabel("TOP-UP IN", SwingConstants.CENTER);
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblLogo.setForeground(new Color(41, 128, 185));
        }

        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblLogo);

        JLabel lblSub = new JLabel("Login ke Akun Kamu", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblSub);

        formCard.add(Box.createRigidArea(new Dimension(0, 30)));

        // INPUT USERNAME
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblUser);

        JTextField txtUser = new JTextField(20);
        txtUser.setMaximumSize(new Dimension(250, 35));
        txtUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(txtUser);

        formCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // INPUT PASSWORD
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblPass);

        JPasswordField txtPass = new JPasswordField(20);
        txtPass.setMaximumSize(new Dimension(250, 35));
        txtPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(txtPass);

        formCard.add(Box.createRigidArea(new Dimension(0, 25)));

        // TOMBOL LOGIN
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setMaximumSize(new Dimension(250, 40));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setFocusPainted(false);

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());

            if (user.equals("") || pass.equals("")) {
                JOptionPane.showMessageDialog(this, "Username dan Password harus diisi ya bang!", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Connection conn = Koneksi.getconnection();
                String sql = "SELECT * FROM tb_user WHERE username = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, user);
                ps.setString(2, pass);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int idUser = rs.getInt("id_user");
                    String namaUser = rs.getString("nama_lengkap");
                    String role = rs.getString("role");

                    JOptionPane.showMessageDialog(this, "Login Berhasil!\nHalo, " + namaUser + " (" + role + ")!",
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);

                    if (role.equalsIgnoreCase("admin")) {
                        DashboardAdmin menuAdmin = new DashboardAdmin();
                        menuAdmin.setVisible(true);
                    } else {
                        // 🔥 KOREKSI: idUser sekarang dioper masuk ke constructor MainMenu
                        MainMenu menuUtama = new MainMenu(idUser);
                        menuUtama.setVisible(true);
                    }

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Username atau Password salah bang!", "Login Gagal",
                            JOptionPane.ERROR_MESSAGE);
                }

                rs.close();
                ps.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal koneksi pas login: " + ex.getMessage(), "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        formCard.add(btnLogin);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // TEKS KLIK BUAT REGIS
        JLabel lblRegis = new JLabel("<html><u>Belum punya akun? Daftar di sini</u></html>");
        lblRegis.setForeground(new Color(41, 128, 185));
        lblRegis.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegis.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRegis.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterMenu menuRegis = new RegisterMenu();
                menuRegis.setVisible(true);
                dispose();
            }
        });

        formCard.add(lblRegis);
        MainCanvas.add(formCard);
    }

    public LoginMenu() {
        initComponents();
        setupCustomUI();
        this.setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainCanvas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout MainCanvasLayout = new javax.swing.GroupLayout(MainCanvas);
        MainCanvas.setLayout(MainCanvasLayout);
        MainCanvasLayout.setHorizontalGroup(
                MainCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE));
        MainCanvasLayout.setVerticalGroup(
                MainCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));

        getContentPane().add(MainCanvas, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new LoginMenu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainCanvas;
    // End of variables declaration//GEN-END:variables
}