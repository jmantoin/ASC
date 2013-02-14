/*
 * Based on code from http://www.coderanch.com/t/334281/GUI/java/JFileChooser-overwrite-file
 */
package GUI;

import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.JOptionPane;
import java.text.MessageFormat;

public class CustomOpenFileChooser extends JFileChooser {

	public void approveSelection() {

		File f = getSelectedFile();

		if (!f.exists()) {
			String msg = "The file \"{0}\" does not exist!";
			msg = MessageFormat.format(msg, new Object[]{f.getName()});
			JOptionPane.showMessageDialog(this, msg , "File Not Found", JOptionPane.WARNING_MESSAGE);
			return;
		} // end if  

		super.approveSelection();

	} // end method  
}