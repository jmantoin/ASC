/**
 *  @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;

/**
 *
 * @author Adam
 */
public class RWDInstruction extends Instruction
{
    @Override
    public boolean execute(String code)
    {
        try {
            String value = "";
            
            // Calls the newValue method in the ASCView class to get the new
            // value.
            do {
                value = ASCView.newValue();

                if (value.equals("NULL")) {
                    EmulatorControl.addError(new EmulatorError(">>ERROR<< INPUT WAS CANCELED.", 0));
                    return false;
                }
                
            } while (value.equalsIgnoreCase("ERROR"));

            // Sets the input field to the new value.
            ASCView.setInputField(formatText(value));
            value = ASCView.getInputField();
            // Sets the acc to the new value.
            ASCView.setAccumulator(formatText(value));
            
            Emulator.incrementProgramCounter();
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE RWD OPERATION.", 0));
        }
        return false;
    }
}
