package br.deni.jasperxmltojb.utils;

import br.deni.jasperxmltojb.utils.JasperField;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Read the JasperXML file
 *
 * @author denidiasjr
 */
public class JasperXMLReader {

    /* Retrive the .jrxml file */
    private Path jasperFile;

    public JasperXMLReader(String file) throws Exception {
        this.jasperFile = Paths.get(file);

        // Certify the file exists
        if (!jasperFile.toFile().exists()) {
            throw new IOException();
        }
    }
    
    public JasperXMLReader(File file) throws Exception {
        this.jasperFile = file.toPath();

        // Certify the file exists
        if (!jasperFile.toFile().exists()) {
            throw new IOException();
        }
        
    }
    
    /* Read the name of the JasperFile and set it to CamelCase */
    public String readJasperName(){
        return JavaUtils.toUpperCamelCase(this.jasperFile.getFileName().toString().split("\\.")[0]);
    }

    /* Read the fields of the JasperFile and return a
    *  an ArrayList with JasperField's objects
    */
    @SuppressWarnings("empty-statement")
    public ArrayList<JasperField> readJasperFields() throws Exception {

        ArrayList<JasperField> fields = new ArrayList<>();

        FileReader fr = new FileReader(jasperFile.toFile());
        BufferedReader reader = new BufferedReader(fr);

        String line = reader.readLine();

        while (line != null) {

            if (line.contains("<field ")) {
                fields.add(catchJasperField(line));
            }
            line = reader.readLine();
        }

        return fields;
    }

    /* Catch the name and the type of a field line in JasperFile
    *  and create a JasperField object.
    */
    private JasperField catchJasperField(String fieldLine) {

        List<String> fieldSplit = Arrays.asList(fieldLine.split(" "));

        // Catch name and type reading the attributes
        String name = null, type = null;
        for (String attribute : fieldSplit) {

            if (attribute.contains("name")) {
                name = catchValue(attribute);
            }

            if (attribute.contains("class")) {
                type = catchValue(attribute);
            }
        }

        return new JasperField(name, type);
    }

    /* Catch an entire attribute and clean it to only get the value */
    private String catchValue(String attribute) {
        attribute = attribute.replace("\"", "").replace("/>", "");
        return attribute.substring(attribute.indexOf('=') + 1);
    }

}
