/**
 *  @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;

public class LDXInstruction extends Instruction
{

    @Override
    public boolean execute(String code)
    {
        seperateCode(code);

        try {

            // Finds a matching case and updates that register.
            if (register.equalsIgnoreCase("1")) {
                ASCView.setIndex1(formatText(getAddress()));
            } else if (register.equalsIgnoreCase("2")) {
                ASCView.setIndex2(formatText(getAddress()));
            } else if (register.equalsIgnoreCase("3")) {
                ASCView.setIndex3(formatText(getAddress()));
            }

            Emulator.incrementProgramCounter();
            return true;
        } catch (Exception e) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE LDX OPERATION.", 0));
        }

        return false;
    }
}
