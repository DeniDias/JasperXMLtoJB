package br.deni.jasperxmltojb;

import java.util.ArrayList;

/**
 *  Main program
 * 
 * @author denidiasjr
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        
        if (args.length < 2){
            throw new IllegalArgumentException("No argument by parameter!");
        }
        
        JasperXMLReader jasperReader = new JasperXMLReader(args[1]);
        ArrayList<Field> fields = jasperReader.readFields();
        
        String className = "JasperClass";
        
        if (args[2] != null){
            className = args[2];
        }
        
        JavaBean javaBean = new JavaBean(className);
        
        if (args[3] != null){
            javaBean.setPackage(args[3]);
        }
        
        javaBean.setImport(true);
        javaBean.exportJavaBean();
    }
}
