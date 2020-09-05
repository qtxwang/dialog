package tmp;
import static java.lang.Math.*;
import java.util.*;
import java.net.*;
import java.io.*;
import static java.util.Collections.*;

public class In1 extends dialog.Answer{
	double angle;
	double x;
	double y;

	public In1() {
		angle = PI/6;
		x = cos(angle);
		y = sin(angle);
		result = x*x+y*y;
	}
}

