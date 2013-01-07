/**
 *  @author Adam 
 */
package Instructions;

import java.util.ArrayList;

public class InstructionManager
{
    private ArrayList<IWC> set;
    
    public InstructionManager()
    {
        set = new ArrayList<IWC>(16);
        populateSet();
    }
    
    /**
     *  Populates the set with the custom nodes of the instructions. 
     */
    private void populateSet()
    {
        InstructionSet iS = new InstructionSet();
        String[] instructions = iS.returnInstructionsSet();
        
        for (int i = 0; i < instructions.length; i++) {
            String[] statement = instructions[i].split("\\s+");
            
            set.add(new IWC(statement[0], statement[1]));
        }
    }
    
    /**
     *  Returns the opcode associated with the mnemonic
     */
    public String getOpCode(String mnemonic)
    {
        String newMnemonic;
        
        if (mnemonic.indexOf("*") == -1)
            newMnemonic = mnemonic;
        else 
            newMnemonic = mnemonic.substring(0, mnemonic.indexOf("*"));
        
        for (int i = 0; i < set.size(); i++) {
            if (set.get(i).getName().equals(newMnemonic))
                return set.get(i).getOpCode();
        }
        return null;
    }
    
    /**
     * Returns a new instruction instance depending on the hex code input. 
     */
    public static Instruction getInstruction(String hex)
    {
        if (hex.equalsIgnoreCase("0"))
            return new HLTInstruction();
        else if (hex.equalsIgnoreCase("1"))
            return new LDAInstruction();
        else if (hex.equalsIgnoreCase("2"))
            return new STAInstruction();
        else if (hex.equalsIgnoreCase("3"))
            return new ADDInstruction();
        else if (hex.equalsIgnoreCase("4"))
            return new TCAInstruction();
        else if (hex.equalsIgnoreCase("5"))
            return new BRUInstruction();
        else if (hex.equalsIgnoreCase("6"))
            return new BIPInstruction();
        else if (hex.equalsIgnoreCase("7"))
            return new BINInstruction();
        else if (hex.equalsIgnoreCase("8"))
            return new RWDInstruction();
        else if (hex.equalsIgnoreCase("9"))
            return new WWDInstruction();
        else if (hex.equalsIgnoreCase("A"))
            return new SHLInstruction();
        else if (hex.equalsIgnoreCase("B"))
            return new SHRInstruction();
        else if (hex.equalsIgnoreCase("C"))
            return new LDXInstruction();
        else if (hex.equalsIgnoreCase("D"))
            return new STXInstruction();
        else if (hex.equalsIgnoreCase("E"))
            return new TIXInstruction();
        else if (hex.equalsIgnoreCase("F"))
            return new TDXInstruction();
        return new HLTInstruction();
    }
    
    
}
