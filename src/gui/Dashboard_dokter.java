package gui;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;
import com.pixelmed.display.TextAnnotationPositioned;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import kontrol.pasien;
import kontrol.rekammedis;
import kontrol.validasiLogin;
import model.anamnesisAtribute;
import model.fisikAtribute;
import model.loginAtribute;
import model.pasienAtribute;
import model.radiologisAtribute;
import model.rmAtribute;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public class Dashboard_dokter extends javax.swing.JFrame {

    private pasien pasienData = new pasien();
    private rekammedis rekam = new rekammedis();
    public ArrayList<pasienAtribute> listDataPasien = new ArrayList<>();
    public ArrayList<rmAtribute> lisAtribute = new ArrayList<>();
    private String LogedUser = loginAtribute.getUsername();
    public static String DATE_FORMAT_NOW = "dd MMMMM YYYY"; 
    public static String JAM_FORMAT_NOW = "HH:mm:ss"; 
    private boolean checking = false;
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter JPEGFILTER = new FileNameExtensionFilter("DCM FILES", "dcm", "dicom");
    public ArrayList<rmAtribute> listRm = new ArrayList<>();
    ActionListener updateClock;
    DefaultListModel pilih = new DefaultListModel();
    detailsFrame frame = new detailsFrame();
    private validasiLogin val = new validasiLogin();
    private int idkey;
    private static final AttributeList list = new AttributeList();
         

    
    public Dashboard_dokter() {
        this.updateClock = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jam.setText(nowHour());
            }
        };
        Timer t = new Timer(1000, updateClock);
        t.start();
        
        initComponents();
        setHeaderElement();
        
        rmScrollpane.getVerticalScrollBar().setUnitIncrement(17);
        labTx.setVisible(false);
        imagePathRad.setVisible(false);
        penunjangGmbBtn.setEnabled(false);
        labGmbBtn.setEnabled(false);
        hiddenpanel.setVisible(false);
    }
    
    
    public void setHeaderElement(){
        dokterLbl.setText(LogedUser);
        waktu.setText(nowDate());
    }
    
    public static String nowDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    public static String nowHour() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(JAM_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    
    public void getHistoryList(String key){
        listRm = (ArrayList<rmAtribute>)rekam.getHistory(key);
        if(!listRm.isEmpty()){
            Object[][] data = new Object[listRm.size()][2];
//            historyList.setModel(pilih);
            int i =0;
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
            for(rmAtribute dat : listRm){
                data[i][0] = dat.getId_rm();
                data[i][1] = sdf.format(dat.getTgl_dtg());
                i++;
            }
            
            historyListTable.setModel(new DefaultTableModel(data,new String[]{"Id Rekam Medis", "Tangggal"}));
            
        }else{
            historyListTable.setModel(new DefaultTableModel(null,new String[]{"Id Rekam Medis", "Tangggal"}));
        }
    }
    
    public void getHistDetail(int id){
        lisAtribute = (ArrayList<rmAtribute>) rekam.getHistoryDetail(id);
        if(!lisAtribute.isEmpty()){
            Object[][] data = new Object[lisAtribute.size()][17];
            int i = 0 ;
            
            for(rmAtribute dat : lisAtribute){
                //Anamnesis
                data[i][0] = dat.getRp_sekarang();
                data[i][1] = dat.getRp_dahulu();
                data[i][2] = dat.getRp_keluarga();
                data[i][3] = dat.getRp_nikah();
                data[i][4] = dat.getRp_sosial();
                //Fisik
                data[i][5] = dat.getKepala();
                data[i][6] = dat.getLeher();
                data[i][7] = dat.getDada();
                data[i][8] = dat.getPerut();
                data[i][9] = dat.getEkstremitas();
                data[i][10] = dat.getSaraf();
                //Raiologis
                data[i][11] = dat.getImage_rad();
                data[i][12] = dat.getHasil_rad();
                //Rekam medis
                data[i][13] = dat.getLab();
                data[i][14] = dat.getSaran();
                data[i][15] = dat.getDiagnosis();
                data[i][16] = dat.getTerapi();
                frame.setAnamnesis(data[i][0].toString(), data[i][1].toString(), data[i][2].toString(), data[i][3].toString(), data[i][4].toString());
                frame.setFisik(data[i][5].toString(), data[i][6].toString(), data[i][7].toString(), data[i][8].toString(), data[i][9].toString(), data[i][10].toString());
                frame.setRadiologis(data[i][11].toString(), data[i][12].toString());
                frame.setRekam(data[i][13].toString(), data[i][14].toString(),data[i][15].toString(), data[i][16].toString() );
            }
        }else{
            System.err.println("Empty List");
        }
    }
    
    public boolean checkingData(String selected){
        listDataPasien = (ArrayList<pasienAtribute>) pasienData.getRmData(selected);
        if(!listDataPasien.isEmpty()){
            checking = true;
        }else{
            checking = false;
        }
        return false;
    }
    
    public void getselectedData(String selected) throws ParseException{
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
                spLabel.setText((String) data[i][9]);
                goldarLabel.setText((String) data[i][10]);
                telpLabel.setText((String) data[i][11]);
                kehamilanLabel.setText((String) data[i][12]);
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
    
    public void getUmur(Object a) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        long dd = format.parse(a.toString()).getTime();
        long d = new Date().getTime();
        long diff = d - dd;
        int umure = (int) ((diff / (1000 * 60 * 60 * 24) )/ 365) ;
        usiaTx.setText(String.valueOf(umure));
    }
    //SET REKAM MEDIS CORE
    public void getRekamMedis(){
        rmAtribute rm = new rmAtribute();
        validasiLogin val = new validasiLogin();
        LocalTime l = new LocalTime();
        DateTime d = new DateTime();
        
        rm.setTgl_dtg(d.toDate());
        rm.setP_norm(rmDokter.getText());
        rm.setLab(labTx.getText());
        rm.setSaran(saranTx.getText());
        rm.setId(val.geidUser(LogedUser));
        rm.setId_anamnesis(getAnamnesa());
        rm.setId_rad(getRadiologis());
        rm.setId_fisik(getFisik());
        rm.setDiagnosis(diagnosisTx.getText());
        rm.setTerapi(terapiTx.getText());
        
        rekam.addRekammedis(rm);
    }
    //SET TERAPI & DIAGNOSIS
    //SET RADIOLOGIS
    public int getRadiologis(){
        radiologisAtribute radiologis = new radiologisAtribute();
        radiologis.setImage_rad(imagePathRad.getText());
        radiologis.setHasil_rad(hasilRad.getText());
        
        return rekam.insertRadiologis(radiologis);
    }
    //SET ANAMNESIS
    public int getAnamnesa(){
        anamnesisAtribute anamnesis = new anamnesisAtribute();
        anamnesis.setRp_sekarang(rp_skrg.getText());
        anamnesis.setRp_dahulu(rp_dahulu.getText());
        anamnesis.setRp_keluarga(rp_kel.getText());
        anamnesis.setRp_nikah(rp_nikah.getText());
        anamnesis.setRp_sosial(rp_sosial.getText());
        
//        c.clearAnamnesa();
        return rekam.insertAnamnesis(anamnesis);
    }
    //SET FISIK
    public int getFisik(){
        fisikAtribute fisik = new fisikAtribute();
        fisik.setKepala(kepalaTx.getText());
        fisik.setLeher(leherTx.getText());
        fisik.setDada(dadaTx.getText());
        fisik.setPerut(perutTx.getText());
        fisik.setEkstremitas(ekstremitasTx.getText());
        fisik.setSaraf(sarafTx.getText());
        
        return rekam.insertFisik(fisik);
    }
    
    //EDIT FISIK
     public boolean editFisik(){
        fisikAtribute fisik = new fisikAtribute();
        fisik.setId(Integer.valueOf(id_fisik.getText()));
        fisik.setKepala(kepalaTx.getText());
        fisik.setLeher(leherTx.getText());
        fisik.setDada(dadaTx.getText());
        fisik.setPerut(perutTx.getText());
        fisik.setEkstremitas(ekstremitasTx.getText());
        fisik.setSaraf(sarafTx.getText());
        
        return rekam.editFisik(fisik);
    }
    //EDIT ANAMNESA
    public boolean editAnamnesa(){
        anamnesisAtribute anamnesis = new anamnesisAtribute();
        anamnesis.setId(Integer.valueOf(id_anamnesis.getText()));
        anamnesis.setRp_sekarang(rp_skrg.getText());
        anamnesis.setRp_dahulu(rp_dahulu.getText());
        anamnesis.setRp_keluarga(rp_kel.getText());
        anamnesis.setRp_nikah(rp_nikah.getText());
        anamnesis.setRp_sosial(rp_sosial.getText());
        
        return rekam.editAnamnesis(anamnesis);
    }
    //EDIT RADIOLOGIS
    public boolean editRadiologis(){
        radiologisAtribute radiologis = new radiologisAtribute();
        radiologis.setId_rad(Integer.valueOf(id_rad.getText()));
        radiologis.setImage_rad(imagePathRad.getText());
        radiologis.setHasil_rad(hasilRad.getText());
        
        return rekam.editRad(radiologis);
    }
    
    //EDIT REKAM MEDIS
    public boolean editRekam(){
        rmAtribute rm = new rmAtribute();
        rm.setId_rm(Integer.valueOf(id_rm.getText()));
        rm.setLab(labTx.getText());
        rm.setSaran(saranTx.getText());
        rm.setDiagnosis(diagnosisTx.getText());
        rm.setTerapi(terapiTx.getText());
        
        return rekam.editRekam(rm);
    }
    
    
    public void getDataRm(int id){
        lisAtribute = (ArrayList<rmAtribute>) rekam.getHistoryDetail(id);
        if(!lisAtribute.isEmpty()){
            Object[][] data = new Object[lisAtribute.size()][27];
            int i = 0 ;
            String path = "D://OnkologiAsset/" ;
            for(rmAtribute dat : lisAtribute){
                //Anamnesis
                data[i][0] = dat.getRp_sekarang();
                rp_skrg.setText(data[i][0].toString());
                data[i][1] = dat.getRp_dahulu();
                rp_dahulu.setText(data[i][1].toString());
                data[i][2] = dat.getRp_keluarga();
                rp_kel.setText(data[i][2].toString());
                data[i][3] = dat.getRp_nikah();
                rp_nikah.setText(data[i][3].toString());
                data[i][4] = dat.getRp_sosial();
                rp_sosial.setText(data[i][4].toString());
                //Fisik
                data[i][5] = dat.getKepala();
                kepalaTx.setText(data[i][5].toString());
                data[i][6] = dat.getLeher();
                leherTx.setText(data[i][6].toString());
                data[i][7] = dat.getDada();
                dadaTx.setText(data[i][7].toString());
                data[i][8] = dat.getPerut();
                perutTx.setText(data[i][8].toString());
                data[i][9] = dat.getEkstremitas();
                ekstremitasTx.setText(data[i][9].toString());
                data[i][10] = dat.getSaraf();
                sarafTx.setText(data[i][10].toString());
                //Raiologis
                data[i][11] = dat.getImage_rad();
                imagePathRad.setText(data[i][11].toString());
                filePenunjang.setText(path + data[i][11].toString());
                data[i][12] = dat.getHasil_rad();
                hasilRad.setText(data[i][12].toString());
                //Rekam medis
                data[i][13] = dat.getLab();
                labTx.setText(data[i][13].toString());
                fileLab.setText(path + data[i][13].toString());
                data[i][14] = dat.getSaran();
                saranTx.setText(String.valueOf(data[i][14]));
                
                //fetch id
                data[i][15] = dat.getId_anamnesis();
                id_anamnesis.setText(data[i][15].toString());
                data[i][16] = dat.getId_fisik();
                id_fisik.setText(data[i][16].toString());
                data[i][17] = dat.getId_rad();
                id_rad.setText(data[i][17].toString());
                
                //REKAM MEDIS DIAGNOSIS DAN TERAPI
                data[i][18] = dat.getDiagnosis();
                diagnosisTx.setText(String.valueOf(data[i][18]));
                data[i][19] = dat.getTerapi();
                terapiTx.setText(String.valueOf(data[i][19]));
                
            }
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

        jPanel2 = new javax.swing.JPanel();
        detailScrollpane = new javax.swing.JScrollPane();
        panelDataPasien = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        rmDokter = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        namaLabel = new javax.swing.JLabel();
        ttlLabel = new javax.swing.JLabel();
        sukuLabel = new javax.swing.JLabel();
        jkLabel = new javax.swing.JLabel();
        sperLabel = new javax.swing.JLabel();
        spLabel = new javax.swing.JLabel();
        agmLabel = new javax.swing.JLabel();
        goldarLabel = new javax.swing.JLabel();
        alamatLbl = new javax.swing.JLabel();
        normLabel = new javax.swing.JLabel();
        usiaTx = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel14 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        telpLabel = new javax.swing.JLabel();
        kehamilanLabel = new javax.swing.JLabel();
        tgldaftarLabel = new javax.swing.JLabel();
        pekerjaanLabel = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        lihatHis = new javax.swing.JButton();
        pilihBtn = new javax.swing.JButton();
        jScrollPane19 = new javax.swing.JScrollPane();
        historyListTable = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        tampilBtn = new javax.swing.JButton();
        rmScrollpane = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        rp_dahulu = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        rp_kel = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        rp_skrg = new javax.swing.JTextArea();
        jPanel20 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        rp_sosial = new javax.swing.JTextArea();
        jScrollPane17 = new javax.swing.JScrollPane();
        rp_nikah = new javax.swing.JTextArea();
        hiddenpanel = new javax.swing.JPanel();
        id_anamnesis = new javax.swing.JTextField();
        id_rad = new javax.swing.JTextField();
        id_fisik = new javax.swing.JTextField();
        id_rm = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        kepalaTx = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        leherTx = new javax.swing.JTextArea();
        jPanel17 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        perutTx = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        dadaTx = new javax.swing.JTextArea();
        jPanel18 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        ekstremitasTx = new javax.swing.JTextArea();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        sarafTx = new javax.swing.JTextArea();
        jLabel30 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        unggahBtn = new javax.swing.JToggleButton();
        labTx = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        labGmbBtn = new javax.swing.JButton();
        fileLab = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        imagePathRad = new javax.swing.JTextField();
        radChooser = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        hasilRad = new javax.swing.JTextArea();
        filePenunjang = new javax.swing.JTextField();
        penunjangGmbBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListPenunjang = new javax.swing.JList<>();
        jPanel8 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        diagnosisTx = new javax.swing.JTextArea();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        terapiTx = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        saranTx = new javax.swing.JTextArea();
        jPanel10 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        simpanBtn = new javax.swing.JButton();
        ubahBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dokterLbl = new javax.swing.JLabel();
        waktu = new javax.swing.JLabel();
        jam = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        profil = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard Dokter");
        setResizable(false);

        panelDataPasien.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel4.setText("No Rekam Medis");

        rmDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rmDokterKeyReleased(evt);
            }
        });

        jLabel5.setText("Nama");

        jLabel6.setText("No Rekam Medis");

        jLabel7.setText("TTL");

        jLabel8.setText("Jenis Kelamin");

        jLabel9.setText("Suku/Ras");

        jLabel10.setText("Status Pernikahan");

        jLabel11.setText("Status Pasien");

        jLabel12.setText("Agama");

        jLabel14.setText("Golongan Darah");

        jLabel15.setText("Alamat");

        jLabel38.setText("Usia");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel15)
                    .addComponent(jLabel38))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        namaLabel.setText("-----------------------");

        ttlLabel.setText("-----------------------");

        sukuLabel.setText("-----------------------");

        jkLabel.setText("-----------------------");

        sperLabel.setText("-----------------------");

        spLabel.setText("-----------------------");

        agmLabel.setText("-----------------------");

        goldarLabel.setText("-----------------------");

        alamatLbl.setText("-----------------------");

        normLabel.setText("-----------------------");

        usiaTx.setText("-----------------------");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(namaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(normLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ttlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jkLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(alamatLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sperLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(agmLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sukuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(goldarLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usiaTx, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
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
                .addComponent(spLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(agmLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sukuLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goldarLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usiaTx)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel25.setText("No Telepon");

        jLabel26.setText("Riwayat Kehamilan");

        jLabel13.setText("Tanggal Daftar");

        jLabel16.setText("Pekerjaan");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel13)
                    .addComponent(jLabel16))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        telpLabel.setText("-----------------------");

        kehamilanLabel.setText("-----------------------");

        tgldaftarLabel.setText("-----------------------");

        pekerjaanLabel.setText("-----------------------");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(telpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                    .addComponent(kehamilanLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tgldaftarLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pekerjaanLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(telpLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kehamilanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tgldaftarLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pekerjaanLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel22.setText("Riwayat Rekam Medis");

        lihatHis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/detail-ic.png"))); // NOI18N
        lihatHis.setText("Lihat");
        lihatHis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lihatHisActionPerformed(evt);
            }
        });

        pilihBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/select.png"))); // NOI18N
        pilihBtn.setText("Pilih");
        pilihBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilihBtnActionPerformed(evt);
            }
        });

        historyListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Rekam Medis", "Tanggal"
            }
        ));
        jScrollPane19.setViewportView(historyListTable);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lihatHis, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                            .addComponent(pilihBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel22))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(lihatHis)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pilihBtn)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        tampilBtn.setText("Tampilkan");
        tampilBtn.setToolTipText("tampilkan informasi dari pasien");
        tampilBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDataPasienLayout = new javax.swing.GroupLayout(panelDataPasien);
        panelDataPasien.setLayout(panelDataPasienLayout);
        panelDataPasienLayout.setHorizontalGroup(
            panelDataPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataPasienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDataPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDataPasienLayout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelDataPasienLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rmDokter, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tampilBtn)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelDataPasienLayout.setVerticalGroup(
            panelDataPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDataPasienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDataPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rmDokter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tampilBtn))
                .addGap(10, 10, 10)
                .addGroup(panelDataPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2))
                .addContainerGap())
        );

        detailScrollpane.setViewportView(panelDataPasien);

        rmScrollpane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1000, 250));

        rp_dahulu.setColumns(20);
        rp_dahulu.setRows(5);
        jScrollPane18.setViewportView(rp_dahulu);

        jLabel19.setText("Riwayat Penyakit Keluarga");

        rp_kel.setColumns(20);
        rp_kel.setRows(5);
        jScrollPane14.setViewportView(rp_kel);

        jLabel17.setText("Riwayat Penyakit Sekarang");

        jLabel18.setText("Riwayat Penyakit Dahulu");

        rp_skrg.setColumns(20);
        rp_skrg.setRows(5);
        jScrollPane13.setViewportView(rp_skrg);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel20.setText("Riwayat Sosial");

        jLabel21.setText("Riwayat Pernikahan");

        rp_sosial.setColumns(20);
        rp_sosial.setRows(5);
        jScrollPane15.setViewportView(rp_sosial);

        rp_nikah.setColumns(20);
        rp_nikah.setRows(5);
        jScrollPane17.setViewportView(rp_nikah);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout hiddenpanelLayout = new javax.swing.GroupLayout(hiddenpanel);
        hiddenpanel.setLayout(hiddenpanelLayout);
        hiddenpanelLayout.setHorizontalGroup(
            hiddenpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hiddenpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(hiddenpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hiddenpanelLayout.createSequentialGroup()
                        .addGroup(hiddenpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(id_rad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id_fisik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(hiddenpanelLayout.createSequentialGroup()
                        .addComponent(id_anamnesis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(id_rm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        hiddenpanelLayout.setVerticalGroup(
            hiddenpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hiddenpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(hiddenpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id_anamnesis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id_rm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(id_rad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(id_fisik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hiddenpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(145, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hiddenpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Anamnesis", jPanel5);

        jPanel12.setPreferredSize(new java.awt.Dimension(320, 250));

        jLabel23.setText("Kepala");

        jLabel29.setText("Leher");

        kepalaTx.setColumns(20);
        kepalaTx.setRows(3);
        jScrollPane2.setViewportView(kepalaTx);

        leherTx.setColumns(20);
        leherTx.setRows(3);
        jScrollPane3.setViewportView(leherTx);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel29))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setPreferredSize(new java.awt.Dimension(320, 250));

        jLabel24.setText("Dada");

        jLabel27.setText("Perut");

        perutTx.setColumns(20);
        perutTx.setRows(3);
        jScrollPane4.setViewportView(perutTx);

        dadaTx.setColumns(20);
        dadaTx.setRows(3);
        jScrollPane5.setViewportView(dadaTx);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(jLabel27))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel28.setText("Ekstermitas");

        ekstremitasTx.setColumns(20);
        ekstremitasTx.setRows(3);
        jScrollPane7.setViewportView(ekstremitasTx);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        sarafTx.setColumns(20);
        sarafTx.setRows(3);
        jScrollPane6.setViewportView(sarafTx);

        jLabel30.setText("Saraf");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel12, jPanel17});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pemeriksaan Fisik", jPanel6);

        unggahBtn.setText("...");
        unggahBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unggahBtnActionPerformed(evt);
            }
        });

        jLabel34.setText("File Pemeriksaaan Lab :");

        labGmbBtn.setText("Lihat Gambar");
        labGmbBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labGmbBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(fileLab, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unggahBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labGmbBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labTx, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(533, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unggahBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labGmbBtn)
                .addContainerGap(345, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Pemeriksaan Lab", jPanel7);

        radChooser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/upload-icon.png"))); // NOI18N
        radChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radChooserActionPerformed(evt);
            }
        });

        jLabel35.setText("File Penunjang lain :");

        jLabel37.setText("Hasil :");

        hasilRad.setColumns(20);
        hasilRad.setRows(5);
        jScrollPane11.setViewportView(hasilRad);

        penunjangGmbBtn.setText("Lihat Gambar");
        penunjangGmbBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penunjangGmbBtnActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jListPenunjang);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel35)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(penunjangGmbBtn)
                            .addComponent(radChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(imagePathRad, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filePenunjang, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(410, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(radChooser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(penunjangGmbBtn))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imagePathRad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filePenunjang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(129, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Penunjang Lain", jPanel19);

        jLabel32.setText("Diagnosis");

        diagnosisTx.setColumns(20);
        diagnosisTx.setRows(5);
        jScrollPane9.setViewportView(diagnosisTx);

        jLabel36.setText("Terapi");

        terapiTx.setColumns(20);
        terapiTx.setRows(5);
        jScrollPane10.setViewportView(terapiTx);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel32)
                    .addComponent(jLabel36)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                    .addComponent(jScrollPane10))
                .addContainerGap(487, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel36)
                .addGap(9, 9, 9)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Diagnosis & Terapi", jPanel8);

        jLabel31.setText("Saran");

        saranTx.setColumns(20);
        saranTx.setRows(5);
        jScrollPane8.setViewportView(saranTx);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(576, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Saran", jPanel9);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        rmScrollpane.setViewportView(jPanel4);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/sweep-ic.png"))); // NOI18N
        jButton1.setText("Batal");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        simpanBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/send-ic.png"))); // NOI18N
        simpanBtn.setText("Simpan");
        simpanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanBtnActionPerformed(evt);
            }
        });

        ubahBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pencil-ic.png"))); // NOI18N
        ubahBtn.setText("Ubah");
        ubahBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ubahBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(simpanBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(simpanBtn)
                    .addComponent(jButton1)
                    .addComponent(ubahBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detailScrollpane)
                    .addComponent(rmScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(detailScrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rmScrollpane, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);

        jLabel1.setText("Login :");

        dokterLbl.setText("-");

        waktu.setText("Tanggal");

        jam.setText("jam");

        jLabel33.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dokterLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(waktu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jam)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dokterLbl)
                    .addComponent(waktu)
                    .addComponent(jam)
                    .addComponent(jLabel33)))
        );

        jMenu1.setText("Menu");

        profil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/profil-ic.png"))); // NOI18N
        profil.setText("Profil");
        profil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profilActionPerformed(evt);
            }
        });
        jMenu1.add(profil);
        jMenu1.add(jSeparator3);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/power-ic.png"))); // NOI18N
        jMenuItem2.setText("Logout");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Rekam Medis");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setBounds(0, 0, 1096, 758);
    }// </editor-fold>//GEN-END:initComponents

    private void rmDokterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rmDokterKeyReleased
        String norm = rmDokter.getText();
        checkingData(norm);
        
        if(rmDokter.getText().isEmpty() || checking == false){
            normLabel.setText("-----------------------");
            namaLabel.setText("-----------------------");
            ttlLabel.setText("-----------------------");
            alamatLbl.setText("-----------------------");
            agmLabel.setText("-----------------------");
            sukuLabel.setText("-----------------------");
            sperLabel.setText("-----------------------");
            spLabel.setText("-----------------------");
            goldarLabel.setText("-----------------------");
            telpLabel.setText("-----------------------");
            kehamilanLabel.setText("-----------------------");
            pekerjaanLabel.setText("-----------------------");
            tgldaftarLabel.setText("-----------------------");
            jkLabel.setText("-----------------------");
            usiaTx.setText("-----------------------");
            historyListTable.setModel(new DefaultTableModel(null,new String[]{"Id Rekam Medis", "Tangggal"}));
        }
        
        if(pilih.isEmpty()){
            tampilBtn.setEnabled(true);
        }else if(!pilih.isEmpty() && checking == false){
            tampilBtn.setEnabled(false);
        }
    }//GEN-LAST:event_rmDokterKeyReleased

    private void simpanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanBtnActionPerformed
        getRekamMedis();
        rmDokter.setText("");
        //ANAMNESIS NULL
        rp_skrg.setText("");
        rp_dahulu.setText("");
        rp_kel.setText("");
        rp_nikah.setText("");
        rp_sosial.setText("");

        //RAD NULL
        hasilRad.setText("");
        imagePathRad.setText("");
        filePenunjang.setText("");

        //FISIK null
        kepalaTx.setText("");
        leherTx.setText("");
        dadaTx.setText("");
        perutTx.setText("");
        ekstremitasTx.setText("");
        sarafTx.setText("");

        //RM null
        saranTx.setText("");
        labTx.setText("");
        fileLab.setText("");
        diagnosisTx.setText("");
        terapiTx.setText("");
        simpanBtn.setEnabled(true);
        
        normLabel.setText("-----------------------");
        namaLabel.setText("-----------------------");
        ttlLabel.setText("-----------------------");
        alamatLbl.setText("-----------------------");
        agmLabel.setText("-----------------------");
        sukuLabel.setText("-----------------------");
        sperLabel.setText("-----------------------");
        spLabel.setText("-----------------------");
        goldarLabel.setText("-----------------------");
        telpLabel.setText("-----------------------");
        kehamilanLabel.setText("-----------------------");
        pekerjaanLabel.setText("-----------------------");
        tgldaftarLabel.setText("-----------------------");
        jkLabel.setText("-----------------------");
        usiaTx.setText("-----------------------");
        historyListTable.setModel(new DefaultTableModel(null,new String[]{"Id Rekam Medis", "Tangggal"}));
        
    }//GEN-LAST:event_simpanBtnActionPerformed

    private void lihatHisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lihatHisActionPerformed
        SimpleDateFormat n = new SimpleDateFormat("dd MMMM yyyy");
        String selected = historyListTable.getValueAt(historyListTable.getSelectedRow(), 0).toString();
        String dateSelected = historyListTable.getValueAt(historyListTable.getSelectedRow(), 1).toString();
//        System.err.println("Selected Value : " + selected);
        try {
            Date date = n.parse(dateSelected);
            frame.initTitle("Rekam Medis Tanggal : " + n.format(date));
            frame.setVisible(true);
            getHistDetail(Integer.valueOf(selected));
        } catch (ParseException ex) {
            Logger.getLogger(Dashboard_dokter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lihatHisActionPerformed

    private void tampilBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilBtnActionPerformed
        String norm = rmDokter.getText();
        
        try {
            
            if(pilih.isEmpty() || checking == false){
                getselectedData(norm);
                getHistoryList(norm);
            }else{
                tampilBtn.setEnabled(false);
            }
          
            
        } catch (ParseException ex) {
            Logger.getLogger(Dashboard_dokter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tampilBtnActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        this.dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void radChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radChooserActionPerformed
//        chooser.addChoosableFileFilter(JPEGFILTER);
//        int ret = chooser.showSaveDialog(this);
//        if(ret == JFileChooser.APPROVE_OPTION){
//            try {
//                File f = chooser.getSelectedFile();
//                File Desc = new File("D:\\OnkologiAsset");
//                imagePathRad.setText(f.getName());
//                filePenunjang.setText(f.getAbsolutePath());
//                FileUtils.copyFileToDirectory(f, Desc);
//            } catch (IOException ex) {
//                Logger.getLogger(Dashboard_dokter.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        jListPenunjang.setModel(pilih);
        pilih.removeAllElements();
        String tochar = "";
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(JPEGFILTER);
        int up = chooser.showSaveDialog(this);
        if(up == JFileChooser.APPROVE_OPTION){
            try{
                File[] files =  chooser.getSelectedFiles();
                File Dest = new File("D:\\OnkologiAsset");
                for (int i = 0; i < files.length; i++) {
                    System.err.println(files[i]);
                    tochar += files[i].getName();
                    pilih.addElement(files[i].getName());
                    FileUtils.copyFileToDirectory(files[i], Dest);
                    if(i < files.length - 1){
                        tochar += ",";
                    }
                }
                filePenunjang.setText(tochar);
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
        
        
        if(!filePenunjang.getText().isEmpty()){
            penunjangGmbBtn.setEnabled(true);
        }else{
            penunjangGmbBtn.setEnabled(false);
        }
    }//GEN-LAST:event_radChooserActionPerformed

    private void unggahBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unggahBtnActionPerformed
        chooser.addChoosableFileFilter(JPEGFILTER);
        int ret = chooser.showSaveDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION){
            try {
                File f = chooser.getSelectedFile();
                File Desc = new File("D:\\OnkologiAsset");
                labTx.setText(f.getName());
                fileLab.setText(f.getAbsolutePath());
                FileUtils.copyFileToDirectory(f, Desc);
            } catch (IOException ex) {
                Logger.getLogger(Dashboard_dokter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(!fileLab.getText().isEmpty()){
            labGmbBtn.setEnabled(true);
        }else{
            labGmbBtn.setEnabled(false);
        }
    }//GEN-LAST:event_unggahBtnActionPerformed

    private void profilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profilActionPerformed
        profil p = new profil();
        p.setVisible(true);
        p.getSeesionDt(val.geidUser(LogedUser),LogedUser);
    }//GEN-LAST:event_profilActionPerformed

    private void pilihBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pilihBtnActionPerformed
        String selected = historyListTable.getValueAt(historyListTable.getSelectedRow(), 0).toString();
        idkey = Integer.valueOf(selected);
        id_rm.setText(selected);
        simpanBtn.setEnabled(false);
        penunjangGmbBtn.setEnabled(true);
        labGmbBtn.setEnabled(true);
        getDataRm(Integer.valueOf(selected));
    }//GEN-LAST:event_pilihBtnActionPerformed

    private void ubahBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahBtnActionPerformed
        if(editAnamnesa() && editFisik() && editRadiologis() && editRekam()){
            simpanBtn.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Rekam medis Berhasil di ubah", "Info", JOptionPane.INFORMATION_MESSAGE);
            //ANAMNESIS NULL
            rp_skrg.setText("");
            rp_dahulu.setText("");
            rp_kel.setText("");
            rp_nikah.setText("");
            rp_sosial.setText("");
            
            //RAD NULL
            hasilRad.setText("");
            imagePathRad.setText("");
            filePenunjang.setText("");
            
            //FISIK null
            kepalaTx.setText("");
            leherTx.setText("");
            dadaTx.setText("");
            perutTx.setText("");
            ekstremitasTx.setText("");
            sarafTx.setText("");
            
            //RM null
            saranTx.setText("");
            labTx.setText("");
            fileLab.setText("");
            diagnosisTx.setText("");
            terapiTx.setText("");
        }else{
            simpanBtn.setEnabled(false);
        }
    }//GEN-LAST:event_ubahBtnActionPerformed

    private void penunjangGmbBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penunjangGmbBtnActionPerformed
        try {
            String dest = "D:\\OnkologiAsset\\";
            dest += jListPenunjang.getSelectedValue();

            JFrame p = new JFrame();
            p.setTitle(dest);
            
            list.read(dest);
            
            SourceImage img = new SourceImage(dest);
            SingleImagePanel s = new SingleImagePanel(img);
            
            s.setSideAndViewAnnotationString(getTagInformation(TagFromName.PatientName),0, "SansSerif",Font.BOLD, 14, Color.WHITE,true);
            
            p.add(s);
            p.getContentPane().setBackground(Color.BLACK);
            p.setSize(img.getWidth()/4,img.getHeight()/4);
            p.setVisible(true);
            
        } catch (IOException ex) {
            Logger.getLogger(Dashboard_dokter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DicomException ex) {
            Logger.getLogger(Dashboard_dokter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_penunjangGmbBtnActionPerformed
    
   
    private static String getTagInformation(AttributeTag attrTag) {
	return Attribute.getDelimitedStringValuesOrEmptyString(list, attrTag);
    }
    
    private void labGmbBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labGmbBtnActionPerformed
        imageTemp i = new imageTemp();
        i.setVisible(true);
        i.setTitle("Gambar : " + fileLab.getText());
        i.setTempLabelDokter(fileLab.getText());
    }//GEN-LAST:event_labGmbBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        rmDokter.setText("");
        //ANAMNESIS NULL
        rp_skrg.setText("");
        rp_dahulu.setText("");
        rp_kel.setText("");
        rp_nikah.setText("");
        rp_sosial.setText("");

        //RAD NULL
        hasilRad.setText("");
        imagePathRad.setText("");
        filePenunjang.setText("");

        //FISIK null
        kepalaTx.setText("");
        leherTx.setText("");
        dadaTx.setText("");
        perutTx.setText("");
        ekstremitasTx.setText("");
        sarafTx.setText("");

        //RM null
        saranTx.setText("");
        labTx.setText("");
        fileLab.setText("");
        diagnosisTx.setText("");
        terapiTx.setText("");
        simpanBtn.setEnabled(true);
        
        normLabel.setText("-----------------------");
        namaLabel.setText("-----------------------");
        ttlLabel.setText("-----------------------");
        alamatLbl.setText("-----------------------");
        agmLabel.setText("-----------------------");
        sukuLabel.setText("-----------------------");
        sperLabel.setText("-----------------------");
        spLabel.setText("-----------------------");
        goldarLabel.setText("-----------------------");
        telpLabel.setText("-----------------------");
        kehamilanLabel.setText("-----------------------");
        pekerjaanLabel.setText("-----------------------");
        tgldaftarLabel.setText("-----------------------");
        jkLabel.setText("-----------------------");
        usiaTx.setText("-----------------------");
        historyListTable.setModel(new DefaultTableModel(null,new String[]{"Id Rekam Medis", "Tangggal"}));
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard_dokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard_dokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard_dokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard_dokter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard_dokter().setVisible(true);
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel agmLabel;
    private javax.swing.JLabel alamatLbl;
    private javax.swing.JTextArea dadaTx;
    private javax.swing.JScrollPane detailScrollpane;
    private javax.swing.JTextArea diagnosisTx;
    private javax.swing.JLabel dokterLbl;
    private javax.swing.JTextArea ekstremitasTx;
    private javax.swing.JTextField fileLab;
    private javax.swing.JTextField filePenunjang;
    private javax.swing.JLabel goldarLabel;
    private javax.swing.JTextArea hasilRad;
    private javax.swing.JPanel hiddenpanel;
    private javax.swing.JTable historyListTable;
    private javax.swing.JTextField id_anamnesis;
    private javax.swing.JTextField id_fisik;
    private javax.swing.JTextField id_rad;
    private javax.swing.JTextField id_rm;
    private javax.swing.JTextField imagePathRad;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jListPenunjang;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jam;
    private javax.swing.JLabel jkLabel;
    private javax.swing.JLabel kehamilanLabel;
    private javax.swing.JTextArea kepalaTx;
    private javax.swing.JButton labGmbBtn;
    private javax.swing.JTextField labTx;
    private javax.swing.JTextArea leherTx;
    public static javax.swing.JButton lihatHis;
    private javax.swing.JLabel namaLabel;
    private javax.swing.JLabel normLabel;
    private javax.swing.JPanel panelDataPasien;
    private javax.swing.JLabel pekerjaanLabel;
    private javax.swing.JButton penunjangGmbBtn;
    private javax.swing.JTextArea perutTx;
    private javax.swing.JButton pilihBtn;
    private javax.swing.JMenuItem profil;
    private javax.swing.JButton radChooser;
    private javax.swing.JTextField rmDokter;
    private javax.swing.JScrollPane rmScrollpane;
    public static javax.swing.JTextArea rp_dahulu;
    public static javax.swing.JTextArea rp_kel;
    public static javax.swing.JTextArea rp_nikah;
    public static javax.swing.JTextArea rp_skrg;
    public static javax.swing.JTextArea rp_sosial;
    private javax.swing.JTextArea sarafTx;
    private javax.swing.JTextArea saranTx;
    private javax.swing.JButton simpanBtn;
    private javax.swing.JLabel spLabel;
    private javax.swing.JLabel sperLabel;
    private javax.swing.JLabel sukuLabel;
    private javax.swing.JButton tampilBtn;
    private javax.swing.JLabel telpLabel;
    private javax.swing.JTextArea terapiTx;
    private javax.swing.JLabel tgldaftarLabel;
    private javax.swing.JLabel ttlLabel;
    private javax.swing.JButton ubahBtn;
    private javax.swing.JToggleButton unggahBtn;
    private javax.swing.JLabel usiaTx;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables
}
