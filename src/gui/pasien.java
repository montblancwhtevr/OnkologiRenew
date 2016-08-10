/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kontrol.validate;
import model.masterUserAtribute;
import model.pasienAtribute;

/**
 *
 * @author Ultramilk
 */
public class pasien extends javax.swing.JFrame {

    /**
     * Creates new form pasien
     */
    public kontrol.pasien pasienData = new kontrol.pasien();
    public ArrayList<masterUserAtribute> listDataUser = new ArrayList<>();
    public ArrayList<pasienAtribute> listDataPasien = new ArrayList<>();
    public static final String DATE_FORMAT_NOW = "dd MMMMM YYYY"; 
    public String searchTyped;
    public String searchPasienTyped;
    private final Date skrg = new Date();
    
    
    public pasien() {
        initComponents();
        populatePasien();
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        tgllahirTx.getDateEditor().addPropertyChangeListener(
            new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                   hitungUmur();
                }
            }
        }
        );
        
        tglskrgTx.setDate(skrg);
    }
    
    //GET NO REKAM MEDIS SELECTED 
    public String getnoRM(){
        String rmDt;
        int row = tablePasien.getSelectedRow();
        rmDt = (String) tablePasien.getValueAt(row, 1);
        return rmDt;
    }
    
    //HITUNG UMUR
    public void hitungUmur(){
        long date = tgllahirTx.getDate().getTime();
        long now = new Date().getTime();
        long diff = now - date;
        int umure = (int) ((diff / (1000 * 60 * 60 * 24) )/ 365) ;
        usiaTx.setText(String.valueOf(umure));
    }
    //END HITUNG UMUR
    
    //TABEL PASIEN
    public void populatePasien(){
        SimpleDateFormat foSimpledate = new SimpleDateFormat("dd MMMM YYYY");
        listDataPasien = (ArrayList<pasienAtribute>) pasienData.getData();
        if(!listDataPasien.isEmpty()){
            Object[][] data = new Object[listDataPasien.size()][8];
            int i = 0;
            for(pasienAtribute dat : listDataPasien){
                data[i][0] = i+1;
                data[i][1] = dat.getP_norm();
                data[i][2] = dat.getP_nama();
                data[i][3] = dat.getP_alamat();
                data[i][4] = dat.getP_tmtplahir() +","+ foSimpledate.format(dat.getP_tgllahir());
                data[i][5] = dat.getP_nokontak();
                switch(dat.getP_jk()){
                    case 1 :
                        data[i][6] = "Laki-laki";
                        break;
                    case 2 : 
                        data[i][6] = "Perempuan";
                        break;
                    default :
                        data[i][6] = "-";
                        break;
                }
                data[i][7] = dat.getP_goldar();
                i++;
            }
            
            tablePasien.addMouseListener(new MouseAdapter() {
 
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        tablePasien.getCellEditor().stopCellEditing();
                    }
                }
            });
            
            this.tablePasien.setModel(new DefaultTableModel(data, new String[]{"No","No Rekam Medis","Nama","Alamat","Tempat, Tgl Lahir","No Kontak","Jenis Kelamin","Golongan Darah"}));
            this.pasienScrollPane.setViewportView(this.tablePasien);
            tablePasien.getColumnModel().getColumn(0).setMaxWidth(40);
            this.tablePasien.setAutoCreateRowSorter(true);
        }else{
            tablePasien.setModel(new DefaultTableModel(null, new String[]{"No","No Rekam Medis","Nama","Alamat","Tempat, Tgl Lahir","No Kontak","Jenis Kelamin","Golongan Darah"}));
        }
    }
    //END TABEL PASIEN
    
    //TAMBAH PASIEN
    public void tambahPasien(){
        String selected;
        pasienAtribute pasienBaru = new pasienAtribute();
        pasienBaru.setP_nama(NamaTx.getText());
        pasienBaru.setP_norm(noRMTx.getText());
        selected = JkTx.getSelectedItem().toString();
        if(null != selected)switch (selected) {
            case "--":
                break;
            case "Laki-laki":
                pasienBaru.setP_jk(1);
                break;
            case "Perempuan":
                pasienBaru.setP_jk(2);
                break;
            default:
                break;
        }
        pasienBaru.setP_tmtplahir(tmptlahirTx.getText());
        pasienBaru.setP_tgllahir((java.util.Date)tgllahirTx.getDate());
        pasienBaru.setP_alamat( alamatTx.getText());
        pasienBaru.setP_pekerjaan(pekerjaanTx.getText());
        pasienBaru.setP_agama(agamaTx.getSelectedItem().toString());
        pasienBaru.setP_suku(sukuTx.getText());
        pasienBaru.setP_statuspernikahan(statusnikahTx.getSelectedItem().toString());
        pasienBaru.setP_status(statusPasienTx.getText());
        pasienBaru.setP_goldar(goldarTx.getSelectedItem().toString());
        pasienBaru.setP_nokontak(noHPTx.getText());
        pasienBaru.setP_riwayatkehamilan(riwayatkehamilanTx.getText());
        pasienBaru.setP_tglskrg(skrg);
        
        validate validatePasien = new validate();
//        if(validatePasien.validateInput(noRMTx.getText(), NamaTx.getText(), tmptlahirTx.getText(), alamatTx.getText(), sukuTx.getText(), statusPasienTx.getText(), noHPTx.getText(), riwayatkehamilanTx.getText(), pekerjaanTx.getText()))
//            pasienData.addPasien(pasienBaru);
//        else
//            JOptionPane.showMessageDialog(null, validatePasien.showMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    //END TAMBAH PASIEN
    
    //EDIT PASIEN
    public void editPasien(){
        String selected;
        pasienAtribute pasienBaru = new pasienAtribute();
        pasienBaru.setP_nama(NamaTx.getText());
        pasienBaru.setP_norm(noRMTx.getText());
        selected = JkTx.getSelectedItem().toString();
        if(null != selected)switch (selected) {
            case "--":
                break;
            case "Laki-laki":
                pasienBaru.setP_jk(1);
                break;
            case "Perempuan":
                pasienBaru.setP_jk(2);
                break;
            default:
                break;
        }
        pasienBaru.setP_tmtplahir(tmptlahirTx.getText());
        pasienBaru.setP_tgllahir((java.util.Date)tgllahirTx.getDate());
        pasienBaru.setP_alamat( alamatTx.getText());
        pasienBaru.setP_pekerjaan(pekerjaanTx.getText());
        pasienBaru.setP_agama(agamaTx.getSelectedItem().toString());
        pasienBaru.setP_suku(sukuTx.getText());
        pasienBaru.setP_statuspernikahan(statusnikahTx.getSelectedItem().toString());
        pasienBaru.setP_status(statusPasienTx.getText());
        pasienBaru.setP_goldar(goldarTx.getSelectedItem().toString());
        pasienBaru.setP_nokontak(noHPTx.getText());
        pasienBaru.setP_riwayatkehamilan(riwayatkehamilanTx.getText());
        pasienBaru.setP_tglskrg(skrg);
        
        validate validatePasien = new validate();
//        if(validatePasien.validateInput(noRMTx.getText(), NamaTx.getText(), tmptlahirTx.getText(), alamatTx.getText(), sukuTx.getText(), statusPasienTx.getText(), noHPTx.getText(), riwayatkehamilanTx.getText(), pekerjaanTx.getText()))
//            pasienData.editPasien(pasienBaru,getnoRM());
//        else
//            JOptionPane.showMessageDialog(null, validatePasien.showMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    //END EDIT PASIEN
    
    
    //CARI PASIEN
    public void searchPasien(String cari){
        SimpleDateFormat foSimpledate = new SimpleDateFormat("dd MMMM YYYY");
        listDataPasien = (ArrayList<pasienAtribute>) pasienData.searchPasien(cari);
        if(!listDataPasien.isEmpty()){
            Object[][] data = new Object[listDataPasien.size()][8];
            int i = 0;
            for(pasienAtribute dat : listDataPasien){
                data[i][0] = i+1;
                data[i][1] = dat.getP_norm();
                data[i][2] = dat.getP_nama();
                data[i][3] = dat.getP_alamat();
                data[i][4] = dat.getP_tmtplahir() +","+ foSimpledate.format(dat.getP_tgllahir());
                data[i][5] = dat.getP_nokontak();
                switch(dat.getP_jk()){
                    case 1 :
                        data[i][6] = "Laki-laki";
                        break;
                    case 2 : 
                        data[i][6] = "Perempuan";
                        break;
                    default :
                        data[i][6] = "-";
                        break;
                }
                data[i][7] = dat.getP_goldar();
                i++;
            }
            
            tablePasien.addMouseListener(new MouseAdapter() {
 
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        tablePasien.getCellEditor().stopCellEditing();
                    }
                }
            });
            
            this.tablePasien.setModel(new DefaultTableModel(data, new String[]{"No","No Rekam Medis","Nama","Alamat","Tempat, Tgl Lahir","No Kontak","Jenis Kelamin","Golongan Darah"}));
            this.pasienScrollPane.setViewportView(this.tablePasien);
            tablePasien.getColumnModel().getColumn(0).setMaxWidth(40);
            this.tablePasien.setAutoCreateRowSorter(true);
        }else{
            tablePasien.setModel(new DefaultTableModel(null, new String[]{"No","No Rekam Medis","Nama","Alamat","Tempat, Tgl Lahir","No Kontak","Jenis Kelamin","Golongan Darah"}));
        }
    }
    //END CARI PASIEN
    
    //GET SELECTED DATA
    public void getselectedData(String selected){
        listDataPasien = (ArrayList<pasienAtribute>) pasienData.getRmData(selected);
        if(!listDataPasien.isEmpty()){
            Object[][] data = new Object[listDataPasien.size()][15];
            int i = 0;
            for(pasienAtribute dat : listDataPasien){
                data[i][0] = dat.getP_norm();
                data[i][1] = dat.getP_nama();
                data[i][2] = dat.getP_jk();
                data[i][3] = dat.getP_tmtplahir();
                data[i][4] = dat.getP_tgllahir();
                data[i][5] = dat.getP_alamat();
                data[i][6] = dat.getP_agama();
                data[i][7] = dat.getP_suku();
                data[i][8] = dat.getP_statuspernikahan();
                data[i][9] = dat.getP_status();
                data[i][10] = dat.getP_goldar();
                data[i][11] = dat.getP_nokontak();
                data[i][12] = dat.getP_riwayatkehamilan();
                data[i][13] = dat.getP_pekerjaan();
                data[i][14] = dat.getP_tglskrg();
                NamaTx.setText((String) data[i][1]);
                JkTx.setSelectedIndex((int) data[i][2]);
                tmptlahirTx.setText((String) data[i][3]);
                tgllahirTx.setDate((Date) data[i][4]);
                alamatTx.setText((String) data[i][5]);
                pekerjaanTx.setText((String) data[i][13]);
                agamaTx.setSelectedItem(data[i][6]);
                sukuTx.setText((String) data[i][7]);
                statusnikahTx.setSelectedItem(data[i][8]);
                statusPasienTx.setText((String) data[i][9]);
                goldarTx.setSelectedItem(data[i][10]);
                noHPTx.setText((String) data[i][11]);
                tglskrgTx.setDate((Date) data[i][14]);
                noRMTx.setText((String) data[i][0]);
                riwayatkehamilanTx.setText((String) data[i][12]);
                
            }
        }else{
            System.err.println("Empty");
        }
    }
    //END DATA SELECTED
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        tambahPanel = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        NamaTx = new javax.swing.JTextField();
        JkTx = new javax.swing.JComboBox<>();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        tmptlahirTx = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        usiaTx = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        alamatTx = new javax.swing.JTextArea();
        jLabel78 = new javax.swing.JLabel();
        pekerjaanTx = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        agamaTx = new javax.swing.JComboBox<>();
        jLabel80 = new javax.swing.JLabel();
        sukuTx = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        statusnikahTx = new javax.swing.JComboBox<>();
        jLabel82 = new javax.swing.JLabel();
        statusPasienTx = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        noRMTx = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        goldarTx = new javax.swing.JComboBox<>();
        jLabel87 = new javax.swing.JLabel();
        noHPTx = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        riwayatkehamilanTx = new javax.swing.JFormattedTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        tgllahirTx = new com.toedter.calendar.JDateChooser();
        tglskrgTx = new com.toedter.calendar.JDateChooser();
        jPanel8 = new javax.swing.JPanel();
        tambahPasien = new javax.swing.JButton();
        ubahPasien = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        pasienSearchTx = new javax.swing.JTextField();
        pasienScrollPane = new javax.swing.JScrollPane();
        tablePasien = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MASTER PASIEN");

        tambahPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Pasien Baru"));
        tambahPanel.setRequestFocusEnabled(false);

        jLabel72.setText("Nama");

        JkTx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Laki-laki", "Perempuan" }));

        jLabel73.setText("Jenis Kelamin");

        jLabel74.setText("Tempat Lahir");

        jLabel75.setText("Tanggal Lahir");

        jLabel76.setText("Usia");

        usiaTx.setEditable(false);

        jLabel77.setText("Alamat");

        alamatTx.setColumns(20);
        alamatTx.setRows(5);
        jScrollPane6.setViewportView(alamatTx);

        jLabel78.setText("Pekerjaan");

        jLabel79.setText("Agama");

        agamaTx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Islam", "Kristen", "Katolik", "Hindu", "Budha", "Konghucu" }));

        jLabel80.setText("Suku/Ras");

        jLabel81.setText("Status Pernikahan");

        statusnikahTx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Menikah", "Belum Menikah", "Janda/Duda" }));

        jLabel82.setText("Status Pasien");

        jLabel83.setText("No Rekam Medis");

        jLabel84.setText("Tanggal Daftar");

        jLabel86.setText("Golongan Darah");

        goldarTx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "A", "AB", "O", "B" }));

        jLabel87.setText("No Telepon");

        jLabel88.setText("Riwayat Kehamilan");

        tambahPasien.setText("Tambah");
        tambahPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahPasienActionPerformed(evt);
            }
        });

        ubahPasien.setText("Ubah");
        ubahPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahPasienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahPasien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ubahPasien, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambahPasien)
                    .addComponent(ubahPasien))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tambahPanelLayout = new javax.swing.GroupLayout(tambahPanel);
        tambahPanel.setLayout(tambahPanelLayout);
        tambahPanelLayout.setHorizontalGroup(
            tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahPanelLayout.createSequentialGroup()
                        .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(tambahPanelLayout.createSequentialGroup()
                                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel72)
                                    .addComponent(jLabel73)
                                    .addComponent(jLabel74)
                                    .addComponent(jLabel75)
                                    .addComponent(jLabel83)
                                    .addComponent(jLabel84)
                                    .addComponent(jLabel86)
                                    .addComponent(jLabel87)
                                    .addComponent(jLabel88)
                                    .addComponent(jLabel80)
                                    .addComponent(jLabel78))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JkTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NamaTx, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tmptlahirTx, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tgllahirTx, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(usiaTx, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pekerjaanTx, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(agamaTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sukuTx, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statusnikahTx, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statusPasienTx, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(noRMTx, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tglskrgTx, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(goldarTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(noHPTx, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(riwayatkehamilanTx, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(25, 25, 25))
                    .addGroup(tambahPanelLayout.createSequentialGroup()
                        .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel79)
                            .addComponent(jLabel76)
                            .addComponent(jLabel81)
                            .addComponent(jLabel82)
                            .addComponent(jLabel77))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        tambahPanelLayout.setVerticalGroup(
            tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(NamaTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73)
                    .addComponent(JkTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(tmptlahirTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tgllahirTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel75))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(usiaTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tambahPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pekerjaanTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel78)))
                    .addComponent(jLabel77))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79)
                    .addComponent(agamaTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(sukuTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(statusnikahTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(statusPasienTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(noRMTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel84)
                    .addComponent(tglskrgTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(goldarTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(noHPTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(riwayatkehamilanTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Pasien"));

        jLabel4.setText("Cari Pasien");

        pasienSearchTx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pasienSearchTxKeyReleased(evt);
            }
        });

        tablePasien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablePasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePasienMouseClicked(evt);
            }
        });
        pasienScrollPane.setViewportView(tablePasien);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pasienScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pasienSearchTx, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(pasienSearchTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pasienScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tambahPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tambahPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahPasienActionPerformed
        tambahPasien();
        populatePasien();
    }//GEN-LAST:event_tambahPasienActionPerformed

    private void ubahPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahPasienActionPerformed
        editPasien();
        populatePasien();
    }//GEN-LAST:event_ubahPasienActionPerformed

    private void pasienSearchTxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pasienSearchTxKeyReleased
        searchPasienTyped = pasienSearchTx.getText();
        searchPasien(searchPasienTyped);
    }//GEN-LAST:event_pasienSearchTxKeyReleased

    private void tablePasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePasienMouseClicked
        getselectedData(getnoRM());
    }//GEN-LAST:event_tablePasienMouseClicked

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
            java.util.logging.Logger.getLogger(pasien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pasien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pasien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pasien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pasien().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> JkTx;
    private javax.swing.JTextField NamaTx;
    private javax.swing.JComboBox<String> agamaTx;
    private javax.swing.JTextArea alamatTx;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JComboBox<String> goldarTx;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextField noHPTx;
    private javax.swing.JTextField noRMTx;
    private javax.swing.JScrollPane pasienScrollPane;
    private javax.swing.JTextField pasienSearchTx;
    private javax.swing.JTextField pekerjaanTx;
    private javax.swing.JFormattedTextField riwayatkehamilanTx;
    private javax.swing.JTextField statusPasienTx;
    private javax.swing.JComboBox<String> statusnikahTx;
    private javax.swing.JTextField sukuTx;
    private javax.swing.JTable tablePasien;
    private javax.swing.JPanel tambahPanel;
    private javax.swing.JButton tambahPasien;
    private com.toedter.calendar.JDateChooser tgllahirTx;
    private com.toedter.calendar.JDateChooser tglskrgTx;
    private javax.swing.JTextField tmptlahirTx;
    private javax.swing.JButton ubahPasien;
    private javax.swing.JTextField usiaTx;
    // End of variables declaration//GEN-END:variables
}
