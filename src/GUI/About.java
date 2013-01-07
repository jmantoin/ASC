/**
 * @author Adam
 */
package GUI;

// BEGINNING IMPORT STATEMENTS.
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
// END IMPORT STATEMENTS

public class About
{

    private ImagePanel panel;

    public About()
    {
        panel = new ImagePanel(new ImageIcon("C:\\Users\\Adam\\Documents\\Programming\\NetBeansProjects\\Assembler\\src\\GUI\\about.png").getImage());
        JFrame frame = new JFrame();
        frame.getContentPane().add(panel);

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

class ImagePanel extends JPanel
{

    private Image img;

    public ImagePanel(String img)
    {
        this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img)
    {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g)
    {
        g.drawImage(img, 0, 0, null);
    }
}