/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.deni.jasperxmltojb;

import br.deni.jasperxmltojb.utils.JavaBean;
import br.deni.jasperxmltojb.utils.JavaUtils;
import br.deni.jasperxmltojb.utils.JasperXMLReader;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * Facade class to run the application
 *
 * @author denidiasjr
 */
public class JasperXMLtoJB {

    private JasperXMLReader jasperReader;
    private String className;
    private String packageName;
    private JavaBean javaBean;

    /* Setters */
    public void setJasperXMLFile(File file) throws Exception {
        jasperReader = new JasperXMLReader(file);
    }

    public void setJasperXMLFile(String file) throws Exception {
        jasperReader = new JasperXMLReader(file);
    }
    
    public void setClassName(String className){
        this.className = JavaUtils.toUpperCamelCase(className.trim().replaceAll("\\s+",""));
    }
       
    public void setPackage(String packageName){
        this.packageName = packageName.trim().replaceAll("\\s+","");
    }
    
    /* Validate package name */
    private void checkPackageName() throws Exception{
        
        if (this.packageName == null){
            throw new Exception("Package not set");
        }
        
        if (this.packageName.isEmpty()) {
            throw new Exception ("Package name is empty!");
        }

    }
    
    /* Validate class name */
    private void checkClassName() throws Exception{
        
        if (this.className == null){
            throw new Exception("Class not set");
        }
        
        if (this.className.isEmpty()) {
            throw new Exception ("Class name is empty!");
        }

        if (this.className.substring(0, 1).matches("\\d+")) {
            throw new Exception ("Class name starts with a digit!");
        }
    }
    
    /* Render JavaBean */
    public void render() throws Exception{
        
        this.checkClassName();
        this.checkPackageName();
                
        if (this.jasperReader == null){
            throw new Exception("JasperXML File not selected");
        }
        
        javaBean = new JavaBean(this.className, this.packageName);
        javaBean.setFields(jasperReader.readJasperFields());
        javaBean.render();
    }
    
    /* Save JavaBean */
    public void save(Path path) throws Exception{
        
        if (this.javaBean == null){
            this.render();
        }
        
        this.javaBean.save(path);
    }
    
    public void save(String path) throws Exception{
        
        if (this.javaBean == null){
            this.render();
        }
        
        this.javaBean.save(Paths.get(path));
    }
}
