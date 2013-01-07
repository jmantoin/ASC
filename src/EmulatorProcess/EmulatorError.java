package EmulatorProcess;

public class EmulatorError
{
    private String errorMessage;
    private int lineNumber;
    
    public EmulatorError(String errorMessage, int lineNumber)
    {
        this.errorMessage = errorMessage;
        this.lineNumber = lineNumber;
    }
    
    public String getErrors()
    {
        String report = "";
        
        report += errorMessage;
        
        return report;
    }
}
