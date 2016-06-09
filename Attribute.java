package br.deni.jasperxmltojb;

import java.util.ArrayList;

public class Attribute {

	private String type;
	private String name;
	private String methodGet;
	private String methodSet;
	private String methodSetForFunctionFile;
	private ArrayList<String> getter;
	private ArrayList<String> setter;

	public Attribute(){
		getter = new ArrayList<>();
		setter = new ArrayList<>();
	}

	public String getType(){
		return this.type;
	}

	public String getMethodGet(){
		return this.methodGet;
	}
	
	public String getMethodSet(){
		return this.methodSet;
	}
	
	public String getMethodSetForFunction(){
		return this.methodSetForFunctionFile;
	}

	public String getName(){
		return this.name;
	}

	public void setType(String type){
		this.type = type;
		if (name != null){
			methodGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "()";
			methodSet = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "("+type+" "+name+")";
			methodSetForFunctionFile = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "(x)";
		}
	}

	public void setName(String name){
		this.name = name;
		if (type != null){
			methodGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "()";
			methodSetForFunctionFile = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "(x)";
		}
	}

	public ArrayList<String> getGetterLines() throws Exception{
		if (name == null){
			throw new Exception("ERRO: Nome do atributo n�o definido.");
		}
		getter.add("	public "+type+" "+methodGet+" {");
		getter.add("		return "+name+";");
		getter.add("	}");
		return getter;
	}

	public ArrayList<String> getSetterLines() throws Exception{
		if (name == null){
			throw new Exception("ERRO: Nome do atributo n�o definido.");
		}
		setter.add("	public void "+methodSet+" {");
		setter.add("		this."+name+" = "+name+";");
		setter.add("	}");
		return setter;
	}
}
