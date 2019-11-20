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
    /**
     * Method for saving a file from the JFileChooser in the GUI
     * @param file is the file to be saved
     */
    public void save (File file) {
        
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
                line = readFile.readLine();
            }
        } catch(FileNotFoundException fileNotFound) {
            
        } catch(IOException ioException) {
            
        }
        
    }
    
    

   
    
}
