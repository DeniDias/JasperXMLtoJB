/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.deni.jasperxmltojb;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Desktop;
import java.net.URI;

/**
 *
 * @author deni
 */
public class Main extends javax.swing.JFrame {

    // Variables declaration
    private javax.swing.JButton btnGenerate;
    private javax.swing.JButton btnJasperXML;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblGitHub;
    private javax.swing.JTextField txtClassName;
    private javax.swing.JTextField txtJasperXML;
    private javax.swing.JTextField txtPackage;
    private File jasperFile;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    /**
     * Creates new form Main
     */
    public Main() {

        // Set look and feel of swing
        setLookAndFeel();

        // Init components
        initComponents();

        // Set events of JFrame
        mainEvents();
    }

    // Set events of window
    private void mainEvents() {

        // Get JasperXML file
        btnJasperXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtJasperXML.setText(getJasperXMLFile());
            }
        });

        // Generate JavaBean
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Validate class and package
                if (!checkJasperFile() || !checkClassName() || !checkPackage()) {
                    return;
                }

                try {
                    generateFile();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        });

        // Open Github page =)
        lblGitHub.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    URI link = new URI("https://github.com/denidiasjr/JasperXMLtoJB");
                    Desktop.getDesktop().browse(link);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
    }

    /* Get JasperXML file path */
    private String getJasperXMLFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JasperXML files", "jrxml");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            jasperFile = fileChooser.getSelectedFile();
            return jasperFile.getAbsolutePath();
        } else {
            JOptionPane.showMessageDialog(null, "Error while selecting .jrxml file");
            return null;
        }
    }

    /* Validate class name */
    private boolean checkClassName() {
        if (txtClassName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Class name is empty!");
            return false;
        }

        if (txtClassName.getText().substring(0, 1).matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Class name starts with a digit!");
            return false;
        }

        return true;
    }

    /* Validate package name */
    private boolean checkPackage() {
        if (txtPackage.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Package is empty!");
            return false;
        }

        return true;
    }
    
    /* Validate JasperXML file */
    private boolean checkJasperFile() {
        if (txtJasperXML.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "JasperXML is empty!");
            return false;
        }

        return true;
    }

    /* Generate JavaBean file */
    private void generateFile() throws Exception {

        JasperXMLReader jasperReader = new JasperXMLReader(jasperFile);
        JavaBean javaBean = new JavaBean(txtClassName.getText());
        javaBean.setPackage(txtPackage.getText());
        javaBean.setFields(jasperReader.readJasperFields());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            
            // Save file
            javaBean.save(fileChooser.getSelectedFile().toPath());
            JOptionPane.showMessageDialog(null, "JavaBean generated successfully!");
            
            // Empty fields
            txtJasperXML.setText("");
            txtClassName.setText("");
            txtPackage.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Error while saving JavaBean!");
            return;
        }
    }

    private void setLookAndFeel() {

        // Get current OS to show best look and feel
        String os = System.getProperties().get("os.name").toString();

        // Set window to center on screen
        Dimension objDimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (objDimension.width - this.getWidth()) / 2;
        int height = (objDimension.height - this.getHeight()) / 2;
        this.setLocation(width, height);

        // Set look and feel according to OS
        try {
            if (os.contains("Linux")) {
                javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } else if (os.contains("Windows")) {
                javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else {
                javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            }

            SwingUtilities.updateComponentTreeUI(this);
            this.pack();
        } catch (Exception e) {
            System.out.println("Error when loading Look and Feel.");
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtJasperXML = new javax.swing.JTextField();
        btnJasperXML = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtClassName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPackage = new javax.swing.JTextField();
        btnGenerate = new javax.swing.JButton();
        lblGitHub = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JasperXML to JavaBean - A tool to convert .jrxml file to JavaBean");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("JasperXML to JavaBean");
        jLabel1.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Cantarell", 0, 17)); // NOI18N
        jLabel2.setText("JasperXML file:");

        btnJasperXML.setText("Search...");

        jLabel3.setFont(new java.awt.Font("Cantarell", 0, 17)); // NOI18N
        jLabel3.setText("Class Name:");

        jLabel4.setFont(new java.awt.Font("Cantarell", 0, 17)); // NOI18N
        jLabel4.setText("Package:");

        btnGenerate.setText("Generate");

        lblGitHub.setFont(new java.awt.Font("Cantarell", 0, 12)); // NOI18N
        lblGitHub.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGitHub.setText("Fork this project on GitHub! - https://github.com/denidiasjr/JasperXMLtoJB");
        lblGitHub.setCursor(new Cursor(Cursor.HAND_CURSOR));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtJasperXML, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnJasperXML, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(68, 68, 68)
                                        .addComponent(btnGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtClassName)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(txtPackage, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29))
                .addComponent(lblGitHub, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtJasperXML, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnJasperXML))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtClassName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPackage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addComponent(btnGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGitHub, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                        .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("JasperXML to JavaBean - A tool to convert .jrxml file to JavaBean");

        pack();
    }
}
