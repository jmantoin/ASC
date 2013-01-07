/**
 *  @author Adam 
 */

package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Ref extends JPanel implements ListSelectionListener
{
    private JTextArea picture;  // The textarea (odd name!)
    private JList list;
    private JSplitPane splitPane;
    private String[] instructionList = {"HLT", "LDA", "STA", "ADD", "TCA", "BRU",
        "BIP", "BIN", "RWD", "WWD", "SHL", "SHR", "LDX", "STX", "TIX", "TDX"};

    public Ref()
    {
        list = new JList(instructionList);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);

        JScrollPane listScrollPane = new JScrollPane(list);
        picture = new JTextArea();
        picture.setFont(new Font("Arial", 0, 12));
        picture.setFont(picture.getFont().deriveFont(Font.ITALIC));

        JScrollPane pictureScrollPane = new JScrollPane(picture);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                listScrollPane, pictureScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        // Setting dimnesions.
        Dimension minimumSize = new Dimension(100, 80);
        Dimension maximumSize = new Dimension(100, 80);
        listScrollPane.setMinimumSize(minimumSize);
        listScrollPane.setMaximumSize(maximumSize);
        pictureScrollPane.setMinimumSize(minimumSize);
        pictureScrollPane.setMaximumSize(maximumSize);
        splitPane.setPreferredSize(new Dimension(410, 250));
        
        
        
        updateLabel(instructionList[list.getSelectedIndex()]);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Listens to the list
    public void valueChanged(ListSelectionEvent e)
    {
        JList list = (JList) e.getSource();
        updateLabel(instructionList[list.getSelectedIndex()]);
    }

    /**
     *  Sets the text area to display information on the currently selected
     *  instruction.
     */
    protected void updateLabel(String name)
    {
        if (name.equals("HLT")) {
            picture.setText("Name: HLT\n\nOpcode: 0\n\nDefinition: Indicates the logical end of the \nprogram."
                    + " Machine will not fetch any \ninstructions after this point. ");
        } else if (name.equals("LDA")) {
            picture.setText("Name: LDA\n\nOpcode: 1\n\nDefinition: LDA loads the ACC with the"
                    + "\n contents of the memory location (MEM) "
                    + "\nspecified. Contents of the MEM are not "
                    + "\nchanged, but the contents of the ACC "
                    + "\nbefore the execution of this instruction"
                    + "\n are replaced by the contents of MEM."
                    + "\n\nREF: LDA MEM ACC <-- M[MEM].");
        } else if (name.equals("STA")) {
            picture.setText("Name: STA\n\nOpcode: 2\n\nDefinition: Stores the accumulators value "
                    + "\nin the specified memory location"
                    + "\n\nREF: REF: STA MEM M[MEM] <-- ACC");
        } else if (name.equals("ADD")) {
            picture.setText("Name: ADD\n\nOpcode: 3\n\nDefinition: Adds the "
                    + "\ncontents of the memory location "
                    + "\nspecified  to the conttents of the "
                    + "\naccumulator. memory contents are "
                    + "\nnot altered"
                    + "\n\n REF: ADD MEM ACC <-- ACC + M[MEM]");
        } else if (name.equals("TCA")) {
            picture.setText("Name: TCA\n\nOpcode: 4\n\nDefinition: Complements each bit of the "
                    + "\naccumulators value to produce the "
                    + "\n1's complement and then adds a 1 "
                    + "\nto produce the 2's complement."
                    + "\n\nREF: REF: TCA ACC <-- ACC' + 1");
        } else if (name.equals("BRU")) {
            picture.setText("Name: BRU\n\nOpcode: 5\n\nDefinition: Branches unconditionally to "
                    + "\nthe specified address"
                    + "\n\nREF: REF: BRU MEM PC <-- MEM");
        } else if (name.equals("BIP")) {
            picture.setText("Name: BIP\n\nOpcode: 6\n\nDefinition: Branches if accumulator value "
                    + "\nis positive."
                    + "\n\nREF: BIN MEM IF ACC > 0 THEN PC <-- MEM");
        } else if (name.equals("BIN")) {
            picture.setText("Name: BIN\n\nOpcode: 7\n\nDefinition: Branches if accumulator value "
                    + "\nis negative."
                    + "\n\nREF: BIN MEM IF ACC < 0 THEN PC <-- MEM");
        } else if (name.equals("RWD")) {
            picture.setText("Name: RWD\n\nOpcode: 8\n\nDefinition: Reads a 16 bit word from the "
                    + "\ninput into the accumulator."
                    + "\n\nREF: RWD ACC <-- Input data");
        } else if (name.equals("WWD")) {
            picture.setText("Name: WWD\n\nOpcode: 9\n\nDefinition: Writes a 16 bit word from the "
                    + "\nACC onto the output device."
                    + "\n\nREF: WWD Output <-- ACC");
        } else if (name.equals("SHL")) {
            picture.setText("Name: SHL\n\nOpcode: A\n\nDefinition: Shifts the accumulators value "
                    + "\none bit to the left and fills in "
                    + "\na 0 for the least signifigant bit."
                    + "\n\nREF: REF: SHL ACC(15-1) <-- ACC(14-0)"
                    + "\n, ACC(0) <-- 0");
        } else if (name.equals("SHR")) {
            picture.setText("Name: SHR\n\nOpcode: B\n\nDefinition: Shifts the accumulators value "
                    + "\none bit to the right, MSB stays "
                    + "\nunchanged, but the LSB is lost"
                    + "\n\nREF: SHR ACC(14-0) <-- ACC(15-1)"
                    + "\n, ACC(15) <-- ACC(15)");
        } else if (name.equals("LDX")) {
            picture.setText("Name: LDX\n\nOpcode: C\n\nDefinition: Loads the specified index "
                    + "\nwith the specified memory location."
                    + "\n\nREF: LDX MEM,INDEX INDEX <-- M[MEM]");
        } else if (name.equals("STX")) {
            picture.setText("Name: STX\n\nOpcode: D\n\nDefinition: Stores the index value in "
                    + "\nthe specified memory location."
                    + "\n\nREF: STX MEM,INDEX M[MEM] <-- INDEX");
        } else if (name.equals("TIX")) {
            picture.setText("Name: TIX\n\nOpcode: E\n\nDefinition: Increments the specified "
                    + "\nindex. Then tests the value of "
                    + "\nthe index to to see if it is 0. "
                    + "\nIf 0, branch to specified memory "
                    + "\nlocation, otherwise proceed with "
                    + "\nthe next command."
                    + "\n\nREF: TIX MEM,INDEX INDEX <-- INDEX "
                    + "\n+ 1, IF INDEX = 0, THEN PC <-- MEM");
        } else if (name.equals("TDX")) {
            picture.setText("Name: TDX\n\nOpcode: F\n\nDefinition: Decrements the specified "
                    + "\nindex. Then tests the value of the "
                    + "\nindex to to see if it is 0. If 0, "
                    + "\nbranch to specified memory location, "
                    + "\notherwise proceed with the next command."
                    + "\n\nREF: TDX MEM,INDEX INDEX <-- INDEX "
                    + "\n- 1, IF INDEX != 0, THEN PC <-- MEM");
        }

    }

    public JSplitPane getSplitPane()
    {
        return splitPane;
    }

    /**
     *  "main method". Gets called by the interface, displays the class.
     */
    public static void createAndShowGUI()
    {
        JFrame frame = new JFrame("Code Reference");
        Ref splitPaneDemo = new Ref();
        frame.getContentPane().add(splitPaneDemo.getSplitPane());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = frame.getSize();
        screenSize.height = screenSize.height / 4;
        screenSize.width = screenSize.width / 4;
        size.height = size.height / 4;
        size.width = size.width / 4;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        frame.setLocation(x, y);
        
        frame.pack();
        frame.setVisible(true);
    }
}
