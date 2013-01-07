package EmulatorProcess;

import GUI.ASCView;
import Instructions.Instruction;
import Instructions.InstructionManager;
import Instructions.InstructionSet;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Emulator
{

    private static ASCView view;
    private String[] codeList;
    private static int programCounter;
    private static int tracker;
    private static int tracker2;
    private static int tolerance;
    private int programStart;
    private String currentCode = "";
    private boolean ran;
    private int counter;
    private String[] programText;
    private boolean terminated;

    /**
     * Initialize variables
     */
    public Emulator(ASCView view, String[] codeList)
    {
        terminated = false;
        this.view = view;
        this.codeList = codeList;
        counter = 0;
        programText = view.getProgram();
        programStart = Integer.parseInt(programText[0].trim().split("\\s+")[1]);
        tracker = 0;
        tracker2 = 0;
        // Assigns the tracker - This is all for highlighting purposes...
        for (int i = 0; i < programText.length; i++) {
            String s = programText[i].trim();
            if (!s.startsWith("ORG")) {
                if ((s.startsWith("BEGIN") || s.startsWith("START"))) {
                    break;
                }
                tracker++;
            }
        }
        
        String[] statement;
        tracker2 = 0;
        for (int i = 0; i < programText.length; i++) {
            if ((programText[i].trim().startsWith("BEGIN") || programText[i].trim().startsWith("START"))) {
                break;
            }
            
            statement = programText[i].split("\\s+");
            if (statement.length > 1) {
                if (statement[1].equals("BSC")) {
                    String[] values = statement[2].split(",");
                    for (int j = 0; j < values.length; j++) {
                        tracker2++;
                    }
                } else if (statement[1].equals("BSS")) {
                    tracker2 +=  Integer.parseInt(statement[2]);
                }
            }
        }
        
        
    }

    /**
     * Set if the program has run.
     */
    public void setProgramStatus(boolean c)
    {
        ran = false;
    }

    public boolean terminated()
    {
        return terminated;
    }

    /**
     * Get the status of the program.
     */
    public boolean getProgramStatus()
    {
        return ran;
    }

    /**
     * Emulate
     */
    public void emulate()
    {
        ran = false;            // tracks if the whole program ran
        boolean good = true;    // tracks if each step was successful

        // Set the next piece of code to be executed.
        String code = (String) view.getMemoryTable().getValueAt(programCounter, 0);

        // Start executing the program.
        int runTracker = 0;
        while (!ran) {
            if (runTracker > 10000) {

                int n = JOptionPane.showConfirmDialog(
                        null,
                        "Program appears to be taking a while to execute (or it may be"
                        + "\nstuck in an infinite loop). Would you like to terminate?",
                        "Infinite Loop detected",
                        JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    terminated = true;
                    return;
                }

                runTracker = 0;
            }

            // If we reach "0000", then it's a HLT statement, end execution.
            if (code.equalsIgnoreCase("0000")) {
                ran = true;
                currentCode = code;
                highlightCode();
                return;
            }

            // While the program hasn't reached the end (just a safety measure, not really needed)
            // Do this.. programCounter != programEnd


            good = executeNextStep(code);   // Execute the code
            setRegisters();                 // Set the status registers accordingly
            counter++;                      // Increment counter

            if (code.startsWith("1")) {
                if (findNegative(code.substring(0, 4))) {
                    view.setNegBit(true);
                }
            }

            // Add an error if something went wrong and return.
            if (!good) {
                EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WHEN EMULATING", 0));
                return;
            }

            // Set the next piece of code.
            code = (String) view.getMemoryTable().getValueAt(programCounter, 0);

            // Used when there's an ORG statement in the middle of the program.
            if (code.equals("") && !code.equals("0000")) {
                getBearing();
                code = (String) view.getMemoryTable().getValueAt(programCounter, 0);
            }

            runTracker++;
        }
    }

    /**
     * Similar to above except it only executes 1 step.
     */
    public void emulateStep()
    {
        String code = (String) view.getMemoryTable().getValueAt(programCounter, 0);

        if (code.equals("") && !code.equals("0000")) {
            getBearing();
            code = (String) view.getMemoryTable().getValueAt(programCounter, 0);
        }

        if (!code.equalsIgnoreCase("0000")) {
            boolean good = executeNextStep(code);
            setRegisters();
            counter++;

            if (code.startsWith("1")) {
                if (findNegative(code.substring(0, 4))) {
                    view.setNegBit(true);
                }
            }

            if (!good) {
                EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WHEN EMULATING", 0));
                return;
            }
        } else {
            executeNextStep(code);
            ran = true;
        }
    }

    /**
     * Executes the steps and adjusts the baseMode being displayed.
     */
    private boolean executeNextStep(String code)
    {
        boolean changed = false;

        if (ASCView.getBaseMode()) {
            view.setHex(); // Grossly ineffient to call this each time. It was
            // an easy, quick way out or having to fix a lot of code.
            changed = true;
        }

        boolean buffer = execute(code);

        if (changed) {
            view.setDec();
        }

        return buffer;
    }

    /**
     * Actual method that (kinda) executes the code.
     */
    private boolean execute(String code)
    {
        currentCode = code;

        try {
            // Due to something I implemented later in development, this is not
            // really needed anymore. 
            if (code.equals("dddd")) {
                incrementProgramCounter();
                return true;
            }

            highlightCode();                            // Highlight the current code.
            String hex = code.substring(0, 1);          // Get the instruction opcode.
            InstructionSet iS = new InstructionSet(1);  // Create a new instruction set.
            String name = iS.getInstructionFromHex(hex);// Get the name of the instruction.

            // Create an instance of the correct instruction.
            Instruction instruction = InstructionManager.getInstruction(hex);

            // Run the code.
            return instruction.execute(code);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Increments the program counter.
     */
    public static void incrementProgramCounter()
    {
        programCounter += 1;
        tracker += 1;
        tracker2 += 1;
        view.setProgramCounter(formatText(Integer.toHexString(programCounter)));
    }

    /**
     * Formats text to be of length 4.
     */
    private static String formatText(String text)
    {
        String result;

        if (text.length() == 1) {
            result = "000" + text;
        } else if (text.length() == 2) {
            result = "00" + text;
        } else if (text.length() == 3) {
            result = "0" + text;
        } else {
            result = text;
        }

        return result.toUpperCase();
    }

    /**
     * Sets the program counter to a new location.
     */
    public static void setNewLocation(int newLocation)
    {
        int difference = tracker;
        programCounter = newLocation;
        tracker = newLocation - tolerance;
        
        difference = difference - tracker;
        tracker2 -= difference;
        view.setProgramCounter(formatText(Integer.toHexString(programCounter)));
    }

    /**
     * Sets the start of the program.
     */
    public void setProgramStart(int programStart)
    {
        programCounter = programStart;
        tolerance = programCounter - tracker;
        view.setProgramCounter(formatText(Integer.toHexString(programCounter)));
    }

    /**
     * Highlights the code in all the tables and lists.
     */
    public void highlightCode()
    {

        currentCode = (String) ASCView.getMemoryTable().getValueAt(programCounter, 0);

        ASCView.getMnemonicPane().clearSelection();
        ASCView.getMnemonicPane().setSelectedIndex(tracker);
        
        ASCView.getHexPane().clearSelection();
        ASCView.getHexPane().setSelectedIndex(tracker2);

        

        /**
         * This piece of code makes sure the correct value is highlighted when
         * the instruction uses indexed addressing.
         */
        int numIndex = 0;
        int valueOfIndex = 0;
        try {
            if (Integer.parseInt(currentCode.substring(0, 1), 16) >= 1 && Integer.parseInt(currentCode.substring(0, 1), 16) <= 3) {
                numIndex = Integer.parseInt(currentCode.substring(1, 2), 16);
                if (numIndex == 1) {
                    valueOfIndex = Integer.parseInt(ASCView.getIndex1(), 16);
                } else if (numIndex == 2) {
                    valueOfIndex = Integer.parseInt(ASCView.getIndex2(), 16);
                } else if (numIndex == 3) {
                    valueOfIndex = Integer.parseInt(ASCView.getIndex3(), 16);
                }
            }
        } catch (Exception e) {
        }

        // Makes sure correct code is highlighted when indirect addressing is used.
        int next = 0;
        int location;
        if (currentCode.substring(2).contains("*")) {
            location = Integer.parseInt(currentCode.substring(2, 4), 16);
            next = Integer.parseInt((String) ASCView.getMemoryTable().getValueAt(location, 0), 16) + valueOfIndex;
        } else {
            next = Integer.parseInt(currentCode.substring(2), 16) + valueOfIndex;
        }

        if (next == 0) {
            ASCView.getMemoryTable().clearSelection();
            return;
        }

        ASCView.getMemoryTable().clearSelection();
        ASCView.getMemoryTable().addColumnSelectionInterval(0, 0);
        ASCView.getMemoryTable().addRowSelectionInterval(next, next);
        ASCView.getMemoryTable().scrollRectToVisible(new Rectangle(ASCView.getMemoryTable().getCellRect(next, 0, true)));
    }

    /**
     * Sets the status registers.
     */
    public void setRegisters()
    {
        String accField = ASCView.getAccField();

        int value = Integer.parseInt(accField, 16);

        if (value == 0) {
            ASCView.setZerBit(true);
        } else {
            ASCView.setZerBit(false);
        }
    }

    /**
     * Sets the new location for the program counter.
     */
    public void getBearing()
    {
        String[] newSpot = codeList[counter + 1].split("\\s+");
        setNewLocation(Integer.parseInt(newSpot[1]));
    }

    public boolean findNegative(String code)
    {
        try {
            if (programText[counter].contains("=")) {
                String num = programText[counter].substring(programText[counter].indexOf("=") + 1);
                if (num.startsWith("-")) {
                    return true;
                }
            }
        } catch (Exception e) {
        }

        boolean loc = true;

        try {
            loc = locationOfCode(codeList[Integer.parseInt(code.substring(2), 16) - programStart]);
        } catch (Exception e) {
        }

        int beginVal = 0;

        if (loc) {
            for (int i = 0; i < codeList.length; i++) {
                if (codeList[i].equals("0000")) {
                    beginVal = i - 1;
                    break;
                }
            }
        }

        int regiValue = Integer.parseInt(code.substring(1, 2));
        if (regiValue == 1) {
            regiValue = Integer.parseInt(view.getIndex1(), 16);
        } else if (regiValue == 2) {
            regiValue = Integer.parseInt(view.getIndex2(), 16);
        } else if (regiValue == 3) {
            regiValue = Integer.parseInt(view.getIndex3(), 16);
        }

        int position = 0;
        if (loc) {
            position = Integer.parseInt(code.substring(2), 16) - 1 + regiValue;
        } else {
            position = Integer.parseInt(code.substring(2), 16) + regiValue;
        }

        ArrayList<String> splitUp = new ArrayList<String>();
        String[] statement;
        String[] values;

        for (int i = 0; i < programText.length; i++) {
            statement = programText[i].split("\\s+");

            if (statement.length > 1) {
                if (statement[1].equals("BSC")) {
                    values = statement[2].split(",");

                    for (int j = 0; j < values.length; j++) {
                        splitUp.add(values[j]);
                    }
                } else if (statement[1].equals("BSS")) {
                    for (int j = 0; j < Integer.parseInt(statement[2]); j++) {
                        splitUp.add("NULL");
                    }
                }
            }
        }

        for (int i = 0; i < splitUp.size(); i++) {
            if (i + beginVal == position) {
                if (splitUp.get(i).startsWith("-")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds out if a person stores data before the program or after the hlt.
     */
    private boolean locationOfCode(String code)
    {
        boolean afterHlt = false;

        for (int i = 0; i < codeList.length; i++) {
            if (codeList[i].equalsIgnoreCase("0000")) {
                afterHlt = true;
            }

            if (afterHlt && codeList[i].equals(code)) {
                return true;
            } else if (!afterHlt && codeList[i].equals(code)) {
                return false;
            }
        }
        return true;
    }
}
