/**
 * @author Adam
 */
package AssembleProcess;

import Program.Program;

/**
 * The AssembleControl class manages the whole assembly process by sending the
 * program through the first and second passes, as well as managing any errors.
 */
public class AssembleControl
{

    public Program assemble(String programText)
    {
        // If there is no program in the textarea, do nothing (return null)
        if (programText == null) {
            return null;
        } else if (programText.length() == 0) {
            return null;
        }

        // Load program
        Program program = new Program(programText);

        String[] text = program.getProgram();
        boolean hasHlt = false;

        // Loop that checks for HLT.
        for (int i = 0; i < text.length; i++) {
            String[] s = text[i].trim().split("\\s+");
            if (s.length == 1) {
                hasHlt = s[0].equalsIgnoreCase("HLT");
                if (hasHlt) {
                    break;
                }
            } else if (s.length == 2) {
                hasHlt = s[1].equalsIgnoreCase("HLT");
                if (hasHlt) {
                    break;
                }
            }
        }

        if (!hasHlt) {
            String[] statement = ("NO HLT").split("\\s+");
            program.addError(new AssemblyError(">>ERROR<< NO HLT STATEMENT FOUND", statement, 0));
        }

        // Checking if END statement has a BEGIN or START following it.
        if (!(text[text.length - 1].contains("BEGIN") || text[text.length - 1].contains("START"))) {
            String[] statement = text[text.length - 1].split("\\s+");
            program.addError(new AssemblyError(">>ERROR<< INVALID END STATEMENT", statement, 0));
        }

        // Run through the first pass.
        FirstPass firstPass = new FirstPass();
        firstPass.process(program);

        // Run through the second pass.
        SecondPass secondPass = new SecondPass(firstPass.getLocationCounter());
        secondPass.process(program);

        // Return the assembled program. (Also contains a lsit of any errors that
        // may have occured.
        return program;
    }
}
