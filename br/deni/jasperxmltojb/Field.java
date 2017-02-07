/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.deni.jasperxmltojb;

/**
 * Abstract fields from JasperXML
 * 
 * @author denidiasjr
 */
public class Field {

    private String type;
    private String typeSimple;
    private String name;

    private String variable;
    private String getter;
    private String setter;

    public Field(String name, String type) {
        this.type = type;
        this.name = name.trim();
        this.setTypeSimple();
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getVariable(boolean optionImport) {
        String type = this.type;

        /* If import option is set, alternate to simple class name on variable */
        if (optionImport) {
            type = this.typeSimple;
        }
        
        return new StringBuilder()
                .append("private ")
                .append(type)
                .append(" ")
                .append(this.name)
                .append(";")
                .toString();
    }

    public String getSetter(boolean optionImport) {
        String type = this.type;

        /* If import option is set, alternate to simple class name on setter */
        if (optionImport) {
            type = this.typeSimple;
        }

        return new StringBuilder()
                .append("public void set")
                .append(this.name)
                .append("(")
                .append(type)
                .append("){")
                .append("\n\t\tthis.")
                .append(this.name)
                .append(" = ")
                .append(this.name)
                .append(";")
                .append("\n\t}")
                .toString();
    }

    public String getGetter(boolean optionImport) {
        String type = this.type;

        /* If import option is set, alternate to simple class name on setter */
        if (optionImport) {
            type = this.typeSimple;
        }

        return new StringBuilder()
                .append("public ")
                .append(type)
                .append(" get")
                .append(this.name)
                .append("(){")
                .append("\n\t\treturn ")
                .append(this.name)
                .append(";")
                .append("\n\t}")
                .toString();
    }

    public String getImportDeclaration() {
        return new StringBuilder()
                .append("import ")
                .append(this.type)
                .append(";")
                .toString();
    }

    public void setTypeSimple() {
        String[] typeSplit = this.type.split("\\.");
        this.typeSimple = typeSplit[typeSplit.length - 1];
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Name: ")
                .append(this.name)
                .append("\nType: ")
                .append(type)
                .toString();
    }
}
