package dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CodeParser {
	private HashMap<String,String> blockMap = new HashMap<String,String>();
	private HashMap<String,String> classMap = new HashMap<String,String>();
	public String formatCode(String packageName, String className, String input) {
		blockMap.clear();
		classMap.clear();

		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		sb2.append("package " + packageName + ";\n");
		sb2.append("import static java.lang.Math.*;\n");
		sb2.append("import java.util.*;\n");
		sb2.append("import java.net.*;\n");
		sb2.append("import java.io.*;\n");
		sb2.append("import static java.util.Collections.*;\n");
		sb.append("public class " + className + " extends dialog.Answer{\n");

		// process input line by line
		String[] commands = parseCommands(input.replace('\n', ' '));
		StringBuffer constructor = new StringBuffer();
		constructor.append("\n\tpublic " + className + "() {\n");
		for(int i = 0; i < commands.length; i++) {
			String line = commands[i];
			int idx = line.indexOf('=');
			if(idx > 0 && line.substring(0,idx).indexOf("(") >= 0) idx = -1;

			line = line.replaceAll("\\$(\\d+)\\.","((tmp.In$1)resolve($1))." );
			
			if(line.startsWith("import ")){
				sb2.append(line+";\n");
				line = "";
			}
			else if(idx > 0  && idx != line.indexOf("==")) { // equal sign valid
				String[] toks = line.substring(0,idx).trim().split("\\s+");
				if(toks.length > 1) {
					sb.append("\t"+line.substring(0,idx).trim() + ";\n");
					line = toks[toks.length-1] + " " + line.substring(idx);
				}				
			}
			else if(line.matches(".*class\\s+[\\w\\d]+\\s*___.*")) { // class
				String cName = line.substring(line.indexOf("class")+6,line.indexOf("___")).trim();
				classMap.put(cName, sb2.toString() + "\n;" + line);
				line = "";
			}
			else if(line.matches(".*\\([\\w\\d,\\s]*\\)\\s*___.*")) { // function
				sb.append("\t"+line+";\n");
				line = "";
			}
			else if(line.matches(".*\\s.*") && line.matches("^\\w[\\w\\d\\s]*$")){ // variable declaration
				sb.append("\t"+line+";\n");
				line = "";				
			}

			line = line.trim();
			if(line.length() == 0) continue;
			if(i == commands.length-1 && (!line.startsWith("for")) && (!line.startsWith("while")))
				constructor.append("\t\tresult = " + line + ";\n");
			else constructor.append("\t\t"+line + ";\n" );
		}
		sb.append(constructor.toString()+"\t}\n");

		sb.append("}\n");

		String program = sb2.toString() + "\n" + sb.toString();
		for(String key: blockMap.keySet()) {
			String value = blockMap.get(key);
			program = program.replaceAll(key,value);

			for(String cName : classMap.keySet()) {
				String cStr = classMap.get(cName);
				classMap.put(cName, cStr.replaceAll(key, value));
			}
		}
		return program;
	}

	public HashMap<String,String> getClasses(){
		return classMap;
	}

	private String[] parseCommands(String code) {
		ArrayList<String> commands = new ArrayList<String>();
		code = code.trim();

		StringBuffer sb = new StringBuffer();
		int idx = 0;
		int firstPosition = 0;
		int lastPosition = 0;
		int level = 0;
		for(int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			if(c == '{') {
				if(level == 0) firstPosition = i;
				level++;
			}
			else if(c == '}' ) {
				level--;
				if(level == 0) {
					sb.append(code.substring(lastPosition, firstPosition));
					sb.append("___" + idx + "___;");
					blockMap.put("___"+idx+"___", i < code.length()? code.substring(firstPosition,i+1): code.substring(firstPosition));
					lastPosition = i+1;
				}
			}
		}
		if(lastPosition < code.length())
			sb.append(code.substring(lastPosition));

		char openChar = ' ';
		lastPosition = 0;
		for(int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if(c == '(' || c =='[' || c=='{') {
				openChar = c;
			}
			else if(c == ')' || c ==']' || c=='}') {
				openChar = ' ';
			}
			else if(c == ';' && openChar == ' ') {
				if(lastPosition < sb.length()) {
					String line = sb.substring(lastPosition,i).trim();
					if(line.length() > 0) commands.add(line);
					lastPosition = i + 1;
				}
			}
		}
		if(lastPosition < sb.length()) {
			String line = sb.substring(lastPosition).trim();
			if(line.length() > 0) commands.add(line);	
		}

		String[] lines = new String[commands.size()];
		commands.toArray(lines);
		return lines;
	}

}
