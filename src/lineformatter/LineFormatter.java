package lineformatter;
import java.io.*;
import java.util.ArrayList;

/**
 * Adam Hardy
 * ADD YOUR NAMES HERE
 * CSE360-85141
 * Team Project
 * This class is intended to format a text file based on commands within the
 * given file.
 * @author ahard
 */
public class LineFormatter {
    private ArrayList<String> errors = new ArrayList<>();
    private ArrayList<String> formattedLines = new ArrayList<>();
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
    /**
     * Method for adding number of blank lines specified by command, then 
     * resetting blank line count to 0 until next blank line command read
     */
    public void addBlankLines() {
        for(int currentCount = 0 ; currentCount < blankLines; currentCount++) {
            formattedLines.add("\n");
        }
        blankLines = 0;
        
    }
    
    /**
     * This method takes in a line and checks if its length is greater than
     * the maximum characters. If it is, it decrements from the max character
     * length until a space is reached, so as not to split in the middle of a 
     * word. The first substring is added to the arraylist of formatted lines.
     * If the second string is not empty, the method is recursively called, with
     * the remaining string being passed in.
     * @param line is the line to be formatted
     */
    public void formatLineCount(String line) {
        int endLine = maxChars;
        String formatted;
        String remainder;
        if(line.length() > maxChars) {
            while(line.charAt(endLine) != ' ') {
                endLine--;
            }
            formatted = line.substring(0, endLine);
            remainder = line.substring(endLine+1);
            if(!remainder.isEmpty()) {
                formattedLines.add(formatted);
                formatLineCount(remainder);
            }
        } else {
            formatted = line;
            formattedLines.add(formatted);
        }
        
        
        
    }
    /**
     * This method adds in a number of spaces before a line based on
     * a given count from a pargraph command
     * @param line is the line be made into a paragraph
     * @return 
     */
    public String formatParagraph(String line) {
        String spacing = "";
        for(int spacingCount = 0; spacingCount < paragraphSpacing; spacingCount++){
            spacing += " ";
        }
        spacing += line;
        line = spacing;
        
        
        return line;
    }
    /**
     * Method for formatting a line based on commands given
     * @param line is the line to be formatted
     * @return 
     */
    public void formatLine(String line) {
        if(justification == 'l' && equalSpacing == false && title == false) {
            if(paragraphSpacing > 0) {
                line = formatParagraph(line);
            }
        }
        if(columns == 1) {
            formatLineCount(line);
        }
        
       
    }
    /**
     * Accessor method for accessing list of formatted lines
     * @return the list of formatted lines
     */
    public ArrayList getFormattedLines() {
        
        return formattedLines;
    }
    /**
     * Accessor method for accessing list of errors
     * @return the list of errors
     */
    public ArrayList getErrors() {
        
        return errors;
    }
    
    /**
     * Method for reading and formatting a file. It will continue to read and 
     * parse commands until a non-command line has been read, at which point the
     * line will be formatted based on these commands. This process will repeat
     * for each line and commands until the end of the file.
     * @param file is the file to be read and formatted
     */
    public void format(File file) {
        String line;
        int lineCount = 0;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader readFile = new BufferedReader(fileReader);
            
            line = readFile.readLine();
            
            while(line != null) {
                //Code and method calls for formatting goes here
                lineCount++;
                if(line.length() > 0 && line.charAt(0) == '-') {
                    if(line.charAt(1) == 'n') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                              if(commandCount > 0) {
                                  maxChars = commandCount;
                              } else {
                                 errors.add("Line " + lineCount + " error: Maximum line length must be greater than 0");
                              }
                        } catch(NumberFormatException exception) {
                            errors.add("Line " + lineCount + " error: Invalid command. Ignoring comand");
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
                            errors.add("Line " + lineCount + " error: Invalid command. Ignoring comand");
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
                                  errors.add("Line " + lineCount + " error: Paragraph spacing must be greater than 0");
                              }
                        } catch(NumberFormatException exception) {
                            errors.add("Line " + lineCount + " error: Invalid command. Ignoring command");
                        }
                    } else if (line.charAt(1) == 'b') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                              if(commandCount > 0) {
                                  blankLines = commandCount;
                                  addBlankLines();
                              } else {
                                  errors.add("Line " + lineCount + " error: Blank lines must be greater than 0");
                              }
                        } catch(NumberFormatException exception) {
                            errors.add("Line " + lineCount + " error: Invalid command. Ignoring command");
                        }
                    } else if (line.charAt(1) == 'a') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                              if(commandCount == 2 || commandCount == 1) {
                                  columns = commandCount;
                              } else {
                                  errors.add("Line " + lineCount + " error: Invalid column count. Column count can be 1 or 2");
                              }
                        } catch(NumberFormatException exception) {
                            errors.add("Line " + lineCount + " error: Invalid command. Ignoring command");
                        }
                    }
                } else if(line.length() > 0) {
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
