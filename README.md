# dialog
An interactive JAVA shell that runs java functions in a command/response terminal. 


# Example:
-- starting dialog verions 0.001 ---

 In[1]:	double angle = PI/6;
       	double x = cos(angle);
       	double y = sin(angle);
       	x*x+y*y
       	
Out[1]:	1.0

 In[2]:	int fabonacci(int k){
       		if(k<=1) return 1;
       		return fabonacci(k-1)+fabonacci(k-2);
       	}
       	
Out[2]:	

 In[3]:	$2.fabonacci(10)
       	
Out[3]:	89
