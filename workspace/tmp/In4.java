package tmp;
import static java.lang.Math.*;
import java.util.*;
import java.net.*;
import java.io.*;
import static java.util.Collections.*;

public class In4 extends dialog.Answer{

	public In4() {
		result = ((tmp.In3)resolve(3)).square(((tmp.In1)resolve(1)).x);
	}
}

