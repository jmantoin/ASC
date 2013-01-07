/**
 *  @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;


public class BRUInstruction extends Instruction
{ 
    /**
     *  Branches to the address of the label. 
     */
    @Override
    public boolean execute(String code)
    {
        try {
            seperateCode(code);
            Emulator.setNewLocation(Integer.parseInt(address, 16));
            return true;
        } catch (Exception e) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE BRU OPERATION.", 0));
        }
        return false;
    }
}
