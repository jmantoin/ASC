/**
 *  @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;
import java.math.BigInteger;

public class SHLInstruction extends Instruction
{
    public boolean execute(String code)
    {
        try {
            seperateCode(code);
            
            // Get the acc
            String acc = ASCView.getAccField();
            
            // Use the big integer class to shift left.
            BigInteger big = new BigInteger(acc, 16);
            big = big.shiftLeft(1);
            
            // Set the acc to the new value.
            ASCView.setAccumulator(formatText(big.toString(16)));
            
        } catch (Exception e) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE SHL OPERATION.", 0));
            return false;
        }
        
        Emulator.incrementProgramCounter();
        return true;
    }
}
