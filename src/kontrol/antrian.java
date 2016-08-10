/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontrol;

import Koneksi.Conn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.antrianAtribute;

/**
 *
 * @author Ultramilk
 */
public class antrian {
    private PreparedStatement pst;
    private Connection koneksi = null;

    public antrian() {
        this.koneksi =  new Conn().Connect();
    }
    
    public List<antrianAtribute> getAntriData(){
        List<antrianAtribute> antriDt = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT * from tempque");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                antrianAtribute collect = new antrianAtribute();
                collect.setNo_antrian(rs.getInt("no_antrian"));
                collect.setRm(rs.getString("rm"));
                antriDt.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return antriDt;
    }
    
    public int getnoAntri(){
        try {
            pst = koneksi.prepareStatement("SELECT no_antrian from tempque ORDER BY no_antrian DESC LIMIT 1"); 
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public void insertQueue(String norm){
        try {
            String sql = "INSERT into tempque (rm) VALUES (?)";
            pst = koneksi.prepareStatement(sql);
            
            pst.setString(1, norm);
            
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(antrian.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public boolean resetQue(){
        try {
            pst = koneksi.prepareStatement("DELETE FROM tempque");
            pst.executeUpdate();
            
            pst = koneksi.prepareStatement("alter sequence antrian restart");
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deQueue(int key){
        try {
            pst = koneksi.prepareStatement("DELETE FROM tempque WHERE no_antrian = ?");
            pst.setInt(1, key);
            
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
