package spaceExGrammar;

import java.io.*;

public class SpaceExSyntaxChecker 
{
	Syntax parser;
	public void checkFlow(String input) throws Exception, Error
	{
		parser = new Syntax(new StringReader(input));
		try
		{parser.syntax();}
		catch(Exception e)
		{throw e;}
		catch(Error e)
		{throw e;}
	}
	public void checkAssignment(String input) throws Exception, Error
	{
		parser = new Syntax(new StringReader(input));
		try
		{parser.lassign();}
		catch(Exception e)
		{throw e;}
		catch(Error e)
		{throw e;}
	}
	public void checkGuard(String input) throws Exception, Error
	{
		parser = new Syntax(new StringReader(input));
		try
		{parser.syntax();}
		catch(Exception e)
		{throw e;}
		catch(Error e)
		{throw e;}
	}
	public void checkInvariant(String input) throws Exception, Error
	{
		parser = new Syntax(new StringReader(input));
		try
		{parser.syntax();}
		catch(Exception e)
		{throw e;}
		catch(Error e)
		{throw e;}
	}
}
