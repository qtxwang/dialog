package dialog;

public class Answer {
	public Object result;
	public Object answer() {
		return result;
	}
	
	public Answer resolve(int idx) {
		return Jnote.resolve(idx);
	}
}
