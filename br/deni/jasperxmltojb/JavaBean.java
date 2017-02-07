/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.deni.jasperxmltojb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Generate the JavaBean file
 * 
 * @author denidiasjr
 */
public class JavaBean {

    /* Class name of the JavaBean */
    private String className;

    /* Set the class declaration into the file */
    private String classDeclaration;

    /* Package of the JavaBean */
    private String packageName;

    /* Set the fields of the JavaBean */
    private ArrayList<Field> fields;

    /* JavaBean path */
    private Path path;

    /* Serializable string */
    private String serializableString;

    /* Option to import the class on JavaBean */
    private boolean optionImport;

    public JavaBean(String className) {
        this.className = className;
        this.serializableString = "java.io.Serializable";
        this.setClassDeclaration();
        this.optionImport = false;
    }

    public JavaBean(String className, String packageName) {
        this.className = className.trim();
        this.serializableString = "java.io.Serializable";
        this.setClassDeclaration();
        this.packageName = packageName.trim();
        this.optionImport = false;
    }

    /*
    * Finally, export the JavaBean file
     */
    public void exportJavaBean() throws IOException {

        /* If path was not set, export in the current directory */
        if (this.path == null) {
            this.path = Paths.get(this.className + ".java");
        } else {
            String absolutePath = new StringBuilder()
                    .append(this.path.toString())
                    .append("/")
                    .append(this.className)
                    .append(".java")
                    .toString();
            this.path = Paths.get(absolutePath);
        }

        /* Check if fields are set */
        if (this.fields == null) {
            throw new NullPointerException("Fields not setted!");
        }

        /* Open the file to write */
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(this.path.toString());
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        /* if the package is not null put it on the file */
        if (this.packageName != null) {
            bufferedWriter.write("package ");
            bufferedWriter.write(this.packageName);
            bufferedWriter.write(";\n\n");
        }

        /* If the import is set */
        if (this.optionImport) {

            /* Import java.io.serializable */
            bufferedWriter.write("import java.io.Serializable;");
            bufferedWriter.write("\n");
            
            /* Catch the imports */
            HashSet<String> hashImports = new HashSet<>();
            
            /* Get the fields and write them on the java bean */
            for (Field field : this.fields) {

                /* If hashImports contains the declaration, jump it! */
                if (!hashImports.contains(field.getImportDeclaration())) {
                    hashImports.add(field.getImportDeclaration());
                    bufferedWriter.write(field.getImportDeclaration());
                    bufferedWriter.write("\n");
                }
            }                       
        }

        bufferedWriter.write("\n");

        /* Write the commentary about the library */
        bufferedWriter.write("/* Generated with JasperXMLtoJB \n");
        bufferedWriter.write(" * Github: https://github.com/denidias/JasperXMLtoJB\n");
        bufferedWriter.write(" * @author denidiasjr\n");
        bufferedWriter.write(" * \n");
        bufferedWriter.write(" */\n\n");
        
        /* Write the class */
        bufferedWriter.write(classDeclaration);

        /* Write the variables */
        for (Field field : this.fields) {
            bufferedWriter.write("\t");
            bufferedWriter.write(field.getVariable(this.optionImport));
            bufferedWriter.write("\n");
        }
        bufferedWriter.write("\n\n");

        /* Write the getters and setters */
        for (Field field : this.fields) {
            bufferedWriter.write("\t");
            bufferedWriter.write(field.getGetter(this.optionImport));
            bufferedWriter.write("\n\n");
            bufferedWriter.write("\t");
            bufferedWriter.write(field.getSetter(this.optionImport));
            bufferedWriter.write("\n\n");
        }
        bufferedWriter.write("}");

        /* Close connections */
        bufferedWriter.close();
        fileWriter.close();

        System.out.println("JavaBean generated successfully! :)");
    }

    public String getPackage() {
        return this.packageName;
    }

    public void setPackage(String packageName) {
        this.packageName = packageName.trim();
    }

    public ArrayList<Field> getFields() {
        return this.fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        if (!path.toFile().isDirectory()) {
            throw new IllegalArgumentException("Path passed in argument not exists!");
        }
        this.path = path;
        System.out.println(this.path);
    }

    private void setClassDeclaration() {
        this.classDeclaration = new StringBuilder()
                .append("public class ")
                .append(this.className)
                .append(" implements ")
                .append(this.serializableString)
                .append(" {")
                .append("\n\n")
                .toString();
    }

    /* Set if the classes of jrxml will be imported  */
    public void setImport(boolean optionImport) {
        this.optionImport = optionImport;

        if (optionImport) {
            this.serializableString = "Serializable";
            this.setClassDeclaration();
        }
    }

}
