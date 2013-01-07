/**
 * @author Adam
 */
package Controller;

// BEGINNING OF IMPORTS
import AssembleProcess.AssembleControl;
import AssembleProcess.AssemblyError;
import EmulatorProcess.EmulatorControl;
import GUI.ASCView;
import GUI.HexModel;
import Program.Program;
import java.util.ArrayList;
import javax.swing.JTable;
// END OF IMPORTS

public class MainControl
{

    private static ASCView view;               // Static variable for the interface
    private Program mainProgram;               // the current program             
    private AssembleControl assembleControl;   // the assembly controller
    private EmulatorControl emulatorControl;   // the emulator controller
    private boolean hasAssembled = false;

    /**
     * Initializes the main control.
     */
    public void initializeSystem()
    {
        this.assembleControl = new AssembleControl();
        this.emulatorControl = new EmulatorControl(view);
    }

    public void initiate()
    {
        view = new ASCView(this);
        initializeSystem();

        view.setVisible(true);
    }

    /**
     * Returns the original text of the program.
     */
    public String getOriginal()
    {
        return mainProgram.getOriginal();
    }
    
    public String[] getProgramText()
    {
        return mainProgram.getProgram();
    }

    /**
     * Returns the hex codes associated with the program.
     */
    public String[] getHexCodes()
    {
        return mainProgram.getHexCodes();
    }

    /**
     * Sends the program to the assembly controller to assemble the program.
     */
    public String assemble(String programText)
    {
        mainProgram = this.assembleControl.assemble(programText);

        // If it detects that memory greater than 256 is used, it expands.
        if (mainProgram.getSymbolTable().largestAddress() > 256) {
            dynamicMemory();
        }

        // Processes any errors from the assemble process.
        String errors = processErrors();

        if (errors.length() != 0) {
            mainProgram.setHasErrors(true);
        } else {
            loadProgram(mainProgram);
        }

        // Set the assemble status to true and return.
        hasAssembled = true;
        return errors;
    }

    /**
     * Returns the assembly status
     */
    public boolean getAssembleStatus()
    {
        return hasAssembled;
    }

    /**
     * Once the program as been assembled, it is loaded (Sent to the emulator
     * controller)
     */
    public void loadProgram(Program program)
    {
        emulatorControl.loadProgram(program);
    }

    /**
     * Sets the status of the program from the emulator controller.
     */
    public void setProgramStatus(boolean c)
    {
        emulatorControl.setProgramStatus(c);
    }

    /**
     * Gets the status of the program from the emulator controller.
     */
    public boolean getStatus()
    {
        return emulatorControl.getProgramStatus();
    }

    /**
     * Tells the emulator controller to emulate.
     */
    public boolean emulate()
    {
        emulatorControl.setIsStepping(false);
        emulatorControl.emulate();

        if (emulatorControl.hasErrors()) {
            return false;
        }
        return true;
    }

    /**
     * Tells the emulator controller to emulate in step mode.
     */
    public void emulateStep()
    {
        emulatorControl.emulateStep();
    }

    /**
     * Processes the errors by formatting them into lines.
     */
    public String processErrors()
    {
        String[] errors = mainProgram.getErrors();
        String errorsByLine = "";

        for (int i = 0; i < errors.length; i++) {
            errorsByLine += errors[i] + "\n";
        }
        return errorsByLine;
    }

    /**
     * Updates the mnemonic pane.
     */
    public String[] updateMnemonicPane()
    {
        String[] programCode = mainProgram.getProgram();
        String[] noOrgCodes = removeOrgs(programCode);  // Removes any ORG statements.

        String[] fixedValues = new String[noOrgCodes.length];

        for (int i = 0; i < noOrgCodes.length; i++) {
            String[] temp = noOrgCodes[i].split("\\s+");
            fixedValues[i] = "";
            
            if (temp.length == 3) {
                for (int j = 1; j < temp.length; j++) {
                    fixedValues[i] += temp[j] + " ";
                }
            } else {
                for (int j = 0; j < temp.length; j++) {
                    fixedValues[i] += temp[j] + " ";
                }
            }
        }
        return fixedValues;
    }

    /**
     * Updates the memory.
     */
    public JTable updateMemoryTable(JTable table)
    {
        JTable newTable = table;
        String[] data = mainProgram.getHexCodes();

        // Determines the starting position of the program.
        String[] org = data[0].split("\\s+");
        int start = Integer.parseInt(org[1]);

        for (int i = start, j = 1; i < table.getRowCount(); i++) {
            if (data[j].length() > 4 && data[j].charAt(4) != '*') {
                // New starting position found.
                org = data[j].split("\\s+");
                i = Integer.parseInt(org[1]) - 1;
                j++;
            } else {
                table.setValueAt(data[j], i, 0);
                j++;

                if (j == data.length) {
                    break;
                }
            }
        }
        return newTable;
    }

    /**
     * Method that removes any statements containing ORG in them unless it
     * appears on the first line.
     */
    private String[] removeOrgs(String[] list)
    {
        ArrayList<String> orgless = new ArrayList<String>();

        for (int i = 0; i < list.length; i++) {
            if (!list[i].contains("ORG")) {
                orgless.add(list[i]);
            }
        }

        String[] newList = new String[orgless.size()];
        for (int i = 0; i < orgless.size(); i++) {
            newList[i] = orgless.get(i);
        }
        return newList;
    }

    /**
     * Updates the hex pane with the hex codes.
     */
    public String[] updateHexPane()
    {
        String[] hexCodes = mainProgram.getHexCodes();
        return removeOrgs(hexCodes);
    }

    /**
     *  Returns the current program. 
     */
    public Program getProgram()
    {
        return mainProgram;
    }

    /**
     *  Method that extends the memory when needed. 
     */
    public void dynamicMemory()
    {
        int newMax = mainProgram.getSymbolTable().largestAddress() + 1;

        // If the program exceeds 2^16, then it displays an error.
        if (newMax > 65536) {
            String a = "RAN.OUT.OF.MEMORY";
            String[] statement = a.split(".");
            mainProgram.addError(new AssemblyError(">>ERROR<< NOT ENOUGH MEMORY", statement, 0));
            return;
        }

        // Creates 2 new table models with the new size.
        HexModel hm = new HexModel(newMax);
        HexModel hm2 = new HexModel(true, newMax);

        // Updates both tables in the interface.
        view.getTable().setModel(hm);
        view.getTable().validate();
        view.getTable().repaint();

        view.getMemoryTable().setModel(hm2);
        view.getMemoryTable().validate();
        view.getMemoryTable().repaint();
    }

    /**
     *  Main method for the whole program. Creates an instance of main control and 
     *  calls the initiate method to start.
     */
    public static void main(String[] args)
    {
        MainControl mc = new MainControl();
        try {
            mc.initiate();
        } catch (Exception e) {
        }
    }
    
    public boolean terminated()
    {
        return emulatorControl.terminated();
    }
}
