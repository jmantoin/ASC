/**
 *  @author Adam
 */
package Instructions;

/**
 *  Custom node class that stores the name and opcode of an instruction.
 */

public class IWC
{
    private String name;
    private String opCode;
    
    public IWC(String name, String opCode)
    {
        this.name = name;
        this.opCode = opCode;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getOpCode()
    {
        return opCode;
    }
}
