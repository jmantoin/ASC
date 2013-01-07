/**
 *  @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;
import java.math.BigInteger;

public class TDXInstruction extends Instruction
{
    public int run()
    {
        String indexValue = "0";
        
        // Gets the index value.
        if (register.equalsIgnoreCase("1"))
            indexValue = ASCView.getIndex1();
        else if (register.equalsIgnoreCase("2"))
            indexValue = ASCView.getIndex2();
        else if (register.equalsIgnoreCase("3"))
            indexValue = ASCView.getIndex3();
        
        int value = 0;
        
        value = Integer.parseInt(indexValue, 16);
        
        // Decrements it.
        value -= 1;
        
        if (value < 0) {
            ASCView.setCarBit(true);
            value = 0;
        }
        
        String val = Integer.toHexString(value);
        
        if (value < 0) {
            val = twosComp(value);
        }

        // Updates the register.
        if (register.equalsIgnoreCase("1"))
            ASCView.setIndex1(formatText(val));
        else if (register.equalsIgnoreCase("2"))
            ASCView.setIndex2(formatText(val));
        else if (register.equalsIgnoreCase("3"))
            ASCView.setIndex3(formatText(val));
        
        if (value != 0)
            return 1;
        return -1;
    }
    
    @Override
    public boolean execute(String code)
    {
        seperateCode(code);
        
        try {
            int value = run();

            if (value == 1) {
                Emulator.setNewLocation(Integer.parseInt(address, 16));
                return true;
            }
            
            Emulator.incrementProgramCounter();
            return true;
        } catch (Exception e) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE TDX OPERATION.", 0));
        }
        return false;
    }
}
