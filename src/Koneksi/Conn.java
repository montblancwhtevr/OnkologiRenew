package Koneksi;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Conn {
    
    Connection conn;
    
    
    public Conn(){
        conn = null;
    }
    
    public Connection Connect(){
        try {
            Class.forName("org.postgresql.Driver");
            String dbURL = "jdbc:postgresql://localhost:5432/onkologidb";
            Properties prop = new Properties();
            prop.put("user", "postgres");
            prop.put("password", "141312");
            
            conn = DriverManager.getConnection(dbURL, prop);
            return conn;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    
}
