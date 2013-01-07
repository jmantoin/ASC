/**
 *  @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;

public class LDAInstruction extends Instruction
{

    @Override
    public boolean execute(String code)
    {
        int indexValue = 0;
        int pointerToAdd = 0;

        String effectiveAddress = "";
        String val = "";

        seperateCode(code);

        // Get addressing mode.
        int addressMode = getAddressingMethod(code);
        try {

            if (isIndirect) {
                address = address.substring(0, address.indexOf("*"));
            }

            // Find address depending on the mode
            if (addressMode == 0) {
                executeLDA(this.address);
            } else {
                if (addressMode == 1 || addressMode == 3) {
                    indexValue = Integer.parseInt(ASCView.getIndexValue(this.register), 16);
                    effectiveAddress = Integer.toHexString(Integer.parseInt(this.address, 16) + indexValue);
                }

                if (addressMode == 2 || addressMode == 3) {
                    pointerToAdd = Integer.parseInt(address, 16);
                    
                    if (isIndexed) {
                        int buffer = Integer.parseInt((String)ASCView.getMemoryTable().getValueAt(pointerToAdd, 0), 16);
                        buffer += indexValue;
                        effectiveAddress = formatText(Integer.toHexString(buffer));
                    } else {
                        effectiveAddress = formatText((String) ASCView.getMemoryTable().getValueAt((pointerToAdd), 0));
                    }
                }

                if (effectiveAddress.equalsIgnoreCase("dddd")) {
                    EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE LDA OPERATION.", 0));
                    return false;
                }

                // Execute using the address.
                executeLDA(effectiveAddress);
            }

        } catch (NumberFormatException nfe) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE LDA OPERATION.", 0));
            return false;
        }
        Emulator.incrementProgramCounter();
        return true;
    }

    private void executeLDA(String address)
    {
        // Get the value to load
        int numToLoad = Integer.parseInt(address, 16);
        
        String locationValue = (String) ASCView.getMemoryTable().getValueAt(numToLoad, 0);

        int result = Integer.parseInt(locationValue, 16);

        // Compliment if needed.
        if (result < 0) {
            ASCView.setNegBit(true);
            locationValue = twosComp(result);
            ASCView.setAccumulator(formatText(locationValue));
        } else {
            ASCView.setNegBit(false);
            String str = Integer.toHexString(result);
            ASCView.setAccumulator(formatText(str));
        }
    }
}
