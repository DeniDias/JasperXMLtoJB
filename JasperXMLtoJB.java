package JasperXMLtoJB;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class JasperXMLtoJB {

	/* Captura o arquivo para efeituar a leitura */
	private static FileReader fr;

	/* Leitura do arquivo JasperXML */
	private static BufferedReader br;

	private static FileWriter fw;

	private static BufferedWriter bw;

	private static ArrayList<String> fields;

	private static ArrayList<Attribute> attributes;

	private static Path java_file;

	private static Path function_file;

	public static void main(String[] args) throws Exception {

		/* Verifica se o arquivo foi passado por parametro */
		if (args[0] == null) {
			System.out.println("ERRO: Necessita de passar o arquivo .jrxml por par�metro");
			return;
		}

		/* Verifica se o nome da classe foi passada por parametro */
		if (args[1] == null) {
			System.out.println("ERRO: Necessita de passar o nome da classe por par�metro");
			return;
		}

		/* Verifica se o arquivo passado � JRXML */
		if (!args[0].substring(args[0].length() - 6, args[0].length()).equals(".jrxml")) {
			System.out.println("ERRO: Arquivo n�o � JRXML");
			return;
		}

		/* Verifica se o arquivo existe */
		try {
			fr = new FileReader(args[0]);
			br = new BufferedReader(fr);
			fields = new ArrayList<>();
			attributes = new ArrayList<>();
		} catch (FileNotFoundException e) {
			System.out.println("ERRO: Arquivo n�o encontrado.");
		}

		/* Efetua a leitura dos arquivos */
		String line = br.readLine();
		while (line != null) {
			if (line.contains("<field")) {
				fields.add(line);
				// System.out.println(line);
			}
			line = br.readLine();
		}

		for (String aux : fields) {
			String[] params = aux.split(" ");
			Attribute at = new Attribute();
			for (int i = 0; i < params.length; i++) {
				if (params[i].contains("name=")) {
					String[] params_name = params[i].split("\"");
					//System.out.println(params_name[1]);
					at.setName(params_name[1]);
				} else if (params[i].contains("class=")) {
					String[] params_class = params[i].split("\"");
					//System.out.println(params_class[1]);
					int last_type_index = params_class[1].lastIndexOf('.') + 1;
					at.setType(params_class[1].substring(last_type_index));
				}

				if (i == params.length-1){
					attributes.add(at);
				}
			}
		}

		java_file = new File(args[1]+".java").toPath();

		if (Files.exists(java_file)) {
			Files.delete(java_file);
		}

		Files.createFile(java_file);


		fw = new FileWriter(args[1]+".java");
		bw = new BufferedWriter(fw);

		bw.newLine();
		bw.newLine();
		bw.write("public class "+args[1]+" {");
		bw.newLine();
		bw.newLine();

		for (Attribute at : attributes){
			bw.write("	private "+at.getType()+" "+at.getName()+";");
			bw.newLine();
		}

		bw.newLine();

		for (Attribute at : attributes){
			for (String lines : at.getGetterLines()){
				bw.write(lines);
				bw.newLine();
			}

			for (String lines : at.getSetterLines()){
				bw.write(lines);
				bw.newLine();
			}
		}

		bw.write("}");
		bw.flush();

		function_file = new File("functions.txt").toPath();

		if (Files.exists(function_file)) {
			Files.delete(function_file);
		}

		Files.createFile(function_file);

		fw = new FileWriter("functions.txt");
		bw = new BufferedWriter(fw);

		for (Attribute at : attributes){
			bw.write(at.getMethodSet());
			bw.newLine();
		}

		bw.flush();

		System.out.println("Opera��o realizada com sucesso!");
	}
}
