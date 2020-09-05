package dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Jnote {
	private String packageName;
	private CodeParser parser;
	private HashMap<String,Class<?>> classMap = new HashMap<String,Class<?>>();
	private JCompiler compiler;
	private static ArrayList<Answer> answers = new ArrayList<Answer>();
	private static HashMap<String, Jnote> allNotes = new HashMap<String,Jnote>();
	protected static String home = System.getenv("DIALOG_HOME") + "/workspace";
	
	public Jnote(String packName) {
		compiler = new JCompiler();
		packageName = packName;
		parser = new CodeParser();
		allNotes.put(packName, this);
	}
	
	public String eval(String input) throws Exception{
		String className = "In" + getIndex();
		try {
			File packDir = new File(home + "/" + packageName + "/");
			if(!packDir.exists()) packDir.mkdir();
			String srcPath = home + "/" + packageName + "/" + className + ".in";
			FileOutputStream fos = new FileOutputStream(srcPath);
			PrintStream ps = new PrintStream(fos);
			ps.println(input);
			ps.close();
			
			input = input.trim();
			if(input.matches("^\\?\\d+$")) {
				int idx = Integer.parseInt(input.substring(1));
				return helpEntry(idx);
			}
			Answer answerObject = createAnswer(className, input);
			answers.add(answerObject);
			Object obj = answerObject.answer();
			return obj==null?"":obj.toString();
		}catch(Exception e) {
			String logPath = home + "/" + packageName + "/" + className + ".log";
			String log = "";
			try{
				log = new String(Files.readAllBytes(Paths.get(logPath)));
				log = log.replaceAll("\\b.*java\\:","");
				log = log.replace("result.*resolve", "");
			} catch(IOException ee) {
				ee.printStackTrace();
			}
			
			throw e;
		}
	}
	
	private Answer createAnswer(String className, String input) throws Exception{
		for(int k = 0; k < getSize(); k++) {
			int i = getSize()-1-k;
			String key = "out"+(i+1);
			String value = "((In"+(i+1)+") dialog.Jnote.getJnote(\"" + packageName + "\").resolve("+i+"))";
			input = input.replace(key, value);
		}
		String code = parser.formatCode(packageName,className,input);
		
		for(Entry<String,String> entry: parser.getClasses().entrySet()) {
			String cName = entry.getKey();
			String cStr = entry.getValue();
			Class<?> cClass = compiler.compile(packageName, cName,cStr);
			classMap.put( packageName + "." + cName, cClass);
		}
		
		Class<?> clazz = compiler.compile(packageName, className,code);
		classMap.put(packageName + "." + className, clazz);
		return (Answer) clazz.getDeclaredConstructor().newInstance();
	}
	
	private String helpEntry(int idx) {
		Answer answer = answers.get(idx-1);
		Class c = answer.getClass();
		
		StringBuilder sb = new StringBuilder(c.getName() + "\n");
		Field[] fields = c.getFields();
		sb.append("fields:\n");
		for(Field field : fields) {
			sb.append("\t" + field.toString() + "\n");
		}
      Method m[] = c.getDeclaredMethods();
      sb.append("functions:\n");
        for (int i = 0; i < m.length; i++)
        sb.append("\t" + m[i].toString() + "\n");
        return sb.toString();
	}
	
	public int getIndex() {
		return getSize()+1;
	}
	
	
	public int getSize() {
		return answers.size();
	}
	
	public static Answer resolve(int k) {
		return k > answers.size()? null:answers.get(k-1);
	}
	
	public static Jnote getJnote(String packName) {
		return allNotes.get(packName);
	}
}