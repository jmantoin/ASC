/**
 * @author Adam
 */
package AssembleProcess;

import Instructions.InstructionManager;
import Instructions.InstructionSet;
import Program.Program;
import Program.STE;
import java.util.ArrayList;

public class SecondPass
{

    private Program program;            // The program being assembled.
    private ArrayList<String> hexList;  // List of hex codes produced.
    private int lineCount;
    private int locationCounter;
    private InstructionManager iM;
    private InstructionSet set;

    /**
     * Constructor that initializes the variables.
     */
    public SecondPass(int locationCounter)
    {
        this.locationCounter = locationCounter;
        this.lineCount = 0;
        hexList = new ArrayList<String>();
        iM = new InstructionManager();
        set = new InstructionSet();
    }

    /**
     * Method that processes the whole program.
     */
    public void process(Program program)
    {
        this.program = program;

        // Load the program into a local variable.
        String[] pText = program.getProgram();

        // Loop through the whole program.
        for (int i = 0; i < pText.length; i++) {
            // Get the current statement.
            String[] statement = splitStatement(pText[i]);


            if (!isOrg(statement)) {
                if (!isEnd(statement)) {
                    if (!isMemoryMnemonic(statement)) {

                        // Reach here. The statement is NOT an ORG, END or memory statement.
                        // Gets the opcode of the current statement.
                        String opCode = iM.getOpCode(getMnemonic(statement));

                        boolean isIndirect = false; // Indirect indicator.

                        if (opCode != null) {
                            // Check to see if the statement is indirect.
                            isIndirect = isIndirect(getMnemonic(statement));

                            // Concatenate the opcode with the rest of the hex code
                            // by calling the processOperand method.
                            opCode = opCode + processOperand(statement);

                            // If the code is indirect, add a * to the hexcode.
                            if (isIndirect) {
                                opCode = opCode + "*";
                            }

                            // Add the code to the hex list.
                            this.hexList.add(opCode.toUpperCase());
                        } else // An error occured.
                        {
                            program.addError(new AssemblyError(">>ERROR<< UNKNOWN CHARACTER FOUND", statement, lineCount));
                        }
                    } else {
                        // Statement is a memory stateemnt, process it.
                        processMemoryMnemonic(statement);
                    }
                } else {
                    // Statement is an END statement, process and literals and finish
                    // the second pass.
                    processLiterals();
                    program.setHexCodes(hexList);
                    return;
                }
                lineCount++;
            } else {
                // Statement was ORG, adds the statement directly to the table
                // Only used for reference purposes and is removed later on.
                hexList.add(statement[0] + " " + statement[1]);
            }
        }
    }

    /**
     * Process the literals.
     */
    public void processLiterals()
    {
        String value = "";

        try {
            for (int i = 0; i < program.getSymbolTable().size(); i++) {
                STE currentVal = program.getSymbolTable().get(i);

                value = currentVal.getSymbol(); // Get the value of the current element.

                // If it's not a literal, continue.
                if (value.indexOf("=") == -1) {
                    continue;
                }

                // Get rid of the equals sign and parse the integer.

                if (value.contains("#")) {
                    String base = value.substring(value.indexOf("#") + 1, value.indexOf("#") + 2);
                    int b = 16;
                    if (base.equalsIgnoreCase("H")) {
                        b = 16;
                    } else if (base.equalsIgnoreCase("B")) {
                        b = 2;
                    } else {
                        b = 8;
                    }
                    
                    if (!(base.equalsIgnoreCase("H") || base.equalsIgnoreCase("B") || base.equalsIgnoreCase("O"))) {
                        String[] statement = new String[1];
                        statement[0] = value;
                        program.addError(new AssemblyError(">>ERROR<< INVALID LITERAL BASE VALUE", statement, lineCount));
                    }
                    
                        int num = Integer.parseInt(value.substring(value.indexOf("#") + 2), b);
                        value = Integer.toHexString(num).trim();
                        value = formatKey(value);
                } else {
                    value = Integer.toHexString(Integer.parseInt(value.substring(value.indexOf("=") + 1).trim()));
                    value = formatKey(value);
                }

                // If the value is negative, negative trailing F's to make it off
                // length 4.
                
                if (value.startsWith("F") && value.length() > 4) {
                    String newKey = "";

                    for (int j = 4; j > 0; j--) {
                        newKey = newKey + String.valueOf(value.charAt(value.length() - j));
                    }

                    value = newKey;
                }
                
                // Add the literal to the hex list.
                hexList.add(value);
            }
            // Catch any exceptions, which means an invalid literal.
        } catch (Exception e) {
            String[] statement = new String[1];
            statement[0] = value;
            program.addError(new AssemblyError(">>ERROR<< INVALID LITERAL VALUE", statement, lineCount));
        }
    }

    /**
     * Processes any memory mnemonics.
     */
    public void processMemoryMnemonic(String[] statement)
    {
        try {
            // Add "dddd" for the correct amount of times to the hex list if
            // the statement is BSS.
            if (statement[1].equals("BSS")) {
                int space = Integer.parseInt(statement[2]);

                for (int i = 0; i < space; i++) {
                    hexList.add("dddd");
                }

            } else {
                // Else, it's a BSC statemnet, split the statement by commas.
                String[] values = statement[2].split(",");
                String hexValue;

                // For each value...
                for (int i = 0; i < values.length; i++) {
                    // Parse it.
                    values[i] = formatKey(Integer.toHexString(Integer.parseInt(values[i])));

                    // If it's negative, remove trailing F's
                    if (values[i].startsWith("F") && values[i].length() > 4) {
                        String newKey = "";

                        for (int j = 4; j > 0; j--) {
                            newKey = newKey + String.valueOf(values[i].charAt(values[i].length() - j));
                        }

                        hexValue = newKey;
                    } else {
                        hexValue = formatKey((values[i]));
                    }

                    // Add the value to the hex list.
                    hexList.add(hexValue);
                }
            }
        } catch (Exception e) {
            program.addError(new AssemblyError(">>ERROR<< INVALID SYNTAX", statement, lineCount));
        }
    }

    /**
     * Process the operand.
     */
    public String processOperand(String[] statement)
    {
        if (statement.length == 1) {
            // Zero-address statement.
            return "000";
        } else if (statement.length == 2 || statement.length == 3) {
            // Get the instruction from the statement.
            String firstToken;
            if (statement.length == 2) {
                firstToken = statement[0];
            } else if (statement.length == 3) {
                firstToken = statement[1];
            } else {
                program.addError(new AssemblyError(">>ERROR<< INVALID SYNTAX", statement, lineCount));
                return null;
            }

            // Check if the instruction is valid.
            if (set.isInstruction(firstToken)) {
                String operand;

                // Get the operand from the statement.
                if (statement.length == 2) {
                    operand = statement[1];
                } else {
                    if (statement.length == 3) {
                        operand = statement[2];
                    } else {
                        program.addError(new AssemblyError(">>ERROR<< INVALID SYNTAX", statement, lineCount));
                        return null;
                    }
                }

                // Check to see if numbers are being reference directly... Not supported in ASC (Can be done easily! I think...)
                if ((operand.charAt(0) >= '0' && operand.charAt(0) <= '9') || operand.charAt(0) == '-') {
                    program.addError(new AssemblyError(">>ERROR<< DIRECT ADDRESSING IS NOT SUPPORTED", statement, lineCount));
                }

                // If the operand is in the symbol table, get the address and return it.
                if (program.getSymbolTable().containsSymbol(operand)) {
                    return "0" + program.getSymbolTable().getSymbolAddress(operand);
                    // Else, if there is =, OR comma.
                } else if (operand.indexOf("=") != -1 || operand.indexOf(",") != -1) {
                    // If there is = AND comma
                    if (operand.indexOf("=") != -1 && operand.indexOf(",") != -1) {
                        // Split by the comma.
                        String[] newOp = operand.split(",");

                        // Get the literal.
                        if (newOp.length == 2) {
                            String literal = newOp[0];

                            // If it's not in the symbol table, add it.
                            if (!program.getSymbolTable().containsSymbol(literal)) {
                                program.getSymbolTable().addEntry(literal, Integer.toHexString(locationCounter));
                            }
                            // Get the register being referenced.
                            String register = newOp[1];

                            try {
                                // Check if it's a valid register.
                                if (isValidRegister(register)) {
                                    // Parse it.
                                    int registerRef = Integer.parseInt(register);

                                    // Increment the location counter and return the register + address.
                                    locationCounter += 1;
                                    return Integer.toHexString(registerRef) + program.getSymbolTable().getSymbolAddress(literal);
                                }
                                // Error time!
                                program.addError(new AssemblyError(">>ERROR<< INVALID REGISTER REFERENCE, GIVEN REGISTER DOES NOT EXIST", statement, lineCount));
                            } catch (NumberFormatException nfe) {
                                program.addError(new AssemblyError(">>ERROR<< INVALID SYNTAX", statement, lineCount));

                                return null;
                            }
                        } else {
                            program.addError(new AssemblyError(">>ERROR<< UNKNOWN CHARACTER FOUND", statement, lineCount));
                        }
                    } else {
                        // If there is no comma but there is an = sign.
                        if (operand.indexOf("=") != -1 && operand.indexOf(",") == -1) {
                            // Get the literal.
                            String literal = operand;

                            // If it's not in the symbol table, add it.
                            if (!program.getSymbolTable().containsSymbol(literal)) {
                                program.getSymbolTable().addEntry(literal, Integer.toHexString(locationCounter));
                                locationCounter += 1;

                                return "0" + program.getSymbolTable().getSymbolAddress(literal);
                            }

                            program.addError(new AssemblyError(">>ERROR<< MULTIPLE DEFINED LABEL", statement, lineCount));

                            return null;
                            // Else, if there is no = but there is a comma.
                        } else if (operand.indexOf("=") == -1 && operand.indexOf(",") != -1) {
                            // Split between the comma.
                            String[] newOp = operand.split(",");

                            // Get the register and label.
                            if (newOp.length == 2) {
                                String label = newOp[0];
                                String register = newOp[1];

                                // Find the address from the symbol table.
                                if (program.getSymbolTable().containsSymbol(label)) {
                                    try {
                                        // Check if it's a valid register reference.
                                        if (isValidRegister(register)) {
                                            int registerRef = Integer.parseInt(register);

                                            // Return the values.
                                            return Integer.toHexString(registerRef) + program.getSymbolTable().getSymbolAddress(label);
                                        }
                                        // Error time!
                                        program.addError(new AssemblyError(">>ERROR<< INVALID REGISTER REFERENCE, GIVEN REGISTER DOES NOT EXIST", statement, lineCount));
                                    } catch (NumberFormatException nfe) {
                                        program.addError(new AssemblyError(">>ERROR<< INVALID SYNTAX", statement, lineCount));
                                        return null;
                                    }
                                } else {
                                    // I left this blank during development... Seems to have no effect on the program. (For now at least)
                                }
                            } else {
                                program.addError(new AssemblyError(">>ERROR<< UNKNOWN CHARACTER FOUND", statement, lineCount));
                            }
                        }
                    }
                } else {
                    program.addError(new AssemblyError(">>ERROR<< LABEL NOT FOUND IN THE SYMBOL TABLE", statement, lineCount));
                }
            } else {
                return "000";
            }
        } else {
            program.addError(new AssemblyError(">>ERROR<< UNKNOWN CHARACTER FOUND", statement, lineCount));
        }
        return null;
    }

    /**
     * Checks to see if the register being referenced is valid.
     */
    public boolean isValidRegister(String register)
    {
        try {
            int ref = Integer.parseInt(register);

            return (ref >= 1 && ref <= 3);
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

    // Check to see if the statement is indirect.
    public boolean isIndirect(String mnemonic)
    {
        return mnemonic.indexOf("*") != -1;
    }

    /**
     * Gets the mnemonic from the statement.
     */
    public String getMnemonic(String[] statement)
    {
        if (statement.length == 1) {
            return statement[0];
        } else if (statement.length == 2) {
            if (set.isInstruction(statement[0])) {
                return statement[0];
            }
            return statement[1];
        } else if (statement.length == 3) {
            return statement[1];
        }

        return null;
    }

    /**
     * Check to see if the statement has a memory mnemonic in it.
     */
    public boolean isMemoryMnemonic(String[] statement)
    {
        if (statement.length == 3) {
            return statement[1].equals("BSS") || statement[1].equals("BSC");
        }

        return false;
    }

    /**
     * Check to see if the statement is an END.
     */
    public boolean isEnd(String[] statement)
    {
        return statement[0].equals("END");
    }

    /**
     * Check to see if the statement is an ORG.
     */
    public boolean isOrg(String[] statement)
    {
        if (statement.length == 2) {
            return statement[0].equals("ORG");
        }
        return false;
    }

    /**
     * Helper method that splits and removes trailing whitespace.
     */
    private String[] splitStatement(String line)
    {
        line = line.trim();
        return line.split("\\s+");
    }

    /**
     * Formats values to be of length 4 by adding additional 0's
     */
    private String formatKey(String key)
    {
        for (int i = key.length(); i < 4; i++) {
            key = "0" + key;
        }

        return key.toUpperCase();
    }

    /**
     * toString method that was used during debugging stage.
     */
    @Override
    public String toString()
    {
        String returns = "";
        for (int i = 0; i < hexList.size(); i++) {
            returns += "\n" + hexList.get(i);
        }

        return returns;
    }
}
