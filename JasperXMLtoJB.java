package br.deni.jasperxmltojb;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.event.ActionEvent;

public class JasperXMLtoJB {

	private JFrame frame;
	private JTextField txtFile;
	private JButton btnSearch;
	private JTextField txtClassName;
	private JLabel lblFile; 
	private JLabel lblClassName;
	private JLabel lblPackage;
	private JTextField txtPackage;
	private JButton btnGenerate;
	
	private File jasperFile;
	private FileReader jasperFileReader;
	private BufferedReader br;
	private FileWriter javaFileWriter;
	private BufferedWriter bw;
	
	private JFileChooser fc;
	
	private Path jbFile;
	private Path functionsFile;
	private String saveFolder;
	
	private String className;
	
	private ArrayList<String> fields;
	private ArrayList<Attribute> attributes;
	
	private Dimension dim;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JasperXMLtoJB window = new JasperXMLtoJB();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JasperXMLtoJB() {
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		fc = new JFileChooser();
		initialize();	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 575, 186);
		frame.setTitle("JasperXML to JavaBean");
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblFile = new JLabel("File: *");
		lblFile.setBounds(56, 11, 40, 19);
		frame.getContentPane().add(lblFile);
		
		txtFile = new JTextField();
		txtFile.setBounds(136, 10, 285, 20);
		frame.getContentPane().add(txtFile);
		txtFile.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchAction();
			}
		});
		btnSearch.setBounds(431, 9, 76, 23);
		frame.getContentPane().add(btnSearch);
		
		txtClassName = new JTextField();
		txtClassName.setBounds(136, 40, 285, 20);
		frame.getContentPane().add(txtClassName);
		txtClassName.setColumns(10);
		
		lblClassName = new JLabel("Class name: *");
		lblClassName.setBounds(56, 41, 83, 19);
		frame.getContentPane().add(lblClassName);
		
		lblPackage = new JLabel("Package:");
		lblPackage.setBounds(56, 72, 58, 19);
		frame.getContentPane().add(lblPackage);
		
		txtPackage = new JTextField();
		txtPackage.setColumns(10);
		txtPackage.setBounds(136, 71, 285, 20);
		frame.getContentPane().add(txtPackage);
		
		btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					generateAction();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		btnGenerate.setBounds(224, 113, 97, 23);
		frame.getContentPane().add(btnGenerate);
	}
	
	/**
	 * Set behavior of search button
	 */
	private void searchAction(){
		FileFilter ff = new FileNameExtensionFilter("JasperXML files", "jrxml");
		fc.setDialogTitle("Select your .jrxml file");
		fc.setFileFilter(ff);
		int result = fc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION){
			jasperFile = fc.getSelectedFile();
			txtFile.setText(jasperFile.getAbsolutePath());
		} else {
			JOptionPane.showMessageDialog(null, "ERROR: Can't open file.");
		}
	}
	
	/**
	 * Set behavior of generate button
	 */
	private void generateAction() throws Exception{
		
		/* Verify if Class Name is blank */
		if (txtClassName.getText().length() == 0) {
			JOptionPane.showMessageDialog(null, "ERROR: Blank space at Class Name.");
			return;
		}

		/* Define official class name */
		className = txtClassName.getText().trim().replaceAll(" ","");

		/* Verify if the chosen file is a JasperXML */
		if (!txtFile.getText().substring(txtFile.getText().length() - 6, txtFile.getText().length()).equals(".jrxml")) {
			JOptionPane.showMessageDialog(null, "ERROR: File is not a JasperXML.");
			return;
		}

		/* Verify if file exists */
		try {
			jasperFileReader = new FileReader(jasperFile);
			br = new BufferedReader(jasperFileReader);
			fields = new ArrayList<>();
			attributes = new ArrayList<>();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		/* Read the file and capture lines that contains fields */
		String line = br.readLine();
		while (line != null) {
			if (line.contains("<field")) {
				fields.add(line);
			}
			line = br.readLine();
		}

		/* Read the field lines to get their names and types */
		for (String aux : fields) {
			String[] params = aux.split(" ");
			Attribute at = new Attribute();
			for (int i = 0; i < params.length; i++) {
				if (params[i].contains("name=")) {
					String[] params_name = params[i].split("\"");
					at.setName(params_name[1]);
				} else if (params[i].contains("class=")) {
					String[] params_class = params[i].split("\"");
					int last_type_index = params_class[1].lastIndexOf('.') + 1;
					at.setType(params_class[1].substring(last_type_index));
				}

				if (i == params.length-1){
					attributes.add(at);
				}
			}
		}
		
		/* Select the folder to save Java Bean */
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fc.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION){
			saveFolder = fc.getSelectedFile().getAbsolutePath();
		}
				
		/* Set file name and delete if it exists */
		jbFile = new File(saveFolder+"\\"+className+".java").toPath();
		if (Files.exists(jbFile)) {
			Files.delete(jbFile);
		}
		Files.createFile(jbFile);
		javaFileWriter = new FileWriter(jbFile.toString());
		bw = new BufferedWriter(javaFileWriter);

		/* Start writing in file */
		if (!txtPackage.getText().isEmpty()){
			bw.write("package "+txtPackage.getText()+";");
		} 
		bw.newLine();
		bw.newLine();
		bw.write("public class "+className+" {");
		bw.newLine();
		bw.newLine();

		for (Attribute at : attributes){
			bw.write("	private "+at.getType()+" "+at.getName()+";");
			bw.newLine();
		}

		bw.newLine();

		for (Attribute at : attributes){
			for (String lines : at.getGetterLines()){
				bw.write(lines);
				bw.newLine();
			}

			for (String lines : at.getSetterLines()){
				bw.write(lines);
				bw.newLine();
			}
		}

		bw.write("}");
		bw.flush();

		/* Set function file name and delete if it exists */
		functionsFile = new File("functions.txt").toPath();
		if (Files.exists(functionsFile)) {
			Files.delete(functionsFile);
		}
		Files.createFile(functionsFile);
		javaFileWriter = new FileWriter(saveFolder+"\\"+"functions.txt");
		bw = new BufferedWriter(javaFileWriter);

		for (Attribute at : attributes){
			bw.write(at.getMethodSetForFunction());
			bw.newLine();
		}

		bw.flush();
		
		JOptionPane.showMessageDialog(null, "Java Bean generated successfully!");

	}
}
