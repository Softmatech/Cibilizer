/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Form;

import com.ezware.oxbow.swingbits.table.filter.TableRowFilterSupport;
import dataBase.Querry;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author legacy
 */
public class client_Register extends javax.swing.JFrame {
    Querry kerry = new Querry();
    int code = 0;
    
    String[] columnas = {"Adresse(s)"};
    String[] columnasDos = {"Telephone(s)"};
    String[] columnasCinco = {"Courrier(s)"};
    String[] columnasTres = {"Reseaux","Id"};
    String[] columnasCuatro = {"Client(s)","Website","Type"};
    String[] columnasSexto = {"Client(s)","Type(s)","Email(s)","Téléphone(s)","Selectionner"};
    String[][] Datos = {};
    String[][] DatosDos = {};
    String[][] DatosTres = {};
    String[][] DatosCuatro = {};
    String[][] DatosCinco = {};
    String[][] DatosSexto = {};
    DefaultTableModel tablemodel;
    DefaultTableModel tablemodelDos;
    DefaultTableModel tablemodelTres;
    DefaultTableModel tablemodelCuatro;
    DefaultTableModel tablemodelCinco;
    DefaultTableModel tablemodelSexto;
    
    /**
     * Creates new form client_Register
     */
    public client_Register() {
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(this); 
        kerry.Combo(client_type, "SELECT desc_type FROM client_type");
        kerry.Combo(comboFilter, "SELECT desc_type FROM client_type");
        kerry.Combo(sociaux, "SELECT name FROM social_network");
        tablemodel = new DefaultTableModel(Datos, columnas);
        tablemodelDos = new DefaultTableModel(DatosDos, columnasDos);
        tablemodelCinco = new DefaultTableModel(DatosCinco, columnasCinco);
        tablemodelTres = new DefaultTableModel(DatosTres, columnasTres);
        tablemodelCuatro = new DefaultTableModel(DatosCuatro, columnasCuatro);
        tablemodelSexto = (DefaultTableModel)tblSearch.getModel();
        
        TableRowFilterSupport.forTable(tblClient).searchable(true).apply();
        TableRowFilterSupport.forTable(tblSearch).searchable(true).apply();
        
        tblAdress.setModel(tablemodel);
        tblTel.setModel(tablemodelDos);
        tblReseaux.setModel(tablemodelTres);
        tblClient.setModel(tablemodelCuatro);
        tblEmail.setModel(tablemodelCinco);
        tblSearch.setModel(tablemodelSexto);
        
        comboChangeListener();
        nouvo();
        
        tableChangeListener();
        tableChangeListenerDos();
        tableChangeListenerTres();
        tableChangeListenerCuatro();
        tableChangeListenerCinco();
        
        adresKeyReleased(null);
        netKeyReleased(null);
        telKeyReleased(null);
        mlKeyReleased(null);
        
        emailFilterItemStateChanged(null);
        telFilterItemStateChanged(null);
        selectsFilterItemStateChanged(null);
        
        jProgressBar1.setIndeterminate(true);
        
//        tblSearch.getColumnModel().getColumn(3).setCellEditor(new Celda_CheckBox());
//        tblSearch.getColumnModel().getColumn(3).setCellRenderer(new Render_CheckBox());
    }
    
    
    //..................................................................2nd Part
   private void getSelectStatus(){
        
        if(selectsFilter.isSelected()){
            selectsFilter.setText("Selectionnez Tous");
            
            for (int i = 0; i < tblSearch.getRowCount(); i++) {
                tblSearch.setValueAt(true, i, 4);
            }
        }
        
        else{
            selectsFilter.setText("Ne pas Selectionnez");
            for (int i = 0; i < tblSearch.getRowCount(); i++) {
                tblSearch.setValueAt(false, i, 4);
            }
        }
    } 
     
   private void fullDatosDos(){
        kerry.removeDatosOnTable(tablemodelSexto);
        String ch = "SELECT idclient,nom,desc_type FROM client c join client_type ct on (c.idclient_type = ct.idclient_type)";
        int codec = kerry.ResultsetCount(ch);
        
        SwingWorker<Void, Void> workers;             
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception
            {
            String sql = "SELECT idclient,nom,desc_type FROM client c join client_type ct on (c.idclient_type = ct.idclient_type) order by idclient desc";
            System.out.println("sql " + sql);
            ResultSet rs = kerry.Rezulta(sql);
            while (rs.next()) {
                jProgressBar1.setIndeterminate(false);
                comboFilter.setEnabled(false);
                emailFilter.setEnabled(false);
                telFilter.setEnabled(false);
                searchButton.setEnabled(false);
                selectsFilter.setEnabled(false);
                String nm = "",wb = "",tp ="",ml = "",tl = ""; int cod = 0; 
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
               cod = rs.getInt("idclient");
               nm = rs.getString("nom");
               tp = kerry.PrepaCad(rs.getString("desc_type"));
               
               //-- email recoger
               sql = "select courrier FROM client_vs_email where idclient = "+cod;
                    System.out.println("sql-->> "+sql);
                    ResultSet rsd = kerry.Rezulta(sql);
                    while (rsd.next()) {   
                        if(ml.isEmpty()){
                    ml = rsd.getString("courrier");
                        }else{
                    ml += "/"+ rsd.getString("courrier");
                        }
                    }
                    //-- tel recoger
               sql = "select numero FROM client_telephone where idclient = "+cod;
                    System.out.println("sql-->> "+sql);
                    ResultSet rsdn = kerry.Rezulta(sql);
                    while (rsdn.next()) {   
                        if(tl.isEmpty()){
                    tl = rsdn.getString("numero");
                        }else{
                    tl += "/"+ rsdn.getString("numero");
                        }
                    }
                } catch (NullPointerException e) {
                }
                
                boolean resp = false;
                
                Object[] Datos = {nm,tp,ml,tl,resp};
                tablemodelSexto.addRow(Datos);
                int sm = ((tblSearch.getRowCount() * 100)/ codec);
                jProgressBar1.setValue(sm);
                jProgressBar1.setStringPainted(true);
                jLabel14.setText(" Nombre de registre(s) : ");
                jLabel15.setText(tblSearch.getRowCount() +"");
                
            }
            setCursor(Cursor.getDefaultCursor());
            comboFilter.setEnabled(true);
            emailFilter.setEnabled(true);
            telFilter.setEnabled(true);
            searchButton.setEnabled(true);
            selectsFilter.setEnabled(true);
        return null;
            }
        };
  workers.execute(); 
    }
    
    
    private void getEmailStatus(){
        
        if(emailFilter.isSelected()){
            emailFilter.setText("Email(s) Inclus");
        }
        
        else{
            emailFilter.setText("Email(s) Non-Inclus");
        }
    } 
    
    private void getTelStatus(){
        
        if(telFilter.isSelected()){
            telFilter.setText("Téléphone(s) Inclus");
        }
        
        else{
            telFilter.setText("Téléphone(s) Non-Inclus");
        }
    } 
    //.....................................................................................
    private void nouvo(){
        name.setText(null);
        sitweb.setText(null);
        adres.setText(null);
        tel.setText(null);
        ml.setText(null);
        net.setText(null);
        client_type.setSelectedIndex(0);
        name.setText(null);
        sitweb.setText(null);
        kerry.removeDatosOnTable(tablemodel);
        kerry.removeDatosOnTable(tablemodelDos);
        kerry.removeDatosOnTable(tablemodelTres);
        kerry.removeDatosOnTable(tablemodelCinco);
        kerry.removeDatosOnTable(tablemodelCuatro);
        tel.setValue(null);
        code = 0;
    }
            
    private void fullDatosRes(int cd){
        kerry.removeDatosOnTable(tablemodelTres);
        SwingWorker<Void, Void> workers;             
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception
            {
            String sql = "SELECT name,id from client_social_network csn join social_network sn on (csn.idsocial_network = sn.idsocial_network) where idclient = "+cd;
            System.out.println("sql " + sql);
            ResultSet rs = kerry.Rezulta(sql);
            while (rs.next()) {
                String sn = "";
                String id = "";
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                
               sn = kerry.PrepaCad(rs.getString("name"));
               id = kerry.PrepaCad(rs.getString("id"));
              
                } catch (NullPointerException e) {
                }
                
                
                Object[] Datos = {sn,id};
                tablemodelTres.addRow(Datos);
            }
            setCursor(Cursor.getDefaultCursor());
        return null;
            }
        };
  workers.execute(); 
    }
    
    private void fullDatosEmail(int cd){
        kerry.removeDatosOnTable(tablemodelCinco);
        SwingWorker<Void, Void> workers;             
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception
            {
            String sql = "SELECT courrier FROM client_vs_email where idclient = "+cd;
            System.out.println("sql " + sql);
            ResultSet rs = kerry.Rezulta(sql);
            while (rs.next()) {
                String cr = "";
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                
               cr = kerry.PrepaCad(rs.getString("courrier"));
              
                } catch (NullPointerException e) {
                }
                
                
                Object[] Datos = {cr};
                tablemodelCinco.addRow(Datos);
            }
            setCursor(Cursor.getDefaultCursor());
        return null;
            }
        };
  workers.execute(); 
    }
    
    private void fullDatosTel(int cd){
        kerry.removeDatosOnTable(tablemodelDos);
        SwingWorker<Void, Void> workers;             
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception
            {
            String sql = "SELECT numero FROM client_telephone where idclient = "+cd;
            System.out.println("sql " + sql);
            ResultSet rs = kerry.Rezulta(sql);
            while (rs.next()) {
                String nu = "";
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                
               nu = kerry.PrepaCad(rs.getString("numero"));
              
                } catch (NullPointerException e) {
                }
                
                
                Object[] Datos = {nu};
                tablemodelDos.addRow(Datos);
            }
            setCursor(Cursor.getDefaultCursor());
        return null;
            }
        };
  workers.execute(); 
    }
    
    private void fullDatosAdress(int cd){
        kerry.removeDatosOnTable(tablemodel);
        SwingWorker<Void, Void> workers;             
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception
            {
            String sql = "SELECT adresse FROM client_adresse where idclient = "+cd;
            System.out.println("sql " + sql);
            ResultSet rs = kerry.Rezulta(sql);
            while (rs.next()) {
                String ad = "";
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                
               ad = kerry.PrepaCad(rs.getString("adresse"));
              
                } catch (NullPointerException e) {
                }
                
                
                Object[] Datos = {ad};
                tablemodel.addRow(Datos);
            }
            setCursor(Cursor.getDefaultCursor());
        return null;
            }
        };
  workers.execute(); 
    }
    
   private void fullDatos(int cd){
        kerry.removeDatosOnTable(tablemodelCuatro);
        SwingWorker<Void, Void> workers;             
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception
            {
            String sql = "SELECT nom,website,desc_type FROM client c join client_type ct on (c.idclient_type = ct.idclient_type) where c.idclient_type = "+cd+" order by idclient desc";
            System.out.println("sql " + sql);
            ResultSet rs = kerry.Rezulta(sql);
            while (rs.next()) {
                String nm = "",wb = "",tp ="";
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                
               nm = rs.getString("nom");
               wb = kerry.PrepaCad(rs.getString("website"));
               tp = kerry.PrepaCad(rs.getString("desc_type"));
              
                } catch (NullPointerException e) {
                }
                
                
                Object[] Datos = {nm,wb,tp};
                tablemodelCuatro.addRow(Datos);
                jLabel12.setText(String.valueOf(tblClient.getRowCount()));
            }
            setCursor(Cursor.getDefaultCursor());
        return null;
            }
        };
  workers.execute(); 
    }
    
    private void fullDatos(){
        kerry.removeDatosOnTable(tablemodelCuatro);
        SwingWorker<Void, Void> workers;             
        workers = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception
            {
            String sql = "SELECT nom,website,desc_type FROM client c join client_type ct on (c.idclient_type = ct.idclient_type) order by idclient desc";
            System.out.println("sql " + sql);
            ResultSet rs = kerry.Rezulta(sql);
            while (rs.next()) {
                String nm = "",wb = "",tp ="";
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                
               nm = rs.getString("nom");
               wb = kerry.PrepaCad(rs.getString("website"));
               tp = kerry.PrepaCad(rs.getString("desc_type"));
              
                } catch (NullPointerException e) {
                }
                
                
                Object[] Datos = {nm,wb,tp};
                tablemodelCuatro.addRow(Datos);
                jLabel12.setText(String.valueOf(tblClient.getRowCount()));
            }
            setCursor(Cursor.getDefaultCursor());
        return null;
            }
        };
  workers.execute(); 
    }
    
    private void save(){
        if(client_type.getSelectedIndex() <=0){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Desole Selectionnez un registre valide");
            client_type.setBackground(Color.red);
            client_type.setForeground(Color.white);
            client_type.requestFocus();
            return;
        }
        
        if(name.getText().isEmpty()){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Desole ce champs ne doit etre vide");
            name.setBackground(Color.red);
            name.setForeground(Color.white);
            name.requestFocus();
            return;
        }
        
//        if(sitweb.getText().isEmpty()){
//            JOptionPane.showMessageDialog(this, "Desole ce champs ne doit etre vide");
//            sitweb.setBackground(Color.red);
//            sitweb.setForeground(Color.white);
//            sitweb.requestFocus();
//            return;
//        }
        
        if(tblAdress.getRowCount() < 0){
            JOptionPane.showMessageDialog(client_type, "Desole vous devez entrer au moins une adresse valide");
            return;
        }
        
        if(tblTel.getRowCount() < 0){
            JOptionPane.showMessageDialog(client_type, "Desole vous devez entrer au moins une adresse valide");
            return;
        }
        
        if(tblEmail.getRowCount() < 0){
            JOptionPane.showMessageDialog(client_type, "Desole vous devez entrer au moins une adresse valide");
            return;
        }
        
        if(tblReseaux.getRowCount() < 0){
            JOptionPane.showMessageDialog(client_type, "Desole vous devez entrer au moins une adresse valide");
            return;
        }
        
        String nom =  kerry.PrepaCad(name.getText().trim().toUpperCase());
        int idtype =  kerry.getCodeFromCombo("client_type", "idclient_type", client_type,"desc_type");
        String websit = kerry.PrepaCad(sitweb.getText().trim());
        
        String sql = "call client_procedure ("+code+",'"+nom+"',"+idtype+",'"+websit+"')";
        System.out.println("sql -->> "+sql);
        if(kerry.ejecutarSql(sql) == 1){
            
            if(code == 0){
            code = codClient();
            }
            
            //delete on correspondant table before add
            kerry.deleteOnTable("client_adresse", "idclient", code);
            //insert adress
            for (int i = 0; i < tblAdress.getRowCount(); i++) {
                
            String adr = kerry.PrepaCad(tblAdress.getValueAt(i, 0).toString().trim());
            sql = "call client_adresse_procedure ("+code+",'"+adr+"')";
                System.out.println("sql1 ->> "+sql);
                kerry.ejecutarSql(sql);
            }
            
            //delete on correspondant table before add
            kerry.deleteOnTable("client_telephone", "idclient", code);
            //insert telephone
            for (int i = 0; i < tblTel.getRowCount(); i++) {
                
            String tele = kerry.PrepaCad(tblTel.getValueAt(i, 0).toString().trim());
            
            sql = "call client_telephone_procedure ("+code+",'"+tele+"')";
                System.out.println("sql2 ->> "+sql);
                kerry.ejecutarSql(sql);
            }
            
            //delete on correspondant table before add
            kerry.deleteOnTable("client_vs_email", "idclient", code);
            //insert email
            for (int i = 0; i < tblEmail.getRowCount(); i++) {
                
            String eml= kerry.PrepaCad(tblEmail.getValueAt(i, 0).toString().trim());
            
            sql = "call client_vs_email_procedure ("+code+",'"+eml+"')";
                System.out.println("sql4 ->> "+sql);
                kerry.ejecutarSql(sql);
            }
            
            //delete on correspondant table before add
            kerry.deleteOnTable("client_social_network", "idclient", code);
            //insert reseaux
            for (int i = 0; i < tblReseaux.getRowCount(); i++) {
                
            String res = kerry.PrepaCad(tblReseaux.getValueAt(i, 0).toString().trim());
            String id = tblReseaux.getValueAt(i, 1).toString().trim();
            int cod = kerry.getCodeFromString("social_network", "idsocial_network", res, "name");
            
            sql = "call client_social_procedure ("+code+","+cod+",'"+id+"')";
                System.out.println("sql3->> "+sql);
                kerry.ejecutarSql(sql);
            }
            
            //insert activites
            String act = activites.getText().trim();
            if(!act.isEmpty()){
            sql = "call activites_procedure ("+code+",'"+act+"')";
            System.out.println("act -->> "+act);
            kerry.ejecutarSql(sql);
            }
            nouvo();
        }
    }
    
    private int codClient(){
        int codclient = 0;
        String sql = "select idclient from client where registration_dat = now()";
        System.out.println("sqq "+sql);
        ResultSet rs = kerry.Rezulta(sql);
        try {
            if(rs.last()){
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                codclient = rs.getInt("idclient");  System.out.println("codclient "+codclient);
            }
        } catch (SQLException ex) {
            Logger.getLogger(client_Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        setCursor(Cursor.getDefaultCursor());
        return codclient;
    }
    
    private void comboChangeListener(){
        
        client_type.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int idtype =  kerry.getCodeFromCombo("client_type", "idclient_type", client_type,"desc_type");
                System.out.println("idtype -->> "+idtype);
                if(idtype > 0){
                fullDatos(idtype);
                }
                else{
                    fullDatos();
                }
            }
        });
    }

    private void tableChangeListener(){

        tblAdress.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tblAdress.getSelectedRow() >= 0 || tblAdress.getRowCount() < 0){
                   adres.setText(tblAdress.getValueAt(tblAdress.getSelectedRow(), 0).toString());
                   ret.setEnabled(true);
            }
                else{
                   ret.setEnabled(false);
                }
            }
            
        });
        ret.setEnabled(false);
    }
    
    private void tableChangeListenerDos(){
        tblTel.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tblTel.getSelectedRow() >= 0 || tblTel.getRowCount() > 0){
                   tel.setText(tblTel.getValueAt(tblTel.getSelectedRow(), 0).toString());
                   ret2.setEnabled(true);
            }
                else{
                   ret2.setEnabled(false);
                }
            }
            
        });
        ret2.setEnabled(false);
    }
    
    private void tableChangeListenerTres(){
        tblReseaux.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tblReseaux.getSelectedRow() >= 0 || tblReseaux.getRowCount() < 0){
                   net.setText(tblReseaux.getValueAt(tblReseaux.getSelectedRow(), 1).toString());
                   ret3.setEnabled(true);
            }
                else{
                   ret3.setEnabled(false);
                }
            }
            
        });
        ret3.setEnabled(false);
    }
    
    private void tableChangeListenerCuatro(){
        tblClient.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tblClient.getSelectedRow() >= 0){
                String nom = tblClient.getValueAt(tblClient.getSelectedRow(), 0).toString();
                String web = tblClient.getValueAt(tblClient.getSelectedRow(), 1).toString();
                code = kerry.getCodeFromString("client", "idclient", nom,"nom");
//                
                name.setText(nom);
                sitweb.setText(web);
//                client_type.setSelectedItem(tblClient.getValueAt(tblClient.getSelectedRow(), 2));
//                
                fullDatosAdress(code);
                fullDatosTel(code);
                fullDatosEmail(code);
                fullDatosRes(code);
            }
            }
        });
//        ret3.setEnabled(false);
    }
    
    private void tableChangeListenerCinco(){
        tblEmail.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tblEmail.getSelectedRow() >= 0 || tblEmail.getRowCount() < 0){
                   ml.setText(tblEmail.getValueAt(tblEmail.getSelectedRow(), 0).toString());
                   ret4.setEnabled(true);
            }
                else{
                   ret4.setEnabled(false);
                }
            }
        });
        ret4.setEnabled(false);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        client_type = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        name = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        sitweb = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        adres = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        add = new javax.swing.JButton();
        ret = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAdress = new javax.swing.JTable(){
            public boolean isCellEditable (int rowIndex,int colIndex){
                return false;
            }
        };
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        add1 = new javax.swing.JButton();
        ret2 = new javax.swing.JButton();
        tel = new javax.swing.JFormattedTextField();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTel = new javax.swing.JTable(){

            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblClient = new javax.swing.JTable(){
            public boolean iscelleditable(int rowIndex,int colIndex){
                return false;
            }
        };
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        add3 = new javax.swing.JButton();
        ret4 = new javax.swing.JButton();
        ml = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblEmail = new javax.swing.JTable(){

            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        net = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        add2 = new javax.swing.JButton();
        ret3 = new javax.swing.JButton();
        sociaux = new javax.swing.JComboBox();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblReseaux = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex,int colIndex){
                return false;
            }
        };
        jPanel21 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        activites = new javax.swing.JTextArea();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        comboFilter = new javax.swing.JComboBox();
        emailFilter = new javax.swing.JCheckBox();
        telFilter = new javax.swing.JCheckBox();
        searchButton = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblSearch = new javax.swing.JTable(){
            boolean res = false;
            public boolean isCellditable(int rowIndex,int colIndex){
                if(colIndex == 3){
                    res = true;
                }
                return res;
            }
        };
        jPanel26 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        selectsFilter = new javax.swing.JCheckBox();

        jMenuItem1.setText("Nouvo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setComponentPopupMenu(jPopupMenu1);

        jPanel2.setBackground(new java.awt.Color(51, 204, 255));
        jPanel2.setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Client Register  ");
        jPanel2.add(jLabel1, "card2");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setComponentPopupMenu(jPopupMenu1);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel2.setLabelFor(client_type);
        jLabel2.setText("Type de Client :");

        client_type.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        client_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        client_type.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                client_typeItemStateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel3.setLabelFor(name);
        jLabel3.setText("Nom du client :");

        name.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        name.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nameMouseClicked(evt);
            }
        });
        name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameKeyReleased(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-save_close 2.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel8.setText("SiteWeb : ");

        sitweb.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        sitweb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sitwebMouseClicked(evt);
            }
        });
        sitweb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sitwebKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(client_type, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sitweb, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(client_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(sitweb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton4)))
        );

        jPanel4.setBorder(new org.edisoncor.gui.util.DropShadowBorder());

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-geo_fence.png"))); // NOI18N
        jLabel4.setText("Adresse ");
        jLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        adres.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        adres.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adresMouseClicked(evt);
            }
        });
        adres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adresActionPerformed(evt);
            }
        });
        adres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                adresKeyReleased(evt);
            }
        });

        jPanel14.setLayout(new java.awt.GridLayout(1, 0));

        add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-circled_chevron_down_filled.png"))); // NOI18N
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });
        jPanel14.add(add);

        ret.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-circled_chevron_up_filled.png"))); // NOI18N
        ret.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retActionPerformed(evt);
            }
        });
        jPanel14.add(ret);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adres, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        tblAdress.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblAdress.setShowGrid(false);
        jScrollPane1.setViewportView(tblAdress);

        jPanel7.add(jScrollPane1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBorder(new org.edisoncor.gui.util.DropShadowBorder());

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-phone_not_being_used_filled.png"))); // NOI18N
        jLabel5.setText("Telephone(s)");
        jLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel15.setLayout(new java.awt.GridLayout(1, 0));

        add1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-circled_chevron_down_filled.png"))); // NOI18N
        add1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add1ActionPerformed(evt);
            }
        });
        jPanel15.add(add1);

        ret2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-circled_chevron_up_filled.png"))); // NOI18N
        ret2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ret2ActionPerformed(evt);
            }
        });
        jPanel15.add(ret2);

        try {
            tel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("+(509) ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        tel.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        tel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                telMouseClicked(evt);
            }
        });
        tel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                telActionPerformed(evt);
            }
        });
        tel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                telKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tel, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));

        tblTel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTel.setShowHorizontalLines(false);
        tblTel.setShowVerticalLines(false);
        jScrollPane2.setViewportView(tblTel);

        jPanel10.add(jScrollPane2);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(new org.edisoncor.gui.util.DropShadowBorder());
        jPanel6.setComponentPopupMenu(jPopupMenu1);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Client Deja Enregistrer");
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel7.setComponentPopupMenu(jPopupMenu1);

        jScrollPane4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblClient.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        tblClient.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblClient.setComponentPopupMenu(jPopupMenu1);
        jScrollPane4.setViewportView(tblClient);

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Nombres de Registre(s) : ");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel12.setText("jLabel12");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12))
                .addContainerGap())
        );

        jPanel17.setBorder(new org.edisoncor.gui.util.DropShadowBorder());

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-edit_file.png"))); // NOI18N
        jLabel9.setText("Email(s)");
        jLabel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel19.setLayout(new java.awt.GridLayout(1, 0));

        add3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-circled_chevron_down_filled.png"))); // NOI18N
        add3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add3ActionPerformed(evt);
            }
        });
        jPanel19.add(add3);

        ret4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-circled_chevron_up_filled.png"))); // NOI18N
        ret4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ret4ActionPerformed(evt);
            }
        });
        jPanel19.add(ret4);

        ml.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        ml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mlActionPerformed(evt);
            }
        });
        ml.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mlKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ml)
                .addGap(18, 18, 18)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(ml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.LINE_AXIS));

        tblEmail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblEmail.setShowHorizontalLines(false);
        tblEmail.setShowVerticalLines(false);
        jScrollPane5.setViewportView(tblEmail);

        jPanel20.add(jScrollPane5);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(new org.edisoncor.gui.util.DropShadowBorder());

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-groups.png"))); // NOI18N
        jLabel6.setText("Reseaux Sociaux");
        jLabel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        net.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        net.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                netMouseClicked(evt);
            }
        });
        net.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                netActionPerformed(evt);
            }
        });
        net.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                netKeyReleased(evt);
            }
        });

        jPanel16.setLayout(new java.awt.GridLayout(1, 0));

        add2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-circled_chevron_down_filled.png"))); // NOI18N
        add2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add2ActionPerformed(evt);
            }
        });
        jPanel16.add(add2);

        ret3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-circled_chevron_up_filled.png"))); // NOI18N
        ret3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ret3ActionPerformed(evt);
            }
        });
        jPanel16.add(ret3);

        sociaux.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        sociaux.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sociaux.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sociauxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(sociaux, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(net, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                    .addContainerGap(288, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sociaux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(net, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        tblReseaux.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblReseaux.setShowHorizontalLines(false);
        tblReseaux.setShowVerticalLines(false);
        jScrollPane3.setViewportView(tblReseaux);

        jPanel13.add(jScrollPane3);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel21.setBorder(new org.edisoncor.gui.util.DropShadowBorder());

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Activité(s)");
        jLabel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        activites.setColumns(20);
        activites.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        activites.setRows(5);
        jScrollPane6.setViewportView(activites);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane6))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Registre(s)", jPanel1);

        jPanel23.setBackground(new java.awt.Color(51, 204, 255));
        jPanel23.setBorder(new org.edisoncor.gui.util.DropShadowBorder());
        jPanel23.setLayout(new java.awt.CardLayout());

        jLabel13.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Opération(s)  ");
        jPanel23.add(jLabel13, "card2");

        jPanel24.setBorder(new org.edisoncor.gui.util.DropShadowBorder());
        jPanel24.setLayout(new java.awt.GridLayout(1, 0));

        comboFilter.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        comboFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel24.add(comboFilter);

        emailFilter.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        emailFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                emailFilterItemStateChanged(evt);
            }
        });
        jPanel24.add(emailFilter);

        telFilter.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        telFilter.setText(" ");
        telFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                telFilterItemStateChanged(evt);
            }
        });
        jPanel24.add(telFilter);

        searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-detective.png"))); // NOI18N
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        jPanel24.add(searchButton);

        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel25.setLayout(new javax.swing.BoxLayout(jPanel25, javax.swing.BoxLayout.LINE_AXIS));

        tblSearch.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tblSearch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Client(s)", "Type(s)", "Email(s)", "Telephone(s)", "Selection"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tblSearch);
        if (tblSearch.getColumnModel().getColumnCount() > 0) {
            tblSearch.getColumnModel().getColumn(0).setPreferredWidth(200);
            tblSearch.getColumnModel().getColumn(1).setPreferredWidth(70);
            tblSearch.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblSearch.getColumnModel().getColumn(3).setPreferredWidth(150);
            tblSearch.getColumnModel().getColumn(4).setMinWidth(70);
            tblSearch.getColumnModel().getColumn(4).setPreferredWidth(30);
            tblSearch.getColumnModel().getColumn(4).setMaxWidth(80);
        }

        jPanel25.add(jScrollPane7);

        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("jLabel14");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel15.setText("jLabel15");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                    .addContainerGap(302, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 858, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        selectsFilter.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        selectsFilter.setText("jCheckBox1");
        selectsFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selectsFilterItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 255, Short.MAX_VALUE)
                        .addComponent(selectsFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel22Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectsFilter))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel22Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(638, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Opération(s)", jPanel22);

        getContentPane().add(jTabbedPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        if(adres.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Desole ce champs ne doit etre vide");
            adres.setBackground(Color.red);
            adres.setForeground(Color.white);
            adres.requestFocus();
            return;
        }
        
        String[] text = {adres.getText()};
        if(kerry.isElementExistInGrid(tblAdress,text)==true){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Cet element est deja dans la grille");
            return;
        }
        else{
            Object [] f ={adres.getText()};
            try {
             tablemodel.addRow(f);   
            } catch (NullPointerException e) {
            }
            
            adres.setText(null);
            adres.requestFocus();
        }
        
    }//GEN-LAST:event_addActionPerformed

    private void add1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add1ActionPerformed
        if(tel.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Desole ce champs ne doit etre vide");
            tel.setBackground(Color.red);
            tel.setForeground(Color.white);
            tel.requestFocus();
            return;
        }
        
        String[] text = {tel.getText()};
        if(kerry.isElementExistInGrid(tblTel,text)==true){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Cet element est deja dans la grille");
            return;
        }
        else{
            Object [] f ={tel.getText()};
            try {
             tablemodelDos.addRow(f);   
            } catch (NullPointerException e) {
            }
            
            tel.setText(null);
            tel.requestFocus();
        }
    }//GEN-LAST:event_add1ActionPerformed

    private void add2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add2ActionPerformed
        if(sociaux.getSelectedIndex()<=0){
            JOptionPane.showMessageDialog(this, "Desole Selectionnez un registre valide");
            sociaux.setBackground(Color.red);
            sociaux.setForeground(Color.white);
            sociaux.requestFocus();
            return;
        }
        
        if(net.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Desole ce champs ne doit etre vide");
            net.setBackground(Color.red);
            net.setForeground(Color.white);
            net.requestFocus();
            return;
        }
        
        String[] text = {net.getText()};
        if(kerry.isElementExistInGrid(tblTel,text)==true){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Cet element est deja dans la grille");
            return;
        }
        else{
            Object [] f ={sociaux.getSelectedItem().toString(),net.getText()};
            try {
             tablemodelTres.addRow(f);   
            } catch (NullPointerException e) {
            }
            
            net.setText(null);
            net.requestFocus();
        }
    }//GEN-LAST:event_add2ActionPerformed

    private void retActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retActionPerformed
        int j = tblAdress.getSelectedRow();
        int rep = JOptionPane.showConfirmDialog(this, "Etre vous sure de vouloir retirer cet element de la liste", "Avetisman", JOptionPane.YES_NO_OPTION);
        if(rep==JOptionPane.YES_OPTION){
            tablemodel.removeRow(j);
        }
    }//GEN-LAST:event_retActionPerformed

    private void ret2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ret2ActionPerformed
        int j = tblTel.getSelectedRow();
        int rep = JOptionPane.showConfirmDialog(this, "Etre vous sure de vouloir retirer cet element de la liste", "Avetisman", JOptionPane.YES_NO_OPTION);
        if(rep==JOptionPane.YES_OPTION){
            tablemodelDos.removeRow(j);
        }
    }//GEN-LAST:event_ret2ActionPerformed

    private void ret3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ret3ActionPerformed
        int j = tblReseaux.getSelectedRow();
        int rep = JOptionPane.showConfirmDialog(this, "Etre vous sure de vouloir retirer cet element de la liste", "Avetisman", JOptionPane.YES_NO_OPTION);
        if(rep==JOptionPane.YES_OPTION){
            tablemodelTres.removeRow(j);
        }
    }//GEN-LAST:event_ret3ActionPerformed

    private void adresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_adresKeyReleased
        if(adres.getText().isEmpty()){
            add.setEnabled(false);
        }
        else{
            add.setEnabled(true);
        }
        
        adres.setBackground(Color.white);
        adres.setForeground(Color.black);
        
        
        String txt = adres.getText();
        String txt_ = "";
        
        try {
            if(evt.getKeyChar() == KeyEvent.VK_SPACE){
        txt_ = txt.substring(txt.indexOf(" ") +1);
        txt = txt_;
                System.out.println("-->> "+txt);
        }
        } catch (NullPointerException e) {
        }
        
//        if(!adres.getText().isEmpty()){
//        kerry.autoCompletePart(txt, "client_adresse", "adresse", adres);
//        }
    }//GEN-LAST:event_adresKeyReleased

    private void netKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netKeyReleased
        if(net.getText().isEmpty() && sociaux.getSelectedIndex() <=0){
            add2.setEnabled(false);
        }
        else{
            add2.setEnabled(true);
        }
        
        net.setBackground(Color.white);
        net.setForeground(Color.black);
    }//GEN-LAST:event_netKeyReleased

    private void telKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telKeyReleased
        if(tel.getText().trim().equals("+(509)")){
            add1.setEnabled(false);
        }
        else{
            add1.setEnabled(true);
        }
        
        tel.setBackground(Color.white);
        tel.setForeground(Color.black);
    }//GEN-LAST:event_telKeyReleased

    private void adresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adresActionPerformed
        add.doClick();
    }//GEN-LAST:event_adresActionPerformed

    private void telActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_telActionPerformed
        add1.doClick();
    }//GEN-LAST:event_telActionPerformed

    private void netActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_netActionPerformed
        add2.doClick();
    }//GEN-LAST:event_netActionPerformed

    private void adresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adresMouseClicked
        adres.setBackground(Color.white);
        adres.setForeground(Color.black);
    }//GEN-LAST:event_adresMouseClicked

    private void telMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_telMouseClicked
        tel.setBackground(Color.white);
        tel.setForeground(Color.black);
    }//GEN-LAST:event_telMouseClicked

    private void netMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_netMouseClicked
        net.setBackground(Color.white);
        net.setForeground(Color.black);
    }//GEN-LAST:event_netMouseClicked

    private void nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameKeyReleased
        name.setBackground(Color.white);
        name.setForeground(Color.black);
    }//GEN-LAST:event_nameKeyReleased

    private void nameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameMouseClicked
        name.setBackground(Color.white);
        name.setForeground(Color.black);
    }//GEN-LAST:event_nameMouseClicked

    private void sitwebKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sitwebKeyReleased
        sitweb.setBackground(Color.white);
        sitweb.setForeground(Color.black);
    }//GEN-LAST:event_sitwebKeyReleased

    private void sitwebMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sitwebMouseClicked
        sitweb.setBackground(Color.white);
        sitweb.setForeground(Color.black);
    }//GEN-LAST:event_sitwebMouseClicked

    private void sociauxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sociauxItemStateChanged
        sociaux.setBackground(Color.white);
        sociaux.setForeground(Color.black);
    }//GEN-LAST:event_sociauxItemStateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        save();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void add3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add3ActionPerformed
        if(ml.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Desole ce champs ne doit etre vide");
            ml.setBackground(Color.red);
            ml.setForeground(Color.white);
            ml.requestFocus();
            return;
        }
        
        if(!kerry.isEmailValidate(ml.getText().trim())){
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(client_type, "Format Incorrect");
        ml.setBackground(Color.red);
        ml.requestFocus();
        return;
    }
        
        String[] text = {ml.getText()};
        if(kerry.isElementExistInGrid(tblEmail,text)==true){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Cet element est deja dans la grille");
            return;
        }
        else{
            Object [] f ={ml.getText()};
            try {
             tablemodelCinco.addRow(f);   
            } catch (NullPointerException e) {
            }
            
            ml.setText(null);
            ml.requestFocus();
        }
    }//GEN-LAST:event_add3ActionPerformed

    private void ret4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ret4ActionPerformed
        int j = tblEmail.getSelectedRow();
        int rep = JOptionPane.showConfirmDialog(this, "Etre vous sure de vouloir retirer cet element de la liste", "Avetisman", JOptionPane.YES_NO_OPTION);
        if(rep==JOptionPane.YES_OPTION){
            tablemodelCinco.removeRow(j);
        }
    }//GEN-LAST:event_ret4ActionPerformed

    private void client_typeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_client_typeItemStateChanged
        client_type.setBackground(Color.WHITE);
    }//GEN-LAST:event_client_typeItemStateChanged

    private void mlKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mlKeyReleased
        if(ml.getText().isEmpty()){
            add3.setEnabled(false);
        }
        else{
            add3.setEnabled(true);
        }
        
        ml.setBackground(Color.white);
        ml.setForeground(Color.black);
    }//GEN-LAST:event_mlKeyReleased

    private void mlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mlActionPerformed
        add3.doClick();
    }//GEN-LAST:event_mlActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        nouvo();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void emailFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_emailFilterItemStateChanged
        getEmailStatus();
    }//GEN-LAST:event_emailFilterItemStateChanged

    private void telFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_telFilterItemStateChanged
        getTelStatus();
    }//GEN-LAST:event_telFilterItemStateChanged

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        fullDatosDos();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void selectsFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selectsFilterItemStateChanged
        getSelectStatus();
    }//GEN-LAST:event_selectsFilterItemStateChanged

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
            java.util.logging.Logger.getLogger(client_Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(client_Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(client_Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(client_Register.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new client_Register().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea activites;
    private javax.swing.JButton add;
    private javax.swing.JButton add1;
    private javax.swing.JButton add2;
    private javax.swing.JButton add3;
    private javax.swing.JTextField adres;
    private javax.swing.JComboBox client_type;
    private javax.swing.JComboBox comboFilter;
    private javax.swing.JCheckBox emailFilter;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField ml;
    private javax.swing.JTextField name;
    private javax.swing.JTextField net;
    private javax.swing.JButton ret;
    private javax.swing.JButton ret2;
    private javax.swing.JButton ret3;
    private javax.swing.JButton ret4;
    private javax.swing.JButton searchButton;
    private javax.swing.JCheckBox selectsFilter;
    private javax.swing.JFormattedTextField sitweb;
    private javax.swing.JComboBox sociaux;
    private javax.swing.JTable tblAdress;
    private javax.swing.JTable tblClient;
    private javax.swing.JTable tblEmail;
    private javax.swing.JTable tblReseaux;
    private javax.swing.JTable tblSearch;
    private javax.swing.JTable tblTel;
    private javax.swing.JFormattedTextField tel;
    private javax.swing.JCheckBox telFilter;
    // End of variables declaration//GEN-END:variables
}
