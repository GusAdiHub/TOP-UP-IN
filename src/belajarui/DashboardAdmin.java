/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package belajarui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author ASUS
 */
public class DashboardAdmin extends javax.swing.JFrame {
    private JLabel lblValTransaksi;
    private JLabel lblValPendapatan;
    private JLabel lblValUser;
    private DefaultTableModel reportTableModel;
    private DefaultTableModel gameTableModel;
    private DefaultTableModel userTableModel;
    private DefaultTableModel transaksiTableModel;
    private DefaultTableModel topupTableModel; // TAMBAHAN: Table model untuk CRUD Topup
    private JComboBox<String> cbBulan;
    private JComboBox<String> cbTahun;

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

        // 3. Setup SIDEBAR
        Sidebar.setLayout(new BorderLayout());
        Sidebar.setBackground(new Color(44, 62, 80));
        Sidebar.setPreferredSize(new Dimension(200, 0));

        // -- Area Gambar/Logo di Atas --
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(52, 73, 94));
        imagePanel.setPreferredSize(new Dimension(200, 180));

        JLabel lblGambar;
        java.net.URL logoURL = getClass().getResource("/belajarui/images/logo.png");
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

        // -- Area Menu di Tengah (5 Tombol Utama setelah di-upgrade) --
        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(new Color(44, 62, 80));

        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // DIUBAH DARI 4 JADI 5
        menuPanel.setBackground(new Color(44, 62, 80));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        JButton btnReport = new JButton("Laporan (Report)");
        JButton btnGame = new JButton("Data Game");
        JButton btnTopup = new JButton("Data Top Up"); // TAMBAHAN TOMBOL BARU
        JButton btnUser = new JButton("Data User");
        JButton btnTransaksi = new JButton("Data Transaksi");

        menuPanel.add(btnReport);
        menuPanel.add(btnGame);
        menuPanel.add(btnTopup); // MASUKIN KE PANEL
        menuPanel.add(btnUser);
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

        // --- BIKIN HALAMAN-HALAMAN CANVAS ---
        JPanel panelReport = createReportPanel();
        JPanel panelGame = createGamePanel();
        JPanel panelTopup = createTopupPanel(); // PANEL BARU KELOLA TOPUP
        JPanel panelUser = createUserPanel();
        JPanel panelTransaksi = createTransaksiPanel();

        // Masukin ke MainCanvas
        MainCanvas.add(panelReport, "REPORT");
        MainCanvas.add(panelGame, "GAME");
        MainCanvas.add(panelTopup, "TOPUP"); // DAFTARKAN CARD TOPUP
        MainCanvas.add(panelUser, "USER");
        MainCanvas.add(panelTransaksi, "TRANSAKSI");

        // Fungsi Pindah Layar pas tombol sidebar diklik
        btnReport.addActionListener(e -> {
            cardLayout.show(MainCanvas, "REPORT");
            loadDataLaporan();
        });
        btnGame.addActionListener(e -> {
            cardLayout.show(MainCanvas, "GAME");
            loadDataGame();
        });
        btnTopup.addActionListener(e -> { // ACTION TOMBOL BARU
            cardLayout.show(MainCanvas, "TOPUP");
            loadDataTopup();
        });
        btnUser.addActionListener(e -> {
            cardLayout.show(MainCanvas, "USER");
            loadDataUser();
        });
        btnTransaksi.addActionListener(e -> {
            cardLayout.show(MainCanvas, "TRANSAKSI");
            loadDataTransaksi();
        });

        // Fungsi Logout
        btnLogout.addActionListener(e -> {
            int pilihan = JOptionPane.showConfirmDialog(this, "Yakin mau keluar dari Admin?", "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);
            if (pilihan == JOptionPane.YES_OPTION) {
                LoginMenu menuLogin = new LoginMenu();
                menuLogin.setVisible(true);
                dispose();
            }
        });

        // Sinkronisasi awal semua data
        loadDataLaporan();
        loadDataGame();
        loadDataTopup(); // LOAD DATA AWAL
        loadDataUser();
        loadDataTransaksi();

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

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Pilih Periode: "));

        String[] bulan = { "Semua Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus",
                "September", "Oktober", "November", "Desember" };
        cbBulan = new JComboBox<>(bulan);

        String[] tahun = { "Semua Tahun", "2025", "2026", "2027" };
        cbTahun = new JComboBox<>(tahun);

        JButton btnFilter = new JButton("Tampilkan Report");
        btnFilter.setBackground(new Color(41, 128, 185));
        btnFilter.setForeground(Color.WHITE);
        btnFilter.addActionListener(e -> loadDataLaporan());

        filterPanel.add(cbBulan);
        filterPanel.add(cbTahun);
        filterPanel.add(btnFilter);
        topPanel.add(filterPanel, BorderLayout.NORTH);

        lblValTransaksi = new JLabel("0");
        lblValPendapatan = new JLabel("Rp 0");
        lblValUser = new JLabel("0");

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setOpaque(false);

        summaryPanel.add(createSummaryCard("Total Transaksi", lblValTransaksi, new Color(46, 204, 113)));
        summaryPanel.add(createSummaryCard("Total Pendapatan", lblValPendapatan, new Color(52, 152, 219)));
        summaryPanel.add(createSummaryCard("Total User Aktif", lblValUser, new Color(155, 89, 182)));

        topPanel.add(summaryPanel, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] kolom = { "ID Transaksi", "Tanggal & Waktu", "Username Pembeli", "ID Topup", "Target ID (Zone)",
                "Metode Pembayaran", "Total Bayar" };
        reportTableModel = new DefaultTableModel(null, kolom);

        JTable table = new JTable(reportTableModel);
        table.setRowHeight(30);
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Detail Laporan Transaksi (Real Database)"));

        panel.add(scrollTable, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSummaryCard(String title, JLabel lblValue, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblValue);

        return card;
    }

    // =================================================================================
    // 🟢 HALAMAN: KELOLA DATA GAME (REAL DATABASE CRUD)
    // =================================================================================
    private JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Kelola Master Data Game");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        String[] kolom = { "ID Game", "Nama Game", "Kategori Game" };
        gameTableModel = new DefaultTableModel(null, kolom);
        JTable table = new JTable(gameTableModel);
        table.setRowHeight(28);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton btnTambah = new JButton("Tambah Game");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        btnTambah.addActionListener(e -> {
            JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
            JTextField txtNama = new JTextField(15);
            JTextField txtKategori = new JTextField(15);
            form.add(new JLabel("Nama Game:"));
            form.add(txtNama);
            form.add(new JLabel("Kategori:"));
            form.add(txtKategori);

            int hasil = JOptionPane.showConfirmDialog(panel, form, "Tambah Game Baru", JOptionPane.OK_CANCEL_OPTION);
            if (hasil == JOptionPane.OK_OPTION && !txtNama.getText().isEmpty()) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String sql = "INSERT INTO tb_game (nama_game, kategori) VALUES (?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, txtNama.getText());
                    ps.setString(2, txtKategori.getText());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Game baru berhasil disimpan!", "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadDataGame();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Pilih baris data game di tabel dulu!");
                return;
            }
            String id = gameTableModel.getValueAt(row, 0).toString();
            JTextField txtNama = new JTextField(gameTableModel.getValueAt(row, 1).toString(), 15);
            JTextField txtKategori = new JTextField(gameTableModel.getValueAt(row, 2).toString(), 15);

            JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
            form.add(new JLabel("Nama Game:"));
            form.add(txtNama);
            form.add(new JLabel("Kategori:"));
            form.add(txtKategori);

            int hasil = JOptionPane.showConfirmDialog(panel, form, "Edit Data Game", JOptionPane.OK_CANCEL_OPTION);
            if (hasil == JOptionPane.OK_OPTION) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String sql = "UPDATE tb_game SET nama_game=?, kategori=? WHERE id_game=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, txtNama.getText());
                    ps.setString(2, txtKategori.getText());
                    ps.setString(3, id);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Data game berhasil diubah!");
                    loadDataGame();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            }
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Pilih data game yang ingin dihapus!");
                return;
            }
            String id = gameTableModel.getValueAt(row, 0).toString();
            int konf = JOptionPane.showConfirmDialog(panel, "Yakin hapus game ini dari database?", "Hapus",
                    JOptionPane.YES_NO_OPTION);
            if (konf == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String sql = "DELETE FROM tb_game WHERE id_game=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, id);
                    ps.executeUpdate();
                    loadDataGame();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            }
        });

        actionPanel.add(btnTambah);
        actionPanel.add(btnEdit);
        actionPanel.add(btnHapus);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    // =================================================================================
    // 🟢 HALAMAN BARU: KELOLA DATA TOPUP (REAL DATABASE CRUD `tb_topup`)
    // =================================================================================
    private JPanel createTopupPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Kelola Master Paket Data Top Up");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        // Header tabel paket top up
        String[] kolom = { "ID Topup", "Nama Game", "Nominal Paket", "Harga Paket" };
        topupTableModel = new DefaultTableModel(null, kolom);
        JTable table = new JTable(topupTableModel);
        table.setRowHeight(28);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton btnTambah = new JButton("Tambah Paket");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        // PROSES 1: TAMBAH PAKET TOPUP
        btnTambah.addActionListener(e -> {
            JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
            JComboBox<String> cbGames = new JComboBox<>();
            JTextField txtNominal = new JTextField(15);
            JTextField txtHarga = new JTextField(15);

            // Ambil relasi data game dari tb_game secara dinamis buat dropdown pembantu
            try {
                Connection conn = Koneksi.getconnection();
                PreparedStatement ps = conn
                        .prepareStatement("SELECT id_game, nama_game FROM tb_game ORDER BY nama_game ASC");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    cbGames.addItem(rs.getInt("id_game") + " - " + rs.getString("nama_game"));
                }
                rs.close();
                ps.close();
            } catch (Exception ex) {
                System.out.println("Gagal load dropdown game: " + ex.getMessage());
            }

            form.add(new JLabel("Pilih Game Target:"));
            form.add(cbGames);
            form.add(new JLabel("Nominal Top Up (Contoh: 86 Diamonds / 475 VP):"));
            form.add(txtNominal);
            form.add(new JLabel("Harga Paket (Angka Saja):"));
            form.add(txtHarga);

            int hasil = JOptionPane.showConfirmDialog(panel, form, "Tambah Paket Top Up Baru",
                    JOptionPane.OK_CANCEL_OPTION);
            if (hasil == JOptionPane.OK_OPTION && cbGames.getSelectedItem() != null && !txtNominal.getText().isEmpty()
                    && !txtHarga.getText().isEmpty()) {
                try {
                    // Ekstrak ID Game asli dari string text "ID - NamaGame" di JComboBox
                    String selectedGame = cbGames.getSelectedItem().toString();
                    int idGame = Integer.parseInt(selectedGame.split(" - ")[0]);

                    Connection conn = Koneksi.getconnection();
                    String sql = "INSERT INTO tb_topup (id_game, nominal_topup, harga) VALUES (?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, idGame);
                    ps.setString(2, txtNominal.getText().trim());
                    ps.setInt(3, Integer.parseInt(txtHarga.getText().trim()));
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(panel, "Paket top up baru sukses disimpan!");
                    loadDataTopup();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Gagal simpan paket: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // PROSES 2: EDIT PAKET TOPUP
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Pilih baris paket di tabel dulu!");
                return;
            }

            String idTopup = topupTableModel.getValueAt(row, 0).toString();
            String namaGameLama = topupTableModel.getValueAt(row, 1).toString();
            String nominalLama = topupTableModel.getValueAt(row, 2).toString();
            String hargaRaw = topupTableModel.getValueAt(row, 3).toString().replaceAll("[^0-9]", ""); // Bersihin format
                                                                                                      // Rp

            JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
            JComboBox<String> cbGames = new JComboBox<>();
            JTextField txtNominal = new JTextField(nominalLama, 15);
            JTextField txtHarga = new JTextField(hargaRaw, 15);

            // Load dropdown game sekalian pilih game yang aktif saat ini secara otomatis
            try {
                Connection conn = Koneksi.getconnection();
                PreparedStatement ps = conn
                        .prepareStatement("SELECT id_game, nama_game FROM tb_game ORDER BY nama_game ASC");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String item = rs.getInt("id_game") + " - " + rs.getString("nama_game");
                    cbGames.addItem(item);
                    if (rs.getString("nama_game").equals(namaGameLama)) {
                        cbGames.setSelectedItem(item);
                    }
                }
                rs.close();
                ps.close();
            } catch (Exception ex) {
                System.out.println("Gagal load dropdown game: " + ex.getMessage());
            }

            form.add(new JLabel("Ubah Game Target:"));
            form.add(cbGames);
            form.add(new JLabel("Nominal Top Up:"));
            form.add(txtNominal);
            form.add(new JLabel("Harga Paket (Rp):"));
            form.add(txtHarga);

            int hasil = JOptionPane.showConfirmDialog(panel, form, "Edit Paket Top Up", JOptionPane.OK_CANCEL_OPTION);
            if (hasil == JOptionPane.OK_OPTION) {
                try {
                    String selectedGame = cbGames.getSelectedItem().toString();
                    int idGame = Integer.parseInt(selectedGame.split(" - ")[0]);

                    Connection conn = Koneksi.getconnection();
                    String sql = "UPDATE tb_topup SET id_game=?, nominal_topup=?, harga=? WHERE id_topup=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, idGame);
                    ps.setString(2, txtNominal.getText().trim());
                    ps.setInt(3, Integer.parseInt(txtHarga.getText().trim()));
                    ps.setInt(4, Integer.parseInt(idTopup));
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(panel, "Data paket top up berhasil diperbarui!");
                    loadDataTopup();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Gagal update data: " + ex.getMessage());
                }
            }
        });

        // PROSES 3: HAPUS PAKET TOPUP
        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Pilih data paket top up yang mau dihapus!");
                return;
            }
            String idTopup = topupTableModel.getValueAt(row, 0).toString();
            int konf = JOptionPane.showConfirmDialog(panel, "Hapus paket top up ini dari database?", "Hapus Paket",
                    JOptionPane.YES_NO_OPTION);
            if (konf == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String sql = "DELETE FROM tb_topup WHERE id_topup=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(idTopup));
                    ps.executeUpdate();
                    loadDataTopup();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error Delete Paket: " + ex.getMessage());
                }
            }
        });

        actionPanel.add(btnTambah);
        actionPanel.add(btnEdit);
        actionPanel.add(btnHapus);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    // =================================================================================
    // 🟢 HALAMAN: KELOLA DATA USER (REAL DATABASE CRUD)
    // =================================================================================
    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Kelola Master Data User / Pelanggan");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        String[] kolom = { "ID User", "Username", "Nama Lengkap", "Email", "Nomor HP" };
        userTableModel = new DefaultTableModel(null, kolom);
        JTable table = new JTable(userTableModel);
        table.setRowHeight(28);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton btnTambah = new JButton("Tambah User");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        btnTambah.addActionListener(e -> {
            JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
            JTextField txtUser = new JTextField(15);
            JPasswordField txtPass = new JPasswordField(15);
            JTextField txtNama = new JTextField(15);
            JTextField txtEmail = new JTextField(15);
            JTextField txtHp = new JTextField(15);

            form.add(new JLabel("Username:"));
            form.add(txtUser);
            form.add(new JLabel("Password:"));
            form.add(txtPass);
            form.add(new JLabel("Nama Lengkap:"));
            form.add(txtNama);
            form.add(new JLabel("Email:"));
            form.add(txtEmail);
            form.add(new JLabel("Nomor HP:"));
            form.add(txtHp);

            int hasil = JOptionPane.showConfirmDialog(panel, form, "Tambah User Baru", JOptionPane.OK_CANCEL_OPTION);
            if (hasil == JOptionPane.OK_OPTION) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String sql = "INSERT INTO tb_user (username, password, nama_lengkap, email, nomor_hp) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, txtUser.getText());
                    ps.setString(2, new String(txtPass.getPassword()));
                    ps.setString(3, txtNama.getText());
                    ps.setString(4, txtEmail.getText());
                    ps.setString(5, txtHp.getText());
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "User registrasi berhasil!");
                    loadDataUser();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error Reg: " + ex.getMessage());
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Pilih user di tabel dulu!");
                return;
            }
            String id = userTableModel.getValueAt(row, 0).toString();
            JTextField txtUser = new JTextField(userTableModel.getValueAt(row, 1).toString(), 15);
            JPasswordField txtPass = new JPasswordField(15);
            JTextField txtNama = new JTextField(userTableModel.getValueAt(row, 2).toString(), 15);
            JTextField txtEmail = new JTextField(userTableModel.getValueAt(row, 3).toString(), 15);
            JTextField txtHp = new JTextField(userTableModel.getValueAt(row, 4).toString(), 15);

            JPanel form = new JPanel(new GridLayout(0, 1, 3, 3));
            form.add(new JLabel("Username:"));
            form.add(txtUser);
            form.add(new JLabel("Password Baru (Kosongkan jika tidak diganti):"));
            form.add(txtPass);
            form.add(new JLabel("Nama Lengkap:"));
            form.add(txtNama);
            form.add(new JLabel("Email:"));
            form.add(txtEmail);
            form.add(new JLabel("Nomor HP:"));
            form.add(txtHp);

            int hasil = JOptionPane.showConfirmDialog(panel, form, "Edit Profile User", JOptionPane.OK_CANCEL_OPTION);
            if (hasil == JOptionPane.OK_OPTION) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String passBaru = new String(txtPass.getPassword());
                    String sql;
                    PreparedStatement ps;

                    if (passBaru.isEmpty()) {
                        sql = "UPDATE tb_user SET username=?, nama_lengkap=?, email=?, nomor_hp=? WHERE id_user=?";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, txtUser.getText());
                        ps.setString(2, txtNama.getText());
                        ps.setString(3, txtEmail.getText());
                        ps.setString(4, txtHp.getText());
                        ps.setString(5, id);
                    } else {
                        sql = "UPDATE tb_user SET username=?, password=?, nama_lengkap=?, email=?, nomor_hp=? WHERE id_user=?";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, txtUser.getText());
                        ps.setString(2, passBaru);
                        ps.setString(3, txtNama.getText());
                        ps.setString(4, txtEmail.getText());
                        ps.setString(5, txtHp.getText());
                        ps.setString(6, id);
                    }
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Data user berhasil diperbarui!");
                    loadDataUser();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error Update: " + ex.getMessage());
                }
            }
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Pilih user yang mau dihapus!");
                return;
            }
            String id = userTableModel.getValueAt(row, 0).toString();
            int konf = JOptionPane.showConfirmDialog(panel, "Yakin ingin menghapus user ini secara permanen?",
                    "Hapus User", JOptionPane.YES_NO_OPTION);
            if (konf == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String sql = "DELETE FROM tb_user WHERE id_user=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, id);
                    ps.executeUpdate();
                    loadDataUser();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error Delete: " + ex.getMessage());
                }
            }
        });

        actionPanel.add(btnTambah);
        actionPanel.add(btnEdit);
        actionPanel.add(btnHapus);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    // =================================================================================
    // 🟢 HALAMAN: KELOLA DATA TRANSAKSI (REAL DATABASE CRUD)
    // =================================================================================
    private JPanel createTransaksiPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Kelola Data Transaksi Top-Up");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        String[] kolom = { "ID Transaksi", "Tanggal & Waktu", "ID User", "ID Topup", "Target ID Game", "Zone", "Metode",
                "Total Bayar" };
        transaksiTableModel = new DefaultTableModel(null, kolom);
        JTable table = new JTable(transaksiTableModel);
        table.setRowHeight(28);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JButton btnTambah = new JButton("Tambah Transaksi");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        btnTambah.addActionListener(e -> {
            JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
            JTextField txtIdUser = new JTextField(15);
            JTextField txtIdTopup = new JTextField(15);
            JTextField txtTargetId = new JTextField(15);
            JTextField txtZone = new JTextField(15);
            JTextField txtMetode = new JTextField(15);
            JTextField txtTotal = new JTextField(15);

            form.add(new JLabel("ID User (Harus sesuai tb_user):"));
            form.add(txtIdUser);
            form.add(new JLabel("ID Topup:"));
            form.add(txtIdTopup);
            form.add(new JLabel("Target ID Game:"));
            form.add(txtTargetId);
            form.add(new JLabel("Target Zone Game:"));
            form.add(txtZone);
            form.add(new JLabel("Metode Pembayaran:"));
            form.add(txtMetode);
            form.add(new JLabel("Total Bayar (Rp):"));
            form.add(txtTotal);

            int hasil = JOptionPane.showConfirmDialog(panel, form, "Input Transaksi Baru",
                    JOptionPane.OK_CANCEL_OPTION);
            if (hasil == JOptionPane.OK_OPTION) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String sql = "INSERT INTO tb_transaksi (tanggal_transaksi, id_user, id_topup, target_id_game, target_zone_game, metode_pembayaran, total_bayar) VALUES (NOW(), ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(txtIdUser.getText()));
                    ps.setInt(2, Integer.parseInt(txtIdTopup.getText()));
                    ps.setString(3, txtTargetId.getText());
                    ps.setString(4, txtZone.getText());
                    ps.setString(5, txtMetode.getText());
                    ps.setInt(6, Integer.parseInt(txtTotal.getText()));

                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Transaksi baru berhasil disimpan!");
                    loadDataTransaksi();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Gagal simpan transaksi: " + ex.getMessage(), "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Pilih data transaksi di tabel dulu!");
                return;
            }
            String idTrx = transaksiTableModel.getValueAt(row, 0).toString();
            JTextField txtIdUser = new JTextField(transaksiTableModel.getValueAt(row, 2).toString(), 15);
            JTextField txtIdTopup = new JTextField(transaksiTableModel.getValueAt(row, 3).toString(), 15);
            JTextField txtTargetId = new JTextField(transaksiTableModel.getValueAt(row, 4).toString(), 15);
            JTextField txtZone = new JTextField(transaksiTableModel.getValueAt(row, 5).toString(), 15);
            JTextField txtMetode = new JTextField(transaksiTableModel.getValueAt(row, 6).toString(), 15);

            String totalRaw = transaksiTableModel.getValueAt(row, 7).toString().replaceAll("[^0-9]", "");
            JTextField txtTotal = new JTextField(totalRaw, 15);

            JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
            form.add(new JLabel("ID User:"));
            form.add(txtIdUser);
            form.add(new JLabel("ID Topup:"));
            form.add(txtIdTopup);
            form.add(new JLabel("Target ID Game:"));
            form.add(txtTargetId);
            form.add(new JLabel("Target Zone Game:"));
            form.add(txtZone);
            form.add(new JLabel("Metode Pembayaran:"));
            form.add(txtMetode);
            form.add(new JLabel("Total Bayar (Rp):"));
            form.add(txtTotal);

            int hasil = JOptionPane.showConfirmDialog(panel, form, "Edit Data Transaksi", JOptionPane.OK_CANCEL_OPTION);
            if (hasil == JOptionPane.OK_OPTION) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String sql = "UPDATE tb_transaksi SET id_user=?, id_topup=?, target_id_game=?, target_zone_game=?, metode_pembayaran=?, total_bayar=? WHERE id_transaksi=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(txtIdUser.getText()));
                    ps.setInt(2, Integer.parseInt(txtIdTopup.getText()));
                    ps.setString(3, txtTargetId.getText());
                    ps.setString(4, txtZone.getText());
                    ps.setString(5, txtMetode.getText());
                    ps.setInt(6, Integer.parseInt(txtTotal.getText()));
                    ps.setInt(7, Integer.parseInt(idTrx));

                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Data transaksi berhasil diperbarui!");
                    loadDataTransaksi();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error Update Transaksi: " + ex.getMessage());
                }
            }
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(panel, "Pilih data transaksi yang mau dihapus!");
                return;
            }
            String idTrx = transaksiTableModel.getValueAt(row, 0).toString();
            int konf = JOptionPane.showConfirmDialog(panel, "Hapus data transaksi ini permanen?", "Hapus Transaksi",
                    JOptionPane.YES_NO_OPTION);
            if (konf == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = Koneksi.getconnection();
                    String sql = "DELETE FROM tb_transaksi WHERE id_transaksi=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(idTrx));
                    ps.executeUpdate();
                    loadDataTransaksi();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error Delete Transaksi: " + ex.getMessage());
                }
            }
        });

        actionPanel.add(btnTambah);
        actionPanel.add(btnEdit);
        actionPanel.add(btnHapus);
        panel.add(actionPanel, BorderLayout.SOUTH);
        return panel;
    }

    // =================================================================================
    // DATABASE SELECT SYNC METHODS
    // =================================================================================

    // METHOD SYNC BARU: Ambil data tb_topup di-JOIN ke tb_game biar keliatan Nama
    // Game aslinya
    private void loadDataTopup() {
        if (topupTableModel == null)
            return;
        topupTableModel.setRowCount(0);
        try {
            Connection conn = Koneksi.getconnection();
            String query = "SELECT t.id_topup, g.nama_game, t.nominal_topup, t.harga "
                    + "FROM tb_topup t "
                    + "INNER JOIN tb_game g ON t.id_game = g.id_game ORDER BY t.id_topup DESC";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            while (rs.next()) {
                topupTableModel.addRow(new Object[] {
                        rs.getInt("id_topup"),
                        rs.getString("nama_game"),
                        rs.getString("nominal_topup"),
                        formatRupiah.format(rs.getInt("harga"))
                });
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            System.out.println("Gagal load tabel topup: " + ex.getMessage());
        }
    }

    private void loadDataTransaksi() {
        if (transaksiTableModel == null)
            return;
        transaksiTableModel.setRowCount(0);
        try {
            Connection conn = Koneksi.getconnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM tb_transaksi ORDER BY id_transaksi DESC");
            ResultSet rs = ps.executeQuery();
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            while (rs.next()) {
                transaksiTableModel.addRow(new Object[] {
                        rs.getInt("id_transaksi"),
                        rs.getString("tanggal_transaksi"),
                        rs.getInt("id_user"),
                        rs.getInt("id_topup"),
                        rs.getString("target_id_game"),
                        rs.getString("target_zone_game"),
                        rs.getString("metode_pembayaran"),
                        formatRupiah.format(rs.getInt("total_bayar"))
                });
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            System.out.println("Gagal load tabel transaksi: " + ex.getMessage());
        }
    }

    private void loadDataGame() {
        if (gameTableModel == null)
            return;
        gameTableModel.setRowCount(0);
        try {
            Connection conn = Koneksi.getconnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM tb_game ORDER BY id_game DESC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gameTableModel.addRow(new Object[] {
                        rs.getInt("id_game"), rs.getString("nama_game"), rs.getString("kategori")
                });
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            System.out.println("Gagal load tabel game: " + ex.getMessage());
        }
    }

    private void loadDataUser() {
        if (userTableModel == null)
            return;
        userTableModel.setRowCount(0);
        try {
            Connection conn = Koneksi.getconnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id_user, username, nama_lengkap, email, nomor_hp FROM tb_user ORDER BY id_user DESC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userTableModel.addRow(new Object[] {
                        rs.getInt("id_user"), rs.getString("username"), rs.getString("nama_lengkap"),
                        rs.getString("email"), rs.getString("nomor_hp")
                });
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            System.out.println("Gagal load tabel user: " + ex.getMessage());
        }
    }

    private void loadDataLaporan() {
        reportTableModel.setRowCount(0);
        int totalTransaksi = 0;
        int totalPendapatan = 0;
        int totalUser = 0;
        int indexBulan = cbBulan.getSelectedIndex();
        String pilihTahun = cbTahun.getSelectedItem().toString();

        try {
            Connection conn = Koneksi.getconnection();
            ResultSet rsUser = conn.prepareStatement("SELECT COUNT(*) AS total FROM tb_user").executeQuery();
            if (rsUser.next())
                totalUser = rsUser.getInt("total");
            rsUser.close();
        } catch (Exception ex) {
            System.out.println("Error hitung user: " + ex.getMessage());
        }

        try {
            Connection conn = Koneksi.getconnection();
            String sqlBase = "SELECT t.id_transaksi, t.tanggal_transaksi, u.username, t.id_topup, "
                    + "t.target_id_game, t.target_zone_game, t.metode_pembayaran, t.total_bayar "
                    + "FROM tb_transaksi t "
                    + "INNER JOIN tb_user u ON t.id_user = u.id_user WHERE 1=1";

            if (indexBulan > 0)
                sqlBase += " AND MONTH(t.tanggal_transaksi) = " + indexBulan;
            if (!pilihTahun.equals("Semua Tahun"))
                sqlBase += " AND YEAR(t.tanggal_transaksi) = '" + pilihTahun + "'";
            sqlBase += " ORDER BY t.tanggal_transaksi DESC";

            PreparedStatement psTrx = conn.prepareStatement(sqlBase);
            ResultSet rsTrx = psTrx.executeQuery();
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            while (rsTrx.next()) {
                int id = rsTrx.getInt("id_transaksi");
                String tgl = rsTrx.getString("tanggal_transaksi");
                String usr = rsTrx.getString("username");
                int idTopup = rsTrx.getInt("id_topup");
                String targetGameComplete = rsTrx.getString("target_id_game") + " ("
                        + rsTrx.getString("target_zone_game") + ")";
                String metode = rsTrx.getString("metode_pembayaran");
                int harga = rsTrx.getInt("total_bayar");

                totalTransaksi++;
                totalPendapatan += harga;
                reportTableModel.addRow(
                        new Object[] { id, tgl, usr, idTopup, targetGameComplete, metode, formatRupiah.format(harga) });
            }

            lblValTransaksi.setText(String.valueOf(totalTransaksi));
            lblValPendapatan.setText(formatRupiah.format(totalPendapatan));
            lblValUser.setText(String.valueOf(totalUser));
            rsTrx.close();
            psTrx.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal sinkronisasi data tabel real: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(DashboardAdmin.class.getName());

    public DashboardAdmin() {
        initComponents();
        setupCustomUI();
        this.setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        Header = new javax.swing.JPanel();
        Sidebar = new javax.swing.JPanel();
        MainCanvas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE));
        HeaderLayout.setVerticalGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 100, Short.MAX_VALUE));
        getContentPane().add(Header, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout SidebarLayout = new javax.swing.GroupLayout(Sidebar);
        Sidebar.setLayout(SidebarLayout);
        SidebarLayout.setHorizontalGroup(SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 100, Short.MAX_VALUE));
        SidebarLayout.setVerticalGroup(SidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 200, Short.MAX_VALUE));
        getContentPane().add(Sidebar, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout MainCanvasLayout = new javax.swing.GroupLayout(MainCanvas);
        MainCanvas.setLayout(MainCanvasLayout);
        MainCanvasLayout.setHorizontalGroup(MainCanvasLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
        MainCanvasLayout.setVerticalGroup(MainCanvasLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 200, Short.MAX_VALUE));
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
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new DashboardAdmin().setVisible(true));
    }

    // Variables declaration - do not modify
    private javax.swing.JPanel Header;
    private javax.swing.JPanel MainCanvas;
    private javax.swing.JPanel Sidebar;
    // End of variables declaration
}