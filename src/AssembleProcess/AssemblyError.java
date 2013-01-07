/**
 * @author Adam
 */
package AssembleProcess;

/**
 *  Class that creates an error associated with an assembly error. 
 */
public class AssemblyError
{
    private String errorMessage;    // The error message.
    private String[] programLine;   // The actual code that caused the error.
    private int lineNumber;         // The line it occured on (I used it earlier however removed it)
    
    public AssemblyError(String errorMessage, String[] programLine, int lineNumber)
    {
        this.errorMessage = errorMessage;
        this.programLine = programLine;
        this.lineNumber = lineNumber;
    }
    
    // Creates the error in a single line form.
    private String constructErrorLine()
    {
        String error = "";
        for (int i = 0; i < programLine.length; i++) {
            error += programLine[i] + " ";
        }
        return error;
    }
    
    /**
     *  Returns the error.
     */
    public String getErrors()
    {
        String report = "";
        String error = constructErrorLine();
        report += (errorMessage + ". REF STATEMENT: " + error + "."); 
        
        return report;
    }
}
