/**
 * @author Adam
 */
package Program;

/**
 *  Custom node class that stores a symbol and an address.
 */

public class STE
{
    private String symbol;
    private String address;
    
    public STE(String symbol, String address)
    {
        this.symbol = symbol;
        this.address = address;
    }
    
    public String getSymbol()
    {
        return symbol;
    }
    
    public String getAddress()
    {
        return address;
    }
}
