package kontrol;

import Koneksi.Conn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.swing.JOptionPane;
import model.masterUserAtribute;
import model.profilAtribute;


public class profil {
    private PreparedStatement pst;
    private Connection koneksi = null;

    public profil() {
        this.koneksi =  new Conn().Connect();
    }
    
    //GET ALL PROFIL DATA WHERE ID = ?
    public List<masterUserAtribute> getAllData(String name){
        List<masterUserAtribute> dataCollected = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT e.id, e.username, e.role, p.nama, p.alamat, p.jenis_kelamin, p.no_kontak from emp e INNER JOIN profil p ON e.id = p.id WHERE e.username = ?");
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                masterUserAtribute collect = new masterUserAtribute();
                collect.setUsername(rs.getString("username"));
                collect.setNama(rs.getString("nama"));
                collect.setRole(rs.getInt("role"));
                collect.setAlamat(rs.getString("alamat"));
                collect.setNo_kontak(rs.getString("no_kontak"));
                collect.setJk(rs.getString("jenis_kelamin"));
                collect.setId(rs.getInt("id"));
                dataCollected.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    //CHECK PROFIL DATA EXIST OR NOT
    public boolean profilChecker(int id){
        try {
            pst = koneksi.prepareStatement("SELECT * FROM profil WHERE id = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    //INSERT NEW PROFIL TO DATABASE
    public void insertProfil(profilAtribute add, int key){
        try {
            pst = koneksi.prepareStatement("INSERT INTO profil (nama, alamat, jenis_kelamin, no_kontak, id) VALUES(?,?,?,?,?)");
            pst.setString(1, add.getNama());
            pst.setString(2, add.getAlamat());
            pst.setString(3, add.getJk());
            pst.setString(4, add.getKontak());
            pst.setInt(5, key);
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "sukses");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void editProfil(profilAtribute add){
        try {
            pst = koneksi.prepareStatement("UPDATE profil SET nama = ?, alamat = ?, jenis_kelamin = ? , no_kontak = ? WHERE id = ?");
            pst.setString(1, add.getNama());
            pst.setString(2, add.getAlamat());
            pst.setString(3, add.getJk());
            pst.setString(4, add.getKontak());
            pst.setInt(5, add.getId());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "sukses");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
