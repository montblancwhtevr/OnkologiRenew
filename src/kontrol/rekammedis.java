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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import model.anamnesisAtribute;
import model.fisikAtribute;
import model.radiologisAtribute;
import model.rmAtribute;

/**
 *
 * @author Ultramilk
 */
public class rekammedis {
    private PreparedStatement pst;
    private Connection koneksi = null;

    public rekammedis() {
        this.koneksi =  new Conn().Connect();
    }

    
     
    public void addRekammedis(rmAtribute rm){
        java.sql.Date now = new java.sql.Date(rm.getTgl_dtg().getTime());
        try {
            String sql = "INSERT INTO rekam_medis (tanggal_dtg,lab,saran,id_anamnesis,id_fisik,id_rad,id,p_norm,diagnosis,terapi) VALUES (?,?,?,?,?,?,?,?,?,?)";
            pst = koneksi.prepareStatement(sql);
            
            pst.setDate(1, now);
            pst.setString(2, rm.getLab());
            pst.setString(3, rm.getSaran());
            pst.setInt(4, rm.getId_anamnesis());
            pst.setInt(5, rm.getId_fisik());
            pst.setInt(6, rm.getId_rad());
            pst.setInt(7, rm.getId());
            pst.setString(8, rm.getP_norm());
            pst.setString(9, rm.getDiagnosis());
            pst.setString(10, rm.getTerapi());
            
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Input Rekam Medis berhasil");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan input data!");        
        }
    }
    
    //SINGLE QUERY
    public int insertAnamnesis(anamnesisAtribute add){
        try{
            String sql = "INSERT INTO anamnesis (rp_sekarang,rp_dahulu,rp_keluarga,rp_nikah,rp_sosial) VALUES(?,?,?,?,?)";
            pst = koneksi.prepareStatement(sql);
            
            pst.setString(1, add.getRp_sekarang());
            pst.setString(2, add.getRp_dahulu());
            pst.setString(3, add.getRp_keluarga());
            pst.setString(4, add.getRp_nikah());
            pst.setString(5, add.getRp_sosial());
            
            pst.executeUpdate();
            
            String CheckQuery = "SELECT id_anamnesis FROM anamnesis ORDER BY id_anamnesis DESC LIMIT 1";
            pst = koneksi.prepareStatement(CheckQuery);
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
    public int insertFisik(fisikAtribute add){
        try{
            String sql = "INSERT INTO fisik (kepala,leher,dada,perut,ekstremitas,saraf) VALUES(?,?,?,?,?,?)";
            pst = koneksi.prepareStatement(sql);
            
            pst.setString(1, add.getKepala());
            pst.setString(2, add.getLeher());
            pst.setString(3, add.getDada());
            pst.setString(4, add.getPerut());
            pst.setString(5, add.getEkstremitas());
            pst.setString(6, add.getSaraf());
            
            pst.executeUpdate();
            
            String CheckQuery = "SELECT id_fisik FROM fisik ORDER BY id_fisik DESC LIMIT 1";
            pst = koneksi.prepareStatement(CheckQuery);
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
    public int insertRadiologis(radiologisAtribute add){
        try{
            String sql = "INSERT INTO radiologis (image_rad,hasil_rad) VALUES(?,?)";
            pst = koneksi.prepareStatement(sql);
            
            pst.setString(1, add.getImage_rad());
            pst.setString(2, add.getHasil_rad());
            
            pst.executeUpdate();
            
            String CheckQuery = "SELECT id_rad FROM radiologis ORDER BY id_rad DESC LIMIT 1";
            pst = koneksi.prepareStatement(CheckQuery);
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
    // -------------------------------------------------
    //EDIT RM
    public boolean editFisik(fisikAtribute f){
        try {
            String sql ="UPDATE fisik SET kepala = ?, leher = ?, dada =? , perut = ?, ekstremitas = ?, saraf = ? WHERE id_fisik = ?";
            pst = koneksi.prepareStatement(sql);
            pst.setString(1, f.getKepala());
            pst.setString(2, f.getLeher());
            pst.setString(3, f.getDada());
            pst.setString(4, f.getPerut());
            pst.setString(5, f.getEkstremitas());
            pst.setString(6, f.getSaraf());
            pst.setInt(7, f.getId());
            
            pst.executeUpdate();
            
            System.err.println("Edit Fisik Sukses");
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean editAnamnesis(anamnesisAtribute f){
        try {
            String sql = "UPDATE anamnesis set rp_sekarang = ?, rp_dahulu = ?, rp_keluarga = ?, rp_nikah = ?, rp_sosial = ? WHERE id_anamnesis = ?";
            pst = koneksi.prepareStatement(sql);
            pst.setString(1, f.getRp_sekarang());
            pst.setString(2, f.getRp_dahulu());
            pst.setString(3, f.getRp_keluarga());
            pst.setString(4, f.getRp_nikah());
            pst.setString(5, f.getRp_sosial());
            pst.setInt(6, f.getId());
            
            pst.executeUpdate();
            
            System.err.println("Edit Anamnesis Sukses");
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean editRad(radiologisAtribute f){
        try {
            String sql ="UPDATE radiologis SET image_rad = ?, hasil_rad = ? WHERE id_rad = ?";
            pst = koneksi.prepareStatement(sql);
            pst.setString(1, f.getImage_rad());
            pst.setString(2, f.getHasil_rad());
            pst.setInt(3, f.getId_rad());
           
            
            pst.executeUpdate();
            
            System.err.println("Edit Radiologis Sukses");
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean editRekam(rmAtribute f){
        try {
            String sql ="UPDATE rekam_medis SET lab = ?, saran = ?, diagnosis = ?, terapi = ? WHERE id_rm = ?";
            pst = koneksi.prepareStatement(sql);
            pst.setString(1, f.getLab());
            pst.setString(2, f.getSaran());
            pst.setInt(5, f.getId_rm());
            pst.setString(3, f.getDiagnosis());
            pst.setString(4, f.getTerapi());
           
            
            pst.executeUpdate();
            
            System.err.println("Edit RM Sukses");
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    //CORE DETAILS REKAM MEDIS
    public List<rmAtribute> getHistoryDetail(int id){
//        java.sql.Date now = new java.sql.Date(get.getTime());
        List<rmAtribute> dataCollected = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("SELECT * FROM rekam_medis r INNER JOIN anamnesis a ON r.id_anamnesis = a.id_anamnesis INNER JOIN radiologis rd ON r.id_rad = rd.id_rad INNER JOIN fisik f ON r.id_fisik = f.id_fisik WHERE r.id_rm = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                rmAtribute rekam = new rmAtribute();
                //Setting Anamnesis
                rekam.setId_anamnesis(rs.getInt("id_anamnesis"));
                rekam.setRp_sekarang(rs.getString("rp_sekarang"));
                rekam.setRp_dahulu(rs.getString("rp_dahulu"));
                rekam.setRp_keluarga(rs.getString("rp_keluarga"));
                rekam.setRp_nikah(rs.getString("rp_nikah"));
                rekam.setRp_sosial(rs.getString("rp_sosial"));
                //Setting Fisik
                rekam.setId_fisik(rs.getInt("id_fisik"));
                rekam.setKepala(rs.getString("kepala"));
                rekam.setLeher(rs.getString("leher"));
                rekam.setDada(rs.getString("dada"));
                rekam.setPerut(rs.getString("perut"));
                rekam.setEkstremitas(rs.getString("ekstremitas"));
                rekam.setSaraf(rs.getString("saraf"));

                //Setting Readiologis
                rekam.setId_rad(rs.getInt("id_rad"));
                rekam.setImage_rad(rs.getString("image_rad"));
                rekam.setHasil_rad(rs.getString("hasil_rad"));
                //Setting Rekam Medis
                rekam.setLab(rs.getString("lab"));
                rekam.setSaran(rs.getString("saran"));
                rekam.setDiagnosis(rs.getString("diagnosis"));
                rekam.setTerapi(rs.getString("terapi"));
                

                dataCollected.add(rekam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
    
    
    //-----------------------------------------------------------
    
    
    //MISC
    
     public List<rmAtribute> getHistory(String get){
        List<rmAtribute> dataCollected = new ArrayList<>();
        try {
            pst = koneksi.prepareStatement("select id_rm, tanggal_dtg from rekam_medis where p_norm = ? ORDER BY id_rm DESC");
            pst.setString(1, get);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                rmAtribute collect = new rmAtribute();
                collect.setId_rm(rs.getInt("id_rm"));
                collect.setTgl_dtg(rs.getDate("tanggal_dtg"));
                dataCollected.add(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollected;
    }
}


