/**
 * @author Adam
 */
package Program;

// BEGINING OF IMPORTS
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import javax.swing.*;
// END OF IMPORTS

public class OBJLST extends JFrame
{

    private JTextArea textBox;
    private JScrollPane container;
    private String text;
    private JToolBar toolBar;
    private JButton save;
    private JFileChooser fc;

    /**
     * Constructor, takes in the text, and the fileChooser being used by the
     * program so when it saves, it uses the same name.
     */
    public OBJLST(String text, JFileChooser fc)
    {
        super("OBJ/LST File");
        setLayout(new BorderLayout());
        toolBar = new JToolBar();
        textBox = new JTextArea();
        container = new JScrollPane(textBox);
        save = new JButton();

        this.fc = fc;

        this.text = text;
        create();
    }

    /**
     * Creates the interface, sets the dimensions and places an action listener
     * on the save button.
     */
    public void create()
    {
        setPreferredSize(new Dimension(650, 400));
        textBox.setEditable(false);
        save = makeNavigationButton("save", "Save the OBJ/LST file.", "");
        save.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                if (fc.getSelectedFile() == null) {
                    saveProcess();
                } else {

                    String a = fc.getSelectedFile().getPath();
                    a = a.substring(0, a.indexOf("."));

                    File file = new File(a + ".LST");
                    try {
                        String s = textBox.getText();
                        FileWriter fw = new FileWriter(file);
                        fw.write(s);
                        fw.close();
                    } catch (Exception ex) {
                    }
                }
            }
        });

        toolBar.add(save);

        add(container, BorderLayout.CENTER);
        add(toolBar, BorderLayout.PAGE_START);
        textBox.setText(text);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = this.getSize();
        screenSize.height = screenSize.height / 4;
        screenSize.width = screenSize.width / 4;
        size.height = size.height / 4;
        size.width = size.width / 4;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        this.setLocation(x, y);

        pack();
        setVisible(true);
    }

    /**
     * Code from the Java tutorials, just to make a fancy save button.
     */
    protected JButton makeNavigationButton(String imageName,
            String toolTipText,
            String altText)
    {
        //Look for the image.
        String imgLocation =
                imageName
                + ".png";
        URL imageURL = OBJLST.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setToolTipText(toolTipText);

        if (imageURL != null) {                      //image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {                                     //no image found
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }

        return button;
    }

    private void saveProcess()
    {
        fc = new JFileChooser();
        fc.setDialogTitle("Save As...");
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = null;

            try {
                String a = fc.getSelectedFile().getPath();
                a = a.substring(0, a.indexOf("."));
                file = new File(a + ".LST");
            } catch (Exception exc) {
                file = new File(fc.getSelectedFile() + ".LST");
            }

            if (file == null) {
                file = new File(fc.getSelectedFile() + ".LST");
            }

            try {
                String s = textBox.getText();
                FileWriter fw = new FileWriter(file);
                fw.write(s);
                fw.close();
            } catch (Exception e) {
            }

        }
    }
}