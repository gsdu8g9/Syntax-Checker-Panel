package spaceExExampleGenerator;

public class Expression{
	public String expression = new String("");
	public String valid, error;
	public int coloumn, line;
	public Expression(String exp)
	{
		expression = exp;
		valid = "";
		error = "";
		coloumn = 0;
		line = 0;
	}
}
