/**
 * @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;

public class STAInstruction extends Instruction
{

    @Override
    public boolean execute(String code)
    {

        int indexValue = 0;
        int pointerToAdd = 0;

        String effectiveAddress = "";
        String val = "";

        seperateCode(code);

        // Get the addressing mode.
        int addressMode = getAddressingMethod(code);

        try {

            if (isIndirect) {
                address = address.substring(0, address.indexOf("*"));
            }

            // Get the corresponding address depending on the type of mode
            if (addressMode == 0) {
                executeSTA(this.address);
            } else {
                if (addressMode == 1 || addressMode == 3) {
                    indexValue = Integer.parseInt(ASCView.getIndexValue(this.register), 16);
                    effectiveAddress = Integer.toHexString(Integer.parseInt(this.address, 16) + indexValue);
                }

                if (addressMode == 2 || addressMode == 3) {
                    pointerToAdd = Integer.parseInt(address, 16);

                    if (isIndexed) {
                        int buffer = Integer.parseInt((String) ASCView.getMemoryTable().getValueAt(pointerToAdd, 0), 16);
                        buffer += indexValue;
                        effectiveAddress = formatText(Integer.toHexString(buffer));
                    } else {
                        effectiveAddress = formatText((String) ASCView.getMemoryTable().getValueAt((pointerToAdd), 0));
                    }
                }


                if (effectiveAddress.equalsIgnoreCase("dddd")) {
                    EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE STA OPERATION.", 0));
                    return false;
                }

                // Execute with the new address
                executeSTA(effectiveAddress);
            }

        } catch (NumberFormatException nfe) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE STA OPERATION.", 0));
            return false;
        }

        Emulator.incrementProgramCounter();
        return true;
    }

    private void executeSTA(String address)
    {
        int destinationAddss = Integer.parseInt(address, 16);

        ASCView.getMemoryTable().setValueAt(ASCView.getAccField(), destinationAddss, 0);
    }
}
