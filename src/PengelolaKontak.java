
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PengelolaKontak extends javax.swing.JFrame {
    
    private void loadKontakData() {
        // Hapus semua data di model tabel
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) daftarTable.getModel();
        model.setRowCount(0);  // Clear the existing rows

        // Ambil data dari database dan tambahkan ke model tabel
        String sql = "SELECT * FROM kontak";
        try (Connection conn = PengelolaKontak.connect();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            // Tambahkan data ke model tabel
            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String nomor = rs.getString("nomor");
                String kategori = rs.getString("kategori");
                model.addRow(new Object[]{id, nama, nomor, kategori});
            }
        } catch (SQLException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    
    private void createTableIfNotExists() {
        // SQL untuk membuat tabel jika belum ada
        String sql = "CREATE TABLE IF NOT EXISTS kontak ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "nama TEXT NOT NULL, "
                   + "nomor TEXT NOT NULL, "
                   + "kategori TEXT NOT NULL"
                   + ");";

        try (Connection conn = PengelolaKontak.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
            System.out.println("Tabel kontak berhasil dibuat atau sudah ada.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
    
    private void eksporKontakKeCSV(String filePath) {
        try (Connection conn = PengelolaKontak.connect();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM kontak");
             java.io.FileWriter csvWriter = new java.io.FileWriter(filePath)) {

            // Tulis header
            csvWriter.append("ID, Nama, Nomor, Kategori\n");

            // Tulis data
            while (rs.next()) {
                csvWriter.append(rs.getInt("id") + ",");
                csvWriter.append(rs.getString("nama") + ",");
                csvWriter.append(rs.getString("nomor") + ",");
                csvWriter.append(rs.getString("kategori") + "\n");
            }

            csvWriter.flush();
            javax.swing.JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke " + filePath);

        } catch (Exception e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    private void imporKontakDariCSV(String filePath) {
        try (java.io.BufferedReader csvReader = new java.io.BufferedReader(new java.io.FileReader(filePath));
             Connection conn = PengelolaKontak.connect()) {

            String row;
            csvReader.readLine(); // Lewati header
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String nama = data[1];
                String nomor = data[2];
                String kategori = data[3];

                // Masukkan data ke database
                String sql = "INSERT INTO kontak(nama, nomor, kategori) VALUES(?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nama);
                    pstmt.setString(2, nomor);
                    pstmt.setString(3, kategori);
                    pstmt.executeUpdate();
                }
            }
            javax.swing.JOptionPane.showMessageDialog(this, "Data berhasil diimpor dari " + filePath);

            // Refresh data di JTable
            loadKontakData();

        } catch (Exception e) {
            System.out.println("Error importing from CSV: " + e.getMessage());
        }
    }


    private static final String URL = "jdbc:sqlite:pengelolakontak.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Koneksi ke database berhasil.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public PengelolaKontak() {
        initComponents();
        createTableIfNotExists(); // Buat tabel jika belum ada
        loadKontakData();
    }
    
    public void tambahKontak(String nama, String nomor, String kategori) {
        String sql = "INSERT INTO kontak(nama, nomor, kategori) VALUES(?, ?, ?)";

        try (Connection conn = PengelolaKontak.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nama);
            pstmt.setString(2, nomor);
            pstmt.setString(3, kategori);
            pstmt.executeUpdate();
            System.out.println("Kontak berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void editKontak(int id, String nama, String nomor, String kategori) {
        String sql = "UPDATE kontak SET nama = ?, nomor = ?, kategori = ? WHERE id = ?";

        try (Connection conn = PengelolaKontak.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nama);
            pstmt.setString(2, nomor);
            pstmt.setString(3, kategori);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Kontak berhasil diubah.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void hapusKontak(int id) {
        String sql = "DELETE FROM kontak WHERE id = ?";

        try (Connection conn = PengelolaKontak.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Kontak berhasil dihapus.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        namaTextField = new javax.swing.JTextField();
        tambahButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        hapusButton = new javax.swing.JButton();
        cariButton = new javax.swing.JButton();
        kategoriComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        daftarTable = new javax.swing.JTable();
        nomorTextField = new javax.swing.JTextField();
        cariTextField = new javax.swing.JTextField();
        importButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nama");

        jLabel2.setText("Nomor");

        jLabel3.setText("Kategori");

        tambahButton.setText("Tambah");
        tambahButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahButtonActionPerformed(evt);
            }
        });

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        hapusButton.setText("Hapus");
        hapusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusButtonActionPerformed(evt);
            }
        });

        cariButton.setText("Cari");
        cariButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariButtonActionPerformed(evt);
            }
        });

        kategoriComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih", "Keluarga", "Teman", "Kerja" }));

        daftarTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "nama", "nomor", "kategori"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(daftarTable);

        importButton.setText("Import");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        exportButton.setText("Export");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel4.setText("APLIKASI PENGELOLA KONTAK");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 693, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(importButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(exportButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(namaTextField)
                                    .addComponent(kategoriComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nomorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(206, 206, 206)
                                .addComponent(cariTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cariButton, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tambahButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(hapusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(213, 213, 213)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel4)
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(nomorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tambahButton)
                        .addComponent(hapusButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kategoriComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editButton)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cariButton)
                    .addComponent(cariTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importButton)
                    .addComponent(exportButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tambahButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahButtonActionPerformed
    // Ambil data dari text field dan combo box
    String nama = namaTextField.getText();
    String nomor = nomorTextField.getText();
    String kategori = (String) kategoriComboBox.getSelectedItem();

    // Validasi input (pastikan nomor hanya angka)
    if (!nomor.matches("\\d+")) {
        javax.swing.JOptionPane.showMessageDialog(this, "Nomor telepon harus berupa angka.");
        return;
    }
    
    if (nomor.length() > 15) {
        javax.swing.JOptionPane.showMessageDialog(this, "Nomor telepon tidak boleh lebih dari 15 digit.");
        return;
    }

    // Tambahkan kontak ke database
    tambahKontak(nama, nomor, kategori);

    // Refresh tampilan tabel (bisa dibuat metode untuk memuat ulang data)
    loadKontakData();
    }//GEN-LAST:event_tambahButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
         // Ambil baris yang dipilih di tabel
    int selectedRow = daftarTable.getSelectedRow();
    if (selectedRow != -1) {
        // Dapatkan data dari baris yang dipilih
        int id = (int) daftarTable.getValueAt(selectedRow, 0);
        String nama = namaTextField.getText();
        String nomor = nomorTextField.getText();
        String kategori = (String) kategoriComboBox.getSelectedItem();

        // Validasi input (pastikan nomor hanya angka)
        if (!nomor.matches("\\d+")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Nomor telepon harus berupa angka.");
            return;
        }

        // Perbarui kontak di database
        editKontak(id, nama, nomor, kategori);

        // Refresh tampilan tabel
        loadKontakData();
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Pilih kontak yang ingin diedit.");
    }
    }//GEN-LAST:event_editButtonActionPerformed

    private void hapusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusButtonActionPerformed
            // Ambil baris yang dipilih di tabel
    int selectedRow = daftarTable.getSelectedRow();
    if (selectedRow != -1) {
        // Dapatkan ID kontak yang dipilih
        int id = (int) daftarTable.getValueAt(selectedRow, 0);

        // Konfirmasi penghapusan
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus kontak ini?");
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            // Hapus kontak dari database
            hapusKontak(id);

            // Refresh tampilan tabel
            loadKontakData();
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Pilih kontak yang ingin dihapus.");
    }
    }//GEN-LAST:event_hapusButtonActionPerformed

    private void cariButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariButtonActionPerformed
        String keyword = cariTextField.getText().trim();
    if (keyword.isEmpty()) {
        loadKontakData(); // Jika pencarian kosong, muat semua data
        return;
    }

    // Hapus semua data di model tabel
    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) daftarTable.getModel();
    model.setRowCount(0);

    // SQL untuk mencari kontak berdasarkan nama atau nomor
    String sql = "SELECT * FROM kontak WHERE nama LIKE ? OR nomor LIKE ?";

    try (Connection conn = PengelolaKontak.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, "%" + keyword + "%");
        pstmt.setString(2, "%" + keyword + "%");
        java.sql.ResultSet rs = pstmt.executeQuery();

        // Tambahkan hasil pencarian ke model tabel
        while (rs.next()) {
            int id = rs.getInt("id");
            String nama = rs.getString("nama");
            String nomor = rs.getString("nomor");
            String kategori = rs.getString("kategori");
            model.addRow(new Object[]{id, nama, nomor, kategori});
        }
    } catch (SQLException e) {
        System.out.println("Error searching data: " + e.getMessage());
    }
    }//GEN-LAST:event_cariButtonActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        // Pilih lokasi file untuk menyimpan CSV
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        if (fileChooser.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".csv";
            eksporKontakKeCSV(filePath);
        }
    }//GEN-LAST:event_exportButtonActionPerformed

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        // Pilih file CSV untuk diimpor
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        if (fileChooser.showOpenDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            imporKontakDariCSV(filePath);
        }
    }//GEN-LAST:event_importButtonActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PengelolaKontak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PengelolaKontak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PengelolaKontak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PengelolaKontak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PengelolaKontak().setVisible(true);
            }
        });
    }
    
    






    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cariButton;
    private javax.swing.JTextField cariTextField;
    private javax.swing.JTable daftarTable;
    private javax.swing.JButton editButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JButton hapusButton;
    private javax.swing.JButton importButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> kategoriComboBox;
    private javax.swing.JTextField namaTextField;
    private javax.swing.JTextField nomorTextField;
    private javax.swing.JButton tambahButton;
    // End of variables declaration//GEN-END:variables

}
