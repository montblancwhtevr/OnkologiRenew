/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kontrol.addUser;
import kontrol.antrian;
import kontrol.clearense;
import kontrol.masterUser;
import model.addUserAtribute;
import model.antrianAtribute;
import model.loginAtribute;
import model.masterUserAtribute;
import model.pasienAtribute;

/**
 *
 * @author Ultramilk
 */
public final class pengguna extends javax.swing.JFrame {

    
    public masterUser masterUserData = new masterUser();
    public addUser add = new addUser();
    public ArrayList<masterUserAtribute> listDataUser = new ArrayList<>();
    public static final String DATE_FORMAT_NOW = "dd MMMMM YYYY"; 
    public String searchTyped;
    
    
    public pengguna() {
        initComponents();
        ftMasterUser();
        setHeaderElement();
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
    
    //SET PASS TO INVISIBLE
    private void setHeaderElement(){
        newPass.setVisible(false);
    }
    //END SET PASS TO INVISIBLE
    
    /*FETCH PENGGUNA*/
    public void ftMasterUser(){
        listDataUser = (ArrayList<masterUserAtribute>)masterUserData.getDt();
        if(!listDataUser.isEmpty()){
            Object[][] data = new Object[listDataUser.size()][3];
            int i = 0;
            for(masterUserAtribute dat : listDataUser){
                data[i][0] = i+1;
                data[i][1] = dat.getUsername();
                switch (dat.getRole()) {
                    case 1:
                        data[i][2] = "Admin";
                        break;
                    case 2:
                        data[i][2] = "Dokter";
                        break;
                    default:
                        data[i][2]= "-";
                        break;
                }
                
                i++;
                
            }
            
            jTable1.addMouseListener(new MouseAdapter() {
 
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        jTable1.getCellEditor().stopCellEditing();
                    }
                }
            });
            this.jTable1.setModel(new DefaultTableModel(data, new String[]{"No","Username","Role"}));
            this.jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            this.jScrollPane1.setViewportView(this.jTable1);
            this.jTable1.setAutoCreateRowSorter(true);
        }
    }
    /*END FETCH PENGGUNA*/
    
    /*TAMBAH PASIEN*/
    public void tambah(){
        String selected;
        addUserAtribute usrBaru = new addUserAtribute();
        usrBaru.setUsername(newUsername.getText());
        usrBaru.setPassword(newPass.getPassword());
        selected = newRole.getSelectedItem().toString();
        
        if(null != selected)switch (selected) {
            case "Admin":
                usrBaru.setRole(1);
                break;
            case "Dokter":
                usrBaru.setRole(2);
                break;
            default:
                break;
        }
        
        if(validateFormUserInput(newUsername.getText(), Arrays.toString(newPass.getPassword()), selected)){
            add.addNewUser(usrBaru);
//            c.clearFormUser();
        }
    }
    
    public boolean validateFormUserInput(String username, String password, String role){
        String errorMsg = "";
        if("".equals(username) || "".equals(password) || role == "--"){
            errorMsg = "Silahkan isi form dengan benar !";
            JOptionPane.showMessageDialog(null, errorMsg, "Warning",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    /*END OF TAMBAH PASIEN*/

    /*SEARCH PENGGUNA*/
//    public void searchUser(String searchString){
//        listDataUser = (ArrayList<masterUserAtribute>)masterUserData.getSearch(searchString);
//        if(!listDataUser.isEmpty()){
//            Object[][] data = new Object[listDataUser.size()][3];
//            int i = 0;
//            for(masterUserAtribute dat : listDataUser){
//                data[i][0] = i+1;
//                data[i][1] = dat.getUsername();
//                switch (dat.getRole()) {
//                    case 1:
//                        data[i][2] = "Admin";
//                        break;
//                    case 2:
//                        data[i][2] = "Dokter";
//                        break;
//                    default:
//                        data[i][2]= "-";
//                        break;
//                }
//                
//                i++;
//                
//            }
//            
//            jTable1.addMouseListener(new MouseAdapter() {
// 
//                @Override
//                public void mousePressed(MouseEvent e) {
//                    if (e.getClickCount() > 1) {
//                        jTable1.getCellEditor().stopCellEditing();
//                    }
//                }
//            });
//            this.jTable1.setModel(new DefaultTableModel(data, new String[]{"No","Username","Role"}));
//            this.jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
//            this.jScrollPane1.setViewportView(this.jTable1);
//            this.jTable1.setAutoCreateRowSorter(true);
//        }else{
//            this.jTable1.setModel(new DefaultTableModel(null, new String[]{"No","Username","Role"}));
//        }
//    }
    /*END OF SEARCH PENGGUNA*/
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cariTx = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        newUsername = new javax.swing.JTextField();
        newPass = new javax.swing.JPasswordField();
        newRole = new javax.swing.JComboBox<>();
        tambahPengguna = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MASTER PENGGUNA");

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabel Pengguna"));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "1", "2", "3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(jTable1);

        cariTx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariTxKeyReleased(evt);
            }
        });

        jLabel35.setText("Cari :");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cariTx, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cariTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Tambah Pengguna Baru"));

        jLabel14.setText("Username");

        jLabel16.setText("Jabatan");

        newPass.setEditable(false);
        newPass.setText("123456");

        newRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Admin", "Dokter" }));

        tambahPengguna.setText("Tambah");
        tambahPengguna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahPenggunaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(newRole, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(newPass, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tambahPengguna, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(newUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tambahPengguna)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                .addComponent(newPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cariTxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariTxKeyReleased
        searchTyped = cariTx.getText();
//        searchUser(searchTyped);
    }//GEN-LAST:event_cariTxKeyReleased

    private void tambahPenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahPenggunaActionPerformed
        tambah();
        ftMasterUser();
    }//GEN-LAST:event_tambahPenggunaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(pengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pengguna().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cariTx;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPasswordField newPass;
    public static javax.swing.JComboBox<String> newRole;
    public static javax.swing.JTextField newUsername;
    private javax.swing.JButton tambahPengguna;
    // End of variables declaration//GEN-END:variables
}
