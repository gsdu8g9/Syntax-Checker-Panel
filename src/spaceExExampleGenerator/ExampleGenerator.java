package spaceExExampleGenerator;

import java.util.*;
import java.io.*;

import spaceExGrammar.*;


import com.google.gson.*;


public class ExampleGenerator {
	
	public static void main(String[] args) throws Exception
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		  int change = 1;
		  System.out.println("Enter the File path: ");
		  Scanner scan = new Scanner(System.in);
		  String file = scan.nextLine();
		  System.out.println("Enter the syntax type to be checked for");
		  String type = scan.next();
		  System.out.printf("Do you want the existing records to be changed (1 for yes 0 for no): ");
		  change = scan.nextInt();
		  	try
		  	{
		  	  	BufferedReader br = new BufferedReader(new FileReader(file));
			 	Expression[] obj = gson.fromJson(br, Expression[].class);
				//System.out.println(obj.length);
			
				String input = new String ("");
				for(int i=0; i<obj.length;i++)
				{
				  		input = obj[i].expression;	
			  			System.out.println("\n" + input);
						SpaceExSyntaxChecker parser = new SpaceExSyntaxChecker(); 
						try
						{
						  	if(type.equalsIgnoreCase("guard") || type.equalsIgnoreCase("invariant"))
						  	{parser.checkGuard(input);}
							else if(type.equalsIgnoreCase("flow")) 
							{parser.checkFlow(input);}
							else if(type.equalsIgnoreCase("assignment"))
							{parser.checkAssignment(input);}
						  	
						 	  		
							System.out.println("Valid Expression");
							if(obj[i].valid.equals("") || change == 1)
							{
							  obj[i].valid = "Yes";
							  obj[i].error = "";
							  obj[i].coloumn = 0;
							  obj[i].line = 0;
							}
							else if(obj[i].valid.equals("Yes"))
							{}
							else
							{
							  System.out.println("Parser gave a different answer to the input expression " + input);
							  System.out.println("Original Result\nValid: " + 	obj[i].valid + "\nError: " + obj[i].error);
							  System.out.println("New Result\nValid: " + 	"Yes" + "\nError: \"\"");
							  System.out.println("Exiting the parser");
							  break;
							}
							
						}
						catch(Exception e)
						{
								System.out.println("Invalid Expression.");
								System.out.println(e.getMessage());
								String s = e.getMessage();
								char col = s.charAt(s.indexOf("column")+7);
								char lin = s.charAt(s.indexOf("line")+5);
								if(obj[i].valid.equals("") || change == 1)
								{
								  obj[i].valid = "No";
								  obj[i].error = e.getMessage();
							      obj[i].coloumn = Integer.parseInt(""+col);
								  obj[i].line = Integer.parseInt(""+lin);
								}
								else if(obj[i].valid.equals("No") && obj[i].error.equals(e.getMessage()))
								{}
								else
								{
								  System.out.println("Parser gave a different answer to the input expression " + input);
								  System.out.println("Original Result\nValid: " + 	obj[i].valid + "\nError: " + obj[i].error);
								  System.out.println("New Result\nValid: " + 	"No" + "\nError: " + e.getMessage());
								  System.out.println("Exiting the parser");
								  break;
								}												
						}
						catch(Error e)
						{
								System.out.println("Token Error!!!.");
								System.out.println(e.getMessage());
								//StringTokenizer st = new StringTokenizer(e.getMessage(), ".");
								String s = e.getMessage();
								char col = s.charAt(s.indexOf("column")+7);
								char lin = s.charAt(s.indexOf("line")+5);
								if(obj[i].valid.equals("") || change == 1)
								{
								  obj[i].valid = "No";
							      obj[i].error = e.getMessage();
								  obj[i].coloumn = Integer.parseInt(""+col);
								  obj[i].line = Integer.parseInt(""+lin);
								}
								else if(obj[i].valid.equals("No") && obj[i].error.equals(e.getMessage()))
								{}
								else
								{
								  System.out.println("Parser gave a different answer to the input expression " + input);
								  System.out.println("Original Result\nValid: " + 	obj[i].valid + "\nError: " + obj[i].error);
								  System.out.println("New Result\nValid: " + 	"No" + "\nError: " + e.getMessage());
								  System.out.println("Exiting the parser");
								  break;
								}
							
						}
				}
				String json = gson.toJson(obj);
				System.out.println(json);
				FileWriter writer = new FileWriter(file);
				writer.write(json);
				writer.close();	
			
			}
			catch(Exception e)
			{
			  System.out.println(e.getMessage());
			}	
	}

	

	/*
	The coloumn number mentioned in the error is counted in the following manner.
	One token is counted as one coloumn like := is one coloumn.
	Next the spaces in between a statement and "&" are not counted but the spaces
	in between the expression are counted.  
	*/

}
