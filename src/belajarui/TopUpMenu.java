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

public class TopUpMenu extends javax.swing.JFrame {
    
private void setupCustomUI() {
        // 1. SETUP SIDEBAR (Gambar & Tombol Back aja)
        Sidebar.setLayout(new BorderLayout()); 
        Sidebar.setBackground(new Color(44, 62, 80));
        Sidebar.setPreferredSize(new Dimension(180, 0));

        // -- Area Gambar di Atas --
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(52, 73, 94));
        imagePanel.setPreferredSize(new Dimension(180, 180));
        
        // --- BAGIAN MANGGIL LOGO DI SIDEBAR ---
        JLabel lblGambar;
        java.net.URL logoURL = getClass().getResource("/belajarui/images/logo.png"); // Ganti nama file logomu di sini
        
        if (logoURL != null) {
            ImageIcon iconOriginal = new ImageIcon(logoURL);
            // Resize gambarnya biar pas di kotak 180x180 (di sini aku set 150x150 biar ada jeda/margin dikit)
            Image imgResized = iconOriginal.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblGambar = new JLabel(new ImageIcon(imgResized), SwingConstants.CENTER);
        } else {
            lblGambar = new JLabel("<html><center>[ GAMBAR / LOGO ]<br>Taruh Sini</center></html>", SwingConstants.CENTER);
            lblGambar.setForeground(Color.LIGHT_GRAY);
        }
        // --------------------------------------
        
        imagePanel.add(lblGambar, BorderLayout.CENTER);
        Sidebar.add(imagePanel, BorderLayout.NORTH);

        // -- Tombol Kembali di Bawah --
        JButton btnBack = new JButton("Kembali");
        btnBack.setBackground(new Color(192, 57, 43));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBack.setPreferredSize(new Dimension(180, 50));
        btnBack.setFocusPainted(false);
        
        // Fungsi biar pas diklik balik ke Main Menu
        btnBack.addActionListener(e -> {
            MainMenu menuUtama = new MainMenu(); // Buka lagi MainMenu
            menuUtama.setVisible(true);
            dispose(); // Tutup layar TopUpMenu ini
        });

        Sidebar.add(btnBack, BorderLayout.SOUTH);

        // 2. SETUP MAIN CANVAS (Isi Halaman Top Up Tiap Game)
        CardLayout cardLayout = new CardLayout();
        MainCanvas.setLayout(cardLayout);

        // Data nominal masing-masing game
        String[] nomValo = {"475", "1000", "2050", "3650", "5350", "11000"};
        String[] nomPb = {"1200", "2400", "6000", "12000", "24000", "36000"};
        String[] nomLol = {"475", "1000", "2050", "3650", "5350", "11000"};
        String[] nomMl = {"5", "12", "50", "70", "140", "284", "344", "600"};
        String[] nomHok = {"16", "80", "240", "400", "560", "800"};
        String[] nomFf = {"5", "12", "50", "70", "140", "355", "720", "1450"};

        // Masukin cetakan UI ke Main Canvas
        // Formatnya: Judul Game, Pake 2 Kolom ID?, Label ID, Contoh Template, Mata Uang, Array Nominal
        MainCanvas.add(createTopUpForm("Valorant", false, "Riot ID", "Contoh: Westbourne#SEA", "VP", nomValo), "Valorant");
        MainCanvas.add(createTopUpForm("Point Blank", false, "Zeppeto ID", "Contoh: pointblank123", "Cash", nomPb), "Point Blank");
        MainCanvas.add(createTopUpForm("League of Legends", false, "Riot ID", "Contoh: Faker#KR1", "RP", nomLol), "League of Legends");
        
        // Khusus ML pake true karena ada User ID + Zone ID
        MainCanvas.add(createTopUpForm("Mobile Legends", true, "User ID", "Contoh: 12345678 (1234)", "Diamonds", nomMl), "Mobile Legends");
        
        MainCanvas.add(createTopUpForm("Honor of Kings", false, "Player ID", "Contoh: 1234567890", "Tokens", nomHok), "Honor of Kings");
        MainCanvas.add(createTopUpForm("Free Fire", false, "Player ID", "Contoh: 531234567", "Diamonds", nomFf), "Free Fire");

        revalidate();
        repaint();
    }

    // =========================================================================
    // METHOD CETAKAN: Pabrik pembuatan form top up
    // =========================================================================
    private JScrollPane createTopUpForm(String gameName, boolean isDoubleId, String idLabelName, String exampleText, String currency, String[] nominals) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(236, 240, 241));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Judul Form
        JLabel lblTitle = new JLabel("Top Up " + gameName);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        container.add(lblTitle);
        container.add(Box.createRigidArea(new Dimension(0, 20))); // Spasi

        // --- SECTION 1: INPUT ID ---
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.Y_AXIS));
        idPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "1. Masukkan " + idLabelName, 0, 0, new Font("SansSerif", Font.BOLD, 14)));
        idPanel.setBackground(Color.WHITE);
        idPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputRow.setBackground(Color.WHITE);
        
        if (isDoubleId) {
            // Khusus ML ada User ID dan Zone ID
            inputRow.add(new JTextField(15));
            inputRow.add(new JLabel(" ( "));
            inputRow.add(new JTextField(8));
            inputRow.add(new JLabel(" ) "));
        } else {
            // Game lain cuma 1 kolom ID
            inputRow.add(new JTextField(25));
        }
        idPanel.add(inputRow);

        JLabel lblSubtext = new JLabel(exampleText);
        lblSubtext.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblSubtext.setForeground(Color.DARK_GRAY);
        lblSubtext.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        idPanel.add(lblSubtext);

        container.add(idPanel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SECTION 2: PILIH NOMINAL ---
        JPanel nomPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        nomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "2. Pilih Nominal (" + currency + ")", 0, 0, new Font("SansSerif", Font.BOLD, 14)));
        nomPanel.setBackground(Color.WHITE);
        nomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String nom : nominals) {
            JToggleButton btnNom = new JToggleButton("<html><center><b>" + nom + "</b><br>" + currency + "</center></html>");
            btnNom.setBackground(Color.WHITE);
            btnNom.setPreferredSize(new Dimension(100, 60));
            nomPanel.add(btnNom);
        }
        container.add(nomPanel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SECTION 3: METODE PEMBAYARAN ---
        JPanel payPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        payPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "3. Pilih Metode Pembayaran", 0, 0, new Font("SansSerif", Font.BOLD, 14)));
        payPanel.setBackground(Color.WHITE);
        payPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] payments = {"QRIS", "DANA", "GoPay", "OVO"};
        for (String pay : payments) {
            JToggleButton btnPay = new JToggleButton(pay);
            btnPay.setBackground(Color.WHITE);
            btnPay.setPreferredSize(new Dimension(100, 50));
            payPanel.add(btnPay);
        }
        container.add(payPanel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SECTION 4: TOMBOL SUBMIT ---
        JButton btnSubmit = new JButton("Beli Sekarang");
        btnSubmit.setBackground(new Color(46, 204, 113)); 
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSubmit.setPreferredSize(new Dimension(200, 45));
        btnSubmit.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        container.add(btnSubmit);

        // Masukin semua ke dalam ScrollPane biar bisa discroll
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        scrollPane.setBorder(null);

        return scrollPane;
    }

    // =========================================================================
    // METHOD PENERIMA PESAN: Dipanggil sama MainMenu pas tombol Top-up diklik
    // =========================================================================
    public void bukaHalamanGame(String namaGame) {
        CardLayout cl = (CardLayout) MainCanvas.getLayout();
        cl.show(MainCanvas, namaGame);
    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TopUpMenu.class.getName());

    /**
     * Creates new form TopUpMenu
     */
    public TopUpMenu() {
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

        Sidebar = new javax.swing.JPanel();
        MainCanvas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout SidebarLayout = new javax.swing.GroupLayout(Sidebar);
        Sidebar.setLayout(SidebarLayout);
        SidebarLayout.setHorizontalGroup(
            SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        SidebarLayout.setVerticalGroup(
            SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        getContentPane().add(Sidebar, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout MainCanvasLayout = new javax.swing.GroupLayout(MainCanvas);
        MainCanvas.setLayout(MainCanvasLayout);
        MainCanvasLayout.setHorizontalGroup(
            MainCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
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
        java.awt.EventQueue.invokeLater(() -> new TopUpMenu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainCanvas;
    private javax.swing.JPanel Sidebar;
    // End of variables declaration//GEN-END:variables
}
