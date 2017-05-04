/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.deni.jasperxmltojb;

import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author deni
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        
        JasperXMLReader jasperReader = new JasperXMLReader("requerimento_ferias.jrxml");
        ArrayList<JasperField> fields = jasperReader.readJasperFields();
        
        JavaBean javaBean = new JavaBean(jasperReader.readJasperName());
        javaBean.setPackage("br.deni.jasperxmltojb");
        javaBean.setFields(fields);
        javaBean.render();
        javaBean.save(Paths.get("/home/deni/Desktop/"));
    }
}
