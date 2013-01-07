/**
 * @author Adam
 */
package AssembleProcess;

import Instructions.InstructionSet;
import Program.Program;
import javax.swing.JOptionPane;

public class FirstPass
{

    private Program program;        // The program being assembled.
    private InstructionSet set;     // A list of the instruction set.
    private int lineCount;          // Tracks the current line.
    private int locationCounter;
    private String startStatement;

    /**
     * Constructor that initializes all the variables.
     */
    public FirstPass()
    {
        this.lineCount = 0;
        set = new InstructionSet();
    }

    /**
     * Begins processing the program.
     */
    public void process(Program program)
    {
        this.program = program;
        String[] pText = program.getProgram();  // Loads program text into local variable.

        // Loop through each line.
        for (int i = 0; i < pText.length; i++) {

            String[] statement = splitStatement(pText[i]);  // next Statement

            // Check to see if there is an ORG statement, and process it if there
            // is.
            if (lineCount == 0) {
                if (!isOrg(statement)) {
                    locationCounter = 0;
                } else {
                    processOrg(statement);
                }
            } else {
                // Else check if there is an END statement.
                if (isEnd(statement)) {
                    try {
                        program.getSymbolTable().getSymbolAddress(startStatement);
                    } catch (Exception e) {
                        program.addError(new AssemblyError(">>ERROR<< NO BEGIN/START MNEMONIC FOUND", statement, lineCount));
                    }
                    return; // End of pass one.
                }

                // Not ORG or END, process the statement.
                processStatement(statement);
            }
            // Increment the line count.
            lineCount++;
        }
    }

    /**
     * Process the statement.
     */
    private void processStatement(String[] statement)
    {
        // Check to see if it has a label, and process the label if it does.
        if (hasLabel(statement)) {
            processLabel(statement);
        }
        // Check to see if there is a valid menmonic, and process it if it does.
        if (isValidMnemonic(statement)) {
            processMnemonic(statement);
        } else {
            // Invalid mnemonic, add an error.
            program.addError(new AssemblyError(">>ERROR<< INVALID MNEMONIC FOUND", statement, lineCount));
        }
    }

    /**
     * Checks if the mnemonic is valid. Checks the position where an instruction
     * should occur against the instruction set, returning a boolean.
     */
    public boolean isValidMnemonic(String[] statement)
    {
        if (statement.length == 1) {
            return set.isInstruction(statement[0]);
        } else if (statement.length == 2) {
            if (program.getSymbolTable().containsSymbol(statement[0])) {
                return set.isInstruction(statement[1]);
            }
            return set.isInstruction(statement[0]);
        } else if (statement.length == 3) {
            return set.isInstruction(statement[1]);
        }

        return false;
    }

    /**
     * Processes the mnemonic depending on which type it is.
     */
    public void processMnemonic(String[] statement)
    {
        // Is it a memory mnemonic?
        if (isMemoryMnemonic(statement)) {
            processMemoryMnemonic(statement);
            // Or another ORG statement?
        } else if (isOrg(statement)) {
            processOrg(statement);
            // Or an EQU statement?
        } else if (isEQU(statement)) {
            processEQU(statement);
        } else {
            // If it isn't any of these, it doesn't get checked in the first pass
            // and locationCounter just gets incremented.
            locationCounter += 1;
        }
    }

    /**
     * Processes BSC and BSS statements.
     */
    public void processMemoryMnemonic(String[] statement)
    {
        int L = 0;  // The value that will be added to the locationCounter.
        try {
            if (statement[1].equalsIgnoreCase("BSS")) {
                    // If it's BSS, parse the number of spots to set aside.
                    L = Integer.parseInt(statement[2]);
                    
            } else if (statement[1].equalsIgnoreCase("BSC")) {
                    // If it's BSC, check to see if there are multiple numbers
                    // by splitting the statement. Set L to the length of the split
                    // array.
                    String[] values = statement[2].split(",");
                    L = values.length;
                    
            }
            
            locationCounter += L;   // Add L to the locationCounter.
        } catch (NumberFormatException nfe) {
            program.addError(new AssemblyError(">>ERROR<< REQUIRED INTEGER, FOUND NON-INTEGER VALUE", statement, lineCount));
        }
    }

    /**
     * Checks if the statement is a memory statement by using it's length Memory
     * statements must have 3 parts to them.
     */
    private boolean isMemoryMnemonic(String[] statement)
    {
        if (statement.length == 3) {
            return statement[1].equals("BSS") || statement[1].equals("BSC");
        }
        return false;
    }

    /**
     * Check to see if the statement is EQU.
     */
    private boolean isEQU(String[] statement)
    {
        if (statement.length == 3) {
            return statement[1].equals("EQU");
        }
        return false;
    }

    /**
     * Process the EQU statement.
     */
    public void processEQU(String[] statement)
    {
        String newRef = statement[0];   // The new reference.
        String mainRef = statement[2];  // The old reference.

        // Find the old reference in the symbol table.
        if (program.getSymbolTable().containsSymbol(mainRef)) {
            // Get its address.
            String address = program.getSymbolTable().getSymbolAddress(mainRef);
            // Due to the way this is programmed, the new reference will already
            // be in the symbol table with an incorrect address, so we remove it.
            program.getSymbolTable().removeEntry(newRef);
            // Add it again, with the new correct address.
            program.getSymbolTable().addEntry(newRef, address);
            locationCounter++;
        } else {
            program.addError(new AssemblyError(">>ERROR<< ERROR WITH THE EQU STATEMENT. CHECK THE LABELS BEING USED", statement, lineCount));
        }
    }

    /**
     * Process any labels.
     */
    private void processLabel(String[] statement)
    {
        // Labels are always the first statement.
        String token = statement[0];

        // Set the starting label.
        if (token.equalsIgnoreCase("BEGIN") || token.equalsIgnoreCase("START")) {
            startStatement = token;
        }

        // If the label is not in the symbol table. Add it.
        if (!program.getSymbolTable().containsSymbol(token)) {
            program.getSymbolTable().addEntry(token, getLocationCounterHexValue());
        } else {
            // Multiple labels of the same name found.
            program.addError(new AssemblyError(">>ERROR<< MULTIPLE DEFINED LABEL FOUND", statement, lineCount));
        }
    }

    /**
     * Check to see if there is a label. Statements with labels are at least 2
     * entries wide.
     */
    private boolean hasLabel(String[] statement)
    {
        if (statement.length == 1) {
            return false;
        }

        if (statement.length == 2) {
            String token = statement[0];
            return !set.isInstruction(token);
        }

        if (statement.length == 3) {
            return true;
        }

        program.addError(new AssemblyError(">>ERROR<< ILLEGAL SPACES FOUND. SPACES MAY NOT APPEAR WITHIN A FIELD", statement, lineCount));
        return false;
    }

    /**
     * Check to see if the statement is END
     */
    private boolean isEnd(String[] statement)
    {
        if (statement[0].equals("END")) {
            program.setEndLocation(locationCounter);
            return true;
        }
        return false;
    }

    /**
     * Process the ORG statement.
     */
    private void processOrg(String[] statement)
    {
        try {
            if (validOrgStatement(statement)) {
                locationCounter = Integer.parseInt(statement[1]);
            }
        } catch (NumberFormatException nfe) {
            program.addError(new AssemblyError(">>ERROR<< REQUIRED INTEGER, FOUND NON-INTEGER VALUE", statement, lineCount));
        }
    }

    /**
     * Check to see if the ORG statement is valid.
     */
    private boolean validOrgStatement(String[] statement)
    {
        if (lineCount == 0) {
            if (Integer.parseInt(statement[1]) > 255) {
                program.addError(new AssemblyError(">>ERROR<< NOT ENOUGH MEMORY", statement, lineCount));
                return false;
            } else 
                return true;
        }
     
        int value = Integer.parseInt(statement[1]);

        if (value >= 255) {
            program.addError(new AssemblyError(">>ERROR<< NOT ENOUGH MEMORY", statement, lineCount));
        }

        if (isOrg(statement) && statement.length == 2) {
            return true;
        }

        return false;
    }

    /**
     * Checks to see if the statement contains ORG.
     */
    private boolean isOrg(String[] statement)
    {
        if (statement.length == 2) {
            return statement[0].equals("ORG");
        }

        return false;
    }

    /**
     * Used to split statements and remove whitespace simultaneously
     */
    private String[] splitStatement(String line)
    {
        line = line.trim();
        return line.split("\\s+");
    }

    // Returns the locationCounter.
    public int getLocationCounter()
    {
        return locationCounter;
    }

    // Returns the locatioNCoutner in hex.
    private String getLocationCounterHexValue()
    {
        return Integer.toHexString(locationCounter);
    }
}
