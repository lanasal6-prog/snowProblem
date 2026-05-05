import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This java code is the snow problem
 * A frame by the name of snowProblemFrame is created for the GUI window.
 * @param xxxxx
 * @author Lana Alsalamah
 * @return
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 **/
public class SnowProblem extends JFrame {

    static final int numberOfBoardRows = 4;
    static final int numberOfBoardColumns = 5;

    enum Kind {treeImage, largeSnowballImage, smalSnowballImage, snowmanHeadImage}
    enum Phase {
        placeFirstTree, placeSecondTree, placeLargeSnowball, placeSmallSnowball, 
        placeSnowmanHead, playStage, builtSnowman, onePieceSlidOffBoard
    }
}