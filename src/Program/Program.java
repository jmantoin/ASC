/**
 * @author Adam
 */
package Program;

import AssembleProcess.AssemblyError;
import java.util.ArrayList;
import java.util.LinkedList;

public class Program
{

    private String[] programText;   // Program text without any comments.
    private String originalText;    // Program text with comments.
    private SymbolTable sTable;     // The symbol table for the program.
    private String[] hexCodes;      // Hex codes associated with the program.
    private int startLocation;      // Starting point of the program.
    private int endLocation;        // Ending point of the program.
    private LinkedList<AssemblyError> errorList;    // Errors
    private boolean hasErrors;

    /**
     * Initializes all the variables being used.
     */
    public Program(String programText)
    {
        errorList = new LinkedList<AssemblyError>();
        originalText = new ProgramReader().programMinusSpace(programText.split("\\n"));
        this.programText = new ProgramReader().constructProgramText(programText.split("\\n"));
        sTable = new SymbolTable();

        hasErrors = false;
        startLocation = 0;
        endLocation = 0;
    }

    // Returns the original (with comments) program text.
    public String getOriginal()
    {
        return originalText;
    }

    // Getter method for errors.
    public boolean getHasErrors()
    {
        return hasErrors;
    }

    // Setter method for errors.
    public void setHasErrors(boolean a)
    {
        hasErrors = a;
    }

    // Adds an error.
    public void addError(AssemblyError ae)
    {
        errorList.add(ae);
    }

    // Returns all errors associated with the program.
    public String[] getErrors()
    {
        int size = errorList.size();
        String[] errors = new String[size];

        for (int i = 0; i < size; i++) {
            errors[i] = errorList.get(i).getErrors();
        }

        return errors;
    }

    /**
     * Sets the hex code list for the program.
     */
    public void setHexCodes(ArrayList<String> als)
    {
        hexCodes = new String[als.size()];

        for (int i = 0; i < hexCodes.length; i++) {
            hexCodes[i] = als.get(i);
        }
    }

    // Returns the hex codes list.
    public String[] getHexCodes()
    {
        return hexCodes;
    }

    // Returns the symbol table.
    public SymbolTable getSymbolTable()
    {
        return sTable;
    }

    // Returns the length of the program.
    public int getProgramLines()
    {
        return programText.length;
    }

    // Sets the end location of the program.
    public void setEndLocation(int endLocation)
    {
        this.endLocation = endLocation;
    }

    // Returns the program text (without comments).
    public String[] getProgram()
    {
        return programText;
    }

    // Gets the starting point of the program.
    public int getStart()
    {
        if (sTable.containsSymbol("BEGIN")) {
            return Integer.parseInt(sTable.getSymbolAddress("BEGIN"),16);
        } else {
            return Integer.parseInt(sTable.getSymbolAddress("START"),16);
        }
        
    }
    
    public void clearErrors()
    {
        hasErrors = false;
    }
}
