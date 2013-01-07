/**
 * @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;
import java.math.BigInteger;

public class TCAInstruction extends Instruction
{

    @Override
    public boolean execute(String code)
    {
        seperateCode(code);

        try {
            if (ASCView.getAccField().equalsIgnoreCase("0000")) {
            } else {

                // use the big integer class to negate the value.
                BigInteger big = new BigInteger(ASCView.getAccField(), 16);
                big = big.negate();

                int num = big.intValue();

                if (ASCView.getNegBit()) {
                    ASCView.setNegBit(false);
                } else {
                    ASCView.setNegBit(true);
                }

                // Set the acc to the twos comp of the num
                ASCView.setAccumulator(formatText(twosComp(num)));
            }
        } catch (Exception e) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE TCA OPERATION.", 0));
            return false;
        }

        Emulator.incrementProgramCounter();
        return true;
    }
}
