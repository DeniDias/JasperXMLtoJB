package JasperXMLtoJB;

import java.util.ArrayList;

public class Attribute {
	
	private String type;
	private String name;
	private String method;
	private ArrayList<String> getter;
	private ArrayList<String> setter;
	
	public Attribute(){
		getter = new ArrayList<>();
		setter = new ArrayList<>();
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getMethod(){
		return this.method;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setType(String type){
		this.type = type;
		if (name != null){
			method = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "()";
			method = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "("+type+" "+name+")";
		}
	}
	
	public void setName(String name){
		this.name = name;
		if (type != null){
			method = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "()";
			method = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "("+type+" "+name+")";
		}
	}
	
	public ArrayList<String> getGetterLines() throws Exception{
		if (name == null){
			throw new Exception("ERRO: Nome do atributo não definido.");
		}
		getter.add("	public "+type+" "+method+" {");
		getter.add("		return "+name+";");
		getter.add("	}");
		return getter;
	}
	
	public ArrayList<String> getSetterLines() throws Exception{
		if (name == null){
			throw new Exception("ERRO: Nome do atributo não definido.");
		}
		setter.add("	public void "+method+" {");
		setter.add("		this."+name+" = "+" name");
		setter.add("	}");
		return setter;
	}
}
