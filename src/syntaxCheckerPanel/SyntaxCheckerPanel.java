package syntaxCheckerPanel;

import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.*;

import spaceExGrammar.*;

import java.util.*;

public class SyntaxCheckerPanel extends JPanel implements ActionListener, KeyListener, SyntaxConstants 
{
	private JTextPane syntaxCheckerTextPanel = new JTextPane();
	private JButton checkSyntaxButton; 
	private TextLineNumber lineNumbers = new TextLineNumber(syntaxCheckerTextPanel);
	private JTextArea resultArea = new JTextArea(5, 5);
	
	private Timer automaticCheckTimer;
	static final int AUTOMATIC_CHECK_DELAY = 2000;
	
	static final int RESULT_AREA_ROWS = 5;
	static final int SYNTAX_CHECK_PANEL_WIDTH = 500;
	static final int SYNTAX_CHECK_PANEL_HEIGHT = 1000;
	
	private boolean syntaxDefined = false;
	private EventListenerList listenerList = new EventListenerList(); 
	Set<String> PrimedParameters = new HashSet<String>();
	Set<String> UnprimedParameters = new HashSet<String>();
	//constructor with file as well as syntax defined
	public SyntaxCheckerPanel(String file, String type)
	{
		this(type);
		try
		{
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			syntaxCheckerTextPanel.read(br, null);
			br.close();
			syntaxCheckerTextPanel.requestFocus();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	//constructor with only syntax defined
	public SyntaxCheckerPanel(String type)
	{
		checkSyntaxButton = new JButton("Check " + type);
		JScrollPane scroll_syntax_pane = new JScrollPane(syntaxCheckerTextPanel);
		scroll_syntax_pane.setPreferredSize(new Dimension(SYNTAX_CHECK_PANEL_WIDTH, SYNTAX_CHECK_PANEL_HEIGHT));
		scroll_syntax_pane.setRowHeaderView( lineNumbers );
		resultArea.setRows(RESULT_AREA_ROWS);
		resultArea.setLineWrap(true);
		resultArea.setWrapStyleWord(true);
		resultArea.setEditable(false);
		syntaxCheckerTextPanel.addKeyListener(this);
		checkSyntaxButton.addActionListener(this);
		this.setLayout(new BorderLayout());
		
		//uncomment the following line to display check type button.
		//panel.add(checkSyntaxButton, BorderLayout.NORTH);
		
		this.add(scroll_syntax_pane, BorderLayout.CENTER);
		JScrollPane scroll_result_area = new JScrollPane(resultArea);
		scroll_syntax_pane.setPreferredSize(new Dimension(500, 1000));
		this.add(scroll_result_area, BorderLayout.SOUTH);
		ActionListener timedCheck = new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				checkSyntaxButton.doClick();
				automaticCheckTimer.stop();
			}
		};
		automaticCheckTimer = new Timer(AUTOMATIC_CHECK_DELAY, timedCheck);
		syntaxDefined = true;
	}
	//constructor with only file specified
	public SyntaxCheckerPanel(File filename)
	{
		this();
		String file = filename.getPath();
		try
		{
			FileReader 	reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			syntaxCheckerTextPanel.read(br, null);
			br.close();
			syntaxCheckerTextPanel.requestFocus();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	//constructor with nothing specified
	public SyntaxCheckerPanel()
	{
		JButton check_assignment_button = new JButton("Check Assignment");
		JButton check_flow_button = new JButton("Check Flow");
		JButton check_guard_button = new JButton("Check Guard");
		JButton check_invariant_button = new JButton("Check Invariant");
		JButton color_button = new JButton("Colourise");
		JPanel buttons_panel = new JPanel();
		JScrollPane scroll_syntax_pane = new JScrollPane(syntaxCheckerTextPanel);
		scroll_syntax_pane.setRowHeaderView( lineNumbers );
		resultArea.setRows(RESULT_AREA_ROWS);
		resultArea.setLineWrap(true);
		resultArea.setWrapStyleWord(true);
		resultArea.setEditable(false);
		syntaxCheckerTextPanel.addKeyListener(this);
		check_assignment_button.addActionListener(this);
		check_flow_button.addActionListener(this);
		check_guard_button.addActionListener(this);
		check_invariant_button.addActionListener(this);
		color_button.addActionListener(this);
		this.setLayout(new BorderLayout());
		buttons_panel.add(check_assignment_button);
		buttons_panel.add(check_flow_button);
		buttons_panel.add(check_guard_button);
		buttons_panel.add(check_invariant_button);
		buttons_panel.add(color_button);
		this.add(buttons_panel, BorderLayout.NORTH);
		this.add(scroll_syntax_pane, BorderLayout.CENTER);
		JScrollPane scroll_result_area = new JScrollPane(resultArea);
		scroll_syntax_pane.setPreferredSize(new Dimension(SYNTAX_CHECK_PANEL_WIDTH, SYNTAX_CHECK_PANEL_HEIGHT));
		this.add(scroll_result_area, BorderLayout.SOUTH);
	}
	//function which returns a panel to be added in any java GUI
	public JPanel toAdd()
	{
		return this;
	}
	//to set text in the JTextPane
	public void setPredicate(String predicate)
	{
		syntaxCheckerTextPanel.setText(predicate);
	}
	//action listener
	public void actionPerformed(ActionEvent event)
	{
		int error_line = 0;
		int error_column = 0;
		try
		{
			
			String predicate = syntaxCheckerTextPanel.getDocument().getText(0, syntaxCheckerTextPanel.getDocument().getLength());
			//if there is nothing written in the text pane then return
			if(predicate.trim().equals(""))
			{resultArea.setText("");return;}
			
			resultArea.setText("");
			SpaceExSyntaxChecker parser = new SpaceExSyntaxChecker();
			try
			{
				if(event.getActionCommand().equalsIgnoreCase("check guard"))
			  	{parser.checkGuard(predicate);}
				else if(event.getActionCommand().equalsIgnoreCase("check invariant"))
				{parser.checkInvariant(predicate);}
				else if(event.getActionCommand().equalsIgnoreCase("check assignment"))
				{parser.checkAssignment(predicate);}
			  	else if(event.getActionCommand().equalsIgnoreCase("check flow"))
			  	{parser.checkFlow(predicate);}
			  	else if(event.getActionCommand().equalsIgnoreCase("colourise"))
			  	{
			  		color(error_line, error_column);
			  		return;
			  	}
				resultArea.append("Valid Expression");
			}
			catch(Exception e)
			{
				resultArea.append("Syntax Error!\n" + e.getMessage());
				error_line = Integer.parseInt(e.getMessage().substring(e.getMessage().indexOf("line")+5, e.getMessage().indexOf(',', e.getMessage().indexOf("line")+5)));
				error_column = Integer.parseInt(e.getMessage().substring(e.getMessage().indexOf("column")+7, e.getMessage().indexOf('.', e.getMessage().indexOf("column")+7)));
			}
			catch(Error e)
			{
				resultArea.append("Token Error! \n" + e.getMessage());
				error_line = Integer.parseInt(e.getMessage().substring(e.getMessage().indexOf("line")+5, e.getMessage().indexOf(',', e.getMessage().indexOf("line")+5)));
				error_column = Integer.parseInt(e.getMessage().substring(e.getMessage().indexOf("column")+7, e.getMessage().indexOf('.', e.getMessage().indexOf("column")+7)));
			}
			lineNumbers.setErrorLine(error_line);
			color(error_line, error_column);
			if(event.getActionCommand().equalsIgnoreCase("check guard") || event.getActionCommand().equalsIgnoreCase("check invariant"))
			{
				if(predicate.indexOf('\'')!=-1)
				{
					if(resultArea.getText().equals("Valid Expression"))
					{resultArea.setText("\nFound \'. Invalid statement.");}
					else
					{resultArea.append("\nFound \'. Invalid statement.");}
					Highlighter error_highlighter = syntaxCheckerTextPanel.getHighlighter();
					RedZigZagPainter red_unerline = new RedZigZagPainter();
					error_highlighter.addHighlight(predicate.indexOf('\'')-1, predicate.indexOf('\'')+2, red_unerline);
					error_line=1;
					for(int i=1;i<=predicate.indexOf('\'');i++)
					{
						if(predicate.charAt(i)=='\n')
							{error_line++;}
					}
					lineNumbers.setErrorLine(error_line+1);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	private void color(int error_line, int error_column)
	{
		try
		{
			PrimedParameters.removeAll(PrimedParameters);
			UnprimedParameters.removeAll(UnprimedParameters);
			String predicate = syntaxCheckerTextPanel.getDocument().getText(0, syntaxCheckerTextPanel.getDocument().getLength());
			Reader predicate_reader = new StringReader(predicate);
			BufferedReader predicate_buffered_reader = new BufferedReader(predicate_reader);
			RedZigZagPainter red_underline = new RedZigZagPainter();
			Highlighter error_highlighter = syntaxCheckerTextPanel.getHighlighter();
			StyledDocument panel_doc = syntaxCheckerTextPanel.getStyledDocument();
			syntaxCheckerTextPanel.setText("");
			Style doc_style = panel_doc.getLogicalStyle(0);
			MutableAttributeSet attribute_set = syntaxCheckerTextPanel.getInputAttributes();
			String predicate_line;
			int lineNo = 0;
			int error_begin=0, error_end=0;
			
			//lineStart is used later to enable spaces at the begning of an
			//empty last line
			int lineStart = 0;
			
			//loop is used as a control flag parameter to stop looping, if wrong token is found 
			boolean loop = true;
			
			Highlighter.Highlight[] remove_highlights = error_highlighter.getHighlights();
			for(int i=0;i<remove_highlights.length;i++)
			{
				error_highlighter.removeHighlight(remove_highlights[i]);
			}
			
			for(predicate_line = predicate_buffered_reader.readLine();predicate_line!=null && loop ;)
			{
				int line_length = predicate_line.length();
				lineNo++;
				SyntaxTokenManager token_manager = new SyntaxTokenManager(new SimpleCharStream(new StringReader(predicate_line)));
				Token currentToken = new Token();
				Color tokenColor;
				//to enable spaces at the start of lines
				for(int i=0;i<predicate_line.length();i++)
				{
					if(predicate_line.charAt(i)==' ')
					{syntaxCheckerTextPanel.getDocument().insertString(syntaxCheckerTextPanel.getDocument().getLength(), " ", null);}
					else
					{break;}
				}
				//lineStart is used later to enable spaces at the begning of an
				//empty last line
				if(lineNo==error_line)
				{lineStart = syntaxCheckerTextPanel.getDocument().getLength();}
				
				for(currentToken = token_manager.getNextToken();currentToken.kind!=0; )
				{	
					String currentTokenString = currentToken.image;
					
					if(currentToken.kind == TRUE || currentToken.kind == FALSE)
						tokenColor = Color.blue;
					else if (currentToken.kind >= SIN && currentToken.kind <= CEIL)
						tokenColor = Color.magenta;
					else if (currentToken.kind == ID)
					{
						tokenColor = new Color(0, 100, 0);						
					}
					else if (currentToken.kind >= EQUALEQUAL && currentToken.kind <= ASSIGNMENT)
						tokenColor = new Color(149, 69, 53);
					else
						tokenColor = Color.black;
					
						
					doc_style = panel_doc.addStyle("StyleFore", null);
					StyleConstants.setForeground(doc_style, tokenColor);
					int length = syntaxCheckerTextPanel.getDocument().getLength();
					panel_doc.insertString(length, currentTokenString, doc_style);
					syntaxCheckerTextPanel.removeStyle("StyleFore");
					Token previousToken = currentToken;				
					//try catch to get next token because some text may not form valid characters
					try
					{currentToken=token_manager.getNextToken();}
					//the catch also sets error_begin and error_end to underline the wrong token
					catch(TokenMgrError e)
					{
						resultArea.append("\n" + e.getMessage());
						resultArea.append("\nColouring failed because of wrong token.");
						syntaxCheckerTextPanel.setText(predicate);
						//currentToken still has previous token, so to get error column, endcolumn works
						error_begin = currentToken.endColumn;
						//to get end, the string after the error token is parsed and first valid token is used to get end
						String remaining = "";
						Token nextToken;
						for(int i = error_begin+1;;)
						{
							remaining = predicate.substring(i);
							SyntaxTokenManager otherTokens = new SyntaxTokenManager(new SimpleCharStream(new StringReader(remaining)));
							try
							{
								nextToken = otherTokens.getNextToken();
								error_end=predicate.indexOf(nextToken.image, error_begin);
								break;
							}
							catch(TokenMgrError error)
							{i++;continue;}
						}
						//since wrong token, no more looping in the text
						loop = false;
						break;
					}
					//for parseexception, begin and end
					if(previousToken.beginColumn==error_column && lineNo==error_line)
					{
						
						error_begin += error_column-1;
						error_end += previousToken.endColumn;
					}
					//to get parameter list, primed and unprimed
					if(previousToken.kind==ID)
					{
						if(currentToken.kind==PRIME)
						{	
							try
							{
								UnprimedParameters.remove(currentTokenString);
								PrimedParameters.add(currentTokenString);
							}
							catch(Exception e)
							{System.out.println(e.getMessage());}
						}
						else
						{	
							try
							{
								if(!(PrimedParameters.contains(currentTokenString)))
									UnprimedParameters.add(currentTokenString);
							}
							catch(Exception e)
							{System.out.println(e.getMessage());}
						}	
					}
					//to enable spaces between tokens
					if(currentToken.beginColumn!=previousToken.endColumn+1)
				    {
				      for(int i=0; i<((currentToken.beginColumn)-(previousToken.endColumn+1));i++)
				      {
				    	  length = syntaxCheckerTextPanel.getDocument().getLength();
				    	  panel_doc.insertString(length, " ", doc_style);
				      }
				    }
					//to enable spaces at the end of line uses lineStart
					if(currentToken.kind == 0)
					{
						for(int i =0 ;i < predicate_line.length()-(syntaxCheckerTextPanel.getCaretPosition()-lineStart);i++)
						{syntaxCheckerTextPanel.getDocument().insertString(syntaxCheckerTextPanel.getDocument().getLength(), " ", null);}
					}
				}
				int length = syntaxCheckerTextPanel.getDocument().getLength();
				if((predicate_line=predicate_buffered_reader.readLine())!=null && loop)
				{panel_doc.insertString(length, "\n", doc_style);}
				else
				{panel_doc.insertString(length, "", doc_style);}
				attribute_set = syntaxCheckerTextPanel.getInputAttributes();
				StyleConstants.setForeground(attribute_set, Color.black);
				panel_doc = syntaxCheckerTextPanel.getStyledDocument();
				panel_doc.setCharacterAttributes(panel_doc.getLength(), panel_doc.getLength()+1, attribute_set, false);
				
				if(lineNo<error_line)
				{
					error_begin+=line_length+1;
					error_end+=line_length+1;
				}
			}
			//begin = end case when the last token is wrong
			if(error_begin == error_end && error_begin!=0 )
			{error_end = error_begin + 3;}
			error_highlighter.addHighlight(error_begin, error_end, red_underline);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	//method to register PaneEventListener
	public void addPaneEventListener(SyntaxCheckerPanelEventListener listener)
	{listenerList.add(SyntaxCheckerPanelEventListener.class, listener);}
	
	//method to unregister PaneEventListener
	public void removePaneEventListener(SyntaxCheckerPanelEventListener listener)
	{listenerList.remove(SyntaxCheckerPanelEventListener.class, listener);}
	
	
	/*
	 * Following method is used to fire events to all the event listeners registered
	 * Also events have to be created at correct places and then fireEvent(event)
	 * must be called 
	 */
	public void fireEvent(SyntaxCheckerPanelEvent event)
	{
		Object[] listeners = listenerList.getListenerList();
		for (int i=0; i<listeners.length; i+=2) 
		{
          if (listeners[i]==SyntaxCheckerPanelEventListener.class) 
          {
        	  //methods called depending on event id like examples below
        	  if(event.paneEventID == SyntaxCheckerPanelEvent.event)
        		  ((SyntaxCheckerPanelEventListener)listeners[i+1]).eventAction(event);
          }
        }
	}
	
	public void keyPressed(KeyEvent e) 
	{}
	
	public void keyReleased(KeyEvent e) 
	{}
	
	public void keyTyped(KeyEvent e) 
	{
		//color the document on any space key and set the caret back to original position
		if(e.getKeyChar()==' ')
		{
			int pos = syntaxCheckerTextPanel.getCaretPosition();
			//when space pressed is the last character
			//without this, the last space is not taken into account
			if(syntaxCheckerTextPanel.getDocument().getLength()==pos)
			{
				try
				{syntaxCheckerTextPanel.getDocument().insertString(pos, " ", null);}
				catch(Exception exception)
				{System.out.println(exception.getMessage());}
			}
			resultArea.setText("");
			color(0, 0);
			if(syntaxCheckerTextPanel.getCaretPosition()!=pos)
			{
				for(int i=0;i<(pos-syntaxCheckerTextPanel.getCaretPosition());i++)
				{
					try 
					{syntaxCheckerTextPanel.getDocument().insertString(syntaxCheckerTextPanel.getDocument().getLength(), " ", null);}
					catch (Exception excep) 
					{System.out.println(excep.getMessage());}
				}
			}
			syntaxCheckerTextPanel.setCaretPosition(pos);
		}
		/*
		 * if syntaxDefined is true i.e. a syntax is defined then check for that syntax if
		 * no key is pressed for 2 seconds. 
		 */
		if(syntaxDefined)
		{
			automaticCheckTimer.setDelay(AUTOMATIC_CHECK_DELAY);
			automaticCheckTimer.setInitialDelay(AUTOMATIC_CHECK_DELAY);
			automaticCheckTimer.restart();
		}
	}
}
