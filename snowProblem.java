import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame; // Import GUI components in Swing class
import javax.swing.JLabel;
import javax.swing.JPanel; // Import JPanel class
/**
 * This java code is the snow problem
 * A frame by the name of snowProblemFrame is created for the GUI window.
 * @param xxxxx
 * @author Lana Alsalamah
 * @return
 **/

public class snowProblem {
    public static void main(String[] args) {

    JFrame snowProblemFrame = new JFrame(); // Create a clean, unpopulated top-level window that acts as the root container for the problem’s GUI. Assign the instance name snowProblemFrame
    snowProblemFrame.setVisible(true); // Make it visible
    snowProblemFrame.setTitle("Snow Problem"); // Set the title of the window to "Snow Problem"
    snowProblemFrame.setSize(1000, 1000); // Set the size of the widow GUI
    snowProblemFrame.setLocationRelativeTo(null); // Locate the widow GUI in the center of the screen
    snowProblemFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /*Execute a clean termination of the ongoing operation, 
    ensure all temporary resources are released, and subsequently close the graphical user interface to conclude the session*/
    JPanel snowProblemPanel = new JPanel(); /** Create an instance of JPanel that functions as the main workspace
    for the interface. Attach this panel to the window by making it the frame’s default content area, which will 
    give a clean canvas for adding buttons, labels, game objects, and custom drawing routines in the next steps */
    snowProblemFrame.setContentPane(snowProblemPanel); // Use panel on Window

    /*Add JPanel "snowProblemDescription" on the top and center of the frame snowProblemFrame GUI to give the player a short description of the game*/
    JLabel snowProblemDescription = new JLabel();
    snowProblemDescription.setOpaque(true); //Back the the panel opaque.
    snowProblemDescription.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));// Set the borders to 1.
    snowProblemDescription.setBackground(Color.GREEN); // Set the background color of the label to green
    snowProblemFrame.add(snowProblemDescription,BorderLayout.NORTH);// Position of the label is set to the top and center of the frame.
    /*Print the game goal on the label*/
    snowProblemDescription.setText("Can you help the snowman? Build the snowman by guiding snowballs around obstacles until they are perfectly positioned to stack");
    
    JButton b = new JButton("Press!"); 
    snowProblemPanel.add(b); 

    }
}
