/**
 * @author Adam
 */

package GUI;

import java.io.File;

class FileFilter extends javax.swing.filechooser.FileFilter
{
    @Override
    public boolean accept(File file)
    {
        // Allow only directories, or files with ".ASM" extension
        return file.isDirectory() || file.getAbsolutePath().endsWith(".ASM");
    }

    @Override
    public String getDescription()
    {
        return "ASM Files (*.ASM)";
    }
    
}
