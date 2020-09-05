# dialog
An interactive JAVA shell that runs java functions in a command/response terminal. Pure JAVA, support all java functionality and nothing but java.
TO run the command, just type "./dialog" at the project directory.

# Example:
<pre>
xwang@potato:~$ dialog
--- starting dialog verions 0.001 ---

 In[1]:	double angle = PI/6;
       	double x = cos(angle);
       	double y = sin(angle);
       	x*x+y*y
       	
Out[1]:	1.0

 In[2]:	int fibonacci(int k){
       		if(k<=1) return 1;
       		return fibonacci(k-1)+fibonacci(k-2);
       	}
       	
Out[2]:	

 In[3]:	$2.fibonacci(10)
       	
Out[3]:	89

</pre>


# Installation
mvn clean install


# How to use existing Java Packages:
  All java packages are available and package can be imported for each block. The following packages and classes can be used directly without imports:
  
  <h5>Pre-imported packages:</h5>
     <li>java.lang
     <li>java.util
     <li>java.io
     <li>java.net
  <h5>Pre-imported static classes:</h5>
      <li>java.lang.Math
      <li>java.util.Collections
       
