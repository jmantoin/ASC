/**
 *  @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;


public class STXInstruction extends Instruction
{
    @Override
    public boolean execute(String code)
    {
        seperateCode(code);
        
        int dest = Integer.parseInt(address,16);
        
        String value;
        
        try {
            
            // Find the matching case and set the acc to the value of that index.
            if (register.equalsIgnoreCase("1"))
                value = ASCView.getIndex1();
            else if (register.equalsIgnoreCase("2"))
                value = ASCView.getIndex2();
            else if (register.equalsIgnoreCase("3"))
                value = ASCView.getIndex3();
            else
                value = null;
            
            ASCView.getMemoryTable().setValueAt(value, dest, 0);
            
        } catch (Exception e) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE STX OPERATION.", 0));
            return false;
        }
        
        Emulator.incrementProgramCounter();
        return true;
    }
}
