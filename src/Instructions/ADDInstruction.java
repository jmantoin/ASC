/**
 *  @author Adam
 */
package Instructions;

import EmulatorProcess.Emulator;
import EmulatorProcess.EmulatorControl;
import EmulatorProcess.EmulatorError;
import GUI.ASCView;

public class ADDInstruction extends Instruction
{
    @Override
    public boolean execute(String code)
    {
        int indexValue = 0;
        int pointerToAdd = 0;

        String effectiveAddress = "";
        String val = "";

        seperateCode(code);

        // Get addressing mode 
        int addressMode = getAddressingMethod(code);

        try {    
            if (isIndirect) {
                address = address.substring(0, address.indexOf("*"));
            }

            // Find address depending on the mode
            if (addressMode == 0) {
                executeADD(this.address);
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
                    EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE ADD OPERATION.", 0));
                    return false;
                }

                // Execute using the address.
                executeADD(effectiveAddress);
            }
        } catch (NumberFormatException nfe) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE ADD OPERATION.", 0));
            return false;
        }
        // Increment program counter and return.
        Emulator.incrementProgramCounter();
        return true;
    }

    private void executeADD(String address)
    {
        try {
            // Get the value to be added.
            int toAdd = Integer.parseInt(address, 16);
            // Get the value of the toAdd variable from the memory table.
            String locationValue = (String) ASCView.getMemoryTable().getValueAt(toAdd, 0);

            // Get compliment if needed
            if (locationValue.substring(0, 1).equalsIgnoreCase("F")) {
                toAdd = getIntValueOfTwosComp(locationValue);
            } else if (locationValue.equalsIgnoreCase("dddd")) {
                toAdd = Integer.parseInt("0000", 16);
            } else {
                ASCView.setNegBit(false);
                toAdd = Integer.parseInt(locationValue, 16);
            }

            // Get the acc field value.
            String accValue = ASCView.getAccField();
            int acc = 0;
            int forCarry = 0;
            // Compliment if needed.
            // accValue.substring(0, 1).equalsIgnoreCase("F")
            if (accValue.substring(0, 1).equalsIgnoreCase("F")) {
                forCarry = Integer.parseInt(accValue, 16);
                acc = getIntValueOfTwosComp(accValue);
            } else {
                acc = Integer.parseInt(accValue, 16);
            }

            int resultForCarry = toAdd + forCarry;
            int result = toAdd + acc;
            String value = "";

            // Compliment if needed.
            if (result < 0) {
                ASCView.setNegBit(true);
                value = twosComp(result);
                ASCView.setAccumulator(formatText(value));
            } else {
                ASCView.setNegBit(false);
                ASCView.setAccumulator(formatText(Integer.toHexString(result)));
            }
            
            // Set the carry bit if needed.
            if (resultForCarry >=65536)
                ASCView.setCarBit(true);
            else
                ASCView.setCarBit(false);
            
        } catch (NumberFormatException nfe) {
            EmulatorControl.addError(new EmulatorError(">>ERROR<< AN ERROR OCCURED WITH THE ADD OPERATION.", 0));
        }
    }
}
