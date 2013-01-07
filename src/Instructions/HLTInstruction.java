/**
 *  @author Adam
 */
package Instructions;

    /**
     *  Does nothing, just returns true. 
     */
public class HLTInstruction extends Instruction
{
    @Override
    public boolean execute(String code)
    {
        return true;
    }
}
