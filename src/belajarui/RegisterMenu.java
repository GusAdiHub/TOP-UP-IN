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
public class RegisterMenu extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RegisterMenu.class.getName());

    private void setupCustomUI() {
        MainCanvas.setLayout(new GridBagLayout());
        MainCanvas.setBackground(new Color(236, 240, 241));

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); 

         // --- BAGIAN LOGO STORE YANG BARU ---
        JLabel lblLogo;
        // Panggil gambar logonya (sesuaiin nama filenya ya, misal logo1.png)
        java.net.URL logoURL = getClass().getResource("/belajarui/images/logo.png");
        
        if (logoURL != null) {
            ImageIcon iconOriginal = new ImageIcon(logoURL);
            // Biar gak kegedean di form login, kita atur ukurannya (misal Lebar 200, Tinggi 60)
            Image imgResized = iconOriginal.getImage().getScaledInstance(200, 60, Image.SCALE_SMOOTH);
            lblLogo = new JLabel(new ImageIcon(imgResized), SwingConstants.CENTER);
        } else {
            // Kalau gambar gak nemu, balikin ke teks biasa
            lblLogo = new JLabel("TOP-UP IN", SwingConstants.CENTER);
            lblLogo.setFont(new Font("SansSerif", Font.BOLD, 28));
            lblLogo.setForeground(new Color(41, 128, 185));
        }
        
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblLogo);
        
        // INPUT USERNAME
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblUser);
        
        JTextField txtUser = new JTextField(20);
        txtUser.setMaximumSize(new Dimension(250, 35));
        txtUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(txtUser);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // INPUT PASSWORD
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblPass);
        
        JPasswordField txtPass = new JPasswordField(20);
        txtPass.setMaximumSize(new Dimension(250, 35));
        txtPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(txtPass);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // INPUT NAMA LENGKAP
        JLabel lblNama = new JLabel("Nama Lengkap");
        lblNama.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblNama);
        
        JTextField txtNama = new JTextField(20);
        txtNama.setMaximumSize(new Dimension(250, 35));
        txtNama.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(txtNama);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        
        //  INPUT EMAIL 
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblEmail);
        
        JTextField txtEmail = new JTextField(20);
        txtEmail.setMaximumSize(new Dimension(250, 35));
        txtEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(txtEmail);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // INPUT NOMOR HP
        JLabel lblHp = new JLabel("Nomor HP");
        lblHp.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblHp.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(lblHp);
        
        JTextField txtHp = new JTextField(20);
        txtHp.setMaximumSize(new Dimension(250, 35));
        txtHp.setAlignmentX(Component.CENTER_ALIGNMENT);
        formCard.add(txtHp);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // --- PROSES TOMBOL DAFTAR (INSERT TO DATABASE) ---
        JButton btnRegister = new JButton("DAFTAR");
        btnRegister.setBackground(new Color(52, 152, 219));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegister.setMaximumSize(new Dimension(250, 40));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setFocusPainted(false);
        
        
        btnRegister.addActionListener(e -> {
            // Tarik semua data dari textfield UI
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword()).trim();
            String namaLengkap = txtNama.getText().trim();
            String email = txtEmail.getText().trim();
            String nomorHp = txtHp.getText().trim();
            
            // Validasi 1: Cegah kolom kosong
            if (username.equals("") || password.equals("") || namaLengkap.equals("") || email.equals("") || nomorHp.equals("")) {
                JOptionPane.showMessageDialog(this, "Semua data wajib diisi ya bro, jangan dikosongin!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validasi 2: Email harus mengandung karakter '@'
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Format email salah, bro! Wajib ada karakter '@'.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtEmail.requestFocus(); // Biar kursor langsung fokus ke textfield email
                return;
            }
            // ⭐ Validasi 3 : Nomor HP wajib angka semua
            if (!nomorHp.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Nomor HP harus berupa angka semua, bro! Jangan masukin huruf.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtHp.requestFocus();
                return;
            }
            // Validasi 4: Cegah nomor HP kepanjangan (Sesuai VARCHAR(13) di DB lu)
            if (nomorHp.length() > 13) {
                JOptionPane.showMessageDialog(this, "Nomor HP kepanjangan, cuy! Maksimal 13 angka.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtHp.requestFocus();
                return;
            }
            
            // Eksekusi Simpan ke MySQL
            try {
                Connection conn = Koneksi.getconnection(); // Memanggil class koneksi lu
                String sql = "INSERT INTO tb_user (username, password, nama_lengkap, email, nomor_hp) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                
                ps.setString(1, username);
                ps.setString(2, password); // Sementara simpan plain text sesuai skema login awal lu
                ps.setString(3, namaLengkap);
                ps.setString(4, email);
                ps.setString(5, nomorHp);
                
                int rowsInserted = ps.executeUpdate();
                
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Akun berhasil dibuat! Silakan Login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Lempar ke menu login
                    LoginMenu menuLogin = new LoginMenu();
                    menuLogin.setVisible(true);
                    dispose();
                }
                
            } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
                // Trigger otomatis kalau Username UNIQUE-nya tabrakan di DB
                JOptionPane.showMessageDialog(this, "Username '" + username + "' udah dipake orang lain, bro! Cari nama lain.", "Registrasi Gagal", JOptionPane.ERROR_MESSAGE);
                txtUser.requestFocus();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal registrasi karena error database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        formCard.add(btnRegister);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // TEKS KLIK BALIK KE LOGIN
        JLabel lblLogin = new JLabel("<html><u>Sudah punya akun? Login di sini</u></html>");
        lblLogin.setForeground(new Color(41, 128, 185));
        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginMenu menuLogin = new LoginMenu();
                menuLogin.setVisible(true);
                dispose();
            }
        });
        
        formCard.add(lblLogin);
        MainCanvas.add(formCard);
    }

    /**
     * Creates new form RegisterMenu
     */
    public RegisterMenu() {
        initComponents();
        setupCustomUI();
        this.setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null); // Biar ke tengah layar
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainCanvas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout MainCanvasLayout = new javax.swing.GroupLayout(MainCanvas);
        MainCanvas.setLayout(MainCanvasLayout);
        MainCanvasLayout.setHorizontalGroup(
            MainCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        MainCanvasLayout.setVerticalGroup(
            MainCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        getContentPane().add(MainCanvas, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new RegisterMenu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainCanvas;
    // End of variables declaration//GEN-END:variables
}
