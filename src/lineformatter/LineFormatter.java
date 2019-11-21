package lineformatter;
import java.io.*;

/**
 * Adam Hardy
 * ADD YOUR NAMES HERE
 * CSE360-85141
 * Assignment 1
 * This class is intended to format a text file based on commands within the
 * given file.
 * @author ahard
 */
public class LineFormatter {
    private int maxChars = 80;
    private char justification = 'l';
    private boolean equalSpacing = false;
    private boolean wrap = false;
    private int lineSpacing = 1;
    private boolean title = false;
    private int paragraphSpacing = 0;
    private int blankLines = 0;
    private int columns = 1;
    
    /**
     * Method for saving a file from the JFileChooser in the GUI
     * @param file is the file to be saved
     */
    public void save (File file) {
        
    }
    
    public String formatLine(String line) {
        
        
        return line;
    }
    
    /**
     * Method for reading and formatting a file
     * @param file is the file to be read and formatted
     */
    public void format(File file) {
        String line;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader readFile = new BufferedReader(fileReader);
            
            line = readFile.readLine();
            
            while(line != null) {
                //Code and method calls for formatting goes here   
                if(line.charAt(0)== '-') {
                    if(line.charAt(1) == 'n') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                              if(commandCount > 0) {
                                  maxChars = commandCount;
                              } else {
                                  //Code to print error to error display
                              }
                        } catch(NumberFormatException exception) {
                            //Code to print error to error display
                        }
                      
                    } else if (line.charAt(1) == 'r') {
                        justification = 'r';
                    } else if (line.charAt(1) == 'l') {
                        justification = 'l';
                    } else if (line.charAt(1) == 'c') {
                        justification = 'c';
                    } else if (line.charAt(1) == 'e') {
                        equalSpacing = true;
                    } else if (line.charAt(1) == 'w') {
                        if(line.charAt(2) == '+') {
                            wrap = true;
                        } else if(line.charAt(2) == '-') {
                            wrap = false;
                        } else {
                            //Code to print error to error display
                        }
                    } else if (line.charAt(1) == 's') {
                        lineSpacing = 1;
                    } else if (line.charAt(1) == 'd') {
                        lineSpacing = 2;
                    } else if (line.charAt(1) == 't') {
                        title = true;
                    } else if (line.charAt(1) == 'p') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                              if(commandCount > 0) {
                                  paragraphSpacing = commandCount;
                              } else {
                                  //Code to print error to error display
                              }
                        } catch(NumberFormatException exception) {
                            //Code to print error to error display
                        }
                    } else if (line.charAt(1) == 'b') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                              if(commandCount > 0) {
                                  blankLines = commandCount;
                              } else {
                                  //Code to print error to error display
                              }
                        } catch(NumberFormatException exception) {
                            //Code to print error to error display
                        }
                    } else if (line.charAt(1) == 'a') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                              if(commandCount == 2 || commandCount == 1) {
                                  columns = commandCount;
                              } else {
                                  //Code to print error to error display
                              }
                        } catch(NumberFormatException exception) {
                            //Code to print error to error display
                        }
                    }
                } else {
                    formatLine(line);
                }
                line = readFile.readLine();
            }
            readFile.close();
        } catch(FileNotFoundException fileNotFound) {
            
        } catch(IOException ioException) {
            
        }
        
    }
    
    

   
    
}
