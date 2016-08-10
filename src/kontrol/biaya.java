/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontrol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import Koneksi.Conn;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.biayaAtribute;

/**
 *
 * @author Ultramilk
 */
public class biaya {
    private PreparedStatement pst;
    private Connection koneksi = null;

    public biaya() {
        this.koneksi = new Conn().Connect();
    }
    
    public List<biayaAtribute> getBiayaData(){
        List<biayaAtribute> dataCollected = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT * FROM biaya ORDER BY id_biaya ASC");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                biayaAtribute biaya = new biayaAtribute();
                biaya.setJenis(rs.getString("jenis"));
                biaya.setJabatan(rs.getInt("jabatan"));
                biaya.setBiaya(rs.getInt("biaya"));
                dataCollected.add(biaya);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    public void insertBiaya(biayaAtribute b){
        try {
            pst = koneksi.prepareStatement("INSERT INTO biaya (jenis,jabatan,biaya) VALUES (?,?,?)");
            pst.setString(1, b.getJenis());
            pst.setInt(2, b.getJabatan());
            pst.setInt(3, b.getBiaya());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Tambah biaya berhasil.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int biayaChecker(String key){
        try {
            pst = koneksi.prepareStatement("SELECT id_biaya FROM biaya WHERE jenis = ?");
            pst.setString(1, key);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return rs.getInt("id_biaya");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void updateBiaya(biayaAtribute b, int id){
        try {
            pst = koneksi.prepareStatement("UPDATE biaya SET jenis=?, jabatan=?, biaya=? WHERE id_biaya = ?");
            pst.setString(1, b.getJenis());
            pst.setInt(2, b.getJabatan());
            pst.setInt(3, b.getBiaya());
            pst.setInt(4, id);
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ubah biaya berhasil.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String hapusBiaya(int id){
        String msg = "";
        try {
            pst = koneksi.prepareStatement("DELETE FROM biaya WHERE id_biaya = ?");
            pst.setInt(1, id);
            
            pst.executeUpdate();
            
            msg += "Biaya berhasil di hapus.";
        } catch (Exception e) {
            e.printStackTrace();
            msg += "Penghapusan biaya gagal.";
        }
        return msg;
    }
    
    
}
