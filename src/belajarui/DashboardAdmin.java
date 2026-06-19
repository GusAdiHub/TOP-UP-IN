/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package belajarui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 *
 * @author ASUS
 */
public class DashboardAdmin extends javax.swing.JFrame {

  private void setupCustomUI() {
        // 1. Atur Posisi Layout Utama
        getContentPane().remove(MainCanvas);
        getContentPane().add(Sidebar, BorderLayout.WEST);
        getContentPane().add(MainCanvas, BorderLayout.CENTER);

        // 2. Setup HEADER
        Header.setLayout(new BorderLayout());
        Header.setBackground(new Color(41, 128, 185)); 
        Header.setPreferredSize(new Dimension(800, 80)); 
        
        JLabel bannerLabel = new JLabel("ADMINISTRATOR DASHBOARD", SwingConstants.CENTER);
        bannerLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        bannerLabel.setForeground(Color.WHITE);
        Header.add(bannerLabel, BorderLayout.CENTER);

        // 3. Setup SIDEBAR (Diupdate tambah logo kotak)
        Sidebar.setLayout(new BorderLayout());
        Sidebar.setBackground(new Color(44, 62, 80)); 
        Sidebar.setPreferredSize(new Dimension(200, 0));

        // -- Area Gambar/Logo di Atas --
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(52, 73, 94));
        imagePanel.setPreferredSize(new Dimension(200, 180)); // Disesuaikan sama lebar sidebar (200px)
        
        JLabel lblGambar;
        java.net.URL logoURL = getClass().getResource("/belajarui/images/logo.png"); // Nama logomu
        if (logoURL != null) {
            ImageIcon iconOriginal = new ImageIcon(logoURL);
            Image imgResized = iconOriginal.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblGambar = new JLabel(new ImageIcon(imgResized), SwingConstants.CENTER);
        } else {
            lblGambar = new JLabel("<html><center>[ LOGO ADMIN ]<br>Taruh Sini</center></html>", SwingConstants.CENTER);
            lblGambar.setForeground(Color.LIGHT_GRAY);
        }
        
        imagePanel.add(lblGambar, BorderLayout.CENTER);
        Sidebar.add(imagePanel, BorderLayout.NORTH);

        // -- Area Menu di Tengah --
        // Pakai bungkus panel (wrapper) biar tombolnya tetep rapat di atas dan gak melar
        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(new Color(44, 62, 80));

        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 10, 10)); 
        menuPanel.setBackground(new Color(44, 62, 80));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12)); 

        JButton btnReport = new JButton("Laporan (Report)");
        JButton btnMaster = new JButton("Data Master");
        JButton btnTransaksi = new JButton("Data Transaksi");

        menuPanel.add(btnReport);
        menuPanel.add(btnMaster);
        menuPanel.add(btnTransaksi);

        menuWrapper.add(menuPanel, BorderLayout.NORTH);
        Sidebar.add(menuWrapper, BorderLayout.CENTER);

        // -- Area Tombol Log Out di Bawah --
        JButton btnLogout = new JButton("Log Out");
        btnLogout.setBackground(new Color(192, 57, 43)); 
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogout.setPreferredSize(new Dimension(200, 45));
        
        Sidebar.add(btnLogout, BorderLayout.SOUTH);

        // 4. Setup MAIN CANVAS (CardLayout)
        CardLayout cardLayout = new CardLayout();
        MainCanvas.setLayout(cardLayout); 
        MainCanvas.setBackground(new Color(236, 240, 241));

        // --- BIKIN HALAMAN 1: DASHBOARD / REPORT TRANSAKSI ---
        JPanel panelReport = createReportPanel();
        
        // --- BIKIN HALAMAN 2: DATA MASTER (CRUD Placeholder) ---
        JPanel panelMaster = createCrudPanel("Kelola Data Master (Game & User)");
        
        // --- BIKIN HALAMAN 3: DATA TRANSAKSI ---
        JPanel panelTransaksi = createCrudPanel("Kelola Data Transaksi");


        // Masukin ke MainCanvas
        MainCanvas.add(panelReport, "REPORT");
        MainCanvas.add(panelMaster, "MASTER");
        MainCanvas.add(panelTransaksi, "TRANSAKSI");

        // Fungsi Pindah Layar pas tombol sidebar diklik
        btnReport.addActionListener(e -> cardLayout.show(MainCanvas, "REPORT"));
        btnMaster.addActionListener(e -> cardLayout.show(MainCanvas, "MASTER"));
        btnTransaksi.addActionListener(e -> cardLayout.show(MainCanvas, "TRANSAKSI"));
        
        // Fungsi Logout
        btnLogout.addActionListener(e -> {
            int pilihan = JOptionPane.showConfirmDialog(this, "Yakin mau keluar dari Admin?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (pilihan == JOptionPane.YES_OPTION) {
                LoginMenu menuLogin = new LoginMenu();
                menuLogin.setVisible(true);
                dispose();
            }
        });

        revalidate();
        repaint();
    }

    // =================================================================================
    // METHOD BIKIN DASHBOARD REPORT 
    // =================================================================================
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Bagian Atas: Filter Periode & Ringkasan
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        // Filter Bulan dan Tahun
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Pilih Periode: "));
        
        String[] bulan = {"Semua Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni"};
        JComboBox<String> cbBulan = new JComboBox<>(bulan);
        
        String[] tahun = {"Semua Tahun", "2025", "2026"};
        JComboBox<String> cbTahun = new JComboBox<>(tahun);
        
        JButton btnFilter = new JButton("Tampilkan Report");
        btnFilter.setBackground(new Color(41, 128, 185));
        btnFilter.setForeground(Color.WHITE);

        filterPanel.add(cbBulan);
        filterPanel.add(cbTahun);
        filterPanel.add(btnFilter);
        topPanel.add(filterPanel, BorderLayout.NORTH);

        // Kartu Ringkasan (Cards)
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setOpaque(false);
        
        summaryPanel.add(createSummaryCard("Total Transaksi", "150", new Color(46, 204, 113)));
        summaryPanel.add(createSummaryCard("Total Pendapatan", "Rp 5.450.000", new Color(52, 152, 219)));
        summaryPanel.add(createSummaryCard("Total User Aktif", "45", new Color(155, 89, 182)));
        
        topPanel.add(summaryPanel, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        // Bagian Tengah: Tabel Data Transaksi
        String[] kolom = {"ID Transaksi", "Tanggal", "User", "Game", "Nominal", "Total Harga"};
        Object[][] dataDummy = {
            {"TRX-001", "2026-06-18", "GusAdi", "Valorant", "1000 VP", "Rp 112.000"},
            {"TRX-002", "2026-06-19", "Budi", "Mobile Legends", "284 Diamonds", "Rp 70.000"},
            {"TRX-003", "2026-06-19", "Ayu", "Free Fire", "720 Diamonds", "Rp 140.000"}
        };
        
        JTable table = new JTable(new DefaultTableModel(dataDummy, kolom));
        table.setRowHeight(30);
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Detail Laporan Transaksi"));
        
        panel.add(scrollTable, BorderLayout.CENTER);

        return panel;
    }

    // Method pembantu buat bikin kotak warna-warni di dashboard
    private JPanel createSummaryCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblValue);
        
        return card;
    }

    // =================================================================================
    // METHOD BIKIN HALAMAN CRUD (Data Master / Transaksi)
    // =================================================================================
    private JPanel createCrudPanel(String titleStr) {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(titleStr);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        // Placeholder tabel kosong
        JTable table = new JTable(new DefaultTableModel(new Object[]{"ID", "Nama", "Detail"}, 0));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Tombol Aksi CRUD di bawah
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);
        actionPanel.add(new JButton("Tambah"));
        actionPanel.add(new JButton("Edit"));
        actionPanel.add(new JButton("Hapus"));
        
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }



  

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardAdmin.class.getName());

    public DashboardAdmin() {
        initComponents();
        setupCustomUI();
        this.setExtendedState(MAXIMIZED_BOTH); // Langsung full screen
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
        Sidebar = new javax.swing.JPanel();
        MainCanvas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
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
            .addGap(0, 200, Short.MAX_VALUE)
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
            .addGap(0, 200, Short.MAX_VALUE)
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
        java.awt.EventQueue.invokeLater(() -> new DashboardAdmin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Header;
    private javax.swing.JPanel MainCanvas;
    private javax.swing.JPanel Sidebar;
    // End of variables declaration//GEN-END:variables
}
