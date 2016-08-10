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
import model.loginAtribute;
import model.profilAtribute;

/**
 *
 * @author Ultramilk
 */
public class validasiLogin {
    
    private PreparedStatement pst;
    private Connection koneksi = null;

    public validasiLogin() {
        this.koneksi =  new Conn().Connect();
    }
    
    public boolean validateLogin(loginAtribute lx){
        String cppass;
        cppass = String.copyValueOf(lx.getPassword());
        try {
            String sql = "SELECT * FROM emp WHERE username = ? AND password = ?";
            pst = koneksi.prepareStatement(sql);
            
            pst.setString(1, lx.getUsername());
            pst.setString(2, cppass);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public boolean validatePassword(String username, String password){
        try {
            String sql = "SELECT * FROM emp WHERE username = ? AND password = ?";
            pst = koneksi.prepareStatement(sql);
            
            pst.setString(1, username);
            pst.setString(2, password);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public int validateRole(loginAtribute a){
        try {
            String sql = "SELECT role FROM emp WHERE username = ?";
            pst = koneksi.prepareStatement(sql);
            
            pst.setString(1, a.getUsername());
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int geidUser(String a){
        try {
            String sql = "SELECT id from emp WHERE username = ?";
             pst = koneksi.prepareStatement(sql);
             
             pst.setString(1, a);
             
             ResultSet rs = pst.executeQuery();
             
             if(rs.next())
                 return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public void ubahPassword(String username, String p){
        try {
            String sql = "UPDATE emp SET password = ? WHERE username = ?";
            pst = koneksi.prepareStatement(sql);
            
            pst.setString(1, p);
            pst.setString(2, username);
            
            pst.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
