/**
 *  @author Adam
 */
package Program;

    /**
     *  Helper class that removes comments and trailing whitespace from the 
     *  program text.
     */

import java.util.ArrayList;

public class ProgramReader
{
    // Removes comments and whitespace.
    protected String[] constructProgramText(String[] lines)
    {
        ArrayList<String> nLines = new ArrayList<String>();
        
        for (int i = 0, j = 0; i < lines.length; i++) {
            if (lines[i].startsWith("*"))
                continue;
            if (lines[i].indexOf(".") != -1)
                lines[i] = stripComments(lines[i]);
            if (lines[i].trim().length() > 0) {
                nLines.add(lines[i]);
            }
        }
        
        String[] newLines = new String[nLines.size()];
        for (int i = 0; i < newLines.length; i++) {
            newLines[i] = nLines.get(i).toUpperCase();
        }
        
        return newLines;
    }
    
    // Removes only whitespace.
    protected String programMinusSpace(String[] lines)
    {
        ArrayList<String> nLines = new ArrayList<String>();
        
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].equals(""))
                nLines.add(lines[i]);
        }
        
        String newLines = "";
        for (int i = 0; i < nLines.size(); i++) {
            newLines += nLines.get(i).toUpperCase();
            newLines += "\n";
        }
        
        return newLines;
    }
    
    // Helper method that strips the comments off the line.
    private String stripComments(String line)
    {
        return line = line.substring(0, line.indexOf("."));
    }
}
