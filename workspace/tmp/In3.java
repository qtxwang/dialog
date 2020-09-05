package tmp;
import static java.lang.Math.*;
import java.util.*;
import java.net.*;
import java.io.*;
import static java.util.Collections.*;

public class In3 extends dialog.Answer{

	public In3() {
		result = ((tmp.In2)resolve(2)).fabonacci(10);
	}
}

