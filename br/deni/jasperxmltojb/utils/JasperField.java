package br.deni.jasperxmltojb.utils;

/**
 * Retrieve fields from JasperXML
 *
 * @author denidiasjr
 */
public class JasperField {

    private final String fieldShortClass;
    private String fieldOriginalName;
    private String fieldName;
    private String fieldClass;

    public JasperField(String fieldName, String fieldClass) {
        this.fieldOriginalName = fieldName;
        this.fieldClass = fieldClass;
        this.fieldName = JavaUtils.toLowerCamelCase(fieldName);
        String[] typeSplit = fieldClass.split("\\.");
        this.fieldShortClass = typeSplit[typeSplit.length - 1];
    }

    public String getFieldShortClass() {
        return fieldShortClass;
    }

    public String getFieldOriginalName() {
        return fieldOriginalName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldClass() {
        return fieldClass;
    }
}
