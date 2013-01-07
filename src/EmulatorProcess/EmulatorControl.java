package EmulatorProcess;

import GUI.ASCView;
import Program.Program;
import java.util.LinkedList;


public class EmulatorControl
{
    private ASCView view;
    private Program program;
    private Emulator emulator;
    private String[] codeList;
    private boolean isStepping;
    private static LinkedList<EmulatorError> errors;
    private static boolean hasErrors = false;
    
    
    public EmulatorControl(ASCView view)
    {
        errors = new LinkedList<EmulatorError>();
        isStepping = false;
        this.view = view;
    }
    
    public boolean getProgramStatus()
    {
        return emulator.getProgramStatus();
    }
    
    public void loadProgram(Program program)
    {
        this.program = program;
        codeList = program.getHexCodes();
        this.emulator = new Emulator(view, codeList);
        initializeEmulator();
        
    }
    
    public static String returnErrors()
    {
        String error = "";
        
        for (int i = 0; i < errors.size(); i++) {
            error += errors.get(i).getErrors() + "\n";
        }
        
        return error;
    }
    
    public boolean hasErrors()
    {
        return hasErrors;
    }
    
    public static void addError(EmulatorError ee)
    {
        errors.add(ee);
        hasErrors = true;
    }
   
    public void emulate()
    {
        hasErrors = false;
        errors = new LinkedList<EmulatorError>();
        
        if (!isStepping) {
            emulator.emulate();
        } else
            emulator.emulateStep();
    }
    
    public void setProgramStatus(boolean c)
    {
        emulator.setProgramStatus(c);
    }
    
    public void emulateStep()
    {
        setIsStepping(true);
        emulate();
    }
    
    public void setIsStepping(boolean condition)
    {
        this.isStepping = condition;
    }
    
    public void initializeEmulator()
    {
        emulator.setProgramStart(program.getStart());
    }
    
    public boolean terminated()
    {
        return emulator.terminated();
    }
}
