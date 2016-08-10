/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontrol;

import Koneksi.Conn;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.masterUserAtribute;

/**
 *
 * @author Fetimeh
 */
public class masterUser {
    
    private PreparedStatement pst;
    private Connection koneksi = null;

    public masterUser() {
        this.koneksi =  new Conn().Connect();
    }
    
    
    public List<masterUserAtribute> getDt(){
        List<masterUserAtribute> dataCollected = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT e.id, e.username, e.role, p.nama, p.alamat, p.jenis_kelamin, p.no_kontak from emp e INNER JOIN profil p ON e.id = p.id ORDER BY e.id ASC");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                masterUserAtribute collect = new masterUserAtribute();
                collect.setUsername(rs.getString("username"));
                collect.setRole(rs.getInt("role"));
                collect.setNama(rs.getString("nama"));
                collect.setAlamat(rs.getString("alamat"));
                collect.setJk(rs.getString("jenis_kelamin"));
                collect.setNo_kontak(rs.getString("no_kontak"));
                dataCollected.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    public List<masterUserAtribute> getSearch(String search, String key){
        List<masterUserAtribute> dataCollected = new ArrayList<>();
        try {
            switch(key){
                case "Nama":
                    key = "p.nama";
                    break;
                case "Alamat":
                    key = "p.alamat";
                    break;
                case "Username":
                    key = "e.username";
                    break;
                case "No Kontak":
                    key = "p.no_kontak";
                    break;
                default:
                    break;
                
            }
            pst = koneksi.prepareStatement("SELECT e.id,e.username, e.role, p.nama, p.alamat, p.jenis_kelamin, p.no_kontak from emp e INNER JOIN profil p ON e.id = p.id WHERE "+ key +"::text like '%"+ search +"%' ORDER BY e.id ASC");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                masterUserAtribute collect = new masterUserAtribute();
                collect.setUsername(rs.getString("username"));
                collect.setRole(rs.getInt("role"));
                collect.setNama(rs.getString("nama"));
                collect.setAlamat(rs.getString("alamat"));
                collect.setJk(rs.getString("jenis_kelamin"));
                collect.setNo_kontak(rs.getString("no_kontak"));
                dataCollected.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    public List<masterUserAtribute> getUser(String key){
        List<masterUserAtribute> dataCollected = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT e.id,e.username, e.role, p.nama, p.alamat, p.jenis_kelamin, p.no_kontak from emp e INNER JOIN profil p ON e.id = p.id  where e.username = ?");
            pst.setString(1, key);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                masterUserAtribute collect = new masterUserAtribute();
                collect.setUsername(rs.getString("username"));
                collect.setRole(rs.getInt("role"));
                collect.setNama(rs.getString("nama"));
                collect.setAlamat(rs.getString("alamat"));
                collect.setJk(rs.getString("jenis_kelamin"));
                collect.setNo_kontak(rs.getString("no_kontak"));
                dataCollected.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    public int getEditIdUser(masterUserAtribute a){
        try {
            pst = koneksi.prepareStatement("SELECT id from emp WHERE username = ?");
            pst.setString(1, a.getUsername());
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public void editJabatan(masterUserAtribute ed, int key){
        try {
            pst = koneksi.prepareStatement("UPDATE emp SET role = ? WHERE id = ?");
            pst.setInt(1, ed.getRole());
            pst.setInt(2, key);
            
            pst.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editPengguna(masterUserAtribute add, int key){
        try {
            pst = koneksi.prepareStatement("UPDATE profil SET nama = ?, alamat = ?, jenis_kelamin = ? , no_kontak = ? WHERE id = ?");
            pst.setString(1, add.getNama());
            pst.setString(2, add.getAlamat());
            pst.setString(3, add.getJk());
            pst.setString(4, add.getNo_kontak());
            pst.setInt(5, key);
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Update pengguna berhasil");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editProfil(masterUserAtribute add){
        try {
            pst = koneksi.prepareStatement("UPDATE profil SET nama = ?, alamat = ?, jenis_kelamin = ? , no_kontak = ? WHERE id = ?");
            pst.setString(1, add.getNama());
            pst.setString(2, add.getAlamat());
            pst.setString(3, add.getJk());
            pst.setString(4, add.getNo_kontak());
            pst.setInt(5, add.getId());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "sukses");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
