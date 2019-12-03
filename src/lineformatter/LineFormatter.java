package lineformatter;

import java.io.*;
import java.util.ArrayList;

/**
 * Yuying Guan
 * Adam Hardy
 * Samuel Maness
 * Mark Snee

 * CSE360-85141
 * Team Project
 * This class is intended to format a text file based on commands within the
 * given file.
 * @author ahard
 */
//extends ArrayList<String>
public class LineFormatter extends ArrayList<String> {

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
    private int countStartIndex; //Beginning of current "block" in array list
    private int countEndIndex; //End of current "block" in array list
    private int replacementIndex = 0;


    boolean calledFromFormatWrap = false;//indicates wrap has been called
    boolean calledFromColumns = false;

    /**
     * Finalizes formatting by ensuring all lines have a newline character
     */
    public void finalizeFormatting() {
        String line;
        for (int i = 0; i < formattedLines.size(); i++) {
            line = formattedLines.get(i);
            if (!line.contains("\n")) {
                line += "\n";
                formattedLines.set(i, line);
            }
        }
    }

    /**
     * Method for saving a file from the JFileChooser in the GUI
     *
     * @param file is the file to be saved
     */
    public void save(File file) {
        try {
            FileWriter fWriter = new FileWriter(file);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            PrintWriter outFile = new PrintWriter(bWriter);
            for (int i = 0; i < formattedLines.size(); i++) {
                outFile.print(formattedLines.get(i));
            }
            outFile.close();

        } catch (IOException ex) {
            errors.add("Error: Invalid File. File could not be written");
        }
    }

    /**
     * Method for adding number of blank lines specified by command, then
     * resetting blank line count to 0 until next blank line command read
     */
    public void addBlankLines() {
        for (int currentCount = 0; currentCount < blankLines; currentCount++) {
            formattedLines.add("\n");
            countStartIndex++;
            countEndIndex++;
        }
        blankLines = 0;

    }

    /**
     * Method for formatting line justification of lines already formatted
     * for line count. Creates two character arrays, one filled with as many
     * spaces as the maximum character count, and the other created from the
     * line to be formatted. This allows the a formatted String to be
     * dynamically built from the point of justification: left to right
     * for left, middle out for centered, and right to left for right.
     */
    public void formatJustification() {
        char[] formatted = new char[maxChars];
        char[] charLine;
        String line;
        for (int formattedIndex = countStartIndex; formattedIndex < countEndIndex; formattedIndex++) {
            for (int blankCount = 0; blankCount < maxChars; blankCount++) {
                formatted[blankCount] = ' ';
            }
            charLine = formattedLines.get(formattedIndex).toCharArray();
            if (justification == 'r') {
                for (int i = charLine.length - 1, j = maxChars - 1; i >= 0; i--, j--) {
                    formatted[j] = charLine[i];
                }
                line = String.copyValueOf(formatted);
                formattedLines.set(formattedIndex, line);
            } else if (justification == 'c') {
                int midpointOriginal = (charLine.length - 1) / 2;
                int midpointFormatted = (maxChars - 1) / 2;
                for (int rightOriginal = midpointOriginal + 1, rightFormatted = midpointFormatted + 1;
                        rightOriginal < charLine.length; rightOriginal++, rightFormatted++) {
                    formatted[rightFormatted] = charLine[rightOriginal];

                }
                for (int leftOriginal = midpointOriginal, leftFormatted = midpointFormatted;
                        leftOriginal >= 0; leftOriginal--, leftFormatted--) {
                    formatted[leftFormatted] = charLine[leftOriginal];

                }
                line = String.copyValueOf(formatted);
                formattedLines.set(formattedIndex, line);
            }

        }

    }

    /**
     * Using the same logic as center justification, a title formatted by
     * building a character array outward from the middle. Title only formats
     * the line after the command.
     */
    public void formatTitle() {
        char[] formatted = new char[maxChars];
        char[] charLine;
        String line;
        String underline = "";
        for (int formattedIndex = countStartIndex; formattedIndex < countEndIndex; formattedIndex++) {
            for (int blankCount = 0; blankCount < maxChars; blankCount++) {
                formatted[blankCount] = ' ';
            }
            charLine = formattedLines.get(formattedIndex).toCharArray();
            int midpointOriginal = (charLine.length - 1) / 2;
            int midpointFormatted = (maxChars - 1) / 2;
            for (int rightOriginal = midpointOriginal + 1, rightFormatted = midpointFormatted + 1;
                    rightOriginal < charLine.length; rightOriginal++, rightFormatted++) {
                formatted[rightFormatted] = charLine[rightOriginal];

            }
            for (int leftOriginal = midpointOriginal, leftFormatted = midpointFormatted;
                    leftOriginal >= 0; leftOriginal--, leftFormatted--) {
                formatted[leftFormatted] = charLine[leftOriginal];

            }
            line = String.copyValueOf(formatted);
            formattedLines.set(formattedIndex, line);
        }
        for (int i = 0; i < maxChars; i++) {
            underline += "-";
        }
        underline += "\n";
        formattedLines.add(underline);
        title = false;
    }

    /**
     * This method takes in a line and checks if its length is greater than the
     * maximum characters. If it is, it decrements from the max character length
     * until a space is reached, so as not to split in the middle of a word. The
     * first substring is added to the arrayList of formatted lines. If the
     * second string is not empty, the method is recursively called, with the
     * remaining string being passed in.
     *
     * @param line is the line to be formatted
     */
    public int formatLineCount(String line) {
        int endLine = maxChars;

        String formatted;
        String remainder;
        if (line.length() > maxChars) {
            while (line.charAt(endLine) != ' ') {
                endLine--;
            }
            formatted = line.substring(0, endLine);
            remainder = line.substring(endLine + 1);
            if (!remainder.isEmpty()) {
                //add to formatted lines
                if (!calledFromFormatWrap) {
                    formattedLines.add(formatted);
                } else {
                    //use set to replace line
                    formattedLines.set(replacementIndex, formatted);
                    replacementIndex++;
                }
                //recursive call
                formatLineCount(remainder);
            }
        } else {
            formatted = line;
            //add to formattedLines
            if (!calledFromFormatWrap) {
                formattedLines.add(formatted);
            } else {
                //use set to replace
                formattedLines.set(replacementIndex, formatted);
                replacementIndex++;

            }
        }

        //System.out.println("replacement index in format line length is " + replacementIndex);
        // for(int i = replacementIndex; i < countEndIndex; i++)
        // {
        //}
        System.out.println("replacementIndex is " + replacementIndex);
        System.out.println("first line to remove is " + formattedLines.get(replacementIndex));

        return replacementIndex;
    }

    /**
     * This method adds in a number of spaces before a line based on a given
     * count from a paragraph command. Paragraph spacing is reset to 0 until
     * next paragraph command given
     *
     * @param line is the line be made into a paragraph
     * @return
     */
    public String formatParagraph(String line) {
        String spacing = "";
        for (int spacingCount = 0; spacingCount < paragraphSpacing; spacingCount++) {
            spacing += " ";
        }
        spacing += line;
        line = spacing;

        paragraphSpacing = 0;
        return line;
    }

    /**
     * Method for formating equal spacing.
     *
     * @param line
     */
    private void formatEqualSpacing() {

        String trimmedLine = null;

        int lengthOfTrimmedLine;

        int numberOfSpacesToInsert;

        int numberOfInsertionPoints;//spaces will be inserted at insertion points/spaces

        for (int formattedIndex = countStartIndex; formattedIndex < countEndIndex; formattedIndex++) {
            trimmedLine = formattedLines.get(formattedIndex).trim();
            lengthOfTrimmedLine = trimmedLine.length();

            numberOfSpacesToInsert = maxChars - lengthOfTrimmedLine;//total number of spaces that need to be inserted

            numberOfInsertionPoints = 0;

            if (trimmedLine.length() > 0) {
                for (int index = 0; index < lengthOfTrimmedLine; index++)//count number of spaces in line
                {

                    if (trimmedLine.charAt(index) == ' ') {

                        numberOfInsertionPoints++;

                    }

                }

                int numberOfSpacesToInsertAfterEachSpace = 0;

                //calculate number of spaces to insert at insertion point
                if (numberOfSpacesToInsert % numberOfInsertionPoints != 0) {

                    numberOfSpacesToInsertAfterEachSpace = (numberOfSpacesToInsert / numberOfInsertionPoints) + 1;

                } else {

                    numberOfSpacesToInsertAfterEachSpace = numberOfSpacesToInsert / numberOfInsertionPoints;

                }

                //declare new char array to add contents of line and added spaces
                char[] equallySpacedLine = new char[maxChars];

                //separate indexes for the trimmed line and dynamically created char array
                int insertIndex = 0;

                int trimmedLineIndex = 0;

                while (insertIndex <= maxChars - 1) {

                    if (trimmedLine.charAt(trimmedLineIndex) != ' ') {

                        equallySpacedLine[insertIndex] = trimmedLine.charAt(trimmedLineIndex);

                        insertIndex++;

                        trimmedLineIndex++;

                    } else {

                        //insert existing zero into equallySpacedLine
                        equallySpacedLine[insertIndex] = trimmedLine.charAt(trimmedLineIndex);

                        insertIndex++;

                        trimmedLineIndex++;

                        //insert additional spaces into equallySpacedLine
                        int remainingNumberOfSpacesToInsert = numberOfSpacesToInsertAfterEachSpace;

                        while (remainingNumberOfSpacesToInsert > 0 && numberOfSpacesToInsert > 0) {

                            equallySpacedLine[insertIndex] = ' ';

                            remainingNumberOfSpacesToInsert--;

                            numberOfSpacesToInsert--;

                            insertIndex++;

                        }

                    }

                }

                String formattedString = String.valueOf(equallySpacedLine);

                formattedLines.set(formattedIndex, formattedString);

            }
        }
        equalSpacing = false;
    }

    /**
     *
     * Method for formating wrap. This method concatenates the String elements
     * of the
     *
     * ArrayList formattedLines into a single string and then calls
     * formatLineCount with this * single string as its parameter to wrap the
     * text. The method must be called only after an * initial call to
     * formatLineCount is complete. *
     *
     *
     */
    private int formatWrap() {
        int count = 0; //Count how many strings get added to singleString.

        calledFromFormatWrap = true;

        String singleString = "";

        boolean initialLine = true;//boolean true until after first line is added to singleString

        for (int i = replacementIndex; i < formattedLines.size(); i++) {

            if (initialLine) {

                singleString = singleString + formattedLines.get(i);

                initialLine = false;//a space will be added between lines when false
                count++;
            } else {

                singleString = singleString + " " + formattedLines.get(i);//add space between strings after initial line
                //System.out.println(singleString);
                //System.out.println("first formatted line is " + formattedLines.get(0));
                //System.out.println("second formatted line is " + formattedLines.get(1));
                //System.out.println("last formatted line is " + formattedLines.get(10));

                count++;

            }

        }

        //make call to formatLineCount using singleString as the parameter
        System.out.println("count is " + count);
        int linesDecreasedTo = formatLineCount(singleString);
        System.out.println("linesDecreased to = " + linesDecreasedTo);

        //Attention there is a question here as to count - 1 or count
        //or linesDecreasedTo to or LinesDecreasedTo -1
        int numberOfArrayElementsToRemove = (count) - linesDecreasedTo;
        if (calledFromColumns || calledFromFormatWrap) {
            while (numberOfArrayElementsToRemove > 0) {

                System.out.println("removed line is " + formattedLines.get(replacementIndex));
                formattedLines.remove(replacementIndex);
                numberOfArrayElementsToRemove--;

            }
            formattedLines.remove(replacementIndex);
        }

        //int removeIndex = replacementIndex;
        //while(removeIndex < countEndIndex);
        //{
        //formattedLines.remove(removeIndex);
        //removeIndex++;
        calledFromFormatWrap = false;
        countEndIndex = formattedLines.size();
        return linesDecreasedTo;
    }

    /**
     * Method for formatting columns. This method must be called after an
     * initial call to formatLineLength with the variable for maximum characters
     * is set to 35. This method calls formatWrap to format the columns. "Rows"
     * here and in the notes below refers to the number of rows created after
     * the call to wrap. The method divides the number of total rows in half and
     * assigns the second half of the rows to be concatenated with the first
     * half.
     *
     */
    public void formatColumns() {
        calledFromColumns = true;
        //countStarting index is showing as 46
        int wrapStartingIndex = 0;
        System.out.println("start of first column = " + formattedLines.get(0));
        System.out.println("wrapStartingIndex from top of format Columns = " + wrapStartingIndex);
        //make call to Wrap method to format rows of length 35
        int linesDecreasedTo = formatWrap();
        System.out.println("linesdecreased to from formatColumns " + linesDecreasedTo);

        int wrapEndIndex = countEndIndex;
        int totalNumberOfRows = linesDecreasedTo;

        int numberOfRowsForLeftColumn = 0;
        int numberOfRowsForRightColumn = 0;
        int indexForRightColumn = 0;

        if (totalNumberOfRows % 2 != 0) {
            //If the number of rows is odd, the left column receives the additional column.
            numberOfRowsForLeftColumn = (totalNumberOfRows / 2) + 1;
            numberOfRowsForRightColumn = totalNumberOfRows - numberOfRowsForLeftColumn;
            indexForRightColumn = wrapStartingIndex + numberOfRowsForLeftColumn;

        } else {
            numberOfRowsForLeftColumn = (totalNumberOfRows / 2);
            numberOfRowsForRightColumn = totalNumberOfRows - numberOfRowsForLeftColumn;
            indexForRightColumn = wrapStartingIndex + numberOfRowsForLeftColumn;

        }

        int numberOfConcatenations = numberOfRowsForRightColumn;
        //Create a string of spaces to separate the columns.
        String tenSpaces = "          ";
        //Concatenate formated formated row with string of 10 spaces and another row to form right column.
        while (numberOfConcatenations > 0) {
            String tempString = formattedLines.get(wrapStartingIndex) + tenSpaces + formattedLines.get(indexForRightColumn);
            //use set to set tempString here
            formattedLines.set(wrapStartingIndex, tempString);
            System.out.println(tempString);
            numberOfConcatenations--;
            wrapStartingIndex++;

            //Do not increment indexForRightColumn when zero to avoid out of bounds exception.
            if (numberOfConcatenations > 0) {
                indexForRightColumn++;
            }
        }
        //When there are an odd number of total columns, the left is favored. The following prints the 
        //last line in the left column, which does not need to be concatenated with another row.
        if (totalNumberOfRows % 2 != 0) {
            String tempString = formattedLines.get(wrapStartingIndex);
            formattedLines.set(wrapStartingIndex, tempString);
            System.out.println(tempString);
        }

        //remove elements from formatted lines
        int numberOfArrayElementsToRemove = linesDecreasedTo - numberOfRowsForLeftColumn;
        int removalIndex = linesDecreasedTo - numberOfRowsForLeftColumn;
        System.out.println("number of elements to remove from columns funtion " + numberOfArrayElementsToRemove);
        System.out.println("removal Index from columns function " + removalIndex);
        while (numberOfArrayElementsToRemove > 0) {

            formattedLines.remove(removalIndex);
            numberOfArrayElementsToRemove--;
        }

        calledFromColumns = false;
    }

    /**
     * Method for formatting a line based on commands given
     *
     * @param line is the line to be formatted
     * @return
     */
    public void formatLine(String line) {
        line = line.trim();
        if (justification == 'l' && equalSpacing == false && title == false) {
            if (paragraphSpacing > 0) {
                line = formatParagraph(line);
            }
        }
        if (columns == 1) {
            countStartIndex = formattedLines.size();
            formatLineCount(line);
            countEndIndex = formattedLines.size();
        }
        if (columns == 2) {

            countStartIndex = formattedLines.size();
            maxChars = 35;
            formatLineCount(line);
            countEndIndex = formattedLines.size();

        }
        if (justification != 'l' && title == false && equalSpacing == false) {
            paragraphSpacing = 0;
            formatJustification();
        }
        if (title == true) {
            formatTitle();
        }
        if (title == false && equalSpacing == true) {
            formatEqualSpacing();
        }

    }

    /**
     * Accessor method for accessing list of formatted lines
     *
     * @return the list of formatted lines
     */
    public ArrayList getFormattedLines() {

        return formattedLines;
    }

    /**
     * Accessor method for accessing list of errors
     *
     * @return the list of errors
     */
    public ArrayList getErrors() {

        return errors;
    }

    /**
     * Removes all formatted lines from respective array list and resets Indices
     * of current "block" to be formatted
     */
    public void clearFormattedLines() {
        formattedLines.clear();
        countStartIndex = 0;
        countEndIndex = 0;
        replacementIndex = 0;
        maxChars = 80;
        justification = 'l';
        equalSpacing = false;
        wrap = false;
        lineSpacing = 1;
        title = false;
        paragraphSpacing = 0;
        blankLines = 0;
        columns = 1;
        countStartIndex = 0; //Beginning of current "block" in array list
        countEndIndex = 0; //End of current "block" in array list
        replacementIndex = 0;
    }

    /**
     * Removes all errors from respective array list
     */
    public void clearErrors() {
        errors.clear();
    }
    
    public void formatDoubleSpace(int start, int end) {
        for (int i = start; i < end; i++) {
            if (!formattedLines.get(i).contains("\n")) {
                 if(i > 40 && i < 55) {
                    System.out.println("Test");
                }
                formattedLines.add(i, "\n");
               
                i++;
                end++;
                countEndIndex++;
            }
        }
    }

    /**
     * Method for reading and formatting a file. It will continue to read and
     * parse commands until a non-command line has been read, at which point the
     * line will be formatted based on these commands. This process will repeat
     * for each line and commands until the end of the file.
     *
     * @param file is the file to be read and formatted
     */
    public void format(File file) {
        String line;
        int lineCount = 0;
        int spaceStartIndex = 0;
        int spaceEndIndex = 0;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader readFile = new BufferedReader(fileReader);

            line = readFile.readLine();

            while (line != null) {
                //Code and method calls for formatting goes here
                lineCount++;
                if (line.length() > 0 && line.charAt(0) == '-') {
                    if (line.charAt(1) == 'n') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                            if (commandCount > 0) {
                                maxChars = commandCount;
                            } else {
                                errors.add("Line " + lineCount + " error: Maximum line length must be greater than 0");
                            }
                        } catch (NumberFormatException exception) {
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
                        if (line.charAt(2) == '+') {
                            wrap = true;
                            replacementIndex = countStartIndex + 1;
                        } else if (line.charAt(2) == '-') {

                            formatWrap();
                            wrap = false;
                        } else {
                            errors.add("Line " + lineCount + " error: Invalid command. Ignoring comand");
                        }
                    } else if (line.charAt(1) == 's') {
                        if(lineSpacing == 2) {
                            spaceEndIndex = countEndIndex;
                            formatDoubleSpace(spaceStartIndex, spaceEndIndex);
                        } 
                        lineSpacing = 1;
                    } else if (line.charAt(1) == 'd') {
                       spaceStartIndex = countStartIndex + 1;
                       lineSpacing = 2;
                    } else if (line.charAt(1) == 't') {
                        title = true;
                    } else if (line.charAt(1) == 'p') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                            if (commandCount > 0) {
                                paragraphSpacing = commandCount;
                            } else {
                                errors.add("Line " + lineCount + " error: Paragraph spacing must be greater than 0");
                            }
                        } catch (NumberFormatException exception) {
                            errors.add("Line " + lineCount + " error: Invalid command. Ignoring command");
                        }
                    } else if (line.charAt(1) == 'b') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                            if (commandCount > 0) {
                                if (lineSpacing == 1) {
                                    blankLines = commandCount;
                                } else {
                                    blankLines = commandCount - 1;
                                }
                                addBlankLines();
                            } else {
                                errors.add("Line " + lineCount + " error: Blank lines must be greater than 0");
                            }
                        } catch (NumberFormatException exception) {
                            errors.add("Line " + lineCount + " error: Invalid command. Ignoring command");
                        }
                    } else if (line.charAt(1) == 'a') {
                        String remainingCommand = line.substring(2);
                        try {
                            int commandCount = Integer.parseInt(remainingCommand);
                            if (commandCount == 2 || commandCount == 1) {
                                columns = commandCount;
                                if (columns == 2 && commandCount == 1) {
                                    formatColumns();
                                }
                            } else {
                                errors.add("Line " + lineCount + " error: Invalid column count. Column count can be 1 or 2");
                            }

                        } catch (NumberFormatException exception) {
                            errors.add("Line " + lineCount + " error: Invalid command. Ignoring command");
                        }
                    }
                } else if (line.length() > 0) {
                    formatLine(line);

                } 
                line = readFile.readLine();
            }
            //make call to format wrap after control has fully exited formatLine(line)
            if (columns == 1 && wrap == true) {
                formatWrap();
            }
            if (columns == 2) {
                formatColumns();
            }
            if(lineSpacing == 2) {
                spaceEndIndex = formattedLines.size();
                formatDoubleSpace(spaceStartIndex, spaceEndIndex);
            }

            readFile.close();
            finalizeFormatting();
        } catch (FileNotFoundException fileNotFound) {
            errors.add("Error: File not found");

        } catch (IOException ioException) {
            errors.add("Error: Invalid file");
        }

    }

}
