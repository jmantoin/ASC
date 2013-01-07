/**
 * @author Adam
 */
package Program;

import java.util.LinkedList;

/**
 *  Stores a symbol table for the program.
 */

public class SymbolTable
{
    private LinkedList<STE> symbolTable;
    
    public SymbolTable()
    {
        symbolTable = new LinkedList<STE>();
    }
    
    public void addEntry(String symbol, String address)
    {
        if (address.length() == 1)
            address = "0" + address;
        symbolTable.add(new STE(symbol, address));
    }
    
    public void removeEntry(String symbol)
    {
        for (int i = 0; i < symbolTable.size(); i++) {
            if (symbolTable.get(i).getSymbol().equals(symbol))
                symbolTable.remove(i);
        }
    }
    
    public boolean containsSymbol(String symbol)
    {
        STE key = new STE(symbol, "null");
        
        for (int i = 0; i < symbolTable.size(); i++) 
            if (key.getSymbol().equals(symbolTable.get(i).getSymbol()))
                return true;
        return false;
        
    }
    
    /**
     *  Method used as a helper for the dynamic memory method in mainControl 
     */
    public int largestAddress()
    {
        int max = 0;
        
        for (int i = 0; i < symbolTable.size(); i++) {
            int value = Integer.parseInt(symbolTable.get(i).getAddress(), 16);
            
            if (value > max)
                max = value;
        }
        return max;
    }
    
    public int size()
    {
        return symbolTable.size();
    }
    
    public STE get(int i)
    {
        return symbolTable.get(i);
    }
    
    public String getSymbolAddress(String symbol)
    {
        STE key = new STE(symbol, "null");
        
        for (int i = 0; i < symbolTable.size(); i++) {
            if (key.getSymbol().equals(symbolTable.get(i).getSymbol()))
                return symbolTable.get(i).getAddress(); 
        }
        
        throw new NullPointerException();
    }
    
    /**
     *  Used during debugging stages. 
     */
    @Override
    public String toString()
    {
        String s = "";
        
        for (int i = 0; i < symbolTable.size(); i++) {
            s += symbolTable.get(i).getSymbol();
            s += " " + symbolTable.get(i).getAddress();
            s += "\n";
        }
        
        return s;
    }
}
