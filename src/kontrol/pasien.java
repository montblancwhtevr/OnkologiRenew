
package kontrol;

import Koneksi.Conn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.pasienAtribute;

public class pasien {
    private PreparedStatement pst;
    private Connection koneksi = null;

    public pasien() {
        this.koneksi =  new Conn().Connect();
    }
    
    
    public List<pasienAtribute> getAllData(){
        List<pasienAtribute> dataCollected = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT * from pasien");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                pasienAtribute collect = new pasienAtribute();
                collect.setP_norm(rs.getString("p_norm"));
                collect.setP_nama(rs.getString("p_nama"));
                collect.setP_jk(rs.getInt("p_jk"));
                collect.setP_tmtplahir(rs.getString("p_tmtlahir"));
                collect.setP_tgllahir(rs.getDate("p_tgllahir"));
                collect.setP_alamat(rs.getString("p_alamat"));
                collect.setP_agama(rs.getString("p_agama"));
                collect.setP_suku(rs.getString("p_suku"));
                collect.setP_statuspernikahan(rs.getString("p_statuspernikahan"));
                collect.setP_status(rs.getString("p_status"));
                collect.setP_goldar(rs.getString("p_goldar"));
                collect.setP_nokontak(rs.getString("p_nokontak"));
                collect.setP_riwayatkehamilan(rs.getString("p_riwayatkehamilan"));
                collect.setP_pekerjaan(rs.getString("pekerjaan"));
                collect.setP_tglskrg(rs.getDate("p_tgldaftar"));
                dataCollected.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    public List<pasienAtribute> getRmData(String rm){
        List<pasienAtribute> dataCollected = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT * from pasien WHERE p_norm = ?");
            pst.setString(1, rm);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                pasienAtribute collect = new pasienAtribute();
                collect.setP_norm(rs.getString("p_norm"));
                collect.setP_nama(rs.getString("p_nama"));
                collect.setP_jk(rs.getInt("p_jk"));
                collect.setP_tmtplahir(rs.getString("p_tmtlahir"));
                collect.setP_tgllahir(rs.getDate("p_tgllahir"));
                collect.setP_alamat(rs.getString("p_alamat"));
                collect.setP_agama(rs.getString("p_agama"));
                collect.setP_suku(rs.getString("p_suku"));
                collect.setP_statuspernikahan(rs.getString("p_statuspernikahan"));
//                collect.setP_status(rs.getString("p_status"));
                collect.setP_goldar(rs.getString("p_goldar"));
                collect.setP_nokontak(rs.getString("p_nokontak"));
//                collect.setP_riwayatkehamilan(rs.getString("p_riwayatkehamilan"));
                collect.setP_pekerjaan(rs.getString("pekerjaan"));
                collect.setP_tglskrg(rs.getDate("p_tgldaftar"));
                dataCollected.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    
    public List<pasienAtribute> getData(){
        List<pasienAtribute> dataCollected = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT p_norm,p_nama,p_jk,p_tmtlahir,p_tgllahir,p_alamat,p_goldar,p_nokontak from pasien");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                pasienAtribute collect = new pasienAtribute();
                collect.setP_norm(rs.getString("p_norm"));
                collect.setP_nama(rs.getString("p_nama"));
                collect.setP_jk(rs.getInt("p_jk"));
                collect.setP_tmtplahir(rs.getString("p_tmtlahir"));
                collect.setP_tgllahir(rs.getDate("p_tgllahir"));
                collect.setP_alamat(rs.getString("p_alamat"));
//                collect.setP_agama(rs.getString("p_agama"));
//                collect.setP_suku(rs.getString("p_suku"));
//                collect.setP_statuspernikahan(rs.getString("p_statuspernikahan"));
//                collect.setP_status(rs.getString("p_status"));
                collect.setP_goldar(rs.getString("p_goldar"));
                collect.setP_nokontak(rs.getString("p_nokontak"));
//                collect.setP_riwayatkehamilan(rs.getString("p_riwayatkehamilan"));
                dataCollected.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    
    
    public List<pasienAtribute> searchPasien(String search){
        List<pasienAtribute> dataCollected = new ArrayList<>();
         try {
            pst = koneksi.prepareStatement("select * from pasien where p_norm like '%"+ search +"%'" +
                    "or p_nama like '%"+ search +"%'" +
                    "or p_alamat like '%"+ search +"%'" +
                    "or p_goldar like '%"+ search +"%'" +
                    "or p_nokontak like '%"+ search +"%'" );
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                pasienAtribute collect = new pasienAtribute();
                collect.setP_norm(rs.getString("p_norm"));
                collect.setP_nama(rs.getString("p_nama"));
                collect.setP_jk(rs.getInt("p_jk"));
                collect.setP_tmtplahir(rs.getString("p_tmtlahir"));
                collect.setP_tgllahir(rs.getDate("p_tgllahir"));
                collect.setP_alamat(rs.getString("p_alamat"));
//                collect.setP_agama(rs.getString("p_agama"));
//                collect.setP_suku(rs.getString("p_suku"));
//                collect.setP_statuspernikahan(rs.getString("p_statuspernikahan"));
//                collect.setP_status(rs.getString("p_status"));
                collect.setP_goldar(rs.getString("p_goldar"));
                collect.setP_nokontak(rs.getString("p_nokontak"));
//                collect.setP_riwayatkehamilan(rs.getString("p_riwayatkehamilan"));
                dataCollected.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    
    public void addPasien(pasienAtribute add){
        java.sql.Date lahir = new java.sql.Date(add.getP_tgllahir().getTime());
        java.sql.Date now = new java.sql.Date(add.getP_tglskrg().getTime());
        try {
            String sql = "INSERT INTO pasien VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = koneksi.prepareStatement(sql);
            pst.setString(1, add.getP_norm());
            pst.setString(2, add.getP_nama());
            pst.setInt(3, add.getP_jk());
            pst.setString(4, add.getP_tmtplahir());
            pst.setDate(5, lahir);
            pst.setString(6, add.getP_alamat());
            pst.setString(7, add.getP_agama());
            pst.setString(8, add.getP_suku());
            pst.setString(9, add.getP_statuspernikahan());
            pst.setString(10, add.getP_status());
            pst.setString(11, add.getP_goldar());
            pst.setString(12, add.getP_nokontak());
            pst.setString(13, add.getP_riwayatkehamilan());
            pst.setString(14, add.getP_pekerjaan());
            pst.setDate(15, now);
            
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "sukses");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"gagal");
        }
    }
    
    public void editPasien(pasienAtribute add,String norm){
        java.sql.Date lahir = new java.sql.Date(add.getP_tgllahir().getTime());
        java.sql.Date now = new java.sql.Date(add.getP_tglskrg().getTime());
        try {
            String sql = "UPDATE pasien SET p_norm=?,p_nama=?, p_jk=?, p_tmtlahir=?, p_tgllahir=?, p_alamat=?,p_agama=?, p_suku=?, p_statuspernikahan=?, p_goldar=?, p_nokontak=?, pekerjaan=?, p_tgldaftar=?  WHERE p_norm = ?";
            pst = koneksi.prepareStatement(sql);
            pst.setString(1, add.getP_norm());
            pst.setString(2, add.getP_nama());
            pst.setInt(3, add.getP_jk());
            pst.setString(4, add.getP_tmtplahir());
            pst.setDate(5, lahir);
            pst.setString(6, add.getP_alamat());
            pst.setString(7, add.getP_agama());
            pst.setString(8, add.getP_suku());
            pst.setString(9, add.getP_statuspernikahan());
//            pst.setString(10, add.getP_status());
            pst.setString(10, add.getP_goldar());
            pst.setString(11, add.getP_nokontak());
//            pst.setString(13, add.getP_riwayatkehamilan());
            pst.setString(12, add.getP_pekerjaan());
            pst.setDate(13, now);
            pst.setString(14, norm);
            
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "sukses");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"gagal");
        }
    }
    
    //MISC
    public boolean verifyRm(String norm){
        try {
            String sql = "SELECT p_norm FROM pasien WHERE p_norm = ?";
            pst = koneksi.prepareStatement(sql);
            pst.setString(1, norm);
            
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<pasienAtribute> getKeterangan(String x){
        List<pasienAtribute> dataPasien = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT * from pasien WHERE p_norm = ?");
            pst.setString(1, x);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                pasienAtribute collect = new pasienAtribute();
                collect.setP_norm(rs.getString("p_norm"));
                collect.setP_nama(rs.getString("p_nama"));
                collect.setP_jk(rs.getInt("p_jk"));
                collect.setP_tmtplahir(rs.getString("p_tmtlahir"));
                collect.setP_tgllahir(rs.getDate("p_tgllahir"));
                collect.setP_alamat(rs.getString("p_alamat"));
//                collect.setP_agama(rs.getString("p_agama"));
//                collect.setP_suku(rs.getString("p_suku"));
//                collect.setP_statuspernikahan(rs.getString("p_statuspernikahan"));
//                collect.setP_status(rs.getString("p_status"));
                collect.setP_goldar(rs.getString("p_goldar"));
                collect.setP_nokontak(rs.getString("p_nokontak"));
//                collect.setP_riwayatkehamilan(rs.getString("p_riwayatkehamilan"));
                dataPasien.add(collect);
            }
        } catch (Exception e) {
        }
        return dataPasien;
    }
}
