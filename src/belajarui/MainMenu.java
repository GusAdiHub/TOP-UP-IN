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

public class MainMenu extends javax.swing.JFrame {
    
private void setupCustomUI() {
        // 1. Atur Posisi Layout Utama
        getContentPane().remove(MainCanvas);
        getContentPane().add(Sidebar, BorderLayout.WEST);
        getContentPane().add(MainCanvas, BorderLayout.CENTER);

        // 2. Setup HEADER
        Header.setLayout(new BorderLayout());
        Header.setBackground(new Color(41, 128, 185)); 
        Header.setPreferredSize(new Dimension(800, 110));
        
        // Ganti "logo.png" sama nama file gambarmu
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/belajarui/images/logo1.png"));
        JLabel bannerLabel = new JLabel(logoIcon, SwingConstants.CENTER);
        Header.add(bannerLabel, BorderLayout.CENTER);

        // 3. Setup SIDEBAR
        Sidebar.setLayout(new BorderLayout());
        Sidebar.setBackground(new Color(44, 62, 80)); 
        Sidebar.setPreferredSize(new Dimension(180, 0));

        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        menuPanel.setBackground(new Color(44, 62, 80));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12)); 

        JButton btnMobile = new JButton("Mobile Game");
        JButton btnPc = new JButton("PC Game");
        JButton btnAbout = new JButton("About Us");

        menuPanel.add(btnMobile);
        menuPanel.add(btnPc);
        menuPanel.add(btnAbout);

        JButton btnLogout = new JButton("Log Out");
        btnLogout.setBackground(new Color(192, 57, 43)); 
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogout.setPreferredSize(new Dimension(180, 45));
        
        // --- FUNGSI TOMBOL LOGOUT ---
        btnLogout.addActionListener(e -> {
            // Nampilin pesan konfirmasi dulu (opsional, biar makin keren)
            int pilihan = JOptionPane.showConfirmDialog(this, "Yakin mau log out bang?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
            if (pilihan == JOptionPane.YES_OPTION) {
                LoginMenu menuLogin = new LoginMenu(); // Buka frame Login
                menuLogin.setVisible(true); // Tunjukin layarnya
                dispose(); // Tutup frame MainMenu ini
            }
        });
        // -----------------------------

        Sidebar.add(menuPanel, BorderLayout.NORTH);
        Sidebar.add(btnLogout, BorderLayout.SOUTH);

        // 4. Setup MAIN CANVAS pakai CardLayout biar bisa gonta-ganti layar
        CardLayout cardLayout = new CardLayout();
        MainCanvas.setLayout(cardLayout); 
        MainCanvas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // -- Bikin Isi Layar 1: Mobile Game
        JPanel panelMobile = createGamePanel(new String[]{"Mobile Legends", "Honor of Kings", "Free Fire"});
        
        // -- Bikin Isi Layar 2: PC Game (Valorant, PUBG, LoL)
        JPanel panelPc = createGamePanel(new String[]{"Valorant", "Point Blank", "League of Legends"});

        // -- Bikin Isi Layar 3: About Us
        JPanel panelAbout = new JPanel();
        panelAbout.setLayout(new BoxLayout(panelAbout, BoxLayout.Y_AXIS)); // Layout numpuk ke bawah
        panelAbout.setBackground(Color.WHITE);
        
        JLabel lblIntro = new JLabel("Web/Aplikasi ini dibuat oleh:");
        lblIntro.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblIntro.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelAbout.add(Box.createRigidArea(new Dimension(0, 50))); // Jarak dari atas
        panelAbout.add(lblIntro);
        panelAbout.add(Box.createRigidArea(new Dimension(0, 30))); // Jarak antar teks

        // Template nama anggota kelompok
        String[] templateKelompok = {
            "1. [Isi Nama Lengkap] - NIM: [NIM INSTIKI]",
            "2. [Isi Nama Lengkap] - NIM: [NIM INSTIKI]",
            "3. [Isi Nama Lengkap] - NIM: [NIM INSTIKI]"
        };

        for (String teks : templateKelompok) {
            JLabel lblNama = new JLabel(teks);
            lblNama.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelAbout.add(lblNama);
            panelAbout.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        // 5. Masukin semua layar tadi ke MainCanvas
        MainCanvas.add(panelMobile, "LAYAR_MOBILE");
        MainCanvas.add(panelPc, "LAYAR_PC");
        MainCanvas.add(panelAbout, "LAYAR_ABOUT");

        // 6. FUNGSI KLIK MENU DI SIDEBAR
        btnMobile.addActionListener(e -> cardLayout.show(MainCanvas, "LAYAR_MOBILE"));
        btnPc.addActionListener(e -> cardLayout.show(MainCanvas, "LAYAR_PC"));
        btnAbout.addActionListener(e -> cardLayout.show(MainCanvas, "LAYAR_ABOUT"));

        // Refresh UI
        revalidate();
        repaint();
    }

    // METHOD TAMBAHAN: Biar gampang nyetak 3 card nyamping tanpa nulis kodenya berulang kali
    private JPanel createGamePanel(String[] listGame) {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 20)); 
        panel.setBackground(new Color(236, 240, 241));

        for (int i = 0; i < 3; i++) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            
            // Judul Game
            JLabel lblTitle = new JLabel(listGame[i], SwingConstants.CENTER);
            lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(12, 5, 12, 5));
            card.add(lblTitle, BorderLayout.NORTH);

// --- BAGIAN PASANG GAMBAR GAME ---
            JPanel imgPanel = new JPanel(new BorderLayout());
            imgPanel.setBackground(new Color(220, 224, 230)); 
            
            // Nama file gambar disamain aja sama nama game di array biar gampang, 
            // misal: "Valorant.jpg", "Mobile Legends.jpg" (perhatiin huruf besar kecilnya dan spasinya)
            String namaFileGambar = "/belajarui/images/" + listGame[i] + ".png"; 
            
            // Coba ambil gambarnya dari package images
            java.net.URL imgURL = getClass().getResource(namaFileGambar);
            JLabel lblImg;
            if (imgURL != null) {
                // Kalau gambar ketemu, ubah ukurannya biar pas sama kotak (Lebar 150, Tinggi 180)
                ImageIcon iconOriginal = new ImageIcon(imgURL);
                Image imgResized = iconOriginal.getImage().getScaledInstance(350, 600, Image.SCALE_SMOOTH);
                ImageIcon iconResized = new ImageIcon(imgResized);
                lblImg = new JLabel(iconResized, SwingConstants.CENTER);
            } else {
                // Kalau gak nemu gambar, nampilin teks dummy aja
                lblImg = new JLabel("[ FOTO " + listGame[i].toUpperCase() + " ]", SwingConstants.CENTER);
                lblImg.setForeground(Color.GRAY);
            }
            
            imgPanel.add(lblImg, BorderLayout.CENTER);
            card.add(imgPanel, BorderLayout.CENTER);

            // Area Tombol Top-up di bawah
            JPanel bottomCardPanel = new JPanel(new GridLayout(1, 1)); 
            bottomCardPanel.setBackground(Color.WHITE);
            bottomCardPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            
            JButton btnAction = new JButton("Top-up");
            btnAction.setBackground(new Color(52, 152, 219)); 
            btnAction.setForeground(Color.WHITE);
            btnAction.setFont(new Font("SansSerif", Font.BOLD, 14));

            // --- BAGIAN YANG DITAMBAHIN BIAR NGE-LINK KE TOPUPMENU ---
            final String namaGamePilihan = listGame[i]; 
            
            btnAction.addActionListener(e -> {
                TopUpMenu menuTopUp = new TopUpMenu(); // Buka frame TopUpMenu
                menuTopUp.bukaHalamanGame(namaGamePilihan); // Langsung arahin ke game yang dipilih
                menuTopUp.setVisible(true); // Tunjukin layarnya
                dispose(); // Tutup frame MainMenu ini
            });
            // --------------------------------------------------------

            bottomCardPanel.add(btnAction);
            card.add(bottomCardPanel, BorderLayout.SOUTH);

            panel.add(card);
        }
        return panel;
    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainMenu.class.getName());

    /**
     * Creates new form MainMenu
     */
    public MainMenu() {
        initComponents();
        setupCustomUI();
        this.setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Header = new javax.swing.JPanel();
        MainCanvas = new javax.swing.JPanel();
        Sidebar = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 529, Short.MAX_VALUE)
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(Header, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout SidebarLayout = new javax.swing.GroupLayout(Sidebar);
        Sidebar.setLayout(SidebarLayout);
        SidebarLayout.setHorizontalGroup(
            SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        SidebarLayout.setVerticalGroup(
            SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout MainCanvasLayout = new javax.swing.GroupLayout(MainCanvas);
        MainCanvas.setLayout(MainCanvasLayout);
        MainCanvasLayout.setHorizontalGroup(
            MainCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainCanvasLayout.createSequentialGroup()
                .addComponent(Sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 429, Short.MAX_VALUE))
        );
        MainCanvasLayout.setVerticalGroup(
            MainCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainCanvasLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        java.awt.EventQueue.invokeLater(() -> new MainMenu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Header;
    private javax.swing.JPanel MainCanvas;
    private javax.swing.JPanel Sidebar;
    // End of variables declaration//GEN-END:variables
}
