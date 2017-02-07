package br.deni.jasperxmltojb;

import java.io.BufferedReader;
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

        /* Certify the file exists */
        if (!jasperFile.toFile().exists()) {
            throw new IOException();
        }
    }

    /* Read the fields of the JasperFile and return a
    *  an ArrayList with Field's objects
     */
    @SuppressWarnings("empty-statement")
    public ArrayList<Field> readFields() throws Exception {

        ArrayList<Field> fields = new ArrayList<>();

        FileReader fr = new FileReader(jasperFile.toFile());
        BufferedReader reader = new BufferedReader(fr);

        String line = reader.readLine();

        while (line != null) {

            if (line.contains("<field")) {
                fields.add(catchField(line));
            }
            line = reader.readLine();
        }

        return fields;
    }

    /* Catch the name and the type of a field line in JasperFile
    *  and create a Field object.
     */
    private Field catchField(String fieldLine) {

        List<String> fieldSplit = Arrays.asList(fieldLine.split(" "));

        /* TODO Try to improve this for */
        String name = null, type = null;
        for (String attribute : fieldSplit) {

            if (attribute.contains("name")) {
                name = catchValue(attribute);
            }

            if (attribute.contains("class")) {
                type = catchValue(attribute);
            }
        }

        return new Field(name, type);
    }

    /* Catch an entire attribute and clean it to only get the value
     */
    private String catchValue(String attribute) {
        attribute = attribute.replace("\"", "").replace("/>", "");
        return attribute.substring(attribute.indexOf('=') + 1);
    }

}
