/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.deni.jasperxmltojb.utils;

/**
 * Utils used by JasperXMLtoJB classes
 * 
 * @author deni
 */
public class JavaUtils {
    
    /* 
    *  Convert text to lower CamelCase
    */
    public static String toLowerCamelCase(String text) {
        String[] textSplit;

        if (text.contains("_")) {
            textSplit = text.split("_");
        } else if (text.contains("-")) {
            textSplit = text.split("-");
        } else {
            return text.substring(0, 1).toLowerCase() + text.substring(1);
        }
        
        String newText = textSplit[0].toLowerCase();
        
        for (int i = 1; i < textSplit.length; i++) {
            newText += textSplit[i].substring(0, 1).toUpperCase() + textSplit[i].substring(1);
        }

        return newText;
    }
    
    /* 
    *  Convert text to upper CamelCase
    */    
    public static String toUpperCamelCase(String text) {
        String[] textSplit;

        if (text.contains("_")) {
            textSplit = text.split("_");
        } else if (text.contains("-")) {
            textSplit = text.split("-");
        } else {
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        
        String newText = "";
        
        for (int i = 0; i < textSplit.length; i++) {
            newText += textSplit[i].substring(0, 1).toUpperCase() + textSplit[i].substring(1);
        }

        return newText;
    }
}
