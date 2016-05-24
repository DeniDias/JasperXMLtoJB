package JasperXMLtoJB;

import java.util.ArrayList;

public class Attribute {

	private String type;
	private String name;
	private String method_get;
	private String method_set;
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
		return this.method_get;
	}

	public String getMethodSet(){
		return this.method_set;
	}

	public String getName(){
		return this.name;
	}

	public void setType(String type){
		this.type = type;
		if (name != null){
			method_get = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "()";
			method_set = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "("+type+" "+name+")";
		}
	}

	public void setName(String name){
		this.name = name;
		if (type != null){
			method_get = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "()";
			method_set = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "("+type+" "+name+")";
		}
	}

	public ArrayList<String> getGetterLines() throws Exception{
		if (name == null){
			throw new Exception("ERRO: Nome do atributo n�o definido.");
		}
		getter.add("	public "+type+" "+method_get+" {");
		getter.add("		return "+name+";");
		getter.add("	}");
		return getter;
	}

	public ArrayList<String> getSetterLines() throws Exception{
		if (name == null){
			throw new Exception("ERRO: Nome do atributo n�o definido.");
		}
		setter.add("	public void "+method_set+" {");
		setter.add("		this."+name+" = "+" "+name+";");
		setter.add("	}");
		return setter;
	}
}
