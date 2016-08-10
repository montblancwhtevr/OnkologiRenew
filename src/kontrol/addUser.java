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
import javax.swing.JOptionPane;
import model.addUserAtribute;

/**
 *
 * @author Ultramilk
 */
public class addUser {
    private PreparedStatement pst;
    private Connection koneksi = null;

    public addUser() {
        this.koneksi =  new Conn().Connect();
    }
    
    public int addNewUser(addUserAtribute add){
        String cppass;
        cppass = String.copyValueOf(add.getPassword());
        try{
            String sql = "INSERT INTO emp (username,password,role) VALUES(?,?,?)";
            pst = koneksi.prepareStatement(sql);
        
            pst.setString(1, add.getUsername());
            pst.setString(2, cppass);
            pst.setInt(3, add.getRole());
            pst.executeUpdate();
            
            String getLast = "SELECT id FROM emp ORDER BY id DESC LIMIT 1";
            pst = koneksi.prepareStatement(getLast);
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return rs.getInt(1);
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Penambahan user gagal !");
            return 0;
        }
        return 0;
    }
    
}
