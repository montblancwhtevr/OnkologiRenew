/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import static gui.Dashboard_dokter.DATE_FORMAT_NOW;
import static gui.Dashboard_dokter.JAM_FORMAT_NOW;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import kontrol.masterUser;
import kontrol.validasiLogin;
import model.loginAtribute;
import model.masterUserAtribute;
import model.pasienAtribute;

/**
 *
 * @author Ultramilk
 */
public class Dashboard_radiolog extends javax.swing.JFrame {

    private final String LogedUser = loginAtribute.getUsername();
    private kontrol.pasien pasienData = new kontrol.pasien();
    public ArrayList<pasienAtribute> listDataPasien = new ArrayList<>();
    private boolean checking = false;
    private kontrol.profil profil = new kontrol.profil();
    private ArrayList<masterUserAtribute> listDataUser = new ArrayList<>();
    private masterUserAtribute pAtribute = new masterUserAtribute();
    private masterUser masterUserData = new masterUser();
    private int session_id;
    private validasiLogin v = new validasiLogin();
    /**
     * Creates new form Dashboard_radiolog
     */
    public Dashboard_radiolog() {
        initComponents();
        setHeaderElement();
        
        this.updateClock = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jam.setText(nowHour());
            }
        };
        Timer t = new Timer(1000, updateClock);
        t.start();
    }
    
    ActionListener updateClock;
    
    private void setHeaderElement(){
        usrLbl.setText(LogedUser);
        waktu.setText(nowDate());
//        newPass.setVisible(false);
    }
    
    private static String nowDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    
    private static String nowHour() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(JAM_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    
    //PROFIL
    public void getProfilData(String key){
        listDataUser = (ArrayList<masterUserAtribute>) profil.getAllData(key);
        if(!listDataUser.isEmpty()){
            Object[][] data = new Object[listDataUser.size()][7];
            int i = 0;
            for(masterUserAtribute dat : listDataUser){
                data[i][0] = dat.getUsername();
                switch (dat.getRole()) {
                    case 1:
                        data[i][2] = "Admin";
                        break;
                    case 2:
                        data[i][2] = "Dokter";
                        break;
                    case 3:
                        data[i][2] = "Radiolog";
                        break;
                    case 4:
                        data[i][2] = "Laboran";
                        break;
                    
                    default:
                        data[i][2]= "-";
                        break;
                }
                
                data[i][3] = dat.getNama();
                data[i][4] = dat.getAlamat();
                data[i][5] = dat.getJk();
                data[i][1] = dat.getNo_kontak();
                data[i][6] = dat.getId();
                lblusername.setText(data[i][0].toString());
                lbljabatan.setText(data[i][2].toString());
                lblnama.setText(data[i][3].toString());
                lblalamat.setText(data[i][4].toString());
                lbljk.setText(data[i][5].toString());
                lblnokontak.setText(data[i][1].toString());
                usernameProfil.setText(data[i][0].toString());
                epNama.setText(data[i][3].toString());
                epJk.setSelectedItem(data[i][5].toString());
                epAlamat.setText(data[i][4].toString());
                epKontak.setText(data[i][1].toString());
                session_id = (int) data[i][6];
            }
        }else{
            JOptionPane.showMessageDialog(rootPane, "ERROR");
        }
    }
    //END PROFIL
    public boolean checkingData(String selected){
        listDataPasien = (ArrayList<pasienAtribute>) pasienData.getRmData(selected);
        if(!listDataPasien.isEmpty()){
            checking = true;
        }else{
            checking = false;
        }
        return false;
    }
    
    private void getUmur(Object a) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        long dd = format.parse(a.toString()).getTime();
        long d = new Date().getTime();
        long diff = d - dd;
        int umure = (int) ((diff / (1000 * 60 * 60 * 24) )/ 365) ;
        usiaTx.setText(String.valueOf(umure));
    }
    
    private void getselectedData(String selected) throws ParseException{
        listDataPasien = (ArrayList<pasienAtribute>) pasienData.getRmData(selected);
        if(!listDataPasien.isEmpty()){
            Object[][] data = new Object[listDataPasien.size()][15];
            int i = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY");
            
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
                normLabel.setText((String) data[i][0]);
                namaLabel.setText((String) data[i][1]);
                ttlLabel.setText((String) data[i][3] +","+ sdf.format(data[i][4]) );
                alamatLbl.setText((String) data[i][5]);
                agmLabel.setText((String) data[i][6]);
                sukuLabel.setText((String) data[i][7]);
                sperLabel.setText((String) data[i][8]);
//                spLabel.setText((String) data[i][9]);
                goldarLabel.setText((String) data[i][10]);
                telpLabel.setText((String) data[i][11]);
//                kehamilanLabel.setText((String) data[i][12]);
                pekerjaanLabel.setText((String) data[i][13]);
                tgldaftarLabel.setText(sdf.format(data[i][14]));
                
                if((int)data[i][2] == 1){
                    jkLabel.setText("Laki-laki");
                }else if((int)data[i][2] == 2){
                    jkLabel.setText("Perempuan");
                }
                getUmur(data[i][4]);
                checking = true;
            }
        }else{
            checking = false;
        }
    }
    
    //ANAMNESIS SET
    private void getAnamnesis(String key){
        
    }
    //END ANAMNESIS
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        infoPanel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        namaLabel = new javax.swing.JLabel();
        normLabel = new javax.swing.JLabel();
        ttlLabel = new javax.swing.JLabel();
        jkLabel = new javax.swing.JLabel();
        alamatLbl = new javax.swing.JLabel();
        sperLabel = new javax.swing.JLabel();
        agmLabel = new javax.swing.JLabel();
        sukuLabel = new javax.swing.JLabel();
        goldarLabel = new javax.swing.JLabel();
        usiaTx = new javax.swing.JLabel();
        telpLabel = new javax.swing.JLabel();
        tgldaftarLabel = new javax.swing.JLabel();
        pekerjaanLabel = new javax.swing.JLabel();
        AnamnesisPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        rpSekarang = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        rpDahulu = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        rpKeluarga = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        rpNikah = new javax.swing.JLabel();
        FisikPanel = new javax.swing.JPanel();
        labPanel = new javax.swing.JPanel();
        penunjangPanel = new javax.swing.JPanel();
        saranPanel = new javax.swing.JPanel();
        profilPane = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblnama = new javax.swing.JLabel();
        lblusername = new javax.swing.JLabel();
        lbljabatan = new javax.swing.JLabel();
        lbljk = new javax.swing.JLabel();
        lblalamat = new javax.swing.JLabel();
        lblnokontak = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel19 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        usernameProfil = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        passLama = new javax.swing.JPasswordField();
        jLabel26 = new javax.swing.JLabel();
        passBaru = new javax.swing.JPasswordField();
        jLabel27 = new javax.swing.JLabel();
        rePassBaru = new javax.swing.JPasswordField();
        jButton3 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        epNama = new javax.swing.JTextField();
        epJk = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        epAlamat = new javax.swing.JTextArea();
        jLabel31 = new javax.swing.JLabel();
        epKontak = new javax.swing.JTextField();
        epUbah = new javax.swing.JButton();
        logoutPanel = new javax.swing.JPanel();
        rmControl = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        noRM = new javax.swing.JTextField();
        tampilkanBtn = new javax.swing.JButton();
        info = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        usrLbl = new javax.swing.JLabel();
        jam = new javax.swing.JLabel();
        waktu = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        infoPanel.setLayout(new javax.swing.BoxLayout(infoPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText("Nama");

        jLabel6.setText("No Rekam Medis");

        jLabel8.setText("TTL");

        jLabel9.setText("Jenis Kelamin");

        jLabel10.setText("Suku/Ras");

        jLabel11.setText("Status Pernikahan");

        jLabel13.setText("Agama");

        jLabel14.setText("Golongan Darah");

        jLabel15.setText("Alamat");

        jLabel38.setText("Usia");

        jLabel25.setText("No Telepon");

        jLabel16.setText("Tanggal Daftar");

        jLabel17.setText("Pekerjaan");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel15)
                    .addComponent(jLabel38)
                    .addComponent(jLabel25)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addGap(13, 13, 13))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addContainerGap(447, Short.MAX_VALUE))
        );

        infoPanel.add(jPanel11);

        namaLabel.setText("-----------------------");

        normLabel.setText("-----------------------");

        ttlLabel.setText("-----------------------");

        jkLabel.setText("-----------------------");

        alamatLbl.setText("-----------------------");

        sperLabel.setText("-----------------------");

        agmLabel.setText("-----------------------");

        sukuLabel.setText("-----------------------");

        goldarLabel.setText("-----------------------");

        usiaTx.setText("-----------------------");

        telpLabel.setText("-----------------------");

        tgldaftarLabel.setText("-----------------------");

        pekerjaanLabel.setText("-----------------------");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(namaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                    .addComponent(normLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ttlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jkLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alamatLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sperLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(agmLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(goldarLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usiaTx, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(telpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                    .addComponent(tgldaftarLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pekerjaanLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(namaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(normLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ttlLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jkLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alamatLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sperLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(agmLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sukuLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goldarLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usiaTx)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(telpLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tgldaftarLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pekerjaanLabel)
                .addContainerGap(447, Short.MAX_VALUE))
        );

        infoPanel.add(jPanel10);

        jTabbedPane1.addTab("Info Pasien", infoPanel);

        AnamnesisPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                AnamnesisPanelComponentShown(evt);
            }
        });

        jLabel2.setText("Riwayat penyakit sekarang");

        jLabel3.setText("Riwayat penyakit dahulu");

        jLabel4.setText("Riwayat penyakit keluarga");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(4, 52));

        rpSekarang.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpSekarang)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpSekarang)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        rpDahulu.setText("-");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpDahulu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpDahulu)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 52));

        rpKeluarga.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpKeluarga)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpKeluarga)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLabel32.setText("Riwayat pernikahan");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 52));

        rpNikah.setText("-");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpNikah)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpNikah)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AnamnesisPanelLayout = new javax.swing.GroupLayout(AnamnesisPanel);
        AnamnesisPanel.setLayout(AnamnesisPanelLayout);
        AnamnesisPanelLayout.setHorizontalGroup(
            AnamnesisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AnamnesisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AnamnesisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 958, Short.MAX_VALUE)
                    .addGroup(AnamnesisPanelLayout.createSequentialGroup()
                        .addGroup(AnamnesisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel32)
                            .addComponent(jLabel4))
                        .addGap(0, 828, Short.MAX_VALUE)))
                .addContainerGap())
        );
        AnamnesisPanelLayout.setVerticalGroup(
            AnamnesisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AnamnesisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(8, 8, 8)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(389, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Anamnesis", AnamnesisPanel);

        javax.swing.GroupLayout FisikPanelLayout = new javax.swing.GroupLayout(FisikPanel);
        FisikPanel.setLayout(FisikPanelLayout);
        FisikPanelLayout.setHorizontalGroup(
            FisikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 978, Short.MAX_VALUE)
        );
        FisikPanelLayout.setVerticalGroup(
            FisikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Fisik", FisikPanel);

        javax.swing.GroupLayout labPanelLayout = new javax.swing.GroupLayout(labPanel);
        labPanel.setLayout(labPanelLayout);
        labPanelLayout.setHorizontalGroup(
            labPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 978, Short.MAX_VALUE)
        );
        labPanelLayout.setVerticalGroup(
            labPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Lab", labPanel);

        javax.swing.GroupLayout penunjangPanelLayout = new javax.swing.GroupLayout(penunjangPanel);
        penunjangPanel.setLayout(penunjangPanelLayout);
        penunjangPanelLayout.setHorizontalGroup(
            penunjangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 978, Short.MAX_VALUE)
        );
        penunjangPanelLayout.setVerticalGroup(
            penunjangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Penunjang", penunjangPanel);

        javax.swing.GroupLayout saranPanelLayout = new javax.swing.GroupLayout(saranPanel);
        saranPanel.setLayout(saranPanelLayout);
        saranPanelLayout.setHorizontalGroup(
            saranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 978, Short.MAX_VALUE)
        );
        saranPanelLayout.setVerticalGroup(
            saranPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Saran", saranPanel);

        profilPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                profilPaneComponentShown(evt);
            }
        });
        profilPane.setLayout(new java.awt.BorderLayout());

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Profil Pengguna"));

        jLabel18.setText("No Kontak");

        jLabel12.setText("Alamat");

        jLabel19.setText("Jenis Kelamin");

        jLabel20.setText("Jabatan");

        jLabel21.setText("Username");

        jLabel22.setText("Nama");

        lblnama.setText("-");

        lblusername.setText("-");

        lbljabatan.setText("-");

        lbljk.setText("-");

        lblalamat.setText("-");

        lblnokontak.setText("-");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20)
                    .addComponent(jLabel19)
                    .addComponent(jLabel12)
                    .addComponent(jLabel18))
                .addGap(23, 23, 23)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblnokontak)
                    .addComponent(lblalamat)
                    .addComponent(lbljk)
                    .addComponent(lbljabatan)
                    .addComponent(lblusername)
                    .addComponent(lblnama))
                .addContainerGap(834, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(lblnama))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(lblusername))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(lbljabatan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(lbljk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblalamat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(lblnokontak))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        profilPane.add(jPanel18, java.awt.BorderLayout.PAGE_START);

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Menu Profil"));

        jLabel23.setText("Username :");

        usernameProfil.setEnabled(false);

        jLabel24.setText("Password lama");

        jLabel26.setText("Password baru");

        jLabel27.setText("Ulangi password baru");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/send-fl-ic.png"))); // NOI18N
        jButton3.setText("Ubah");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel23)
                        .addComponent(usernameProfil)
                        .addComponent(jLabel24)
                        .addComponent(passLama)
                        .addComponent(jLabel26)
                        .addComponent(jLabel27)
                        .addComponent(rePassBaru, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addComponent(passBaru))
                    .addComponent(jButton3))
                .addContainerGap(750, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameProfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passLama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passBaru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rePassBaru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap(245, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Ubah password", jPanel19);

        jLabel28.setText("Nama");

        jLabel29.setText("Jenis Kelamin");

        jLabel30.setText("Alamat");

        epJk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Laki-laki", "Perempuan" }));

        epAlamat.setColumns(20);
        epAlamat.setLineWrap(true);
        epAlamat.setRows(5);
        jScrollPane3.setViewportView(epAlamat);

        jLabel31.setText("No Kontak");

        epUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/send-fl-ic.png"))); // NOI18N
        epUbah.setText("Ubah");
        epUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                epUbahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(epUbah)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(epJk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(epNama, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(epKontak, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(392, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(epNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(epJk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(epKontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(epUbah)
                .addContainerGap(249, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Edit Profil", jPanel21);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        profilPane.add(jPanel20, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Profil", profilPane);

        logoutPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                logoutPanelComponentShown(evt);
            }
        });

        javax.swing.GroupLayout logoutPanelLayout = new javax.swing.GroupLayout(logoutPanel);
        logoutPanel.setLayout(logoutPanelLayout);
        logoutPanelLayout.setHorizontalGroup(
            logoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 978, Short.MAX_VALUE)
        );
        logoutPanelLayout.setVerticalGroup(
            logoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Logout", logoutPanel);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        rmControl.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("No Rekam Medis :");

        noRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                noRMKeyReleased(evt);
            }
        });

        tampilkanBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/send-fl-ic.png"))); // NOI18N
        tampilkanBtn.setText("Tampilkan");
        tampilkanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilkanBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rmControlLayout = new javax.swing.GroupLayout(rmControl);
        rmControl.setLayout(rmControlLayout);
        rmControlLayout.setHorizontalGroup(
            rmControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rmControlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noRM, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tampilkanBtn)
                .addContainerGap(673, Short.MAX_VALUE))
        );
        rmControlLayout.setVerticalGroup(
            rmControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rmControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rmControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(noRM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tampilkanBtn))
                .addContainerGap())
        );

        getContentPane().add(rmControl, java.awt.BorderLayout.PAGE_START);

        info.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        info.setFocusCycleRoot(true);

        jLabel7.setText("Status : ");

        usrLbl.setText("-");

        jam.setText("-");

        waktu.setText("-");

        javax.swing.GroupLayout infoLayout = new javax.swing.GroupLayout(info);
        info.setLayout(infoLayout);
        infoLayout.setHorizontalGroup(
            infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usrLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 964, Short.MAX_VALUE)
                .addComponent(waktu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jam)
                .addContainerGap())
        );
        infoLayout.setVerticalGroup(
            infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoLayout.createSequentialGroup()
                .addGroup(infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(usrLbl)
                    .addComponent(jam)
                    .addComponent(waktu))
                .addGap(0, 2, Short.MAX_VALUE))
        );

        getContentPane().add(info, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_logoutPanelComponentShown
        // TODO add your handling code here:
        this.dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_logoutPanelComponentShown

    private void noRMKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noRMKeyReleased
        // TODO add your handling code here:
        
        String norm = noRM.getText();
        checkingData(norm);
        
         if(noRM.getText().isEmpty() || checking == false){
            normLabel.setText("-----------------------");
            namaLabel.setText("-----------------------");
            ttlLabel.setText("-----------------------");
            alamatLbl.setText("-----------------------");
            agmLabel.setText("-----------------------");
            sukuLabel.setText("-----------------------");
            sperLabel.setText("-----------------------");
//            spLabel.setText("-----------------------");
            goldarLabel.setText("-----------------------");
            telpLabel.setText("-----------------------");
//            kehamilanLabel.setText("-----------------------");
            pekerjaanLabel.setText("-----------------------");
            tgldaftarLabel.setText("-----------------------");
            jkLabel.setText("-----------------------");
            usiaTx.setText("-----------------------");
        }else{
            try {
                getselectedData(norm);
            } catch (ParseException ex) {
                Logger.getLogger(Dashboard_radiolog.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
    }//GEN-LAST:event_noRMKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        boolean podo,ada;
        String errorMsg = "";

        if( !Arrays.toString(passBaru.getPassword()).equals(Arrays.toString(rePassBaru.getPassword())) ){
            podo = false;
            errorMsg += "Password yang anda masukkan tidak sama ! \n";
        }else{
            podo = true;
        }

        if(  v.validatePassword(usernameProfil.getText(), String.copyValueOf(passLama.getPassword())) ){
            ada = true;
        }else{
            ada = false;
            errorMsg += "Password salah ! \n";
        }

        if(ada && podo){
            v.ubahPassword(usernameProfil.getText(), String.copyValueOf(rePassBaru.getPassword()));
            JOptionPane.showMessageDialog(rootPane, "Password berhasil diubah", "Pesan", JOptionPane.INFORMATION_MESSAGE);
            passBaru.setText("");
            rePassBaru.setText("");
            passLama.setText("");
        }else{
            JOptionPane.showMessageDialog(rootPane, errorMsg, "Warning !" , JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void epUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_epUbahActionPerformed
        // TODO add your handling code here:
        pAtribute.setNama(epNama.getText());
        pAtribute.setAlamat(epAlamat.getText());
        pAtribute.setNo_kontak(epKontak.getText());
        pAtribute.setId(session_id);
        pAtribute.setJk(epJk.getSelectedItem().toString());

        masterUserData.editProfil(pAtribute);
        getProfilData(LogedUser);
    }//GEN-LAST:event_epUbahActionPerformed

    private void profilPaneComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_profilPaneComponentShown
        // TODO add your handling code here:
        System.err.println(LogedUser);
        getProfilData(LogedUser);
    }//GEN-LAST:event_profilPaneComponentShown

    private void AnamnesisPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_AnamnesisPanelComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_AnamnesisPanelComponentShown

    private void tampilkanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilkanBtnActionPerformed
        // TODO add your handling code here:
        String key = noRM.getText();
        //SHOW ANAMNESIS
        getAnamnesis(key);
        //SHOW FISIK
        //SHOW LAB
        //SHOW PENUNJANG
        //SHOW SARAN
    }//GEN-LAST:event_tampilkanBtnActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard_radiolog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard_radiolog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard_radiolog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard_radiolog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard_radiolog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AnamnesisPanel;
    private javax.swing.JPanel FisikPanel;
    private javax.swing.JLabel agmLabel;
    private javax.swing.JLabel alamatLbl;
    private javax.swing.JTextArea epAlamat;
    private javax.swing.JComboBox<String> epJk;
    private javax.swing.JTextField epKontak;
    private javax.swing.JTextField epNama;
    private javax.swing.JButton epUbah;
    private javax.swing.JLabel goldarLabel;
    private javax.swing.JPanel info;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel jam;
    private javax.swing.JLabel jkLabel;
    private javax.swing.JPanel labPanel;
    private javax.swing.JLabel lblalamat;
    private javax.swing.JLabel lbljabatan;
    private javax.swing.JLabel lbljk;
    private javax.swing.JLabel lblnama;
    private javax.swing.JLabel lblnokontak;
    private javax.swing.JLabel lblusername;
    private javax.swing.JPanel logoutPanel;
    private javax.swing.JLabel namaLabel;
    private javax.swing.JTextField noRM;
    private javax.swing.JLabel normLabel;
    private javax.swing.JPasswordField passBaru;
    private javax.swing.JPasswordField passLama;
    private javax.swing.JLabel pekerjaanLabel;
    private javax.swing.JPanel penunjangPanel;
    private javax.swing.JPanel profilPane;
    private javax.swing.JPasswordField rePassBaru;
    private javax.swing.JPanel rmControl;
    private javax.swing.JLabel rpDahulu;
    private javax.swing.JLabel rpKeluarga;
    private javax.swing.JLabel rpNikah;
    private javax.swing.JLabel rpSekarang;
    private javax.swing.JPanel saranPanel;
    private javax.swing.JLabel sperLabel;
    private javax.swing.JLabel sukuLabel;
    private javax.swing.JButton tampilkanBtn;
    private javax.swing.JLabel telpLabel;
    private javax.swing.JLabel tgldaftarLabel;
    private javax.swing.JLabel ttlLabel;
    private javax.swing.JTextField usernameProfil;
    private javax.swing.JLabel usiaTx;
    private javax.swing.JLabel usrLbl;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
