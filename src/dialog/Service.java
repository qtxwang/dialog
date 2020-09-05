package dialog;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.HashMap;

public class Service extends Thread{
	private static HashMap<String,Jnote> notes = new HashMap<String,Jnote>();
	private final Scanner scanner;
	private final PrintStream out;
	private Jnote note;
	private int counter = 0;
	
	public Service(){
		scanner = new Scanner(System.in);
		out = new PrintStream(System.out);
		out.println("--- starting dialog verions 0.001 ---");
	}
	
	public void run(){
		String packName = "tmp";
		note = new Jnote(packName);
		notes.put(packName, note);
	
		while(true) {
			out.print("\n In[" + note.getIndex() + "]:\t");
			counter = note.getIndex();
			String code = readCode();
			if(code.trim().length() < 2){
				response("");
				continue;
			}
			if(code.equals("EXIT")) {
				scanner.close();
				out.close();
				System.exit(0);
				break;
			}
			String answer = "";
			try{
				answer = note.eval(code);
			} catch(Exception e) {
				out.println( e.getMessage());
				continue;
			}
			response(answer);
		}
	}
	
	public String readCode() {
		String input = "";
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(line.length() == 0) break;
			else if(line.startsWith("quit"))
				return "EXIT";
			input = input + line + "\n";
			out.print("       \t");
		}
		return input;
	}
	
	public void response(String answer) {
		out.print("Out[" + counter + "]:\t");
		out.println(answer );
	}
}

