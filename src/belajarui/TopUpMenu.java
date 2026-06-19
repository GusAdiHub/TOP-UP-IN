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
import java.sql.*; 

public class TopUpMenu extends javax.swing.JFrame {
private int idUser;

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/topup-in";
        String username = "root";
        String password = ""; 
        return DriverManager.getConnection(url, username, password);
    }

    private void setupCustomUI() {
        Sidebar.setLayout(new BorderLayout());
        Sidebar.setBackground(new Color(44, 62, 80));
        Sidebar.setPreferredSize(new Dimension(180, 0));

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(52, 73, 94));
        imagePanel.setPreferredSize(new Dimension(180, 180));

        JLabel lblGambar;
        java.net.URL logoURL = getClass().getResource("/belajarui/images/logo1.png");

        if (logoURL != null) {
            ImageIcon iconOriginal = new ImageIcon(logoURL);
            Image imgResized = iconOriginal.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblGambar = new JLabel(new ImageIcon(imgResized), SwingConstants.CENTER);
        } else {
            lblGambar = new JLabel("<html><center>[ GAMBAR / LOGO ]<br>Taruh Sini</center></html>",
                    SwingConstants.CENTER);
            lblGambar.setForeground(Color.LIGHT_GRAY);
        }

        imagePanel.add(lblGambar, BorderLayout.CENTER);
        Sidebar.add(imagePanel, BorderLayout.NORTH);

        JButton btnBack = new JButton("Kembali");
        btnBack.setBackground(new Color(192, 57, 43));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBack.setPreferredSize(new Dimension(180, 50));
        btnBack.setFocusPainted(false);

        // 🔥 KOREKSI: Oper balik `this.idUser` pas klik kembali biar datanya gak ilang di MainMenu
        btnBack.addActionListener(e -> {
            MainMenu menuUtama = new MainMenu(this.idUser);
            menuUtama.setVisible(true);
            dispose();
        });

        Sidebar.add(btnBack, BorderLayout.SOUTH);

        CardLayout cardLayout = new CardLayout();
        MainCanvas.setLayout(cardLayout);

        // KATEGORI MOBILE GAMES
        MainCanvas.add(createTopUpForm("Mobile Legends", true, "User ID", "Contoh: 12345678 (1234)", "Diamonds"), "Mobile Legends");
        MainCanvas.add(createTopUpForm("Honor of Kings", false, "Player ID", "Contoh: 1234567890", "Tokens"), "Honor of Kings");
        MainCanvas.add(createTopUpForm("Free Fire", false, "Player ID", "Contoh: 531234567", "Diamonds"), "Free Fire");
        MainCanvas.add(createTopUpForm("Genshin Impact", false, "UID Game", "Contoh: 812345678", "Genesis Crystals"), "Genshin Impact");
        MainCanvas.add(createTopUpForm("PUBG Mobile", false, "Character ID", "Contoh: 5123456789", "UC"), "PUBG Mobile");
        MainCanvas.add(createTopUpForm("Honkai Star Rail", false, "UID Game", "Contoh: 601234567", "Oneiric Shards"), "Honkai Star Rail");

        // KATEGORI PC GAMES
        MainCanvas.add(createTopUpForm("Valorant", false, "Riot ID", "Contoh: Westbourne#SEA", "VP"), "Valorant");
        MainCanvas.add(createTopUpForm("Point Blank", false, "Zeppeto ID", "Contoh: pointblank123", "Cash"), "Point Blank");
        MainCanvas.add(createTopUpForm("League of Legends", false, "Riot ID", "Contoh: Faker#KR1", "RP"), "League of Legends");
        MainCanvas.add(createTopUpForm("Apex Legends", false, "EA ID", "Contoh: ApexPlayer", "Coins"), "Apex Legends");
        MainCanvas.add(createTopUpForm("Zenless Zone Zero", false, "UID Game", "Contoh: 12345678", "Monochrome"), "Zenless Zone Zero");
        MainCanvas.add(createTopUpForm("NTE", false, "UID Game", "Contoh: 12345678", "Crystals"), "NTE");

        revalidate();
        repaint();
    }

    private JScrollPane createTopUpForm(String gameName, boolean isDoubleId, String idLabelName, String exampleText, String currency) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(236, 240, 241));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Top Up " + gameName);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        container.add(lblTitle);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel idPanel = new JPanel();
        idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.Y_AXIS));
        idPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "1. Masukkan " + idLabelName, 0, 0, new Font("SansSerif", Font.BOLD, 14)));
        idPanel.setBackground(Color.WHITE);
        idPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputRow.setBackground(Color.WHITE);

        JTextField txtId1;
        JTextField txtId2 = new JTextField(8); 

        if (isDoubleId) {
            txtId1 = new JTextField(15);
            inputRow.add(txtId1);
            inputRow.add(new JLabel(" ( "));
            inputRow.add(txtId2);
            inputRow.add(new JLabel(" ) "));
        } else {
            txtId1 = new JTextField(25);
            inputRow.add(txtId1);
        }
        idPanel.add(inputRow);

        JLabel lblSubtext = new JLabel(exampleText);
        lblSubtext.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblSubtext.setForeground(Color.DARK_GRAY);
        lblSubtext.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        idPanel.add(lblSubtext);

        container.add(idPanel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel nomPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        nomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "2. Pilih Nominal (" + currency + ")", 0, 0, new Font("SansSerif", Font.BOLD, 14)));
        nomPanel.setBackground(Color.WHITE);
        nomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ButtonGroup nomGroup = new ButtonGroup();

        String sqlQuery = "SELECT t.id_topup, t.nominal_topup, t.harga FROM tb_topup t "
                + "JOIN tb_game g ON t.id_game = g.id_game WHERE g.nama_game = ?";

        boolean dataDitemukan = false;

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)) {

            ps.setString(1, gameName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dataDitemukan = true;
                    int idTopup = rs.getInt("id_topup"); 
                    String nominal = rs.getString("nominal_topup");
                    int harga = rs.getInt("harga");

                    String teksTombol = "<html><center><b>" + nominal + "</b><br><font color='blue'>Rp "
                            + String.format("%,d", harga) + "</font></center></html>";
                    JToggleButton btnNom = new JToggleButton(teksTombol);
                    btnNom.setBackground(Color.WHITE);
                    btnNom.setPreferredSize(new Dimension(110, 75));

                    btnNom.putClientProperty("id_topup", idTopup);
                    btnNom.putClientProperty("nominal", nominal);
                    btnNom.putClientProperty("harga", harga);

                    nomGroup.add(btnNom);
                    nomPanel.add(btnNom);
                }
            }
        } catch (SQLException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        if (!dataDitemukan) {
            JLabel lblKosong = new JLabel("Belum ada paket topup di database untuk game ini.", SwingConstants.CENTER);
            lblKosong.setForeground(Color.RED);
            lblKosong.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            nomPanel.add(lblKosong);
        }

        container.add(nomPanel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel payPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        payPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "3. Pilih Metode Pembayaran", 0, 0, new Font("SansSerif", Font.BOLD, 14)));
        payPanel.setBackground(Color.WHITE);
        payPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ButtonGroup payGroup = new ButtonGroup();
        String[] payments = { "QRIS", "DANA", "GoPay", "OVO" };
        for (String pay : payments) {
            JToggleButton btnPay = new JToggleButton(pay);
            btnPay.setBackground(Color.WHITE);
            btnPay.setPreferredSize(new Dimension(100, 50));
            btnPay.putClientProperty("metode", pay);

            payGroup.add(btnPay);
            payPanel.add(btnPay);
        }
        container.add(payPanel);
        container.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnSubmit = new JButton("Beli Sekarang");
        btnSubmit.setBackground(new Color(46, 204, 113));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnSubmit.setPreferredSize(new Dimension(200, 45));
        btnSubmit.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnSubmit.addActionListener(e -> {
            String inputID = txtId1.getText().trim();
            String zoneID = txtId2.getText().trim();

            if (inputID.isEmpty() || (isDoubleId && zoneID.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Tolong masukkan ID Game kamu dengan benar, Bang!", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nominalTerpilih = "";
            int hargaTerpilih = 0;
            int idTopupTerpilih = 0;
            for (Component c : nomPanel.getComponents()) {
                if (c instanceof JToggleButton && ((JToggleButton) c).isSelected()) {
                    idTopupTerpilih = (int) ((JToggleButton) c).getClientProperty("id_topup"); 
                    nominalTerpilih = (String) ((JToggleButton) c).getClientProperty("nominal");
                    hargaTerpilih = (int) ((JToggleButton) c).getClientProperty("harga");
                    break;
                }
            }

            String metodeTerpilih = "";
            for (Component c : payPanel.getComponents()) {
                if (c instanceof JToggleButton && ((JToggleButton) c).isSelected()) {
                    metodeTerpilih = (String) ((JToggleButton) c).getClientProperty("metode");
                    break;
                }
            }

            if (idTopupTerpilih == 0 || nominalTerpilih.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih nominal item dulu, Cuy!", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (metodeTerpilih.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Metode pembayarannya belum lu pilih!", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sqlInsert = "INSERT INTO tb_transaksi (tanggal_transaksi, id_user, id_topup, target_id_game, target_zone_game, metode_pembayaran, total_bayar) "
                    + "VALUES (NOW(), ?, ?, ?, ?, ?, ?)";

            try (Connection conn = getConnection();
                    PreparedStatement ps = conn.prepareStatement(sqlInsert)) {

                // 🔥 KOREKSI UTAMA: Menggunakan variabel dinamis this.idUser hasil operan login, bukan hardcode 1 lagi!
                ps.setInt(1, this.idUser);
                ps.setInt(2, idTopupTerpilih);
                ps.setString(3, inputID);
                ps.setString(4, zoneID.isEmpty() ? "-" : zoneID);
                ps.setString(5, metodeTerpilih);
                ps.setInt(6, hargaTerpilih);

                ps.executeUpdate();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal memproses transaksi ke database:\n" + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                logger.log(java.util.logging.Level.SEVERE, null, ex);
                return; 
            }

            String fullID = isDoubleId ? inputID + " (" + zoneID + ")" : inputID;
            String pesanSukses = "=== TRANSAKSI BERHASIL ===\n\n"
                    + "Game: " + gameName + "\n"
                    + "Target ID: " + fullID + "\n"
                    + "Paket: " + nominalTerpilih + " " + currency + "\n"
                    + "Total Bayar: Rp " + String.format("%,d", hargaTerpilih) + "\n"
                    + "Metode: " + metodeTerpilih + "\n\n"
                    + "Data sukses disimpan! Pesanan sedang diproses otomatis oleh sistem.";

            JOptionPane.showMessageDialog(this, pesanSukses, "Sukses", JOptionPane.INFORMATION_MESSAGE);

            txtId1.setText("");
            txtId2.setText("");
            nomGroup.clearSelection();
            payGroup.clearSelection();
        });

        container.add(btnSubmit);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        return scrollPane;
    }

    public void bukaHalamanGame(String namaGame) {
        CardLayout cl = (CardLayout) MainCanvas.getLayout();
        cl.show(MainCanvas, namaGame);
    }

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(TopUpMenu.class.getName());

    // 🔥 KOREKSI: Sediakan constructor kosongan agar method main di bawah tidak error kompilasi
    public TopUpMenu() {
        initComponents();
        setupCustomUI();
        this.setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    public TopUpMenu(int idUser) {
        initComponents();
        this.idUser = idUser;
        setupCustomUI();
        this.setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        Sidebar = new javax.swing.JPanel();
        MainCanvas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout SidebarLayout = new javax.swing.GroupLayout(Sidebar);
        Sidebar.setLayout(SidebarLayout);
        SidebarLayout.setHorizontalGroup(
                SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE));
        SidebarLayout.setVerticalGroup(
                SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));

        getContentPane().add(Sidebar, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout MainCanvasLayout = new javax.swing.GroupLayout(MainCanvas);
        MainCanvas.setLayout(MainCanvasLayout);
        MainCanvasLayout.setHorizontalGroup(
                MainCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));
        MainCanvasLayout.setVerticalGroup(
                MainCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE));

        getContentPane().add(MainCanvas, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>

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

        java.awt.EventQueue.invokeLater(() -> new TopUpMenu().setVisible(true));
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel MainCanvas;
    private javax.swing.JPanel Sidebar;
    // End of variables declaration
}