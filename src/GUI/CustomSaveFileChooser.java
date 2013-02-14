/*
 * Based on code from http://www.coderanch.com/t/334281/GUI/java/JFileChooser-overwrite-file
 */
package GUI;

import javax.swing.JFileChooser;
import java.io.*;
import java.io.File;
import javax.swing.JOptionPane;
import java.text.MessageFormat;

public class CustomSaveFileChooser extends JFileChooser {

	public void approveSelection() {

		File f = getSelectedFile();
		String filePath = f.getPath();
		if(!filePath.toUpperCase().endsWith(".ASM")){
			f = new File(filePath + ".ASM");
		}
		if (f.exists()) {
			String msg = "The file \"{0}\" already exists!\nDo you want to replace it?";
			msg = MessageFormat.format(msg, new Object[]{f.getName()});
			String title = getDialogTitle();
			int option = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (option == JOptionPane.NO_OPTION) {
				return;
			} // end if  
		} // end if  

		super.approveSelection();

	} // end method  
	
}