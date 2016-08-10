
package gui;

import static gui.Dashboard_dokter.JAM_FORMAT_NOW;
import static gui.pengguna.newRole;
import static gui.pengguna.newUsername;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import kontrol.addUser;
import kontrol.biaya;
import kontrol.masterUser;
import kontrol.pasien;
import kontrol.validasiLogin;
import kontrol.validate;
import model.addUserAtribute;
import model.biayaAtribute;
import model.loginAtribute;
import model.masterUserAtribute;
import model.pasienAtribute;
import model.profilAtribute;


public class Dashboard_admin extends javax.swing.JFrame {

    private masterUser masterUserData = new masterUser();
    private pasien pasienData = new pasien();
    private addUser add = new addUser();
    private biaya BIAYA = new biaya();
    private kontrol.profil profil = new kontrol.profil();
    private masterUserAtribute pAtribute = new masterUserAtribute();
    private ArrayList<masterUserAtribute> listDataUser = new ArrayList<>();
    private ArrayList<biayaAtribute> biayaList = new ArrayList<>();
    private ArrayList<pasienAtribute> listDataPasien = new ArrayList<>();
    private static final String DATE_FORMAT_NOW = "dd MMMMM YYYY"; 
    private final String LogedUser = loginAtribute.getUsername();
    private String searchTyped;
    private String searchPasienTyped;
    private final DefaultTableModel dm = new DefaultTableModel(0,0);
    private validasiLogin v = new validasiLogin();
    private final Date skrg = new Date();
    private int session_id;

    
    public Dashboard_admin() {
        initComponents();
        setHeaderElement();
        populatePasien();
        ftMasterUser();
        biayaLoadTable();
        loadingPane();
        
        
        biayaBiaya.setText("0");
        id_biaya.setVisible(false);
        
        
        this.updateClock = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jam.setText(nowHour());
            }
        };
        Timer t = new Timer(1000, updateClock);
        t.start();
        
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
    
    ActionListener updateClock;
    
    private void loadingPane(){
//        ImageIcon dashboard = createImageIcon("../drawable/dashboard-ic.png");
//        ImageIcon pasien = createImageIcon("../drawable/pasien-ic.png");
//        ImageIcon pengguna = createImageIcon("../drawable/multi-user-ic.png");
//        adminPane.setIconAt(0, dashboard);
//        adminPane.setIconAt(1, pasien);
//        adminPane.setIconAt(2, pengguna);
    }
    
     private ImageIcon createImageIcon(String path) {
        java.net.URL imgUrl = getClass().getResource(path);
        if(imgUrl != null){
            return new ImageIcon(imgUrl);
        }else{
            return null;
        }
    }
     
    //BIAYA
    //SET SELECTED BIAYA EDIT
     private void setSelectedBiaya(){
         int row = biayaTable.getSelectedRow();
         String selected = (String) biayaTable.getValueAt(row, 2);
         biayaJenis.setText(biayaTable.getValueAt(row, 1).toString().trim());
         biayaBiaya.setText(String.valueOf(biayaTable.getValueAt(row, 3)).trim());
         switch (selected) {
            case "Admin":
                biayaJabatan.setSelectedIndex(1);
                break;
            case "Dokter":
                biayaJabatan.setSelectedIndex(2);
                break;
            case "Radiolog":
                biayaJabatan.setSelectedIndex(3);
                break;
            case "Laboran":
                biayaJabatan.setSelectedIndex(4);
                break;
            default:
                biayaJabatan.setSelectedIndex(0);
                break;
        }
         
        int id_b = BIAYA.biayaChecker(biayaJenis.getText());
        
        id_biaya.setText(String.valueOf(id_b));
     }
     
    //UBAH BIAYA
    private void ubahBiaya(int id){
        biayaAtribute biaya = new biayaAtribute();
        biaya.setJenis(biayaJenis.getText().toUpperCase());
        biaya.setJabatan(biayaJabatan.getSelectedIndex());
        biaya.setBiaya(Integer.parseInt(biayaBiaya.getText()));
        
        BIAYA.updateBiaya(biaya, id);
    }
    //TAMBAH BIAYA
    private void tambahBiaya(){
        biayaAtribute biaya = new biayaAtribute();
        biaya.setJenis(biayaJenis.getText().toUpperCase());
        biaya.setJabatan(biayaJabatan.getSelectedIndex());
        biaya.setBiaya(Integer.parseInt(biayaBiaya.getText()));
        
        BIAYA.insertBiaya(biaya);
    }
    //LOAD TABLE BIAYA
    private void biayaLoadTable(){
        biayaList = (ArrayList<biayaAtribute>) BIAYA.getBiayaData();
        if(!biayaList.isEmpty()){
            //fetch data
            Object[][] data = new Object[biayaList.size()][4];
            int i = 0;
            
            for(biayaAtribute dat : biayaList){
                data[i][0] = i+1;
                data[i][1] = dat.getJenis();
                switch (dat.getJabatan()) {
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
                data[i][3] = dat.getBiaya();
                i++;
            }
            
            biayaTable.addMouseListener(new MouseAdapter() {
 
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        biayaTable.getCellEditor().stopCellEditing();
                    }
                }
            });
            
            biayaTable.setModel(new DefaultTableModel(data, new String[]{"No","Jenis Penanganan","Jabatan","Biaya"} ));
            biayaTable.getColumnModel().getColumn(0).setMaxWidth(50);
            biayaTable.setAutoCreateRowSorter(true);
        }else{
            //settable to null
            biayaTable.setModel(new DefaultTableModel(null, new String[]{"No","Jenis Penanganan","Jabatan","Biaya"} ));
        }
    }
    //END TAMBAH BIAYA 
    //END BIAYA
    
    //GET PROFIL USER
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
    //END GET
    /*FETCH PENGGUNA*/
    public void ftMasterUser(){
        listDataUser = (ArrayList<masterUserAtribute>)masterUserData.getDt();
        if(!listDataUser.isEmpty()){
            Object[][] data = new Object[listDataUser.size()][7];
            int i = 0;
            for(masterUserAtribute dat : listDataUser){
                data[i][0] = i+1;
                data[i][1] = dat.getUsername();
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
                data[i][6] = dat.getNo_kontak();
                
                i++;
                
            }
            
            jTable1.addMouseListener(new MouseAdapter() {
 
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        jTable1.getCellEditor().stopCellEditing();
                    }
                }
            });
            this.jTable1.setModel(new DefaultTableModel(data, new String[]{"No","Username","Jabatan", "Nama", "Alamat", "Jenis Kelamin", "No Kontak"}));
            this.jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            this.jScrollPane1.setViewportView(this.jTable1);
            this.jTable1.setAutoCreateRowSorter(true);
        }
    }
    /*END FETCH PENGGUNA*/
    
    /*TAMBAH PENGGUNA*/
    public int tambah(){
        String selected;
        addUserAtribute usrBaru = new addUserAtribute();
        usrBaru.setUsername(newUsername.getText().toLowerCase());
        usrBaru.setPassword(newPass.getPassword());
        
        
        selected = newRole.getSelectedItem().toString();
        
        if(null != selected)switch (selected) {
            case "Admin":
                usrBaru.setRole(1);
                break;
            case "Dokter":
                usrBaru.setRole(2);
                break;
            case "Radiolog":
                usrBaru.setRole(3);
                break;
            case "Laboran":
                usrBaru.setRole(4);
                break;
            default:
                break;
        }
        
        if(validateFormUserInput(newUsername.getText(), Arrays.toString(newPass.getPassword()), selected)){
            return add.addNewUser(usrBaru);
        }
        
        return 0;
    }
    
    private void addProfil(int key){
        profilAtribute usrprofil = new profilAtribute();
        usrprofil.setNama(penggunaNama.getText().toLowerCase());
        usrprofil.setAlamat(penggunaAlamat.getText().toLowerCase());
        usrprofil.setJk(penggunaJk.getSelectedItem().toString());
        usrprofil.setKontak(penggunaKontak.getText().toLowerCase());
        
        profil.insertProfil(usrprofil, key);
    }
    
    public boolean validateFormUserInput(String username, String password, String role){
        String errorMsg = "";
        if("".equals(username) || "".equals(password) || role == "--"){
            errorMsg = "Silahkan isi form dengan benar !";
            JOptionPane.showMessageDialog(null, errorMsg, "Warning",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    /*END OF TAMBAH PENGGUNA*/

     public void getUser(String searchString){
        listDataUser = (ArrayList<masterUserAtribute>)masterUserData.getUser(searchString);
        if(!listDataUser.isEmpty()){
            Object[][] data = new Object[listDataUser.size()][7];
            int i = 0;
            for(masterUserAtribute dat : listDataUser){
                data[i][0] = dat.getUsername();
                data[i][1] = dat.getRole();
                data[i][2] = dat.getNama();
                data[i][3] = dat.getAlamat();
                data[i][4] = dat.getJk();
                data[i][5] = dat.getNo_kontak();
                
                newUsername.setText(data[i][0].toString());
                newRole.setSelectedItem(data[i][1]);
                switch(data[i][1].toString()){
                    case "1":
                        newRole.setSelectedItem("Admin");
                        break;
                    case "2":
                        newRole.setSelectedItem("Dokter");
                        break;
                    case "3":
                        newRole.setSelectedItem("Radiolog");
                        break;
                    case "4":
                        newRole.setSelectedItem("Laboran");
                        break;   
                     
                    default:
                        newRole.setSelectedItem("--");
                        break;
                }
                penggunaNama.setText(data[i][2].toString());
                penggunaAlamat.setText(data[i][3].toString());
                penggunaJk.setSelectedItem(data[i][4].toString());
                penggunaKontak.setText(data[i][5].toString());
            }
            
        }else{
            
        }
     }
    /*SEARCH PENGGUNA*/
    public void searchUser(String searchString, String key){
        listDataUser = (ArrayList<masterUserAtribute>)masterUserData.getSearch(searchString, key);
        if(!listDataUser.isEmpty()){
            Object[][] data = new Object[listDataUser.size()][7];
            int i = 0;
            for(masterUserAtribute dat : listDataUser){
                data[i][0] = i+1;
                data[i][1] = dat.getUsername();
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
                data[i][6] = dat.getNo_kontak();
                
                i++;
                
            }
            
            jTable1.addMouseListener(new MouseAdapter() {
 
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        jTable1.getCellEditor().stopCellEditing();
                    }
                }
            });
            this.jTable1.setModel(new DefaultTableModel(data, new String[]{"No","Username","Jabatan", "Nama", "Alamat", "Jenis Kelamin", "No Kontak"}));
            this.jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            this.jScrollPane1.setViewportView(this.jTable1);
            this.jTable1.setAutoCreateRowSorter(true);
        }else{
            this.jTable1.setModel(new DefaultTableModel(null, new String[]{"No","Username","Jabatan", "Nama", "Alamat", "Jenis Kelamin", "No Kontak"}));
        }
    }
    /*END OF SEARCH PENGGUNA*/
    
     //get user selected
    public String getUserSelected(){
        String user;
        int row = jTable1.getSelectedRow();
        user = (String) jTable1.getValueAt(row, 1);
        return user;
    }
    
    //edit pengguna
    public void editPengguna(){
        String role;
        int roleInt = 0, id;
        masterUserAtribute d = new masterUserAtribute();
        
        d.setUsername(newUsername.getText());
        d.setNama(penggunaNama.getText());
        d.setAlamat(penggunaAlamat.getText());
        d.setNo_kontak(penggunaKontak.getText());
        d.setJk(penggunaJk.getSelectedItem().toString());
        role = newRole.getSelectedItem().toString();
        
        switch(role){
            case "Admin":
                roleInt = 1;
                break;
            case "Dokter":
                roleInt = 2;
                break;
            case "Radiolog":
                roleInt = 3;
                break;
            case "Laboran":
                roleInt = 4;
                break;
            default :
                break;
        }
        d.setRole(roleInt);
        
        id = masterUserData.getEditIdUser(d);
        masterUserData.editJabatan(d, id);
        masterUserData.editPengguna(d, id);
        
    }
    //END edit pengguna
    
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
//        pasienBaru.setP_status(statusPasienTx.getText());
        pasienBaru.setP_goldar(goldarTx.getSelectedItem().toString());
        pasienBaru.setP_nokontak(noHPTx.getText());
//        pasienBaru.setP_riwayatkehamilan(riwayatkehamilanTx.getText());
        pasienBaru.setP_tglskrg(skrg);
        
        validate validatePasien = new validate();
        if(validatePasien.validateInput(noRMTx.getText(), NamaTx.getText(), tmptlahirTx.getText(), alamatTx.getText(), sukuTx.getText(), noHPTx.getText(), pekerjaanTx.getText()))
            pasienData.addPasien(pasienBaru);
        else
            JOptionPane.showMessageDialog(null, validatePasien.showMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
//        pasienBaru.setP_status(statusPasienTx.getText());
        pasienBaru.setP_goldar(goldarTx.getSelectedItem().toString());
        pasienBaru.setP_nokontak(noHPTx.getText());
//        pasienBaru.setP_riwayatkehamilan(riwayatkehamilanTx.getText());
        pasienBaru.setP_tglskrg(skrg);
        
        validate validatePasien = new validate();
        if(validatePasien.validateInput(noRMTx.getText(), NamaTx.getText(), tmptlahirTx.getText(), alamatTx.getText(), sukuTx.getText(),  noHPTx.getText(),  pekerjaanTx.getText()))
            pasienData.editPasien(pasienBaru,getnoRM());
        else
            JOptionPane.showMessageDialog(null, validatePasien.showMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
//                statusPasienTx.setText((String) data[i][9]);
                goldarTx.setSelectedItem(data[i][10]);
                noHPTx.setText((String) data[i][11]);
                tglskrgTx.setDate((Date) data[i][14]);
                noRMTx.setText((String) data[i][0]);
//                riwayatkehamilanTx.setText((String) data[i][12]);
                
            }
        }else{
            System.err.println("Empty");
        }
    }
    //END DATA SELECTED
    public int getKetumur(Date d){
        long date = d.getTime();
        long now = new Date().getTime();
        long diff = now - date;
        int umure = (int) ((diff / (1000 * 60 * 60 * 24) )/ 365) ;
        return umure;
    }
    
    private void setHeaderElement(){
        usrLbl.setText(LogedUser);
        waktu.setText(nowDate());
        newPass.setVisible(false);
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jPanel5 = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        adminPane = new javax.swing.JTabbedPane();
        Dashboard = new javax.swing.JPanel();
        LaporanPasien = new javax.swing.JPanel();
        LaporanBiaya = new javax.swing.JPanel();
        Pasien = new javax.swing.JPanel();
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
        jLabel83 = new javax.swing.JLabel();
        noRMTx = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        goldarTx = new javax.swing.JComboBox<>();
        jLabel87 = new javax.swing.JLabel();
        noHPTx = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        tgllahirTx = new com.toedter.calendar.JDateChooser();
        tglskrgTx = new com.toedter.calendar.JDateChooser();
        jPanel8 = new javax.swing.JPanel();
        tambahPasien = new javax.swing.JButton();
        ubahPasien = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        pasienSearchTx = new javax.swing.JTextField();
        pasienScrollPane = new javax.swing.JScrollPane();
        tablePasien = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        cetakKartu = new javax.swing.JButton();
        Pengguna = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        newUsername = new javax.swing.JTextField();
        newPass = new javax.swing.JPasswordField();
        newRole = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        penggunaNama = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        penggunaAlamat = new javax.swing.JTextArea();
        penggunaKontak = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        tambahPengguna = new javax.swing.JButton();
        ubahPengguna = new javax.swing.JButton();
        clearFormPengguna = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        penggunaJk = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cariTx = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        filterUser = new javax.swing.JComboBox<>();
        Biaya = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        biayaJenis = new javax.swing.JTextField();
        biayaBiaya = new javax.swing.JTextField();
        biayaJabatan = new javax.swing.JComboBox<>();
        jPanel26 = new javax.swing.JPanel();
        clearBiaya = new javax.swing.JButton();
        tambahBiaya = new javax.swing.JButton();
        ubahBiaya = new javax.swing.JButton();
        biayaHapus = new javax.swing.JButton();
        id_biaya = new javax.swing.JTextField();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        biayaTable = new javax.swing.JTable();
        Pembayaran = new javax.swing.JPanel();
        profilPanel = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblnama = new javax.swing.JLabel();
        lblusername = new javax.swing.JLabel();
        lbljabatan = new javax.swing.JLabel();
        lbljk = new javax.swing.JLabel();
        lblalamat = new javax.swing.JLabel();
        lblnokontak = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel19 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        usernameProfil = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        passLama = new javax.swing.JPasswordField();
        jLabel20 = new javax.swing.JLabel();
        passBaru = new javax.swing.JPasswordField();
        jLabel21 = new javax.swing.JLabel();
        rePassBaru = new javax.swing.JPasswordField();
        jButton3 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        epNama = new javax.swing.JTextField();
        epJk = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        epAlamat = new javax.swing.JTextArea();
        jLabel25 = new javax.swing.JLabel();
        epKontak = new javax.swing.JTextField();
        epUbah = new javax.swing.JButton();
        Logout = new javax.swing.JPanel();
        info = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        usrLbl = new javax.swing.JLabel();
        jam = new javax.swing.JLabel();
        waktu = new javax.swing.JLabel();

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        jRadioButtonMenuItem2.setSelected(true);
        jRadioButtonMenuItem2.setText("jRadioButtonMenuItem2");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard Admin");
        setResizable(false);

        jPanel5.setPreferredSize(new java.awt.Dimension(1080, 768));
        jPanel5.setLayout(new java.awt.BorderLayout());

        headerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, java.awt.Color.lightGray));
        headerPanel.setMaximumSize(new java.awt.Dimension(70, 72));
        headerPanel.setMinimumSize(new java.awt.Dimension(70, 72));
        headerPanel.setLayout(new java.awt.GridBagLayout());

        logoLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/onkologi.jpg"))); // NOI18N
        logoLabel.setText("SISTEM INFORMASI KLINIK ONKOLOGI KOTABARU YOGYAKARTA");
        logoLabel.setToolTipText("");
        headerPanel.add(logoLabel, new java.awt.GridBagConstraints());

        jPanel5.add(headerPanel, java.awt.BorderLayout.PAGE_START);

        adminPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        Dashboard.setLayout(new javax.swing.BoxLayout(Dashboard, javax.swing.BoxLayout.LINE_AXIS));

        LaporanPasien.setBorder(javax.swing.BorderFactory.createTitledBorder("Laporan Pasien"));

        javax.swing.GroupLayout LaporanPasienLayout = new javax.swing.GroupLayout(LaporanPasien);
        LaporanPasien.setLayout(LaporanPasienLayout);
        LaporanPasienLayout.setHorizontalGroup(
            LaporanPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
        );
        LaporanPasienLayout.setVerticalGroup(
            LaporanPasienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 682, Short.MAX_VALUE)
        );

        Dashboard.add(LaporanPasien);

        LaporanBiaya.setBorder(javax.swing.BorderFactory.createTitledBorder("Laporan Biaya"));

        javax.swing.GroupLayout LaporanBiayaLayout = new javax.swing.GroupLayout(LaporanBiaya);
        LaporanBiaya.setLayout(LaporanBiayaLayout);
        LaporanBiayaLayout.setHorizontalGroup(
            LaporanBiayaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 459, Short.MAX_VALUE)
        );
        LaporanBiayaLayout.setVerticalGroup(
            LaporanBiayaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 682, Short.MAX_VALUE)
        );

        Dashboard.add(LaporanBiaya);

        adminPane.addTab("Dashboard", Dashboard);

        Pasien.setLayout(new javax.swing.BoxLayout(Pasien, javax.swing.BoxLayout.LINE_AXIS));

        tambahPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Master Pasien"));
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
        alamatTx.setLineWrap(true);
        alamatTx.setRows(5);
        jScrollPane6.setViewportView(alamatTx);

        jLabel78.setText("Pekerjaan");

        jLabel79.setText("Agama");

        agamaTx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Islam", "Kristen", "Katolik", "Hindu", "Budha", "Konghucu" }));

        jLabel80.setText("Suku/Ras");

        jLabel81.setText("Status Pernikahan");

        statusnikahTx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Menikah", "Belum Menikah", "Janda/Duda" }));

        jLabel83.setText("No Rekam Medis");

        jLabel84.setText("Tanggal Daftar");

        jLabel86.setText("Golongan Darah");

        goldarTx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "A", "AB", "O", "B" }));

        jLabel87.setText("No Telepon");

        tgllahirTx.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        tambahPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/plus-black-ic.png"))); // NOI18N
        tambahPasien.setText("Tambah");
        tambahPasien.setToolTipText("Tambah pasien");
        tambahPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahPasienActionPerformed(evt);
            }
        });

        ubahPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pencil-ic.png"))); // NOI18N
        ubahPasien.setText("Ubah");
        ubahPasien.setToolTipText("Ubah pasien");
        ubahPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahPasienActionPerformed(evt);
            }
        });

        clearBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/sweep-ic.png"))); // NOI18N
        clearBtn.setText("Clear");
        clearBtn.setToolTipText("Bersihkan form");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahPasien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ubahPasien)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearBtn)
                .addGap(112, 112, 112))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ubahPasien, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(clearBtn, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tambahPasien))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout tambahPanelLayout = new javax.swing.GroupLayout(tambahPanel);
        tambahPanel.setLayout(tambahPanelLayout);
        tambahPanelLayout.setHorizontalGroup(
            tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel72)
                    .addComponent(jLabel73)
                    .addComponent(jLabel74)
                    .addComponent(jLabel75)
                    .addComponent(jLabel83)
                    .addComponent(jLabel80)
                    .addComponent(jLabel78)
                    .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel79)
                    .addComponent(jLabel76)
                    .addComponent(jLabel81)
                    .addComponent(jLabel77)
                    .addComponent(jLabel84)
                    .addGroup(tambahPanelLayout.createSequentialGroup()
                        .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel86)
                            .addComponent(jLabel87))
                        .addGap(47, 47, 47)
                        .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tglskrgTx, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(goldarTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noRMTx, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statusnikahTx, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sukuTx, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(agamaTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pekerjaanTx, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usiaTx, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tgllahirTx, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tmptlahirTx, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JkTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NamaTx, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noHPTx, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                    .addComponent(noRMTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel83))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel84)
                    .addComponent(tglskrgTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel86)
                    .addComponent(goldarTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel87)
                    .addComponent(noHPTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(147, 147, 147)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Pasien.add(tambahPanel);

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

        cetakKartu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/cc-ic.png"))); // NOI18N
        cetakKartu.setText("Cetak kartu pasien");
        cetakKartu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakKartuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cetakKartu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cetakKartu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(pasienScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
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
                .addComponent(pasienScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Pasien.add(jPanel6);

        adminPane.addTab("Pasien", Pasien);

        Pengguna.setLayout(new javax.swing.BoxLayout(Pengguna, javax.swing.BoxLayout.LINE_AXIS));

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Master Pengguna"));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Username");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Jabatan");

        newUsername.setToolTipText("username pengguna");

        newPass.setEditable(false);
        newPass.setText("123456");

        newRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Admin", "Dokter", "Radiolog", "Laboran" }));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Nama");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Alamat");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("No Kontak");

        penggunaNama.setToolTipText("nama pengguna");

        penggunaAlamat.setColumns(20);
        penggunaAlamat.setLineWrap(true);
        penggunaAlamat.setRows(3);
        penggunaAlamat.setTabSize(4);
        penggunaAlamat.setToolTipText("alamat pengguna");
        jScrollPane2.setViewportView(penggunaAlamat);

        penggunaKontak.setToolTipText("no kontak pengguna");

        tambahPengguna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/plus-black-ic.png"))); // NOI18N
        tambahPengguna.setText("Tambah");
        tambahPengguna.setToolTipText("Tambah pengguna");
        tambahPengguna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahPenggunaActionPerformed(evt);
            }
        });

        ubahPengguna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pencil-ic.png"))); // NOI18N
        ubahPengguna.setText("Ubah");
        ubahPengguna.setToolTipText("Ubah pengguna");
        ubahPengguna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahPenggunaActionPerformed(evt);
            }
        });

        clearFormPengguna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/sweep-ic.png"))); // NOI18N
        clearFormPengguna.setText("Clear");
        clearFormPengguna.setToolTipText("Bersihakn Form");
        clearFormPengguna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFormPenggunaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tambahPengguna)
                .addGap(5, 5, 5)
                .addComponent(ubahPengguna)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearFormPengguna)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ubahPengguna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clearFormPengguna, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tambahPengguna))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Jenis Kelamn");

        penggunaJk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Laki-laki", "Perempuan" }));

        jLabel9.setForeground(new java.awt.Color(0, 0, 255));
        jLabel9.setText("Pastikan semua isian sudah terisi dengan benar !");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(penggunaJk, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(newPass, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addComponent(jLabel14)
                                .addComponent(newRole, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel16)
                                .addComponent(newUsername)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addComponent(jLabel1)
                                .addComponent(jLabel5)
                                .addComponent(penggunaNama)
                                .addComponent(penggunaKontak, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(newUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penggunaNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penggunaJk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(penggunaKontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                .addComponent(newPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Pengguna.add(jPanel12);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel Pengguna"));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "1", "2", "3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        cariTx.setToolTipText("Kata kunci pencarian");
        cariTx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariTxKeyReleased(evt);
            }
        });

        jLabel35.setText("Cari :");

        filterUser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nama", "Username", "Alamat", "No Kontak" }));
        filterUser.setToolTipText("Cari berdasarkan");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cariTx, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cariTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(filterUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                .addContainerGap())
        );

        Pengguna.add(jPanel10);

        adminPane.addTab("Pengguna", Pengguna);

        Biaya.setLayout(new javax.swing.BoxLayout(Biaya, javax.swing.BoxLayout.LINE_AXIS));

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Master Biaya"));

        jLabel26.setText("Jenis");

        jLabel27.setText("Jabatan");

        jLabel28.setText("Biaya");

        biayaJabatan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Admin", "Dokter", "Radiolog", "Laboran" }));

        clearBiaya.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/sweep-ic.png"))); // NOI18N
        clearBiaya.setText("Clear");
        clearBiaya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBiayaActionPerformed(evt);
            }
        });

        tambahBiaya.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/plus-black-ic.png"))); // NOI18N
        tambahBiaya.setText("Tambah");
        tambahBiaya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahBiayaActionPerformed(evt);
            }
        });

        ubahBiaya.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/pencil-ic.png"))); // NOI18N
        ubahBiaya.setText("Ubah");
        ubahBiaya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahBiayaActionPerformed(evt);
            }
        });

        biayaHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/drawable/send-fl-ic.png"))); // NOI18N
        biayaHapus.setText("Hapus");
        biayaHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                biayaHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tambahBiaya, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clearBiaya, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ubahBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(biayaHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambahBiaya)
                    .addComponent(ubahBiaya))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearBiaya)
                    .addComponent(biayaHapus))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        id_biaya.setEditable(false);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(biayaJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel28))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(biayaJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(biayaBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(id_biaya, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 127, Short.MAX_VALUE))))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(biayaJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(biayaJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(biayaBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(id_biaya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(408, Short.MAX_VALUE))
        );

        Biaya.add(jPanel24);

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Biaya"));

        biayaTable.setModel(new javax.swing.table.DefaultTableModel(
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
        biayaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                biayaTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(biayaTable);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
                .addContainerGap())
        );

        Biaya.add(jPanel25);

        adminPane.addTab("Biaya", Biaya);

        javax.swing.GroupLayout PembayaranLayout = new javax.swing.GroupLayout(Pembayaran);
        Pembayaran.setLayout(PembayaranLayout);
        PembayaranLayout.setHorizontalGroup(
            PembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 942, Short.MAX_VALUE)
        );
        PembayaranLayout.setVerticalGroup(
            PembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 705, Short.MAX_VALUE)
        );

        adminPane.addTab("Pembayaran", Pembayaran);

        profilPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                profilPanelComponentShown(evt);
            }
        });
        profilPanel.setLayout(new java.awt.BorderLayout());

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Profil Pengguna"));

        jLabel13.setText("No Kontak");

        jLabel12.setText("Alamat");

        jLabel15.setText("Jenis Kelamin");

        jLabel11.setText("Jabatan");

        jLabel17.setText("Username");

        jLabel10.setText("Nama");

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
                    .addComponent(jLabel10)
                    .addComponent(jLabel17)
                    .addComponent(jLabel11)
                    .addComponent(jLabel15)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(23, 23, 23)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblnokontak)
                    .addComponent(lblalamat)
                    .addComponent(lbljk)
                    .addComponent(lbljabatan)
                    .addComponent(lblusername)
                    .addComponent(lblnama))
                .addContainerGap(798, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblnama))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblusername))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(lbljabatan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lbljk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblalamat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
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

        profilPanel.add(jPanel18, java.awt.BorderLayout.PAGE_START);

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Menu Profil"));

        jLabel18.setText("Username :");

        usernameProfil.setEnabled(false);

        jLabel19.setText("Password lama");

        jLabel20.setText("Password baru");

        jLabel21.setText("Ulangi password baru");

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
                        .addComponent(jLabel18)
                        .addComponent(usernameProfil)
                        .addComponent(jLabel19)
                        .addComponent(passLama)
                        .addComponent(jLabel20)
                        .addComponent(jLabel21)
                        .addComponent(rePassBaru, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                        .addComponent(passBaru))
                    .addComponent(jButton3))
                .addContainerGap(714, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameProfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passLama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passBaru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rePassBaru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap(238, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ubah password", jPanel19);

        jLabel22.setText("Nama");

        jLabel23.setText("Jenis Kelamin");

        jLabel24.setText("Alamat");

        epJk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Laki-laki", "Perempuan" }));

        epAlamat.setColumns(20);
        epAlamat.setLineWrap(true);
        epAlamat.setRows(5);
        jScrollPane3.setViewportView(epAlamat);

        jLabel25.setText("No Kontak");

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
                            .addComponent(jLabel22)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25))
                        .addGap(58, 58, 58)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(epJk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(epNama, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(epKontak, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(356, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(epNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(epJk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(epKontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(epUbah)
                .addContainerGap(242, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Edit Profil", jPanel21);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        profilPanel.add(jPanel20, java.awt.BorderLayout.CENTER);

        adminPane.addTab("Profil", profilPanel);

        Logout.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                LogoutComponentShown(evt);
            }
        });

        javax.swing.GroupLayout LogoutLayout = new javax.swing.GroupLayout(Logout);
        Logout.setLayout(LogoutLayout);
        LogoutLayout.setHorizontalGroup(
            LogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 942, Short.MAX_VALUE)
        );
        LogoutLayout.setVerticalGroup(
            LogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 705, Short.MAX_VALUE)
        );

        adminPane.addTab("Logout", Logout);

        jPanel5.add(adminPane, java.awt.BorderLayout.CENTER);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 934, Short.MAX_VALUE)
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

        jPanel5.add(info, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

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
        cetakKartu.setEnabled(true);
        tambahPasien.setEnabled(false);
    }//GEN-LAST:event_tablePasienMouseClicked

    private void cariTxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariTxKeyReleased
        searchTyped = cariTx.getText();
        String key =  filterUser.getSelectedItem().toString();
        searchUser(searchTyped, key);
    }//GEN-LAST:event_cariTxKeyReleased

    private void tambahPenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahPenggunaActionPerformed
        addProfil(tambah());
        ftMasterUser();
    }//GEN-LAST:event_tambahPenggunaActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        // TODO add your handling code here:
        java.util.Date nulDate = new Date(0);
        cetakKartu.setEnabled(false);
        NamaTx.setText("");
        alamatTx.setText("");
        JkTx.setSelectedIndex(0);
        tmptlahirTx.setText("");
        tgllahirTx.setDate(nulDate);
        usiaTx.setText("");
        pekerjaanTx.setText("");
        agamaTx.setSelectedIndex(0);
        sukuTx.setText("");
        statusnikahTx.setSelectedIndex(0);
        noRMTx.setText("");
        noHPTx.setText("");
        tglskrgTx.setDate(skrg);
        goldarTx.setSelectedIndex(0);
        tambahPasien.setEnabled(true);
    }//GEN-LAST:event_clearBtnActionPerformed

    private void LogoutComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_LogoutComponentShown
        this.dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_LogoutComponentShown

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        getUser(getUserSelected());
        newUsername.setEditable(false);
        tambahPengguna.setEnabled(false);
    }//GEN-LAST:event_jTable1MouseClicked

    private void clearFormPenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearFormPenggunaActionPerformed
        // CLEARING FORM
        newUsername.setText("");
        newRole.setSelectedItem("--");
        penggunaNama.setText("");
        penggunaAlamat.setText("");
        penggunaKontak.setText("");
        penggunaJk.setSelectedItem("--");
        newUsername.setEditable(true);
        tambahPengguna.setEnabled(true);
    }//GEN-LAST:event_clearFormPenggunaActionPerformed

    private void ubahPenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahPenggunaActionPerformed
        // TODO add your handling code here:
        editPengguna();
        ftMasterUser();
    }//GEN-LAST:event_ubahPenggunaActionPerformed

    private void profilPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_profilPanelComponentShown
        // TODO add your handling code here:
        System.err.println(LogedUser);
        getProfilData(LogedUser);
    }//GEN-LAST:event_profilPanelComponentShown

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

    private void cetakKartuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakKartuActionPerformed
        
    }//GEN-LAST:event_cetakKartuActionPerformed

    private void tambahBiayaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahBiayaActionPerformed
        // TODO add your handling code here:
        tambahBiaya();
        biayaLoadTable();
    }//GEN-LAST:event_tambahBiayaActionPerformed

    private void biayaTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_biayaTableMouseClicked
        setSelectedBiaya();
        tambahBiaya.setEnabled(false);
    }//GEN-LAST:event_biayaTableMouseClicked

    private void clearBiayaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBiayaActionPerformed
        // TODO add your handling code here:
        biayaJenis.setText("");
        biayaJabatan.setSelectedIndex(0);
        biayaBiaya.setText("0");
        id_biaya.setText("0");
        
        tambahBiaya.setEnabled(true);
    }//GEN-LAST:event_clearBiayaActionPerformed

    private void ubahBiayaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahBiayaActionPerformed
        // TODO add your handling code here:
        ubahBiaya(Integer.parseInt(id_biaya.getText()));
        biayaJenis.setText("");
        biayaJabatan.setSelectedIndex(0);
        biayaBiaya.setText("0");
        id_biaya.setText("0");
        biayaLoadTable();
        tambahBiaya.setEnabled(true);
    }//GEN-LAST:event_ubahBiayaActionPerformed

    private void biayaHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_biayaHapusActionPerformed
        // TODO add your handling code here:
        String msg = BIAYA.hapusBiaya(Integer.parseInt(id_biaya.getText()));
        
        JOptionPane.showMessageDialog(null, msg);
        biayaLoadTable();
    }//GEN-LAST:event_biayaHapusActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard_admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard_admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Biaya;
    private javax.swing.JPanel Dashboard;
    private javax.swing.JComboBox<String> JkTx;
    private javax.swing.JPanel LaporanBiaya;
    private javax.swing.JPanel LaporanPasien;
    private javax.swing.JPanel Logout;
    private javax.swing.JTextField NamaTx;
    private javax.swing.JPanel Pasien;
    private javax.swing.JPanel Pembayaran;
    private javax.swing.JPanel Pengguna;
    private javax.swing.JTabbedPane adminPane;
    private javax.swing.JComboBox<String> agamaTx;
    private javax.swing.JTextArea alamatTx;
    private javax.swing.JTextField biayaBiaya;
    private javax.swing.JButton biayaHapus;
    private javax.swing.JComboBox<String> biayaJabatan;
    private javax.swing.JTextField biayaJenis;
    private javax.swing.JTable biayaTable;
    private javax.swing.JTextField cariTx;
    private javax.swing.JButton cetakKartu;
    private javax.swing.JButton clearBiaya;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton clearFormPengguna;
    private javax.swing.JTextArea epAlamat;
    private javax.swing.JComboBox<String> epJk;
    private javax.swing.JTextField epKontak;
    private javax.swing.JTextField epNama;
    private javax.swing.JButton epUbah;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JComboBox<String> filterUser;
    private javax.swing.JComboBox<String> goldarTx;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JTextField id_biaya;
    private javax.swing.JPanel info;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel jam;
    private javax.swing.JLabel lblalamat;
    private javax.swing.JLabel lbljabatan;
    private javax.swing.JLabel lbljk;
    private javax.swing.JLabel lblnama;
    private javax.swing.JLabel lblnokontak;
    private javax.swing.JLabel lblusername;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPasswordField newPass;
    public static javax.swing.JComboBox<String> newRole;
    public static javax.swing.JTextField newUsername;
    private javax.swing.JTextField noHPTx;
    private javax.swing.JTextField noRMTx;
    private javax.swing.JScrollPane pasienScrollPane;
    private javax.swing.JTextField pasienSearchTx;
    private javax.swing.JPasswordField passBaru;
    private javax.swing.JPasswordField passLama;
    private javax.swing.JTextField pekerjaanTx;
    private javax.swing.JTextArea penggunaAlamat;
    private javax.swing.JComboBox<String> penggunaJk;
    private javax.swing.JTextField penggunaKontak;
    private javax.swing.JTextField penggunaNama;
    private javax.swing.JPanel profilPanel;
    private javax.swing.JPasswordField rePassBaru;
    private javax.swing.JComboBox<String> statusnikahTx;
    private javax.swing.JTextField sukuTx;
    private javax.swing.JTable tablePasien;
    private javax.swing.JButton tambahBiaya;
    private javax.swing.JPanel tambahPanel;
    private javax.swing.JButton tambahPasien;
    private javax.swing.JButton tambahPengguna;
    private com.toedter.calendar.JDateChooser tgllahirTx;
    private com.toedter.calendar.JDateChooser tglskrgTx;
    private javax.swing.JTextField tmptlahirTx;
    private javax.swing.JButton ubahBiaya;
    private javax.swing.JButton ubahPasien;
    private javax.swing.JButton ubahPengguna;
    private javax.swing.JTextField usernameProfil;
    private javax.swing.JTextField usiaTx;
    private javax.swing.JLabel usrLbl;
    private javax.swing.JLabel waktu;
    // End of variables declaration//GEN-END:variables

   

    
}
