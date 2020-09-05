package dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class JCompiler {	
	private JavaCompiler compiler;
	private ClassLoader classLoader;
	
	public JCompiler() {
		compiler = ToolProvider.getSystemJavaCompiler();
		classLoader = ClassLoader.getSystemClassLoader();
	}
	
	public Class<?> compile(String packageName, String className, String code) throws Exception{
		// Remove .class file
		File classFile = new File(Jnote.home + "/" + packageName + "/" + className + ".class");
		if(classFile.exists()) classFile.delete();
		
		// Write code to .java file
		String srcPath = Jnote.home + "/" + packageName + "/" + className + ".java";
		FileOutputStream fos = new FileOutputStream(srcPath);
		PrintStream ps = new PrintStream(fos);
		ps.println(code);
		ps.close();
		
		// Write error to log file
		String logPath = Jnote.home + "/" + packageName + "/" + className + ".log";
		FileOutputStream log = new FileOutputStream(logPath);
			
		String opts[] = {srcPath};
		compiler.run(null, null, log, opts);
		log.close();
		String errMsg = Files.readString(Paths.get(logPath));
		
		if(errMsg.length() > 2) {
			String[] lines = errMsg.split("\\n");
			StringBuilder sb = new StringBuilder();
			for(int i = 3; i < lines.length; i++) sb.append(lines[i] + "\n");
			throw new Exception(sb.toString());
		}
		log.close();
	    return classLoader.loadClass(packageName+"."+className);	
	}
	
}
