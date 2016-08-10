/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Ultramilk
 */
public class rmAtribute {
    
    int id_rm;
    Date jam_dtg,tgl_dtg;
    String image_rad,hasil_rad;
    String lab,saran,p_norm, diagnosis, terapi;
    String kepala,leher,dada,perut,ekstremitas,saraf;
    String rp_sekarang,rp_dahulu,rp_keluarga,rp_nikah,rp_sosial;
    
    
    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTerapi() {
        return terapi;
    }

    public void setTerapi(String terapi) {
        this.terapi = terapi;
    }
    

    public String getImage_rad() {
        return image_rad;
    }

    public void setImage_rad(String image_rad) {
        this.image_rad = image_rad;
    }

    public String getHasil_rad() {
        return hasil_rad;
    }

    public void setHasil_rad(String hasil_rad) {
        this.hasil_rad = hasil_rad;
    }

    public String getKepala() {
        return kepala;
    }

    public void setKepala(String kepala) {
        this.kepala = kepala;
    }

    public String getLeher() {
        return leher;
    }

    public void setLeher(String leher) {
        this.leher = leher;
    }

    public String getDada() {
        return dada;
    }

    public void setDada(String dada) {
        this.dada = dada;
    }

    public String getPerut() {
        return perut;
    }

    public void setPerut(String perut) {
        this.perut = perut;
    }

    public String getEkstremitas() {
        return ekstremitas;
    }

    public void setEkstremitas(String ekstremitas) {
        this.ekstremitas = ekstremitas;
    }

    public String getSaraf() {
        return saraf;
    }

    public void setSaraf(String saraf) {
        this.saraf = saraf;
    }

    public String getRp_sekarang() {
        return rp_sekarang;
    }

    public void setRp_sekarang(String rp_sekarang) {
        this.rp_sekarang = rp_sekarang;
    }

    public String getRp_dahulu() {
        return rp_dahulu;
    }

    public void setRp_dahulu(String rp_dahulu) {
        this.rp_dahulu = rp_dahulu;
    }

    public String getRp_keluarga() {
        return rp_keluarga;
    }

    public void setRp_keluarga(String rp_keluarga) {
        this.rp_keluarga = rp_keluarga;
    }

    public String getRp_nikah() {
        return rp_nikah;
    }

    public void setRp_nikah(String rp_nikah) {
        this.rp_nikah = rp_nikah;
    }

    public String getRp_sosial() {
        return rp_sosial;
    }

    public void setRp_sosial(String rp_sosial) {
        this.rp_sosial = rp_sosial;
    }

    public int getId_ra() {
        return id_ra;
    }

    public void setId_ra(int id_ra) {
        this.id_ra = id_ra;
    }
    int id_anamnesis,id_rad,id,id_fisik;
    int id_ra;
    

    public int getId_rm() {
        return id_rm;
    }

    public int getId_fisik() {
        return id_fisik;
    }

    public void setId_fisik(int id_fisik) {
        this.id_fisik = id_fisik;
    }

    public void setId_rm(int id_rm) {
        this.id_rm = id_rm;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getSaran() {
        return saran;
    }

    public void setSaran(String saran) {
        this.saran = saran;
    }

    public Date getJam_dtg() {
        return jam_dtg;
    }

    public void setJam_dtg(Date jam_dtg) {
        this.jam_dtg = jam_dtg;
    }

    public Date getTgl_dtg() {
        return tgl_dtg;
    }

    public void setTgl_dtg(Date tgl_dtg) {
        this.tgl_dtg = tgl_dtg;
    }

    public int getId_anamnesis() {
        return id_anamnesis;
    }

    public void setId_anamnesis(int id_anamnesis) {
        this.id_anamnesis = id_anamnesis;
    }

    public int getId_rad() {
        return id_rad;
    }

    public void setId_rad(int id_rad) {
        this.id_rad = id_rad;
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public String getP_norm() {
        return p_norm;
    }

    public void setP_norm(String _p_norm) {
        this.p_norm = _p_norm;
    }
    
    
}
