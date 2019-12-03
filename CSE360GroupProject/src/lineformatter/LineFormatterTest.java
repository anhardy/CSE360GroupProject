package lineformatter;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.Test;

/**
 * Junit Test Program
 * CSE360-85141
 * Team Project
 * @author Yuying Guan
 *
 */
class LineFormatterTest {
	
	//private LineFormatter formatter = new LineFormatter();
	
	/**
	 * Method that compares 2 files' content line by line
	 * @param expectedFileName expected_output file name
	 * @param actualFileName actual output file name
	 * @return boolean value. True if 2 files have the same content, Otherwise False
	 */
	private static boolean equalFile(String expectedFileName, String actualFileName)
	{
		boolean equal;
	    BufferedReader bExpected;
	    BufferedReader bActual;
	    String expectedLine ;
	    String actualLine ;

	    equal = false;
	    bExpected = null ;
	    bActual = null ;

	    try {
	        bExpected = new BufferedReader(new FileReader(expectedFileName));
	        bActual = new BufferedReader(new FileReader(actualFileName));

	        if ((bExpected != null) && (bActual != null)) {
	            expectedLine = bExpected.readLine() ;
	            actualLine = bActual.readLine() ;

	            if(((expectedLine != null) && expectedLine.equals(actualLine)) || ((expectedLine == null) && (actualLine == null)) )
	            	equal=true;

	            while(expectedLine != null && equal)
	            {
	                expectedLine = bExpected.readLine() ;
	                actualLine = bActual.readLine() ; 
	                equal = expectedLine.equals(actualLine) ;
	            }
	        }
	    } 
	    catch (Exception e) {} 
	    finally {
	        try {
	            if (bExpected != null) {
	                bExpected.close();
	            }
	            if (bActual != null) {
	                bActual.close();
	            }
	        } catch (Exception e) {}
	    }
	    return equal;    
	}
	
	
	/**
	 * Just a sample test case to check if the equalFile method works
	 */
	@Test
	void test() throws IOException
	{
			File f1=new File("testcase1.txt");
			f1.createNewFile();
			FileWriter fileWriter;
			fileWriter = new FileWriter(f1.getPath());
			String str1="I am a test string and number one two three four five six seven eight nine ten eleven twelve.";
			fileWriter.write(str1);
		    fileWriter.close();

		    /**
		     * TO-DO
		     * call formatter to get actual.txt
		     */
		    
		    File f2=new File("expected1.txt");
			f2.createNewFile();
			FileWriter fileWriter2 = new FileWriter(f2.getPath());
			String str2="I am a test string and number one two three four five six seven eight nine ten\neleven twelve.";
			fileWriter2.write(str2);
		    fileWriter2.close();
		    		    
			assertFalse(equalFile("testcase1.txt","expected1.txt"));	
	}

}

