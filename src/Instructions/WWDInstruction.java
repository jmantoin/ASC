/**
 *  @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;


public class WWDInstruction extends Instruction
{
    public boolean execute(String code)
    {
        try {
            // Sets the output field to the value in the acc.
            String acc = ASCView.getAccField();
            ASCView.setOutputField(formatText(acc));
            Emulator.incrementProgramCounter();
            return true;
        } catch (Exception e) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE WWD OPERATION.", 0));
        }
        return false;
    }
}
