package br.deni.jasperxmltojb.utils;

import br.deni.jasperxmltojb.utils.JavaUtils;
import br.deni.jasperxmltojb.utils.JasperField;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Generate the JavaBean file
 *
 * @author denidiasjr
 */
public class JavaBean {

    /* Class name of JavaBean */
    private String className;

    /* Nome of JavaBean file */
    private String fileName;

    /* Package of JavaBean */
    private String packageName;

    /* Set fields of JavaBean */
    private ArrayList<JasperField> fields;

    /* Buffer of JavaBean */
    private HashMap<String, String> content;

    /* Constructors */
    public JavaBean(String className) {
        this.className = className.trim().replaceAll("\\s+","");
        this.setDefaultOptions();
    }

    public JavaBean(String className, String packageName) {
        this.className = className.trim().replaceAll("\\s+","");
        this.packageName = packageName.trim().replaceAll("\\s+","");
        this.setDefaultOptions();
    }

    public void setPackage(String packageName) {
        this.packageName = packageName.trim().replaceAll("\\s+","");
    }

    public ArrayList<JasperField> getFields() {
        return this.fields;
    }

    public void setFields(ArrayList<JasperField> fields) {
        this.fields = fields;
    }

    private void setDefaultOptions() {
        this.fileName = this.className + ".java";
        this.content = new HashMap();
    }

    /* Get imports from JasperFields */
    private String getImports() {

        HashSet<String> imports = new HashSet();
        StringBuilder sb = new StringBuilder();

        for (JasperField field : this.fields) {

            // Check if class was already imported
            if (!imports.contains(field.getFieldClass())) {
                sb.append("import ");
                sb.append(field.getFieldClass());
                sb.append(";\n");
                imports.add(field.getFieldClass());
            }
        }

        return sb.toString();
    }
    
    /* Get attributes from fields */
    private String getAttributes(){
        StringBuilder sb = new StringBuilder();
        
        for (JasperField field : this.fields){
            sb.append("\tprivate ");
            sb.append(field.getFieldShortClass());
            sb.append(" ");
            sb.append(field.getFieldName());
            sb.append(";\n");
        }
        
        return sb.toString();
    }

    /* Get getter and setter functions from Field */
    private String getGetterAndSetter(JasperField field) {
        StringBuilder sb = new StringBuilder();

        // Getter
        sb.append("\tpublic ");
        sb.append(field.getFieldShortClass());
        sb.append(" get");
        sb.append(JavaUtils.toUpperCamelCase(field.getFieldName()));
        sb.append("(){\n\t\treturn ");
        sb.append(field.getFieldName());
        sb.append(";\n\t}\n\n");
        
        // Setter
        sb.append("\tpublic void set");
        sb.append(JavaUtils.toUpperCamelCase(field.getFieldName()));
        sb.append("(");
        sb.append(field.getFieldShortClass());
        sb.append(" ");
        sb.append(field.getFieldName());
        sb.append("){\n\t\tthis.");
        sb.append(field.getFieldName());
        sb.append(" = ");
        sb.append(field.getFieldName());
        sb.append(";\n\t}\n\n");
        
        return sb.toString();
    }

    /* 
    *   TODO Create function to render the JavaBean 
    *   
    *   Function will buffer JavaBean content before write in the file 
    *
     */
    public void render() {
        
        // Check if package name is set and render it
        if (packageName != null) {
            content.put("package", "package " + packageName + ";\n");
        }

        // Get imports and attributes
        content.put("imports", getImports());
        content.put("attributes", getAttributes());
       
        // Catch getters and setters
        for (JasperField field : this.fields) {
            content.put(field.getFieldName(), getGetterAndSetter(field));
        }
    }

    /* 
    *  Save JavaBean
    */
    public void save(Path path) throws IOException {
        
        // Check if JavaBean was rendered before
        if (content.get("attributes") == null){
            this.render();
        }

        // Check path and set filename on it
        if (path == null) {
            path = Paths.get(this.fileName);
        } else if (Files.isDirectory(path)) {
            path = Paths.get(path + File.separator + this.fileName);
        } else {
            path = Paths.get(path.getParent() + File.separator + this.fileName);
        }
        
        // If file exists, delete it
        if (Files.exists(path)){
            Files.delete(path);
        }

        // Create file
        Files.createFile(path);

        // Vars to write on file
        FileWriter fw = new FileWriter(path.toFile());
        BufferedWriter bw = new BufferedWriter(fw);

        // Write content into file
        bw.write(content.get("package"));
        bw.newLine();
        bw.write(content.get("imports"));
        bw.newLine();
        bw.write("public class "+this.className+" implements java.io.Serializable {\n\n");
        bw.write(content.get("attributes"));
                bw.newLine();

        for (JasperField field : this.fields) {
            bw.write(content.get(field.getFieldName()));
        }
        bw.write("}");
        
        // Close stream
        bw.close();
        fw.close();
        
        System.out.println("JavaBean generated successfully! :)");
    }
}
