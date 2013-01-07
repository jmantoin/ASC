/**
 * @author Adam
 */
package Instructions;

import java.util.HashSet;
import java.util.Set;

/**
 *  Class that stores a set of the instructions that are valid in ASC. 
 * 
 */

public class InstructionSet
{
    // The instruction set.
    private String[] setInstructions = {"HLT 0", "LDA 1", "STA 2", "ADD 3", "TCA 4", "BRU 5", "BIP 6", "BIN 7", "RWD 8", "WWD 9", "SHL A", "SHR B", "LDX C","STX D", "TIX E", "TDX F", "ORG 0", "END 0", "BSS 0", "BSC 0", "EQU 0"};
    private Set<String> set = new HashSet<String>();
    
    // Construction that removes the opcodes from each element.
    public InstructionSet()
    {
         for (int i = 0; i < setInstructions.length; i++) 
             set.add(setInstructions[i].substring(0,3));
    }
    
    public InstructionSet(int a)
    {
        // does nothing.
    }
    
    public String[] returnInstructionsSet()
    {
        return setInstructions;
    }
    
    // Returns true/false depending on if the instruction is within the set.
    public boolean isInstruction(String key)
    {
        if (key.indexOf("*") == -1)
            return set.contains(key);
        return set.contains(key.substring(0, key.indexOf("*")));
    }         
    
    // Returns an instruction from a hex code.
    public String getInstructionFromHex(String hex)
    {
        for (int i = 0; i < 15; i++) {
            if (setInstructions[i].substring(4).equals(hex))
                return setInstructions[i].substring(0,3);
        }
        return null;
    }

}
