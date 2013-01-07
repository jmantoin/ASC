/**
 * @author Adam
 */
package Instructions;

import GUI.ASCView;

/**
 *  Instruction abstract class. The base of all the instructions.
 */
public abstract class Instruction
{
    private String instructionName;    
    private String hexCode;            
    protected String register;          
    protected String address;           
    protected boolean isIndexed;
    protected boolean isIndirect;

    /**
     *  Sets the new instruction
     */
    public void setInstruction(String instructionName, String hexCode)
    {
        this.instructionName = instructionName;
        this.hexCode = hexCode;
    }

    // Execute method overriden by all instructions.
    public abstract boolean execute(String code);

    /**
     *  Splits the code into its register and address components.
     */
    protected void seperateCode(String code)
    {
        this.register = code.substring(1, 2);
        this.address = code.substring(2);
    }

    /**
     *  Gets the addressing mode of the instruction.
     */
    protected int getAddressingMethod(String code)
    {
        if (this.register.equalsIgnoreCase("0")) {
            this.isIndexed = false;
        } else {
            this.isIndexed = true;
        }

        if (code.indexOf("*") == -1) {
            this.isIndirect = false;
        } else {
            this.isIndirect = true;
        }

        if ((this.isIndirect) || (this.isIndexed)) {
            if (this.isIndexed) {
                if (this.isIndirect) {
                    return 3;
                }
                return 1;
            }
            return 2;
        }
        return 0;
    }
    
    /**
     *  Returns the address of the code.
     */
    public String getAddress()
    {
        try {
            int value = Integer.parseInt(address,16);
            String val = (String)ASCView.getMemoryTable().getValueAt(value, 0);
            
            if (val.equalsIgnoreCase("dddd"))
                val = "0000";
            
            return val;
        } catch (Exception e) {
        }
        return null;
    }
    
    /**
     *  Returns the twos comp of the parameter.
     */
    public String twosComp(int binNum)
    {
        // CHanges the number into a binary string.
        String binary = Integer.toBinaryString(binNum);
        if (binary.length() == 32)
        {
            // Parse the last 16 bits.
            String first = Integer.toHexString(Integer.parseInt(binary.substring(16,20), 2));
            String second = Integer.toHexString(Integer.parseInt(binary.substring(20,24), 2));
            String third = Integer.toHexString(Integer.parseInt(binary.substring(24,28), 2));
            String fourth = Integer.toHexString(Integer.parseInt(binary.substring(28), 2));
            
            return first + second + third + fourth;
        } else {
            // Not valid
        }
        return null;
    }

    /**
     *  Formats the text so that it is of length 4.
     */
    protected String formatText(String text)
    {
        String result;

        if (text.length() == 1) {
            result = "000" + text;
        } else if (text.length() == 2) {
            result = "00" + text;
        } else if (text.length() == 3) {
            result = "0" + text;
        } else {
            result = text;
        }

        return result.toUpperCase();
    }
    
    public int getIntValueOfTwosComp(String twosCompHexForm)
    {
        int value = Integer.parseInt(twosCompHexForm, 16);
        int base = Integer.parseInt("FFFF", 16);

        return value - base - 1;
    }
}
