package editor;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import syntaxCheckerPanel.*;

public class Edit implements ActionListener
{
	private JFrame frame;
	private SyntaxCheckerPanel pane;
	private JMenuBar menu = new JMenuBar();
	private JMenu filemenu = new JMenu("File");
	private String file, type;
	public Edit()
	{
		frame = new JFrame("Edit");
		pane = new SyntaxCheckerPanel();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(pane, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setSize(700, 500);
		JMenuItem New = new JMenuItem("New");
		JMenuItem Open = new JMenuItem("Open");
		JMenuItem Save = new JMenuItem("Save");
		filemenu.add(New);
		filemenu.add(Open);
		filemenu.add(Save);
		menu.add(filemenu);
		New.addActionListener(this);
		Open.addActionListener(this);
		Save.addActionListener(this);
		frame.setJMenuBar(menu);
		
	}
	public static void main(String args[])
	{
		Edit edit = new Edit();
		
	}
	public void actionPerformed(ActionEvent event)
	{
		if(event.getActionCommand().equals("New"))
		{
			frame.getContentPane().remove(pane);
			pane = new SyntaxCheckerPanel();
			frame.add(pane);
		}
		else if(event.getActionCommand().equals("Open"))
		{
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(frame);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				
				Object[] options = {"Assignment", "Flow", "Guard", "Invariant"};
				type = (String)JOptionPane.showInputDialog(frame, "Choose the syntax to be cheked for: \n", "Syntax Chooser", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if(type==null)
				{
					frame.getContentPane().remove(pane);
					pane = new SyntaxCheckerPanel(fc.getSelectedFile());
					frame.add(pane);
				}
				else
				{
					file = fc.getSelectedFile().getPath();
					frame.getContentPane().remove(pane);
					pane = new SyntaxCheckerPanel(file, type);
					frame.add(pane);
				}
			}	
		}
		else if(event.getActionCommand().equals("Save"))
		{
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(frame);
			if(returnVal == JFileChooser.APPROVE_OPTION)
			{
				file = fc.getSelectedFile().getPath();
				System.out.println(file);
				try
				{
					BufferedReader br = new BufferedReader(new FileReader(file));
					String text = "", line = "";
					while((line = br.readLine())!=null)
					{
						text = text + line + "\n";
					}
					text = text.substring(0, text.length());
					pane.setPredicate(text);
				}
				catch(Exception e)
				{System.out.println(e.getMessage());}
			}
		}
	}
}
