Syntax-Checker-Panel
====================

A java based text editor which checks the text based on a grammar. 

This was built to be a part of the SpaceEx tool GUI, available at http://spaceex.imag.fr/ 

To run the code, build it with google-gson-2.2.1 library available for working with .json files, used to store examples.

Project can be seen as four parts:
  
  The folder "spaceExGrammar" has the SpaceEx grammar in the .jj file and the lexer and parser generated using JavaCC Library.
  
  Folder "spaceExExampleGenerator" has codes to test the grammar against examples in a file to be provided as input (command line based).
  
  "syntaxCheckerPanel" has the codes for the text panel. This is the final GUI which can be used anywhere by using SyntaxCheckerPanel. 
  
  "editor" has a test implementation of the above SyntaxCheckerPanel, with some added funcitonalities.
  
