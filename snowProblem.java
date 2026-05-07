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
 * 
 * A 5x4 sliding puzzle style game. Place two trees, one large snowball, one small snowball, and one snowman head.
 * Slide the large snowball, small snowball, and snowman head until they form a stack
 * 
 * Rules:
 * -Snowballs slide until they hit a tree, another piece, or the edge 
 * of the board (sliding off the edge ends the game).
 * -Small snowball can only stack on the large snowball and the snowman 
 * head can only stack if the small and large snowballs are stackrd.
 * -The snowman head moves one step at a time.
 * 
 * @author Lana Alsalamah
 * */
public class SnowProblem extends JFrame {
/**Defines the grid size for the game.
 * -numberOfBoardRows: vertical height (4 cells).
 * -numberOfBoardColumns: horizontal width (5 cells). */
    static final int numberOfBoardRows = 4;
    static final int numberOfBoardColumns = 5;

    /**-Kind: recognizes what kind of game piece it is, which dictates how it looks and how it behaves with others.
     * -Phase: manages the game state as a sequential log. Tracks every step starting when pieces are first placed all the way to win or loss checks. */
    enum Kind {treeImage, largeSnowballImage, smallSnowballImage, snowmanHeadImage}
    enum Phase {
        placeFirstTree, placeSecondTree, placeLargeSnowball, placeSmallSnowball, 
        placeSnowmanHead, playStage, builtSnowman, onePieceSlidOffBoard
    }

    /**-id/kind: type and identity.
     * -column/row: grid location. */
    static class Piece {
        final String id;
        final Kind kind;
        int selectedBoardColumn, selectedBoardRow;
        Piece (String id, Kind kind, int selectedBoardColumn, int selectedBoardRow) {
            this.id = id; this.kind = kind; 
            this.selectedBoardColumn =selectedBoardColumn; this.selectedBoardRow = selectedBoardRow;
        }
    }

    /**-pieces: stores the movable parts of the snowman (small and large snowballs, snowman head).
     * -trees: stores static obstacles that block movement.
     * -phase: tracks the current stage. 
     * -selectedId: recognizes which snowball is currently being controlled.
     * -moveCount:
     *  */
    private final List<Piece> pieces = new ArrayList<>(); 
    private final List<Piece> trees= new ArrayList<>(); 
    private Phase phase = Phase.placeFirstTree;
    private String selectedId = null;
    private int moveCount = 0;
    private final JLabel movesLabel = new JLabel("Moves: 0", SwingConstants.CENTER);

    /**-boardSnowProblem: the panel where the game is drawn.
     * -up/down/left/right: buttons to control pieces movement.
     * -reset: button to clear the board and reset the game. */
    private final BoardPanel boardSnowProblem = new BoardPanel();
    private final JLabel message = new JLabel("Place the first tree on the board.", SwingConstants.CENTER);
    private final JButton up = new JButton("UP");
    private final JButton down = new JButton("DOWN");
    private final JButton left = new JButton("LEFT");
    private final JButton right = new JButton("RIGHT");
    private final JButton reset = new JButton("RESET");

    //Board images (trees, small and large snowballs, and snowman head).
    private final Image imageTree = loadImage("/Users/lanaalsalamah/Desktop/snowproblem/snowProblem/tree.png");
    private final Image imageLargeSnowball = loadImage("/Users/lanaalsalamah/Desktop/snowproblem/snowProblem/large snowball.png");
    private final Image imageSmallSnowball = loadImage("/Users/lanaalsalamah/Desktop/snowproblem/snowProblem/small snowball.png");
    private final Image imageSnowmanHead = loadImage("/Users/lanaalsalamah/Desktop/snowproblem/snowProblem/snowman head.png");
    private final Image imageSnowballsStacked = loadImage("snowmanStack.png");
    private final Image imageSnowmanComplete = loadImage("snowmanBlue.png");

    // Setting up the BorderLayout, adding the title, game board, and a grid based control panel with movement buttons.
    public SnowProblem() {
        super("Snow Problem");
        resetGame();
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Ensures the application is fully closed and stops running when the window is shut.
        // Sets the window layout to BorderLayout with 10-pixel gaps and adds 12-pixel outer pudding.
        setLayout (new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel levelLabel = new JLabel("Level 1", SwingConstants.CENTER); // Add a text lable using JLable class to display level 1 and centering it.
       levelLabel.setFont(levelLabel.getFont().deriveFont(Font.BOLD, 16f)); //Set the font type and size.

       JLabel lableSnowProblem = new JLabel ("Snow Problem", SwingConstants.CENTER); // Add a text lable using JLable class to display the game title and centering it.
       lableSnowProblem.setFont(lableSnowProblem.getFont().deriveFont(Font.BOLD, 24f)); //Set the font type and size.
       // Creating a BorderLayout panel inside the frame located on the top so two labels can stack at the top of the screen.
       JPanel north = new JPanel(new BorderLayout());
       north.add(levelLabel, BorderLayout.NORTH);
       north.add(lableSnowProblem, BorderLayout.CENTER);
       movesLabel.setFont(movesLabel.getFont().deriveFont(Font.BOLD,14f));
       north.add(movesLabel, BorderLayout.SOUTH);
       add(north, BorderLayout.NORTH);
       add(boardSnowProblem, BorderLayout.CENTER);

        // Setup for the message display area at the bottom of the screen.
        JPanel south = new JPanel(new BorderLayout(8, 8));
        message.setFont(message.getFont().deriveFont(Font.PLAIN,14f));
        south.add(message, BorderLayout.NORTH);
        // Setup the button layout. A 3x3 grid with the UP, DOWN, LEFT, RIGHT, and RESET.
        JPanel controls = new JPanel(new GridLayout(3, 3 , 4, 4));
        controls.add(new JLabel()); controls.add(up); controls.add(new JLabel());
        controls.add(left); controls.add(reset); controls.add(right);
        controls.add(new JLabel()); controls.add(down); controls.add(new JLabel());
        south.add(controls, BorderLayout.CENTER);
        
        add(south, BorderLayout.SOUTH); // Places the messages and buttons at the bottom of the window.
        /*Specify the actions for the buttons using class addActionListener.
        References:
        https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html
        https://www.geeksforgeeks.org/advance-java/java-actionlistener-in-awt/ */
        up.addActionListener(e -> movePiece(0, -1));
        down.addActionListener(e -> movePiece(0, 1));
        left.addActionListener(e -> movePiece(-1, 0));
        right.addActionListener(e -> movePiece(1, 0));
        reset.addActionListener(e -> resetGame());

        setSize(600, 700); // Size of the game window.
        setLocationRelativeTo(null);
        updateControls();
    }

    // Converts a file path into an image object and returns null if the file is missing.
    private Image loadImage(String path) {
        File f = new File(path);
        if (!f.exists()) return null;
        return new ImageIcon(path).getImage();
    }

    // Resets the game, places the starting pieces, and refreshes the UI.
    private void resetGame() {
        pieces.clear();
        selectedId = null;
        moveCount = 0;
        movesLabel.setText("Moves: " + moveCount);
        pieces.add(new Piece("head", Kind.snowmanHeadImage, 0, 3)); //
        pieces.add(new Piece("large", Kind.largeSnowballImage, 4, 3)); //
        pieces.add(new Piece("small", Kind.smallSnowballImage, 1, 0)); //
        phase = Phase.playStage;
        setMessage("Pick a snowball or the head, then press an arrow.");
        updateControls();
        boardSnowProblem.repaint();
    }

    private void setMessage(String s) {message.setText(s);} // Refreshes the instructions and status.

    // Enables movement buttons only when a piece is selected.
    private void updateControls() {
        boolean playing = phase == Phase.playStage && selectedId != null;
        up.setEnabled(playing); down.setEnabled(playing);
        left.setEnabled(playing); right.setEnabled(playing);
    }

    // Returns the piece or tree located at a specific grid position (column, row).
    private Piece pieceAt(int c, int r) {
        for (Piece p : trees) if (p.selectedBoardColumn == c && p.selectedBoardRow == r) return p;
        for (Piece p : pieces) if (p.selectedBoardColumn == c && p.selectedBoardRow == r) return p;
        return null;
    }

    // Manages the clicks on a board cell to select or deselect a movable piece.
    private void handleCellClick(int selectedBoardColumn, int selectedBoardRow) {
       if (phase != Phase.playStage) return;
       Piece p = pieceAt(selectedBoardColumn, selectedBoardRow);
       if (p != null && p.kind != Kind.treeImage) {
        selectedId = p.id.equals(selectedId) ? null : p.id;
        setMessage(selectedId == null ? "Pick a piece." : "Selected: " + selectedId);
        boardSnowProblem.repaint();
        updateControls();
       }
    }

    // Checks if the large snowball, small snowball, and snowman head are all stacked on the same square.
    private boolean checkWin() {
        Piece large = findPiece("large"), small = findPiece("small"), head = findPiece("head");
        if (large == null || small == null || head == null) return false;
        return large.selectedBoardColumn == small.selectedBoardColumn && small.selectedBoardColumn == head.selectedBoardColumn && large.selectedBoardRow == small.selectedBoardRow && small. selectedBoardRow == head.selectedBoardRow;
    }

    // Returns true when the small snowball is stacked over the large snowball (same cell).
    private boolean snowballsStacked() {
        Piece large = findPiece("large");
        Piece small = findPiece("small");
        return large != null && small != null && large.selectedBoardColumn == small.selectedBoardColumn && large.selectedBoardRow == small.selectedBoardRow;
    }

    // Locates a game piece from the board using its ID.
    private Piece findPiece(String id) {
        for (Piece p : pieces) if (p.id.equals(id)) return p;
        return null;
    }

    // Converts piece's type into a rank (0-3) to determine its stacking order.
    private int rank(Kind k) {
        switch (k) {
            case treeImage: return 0;
            case largeSnowballImage: return 1;
            case smallSnowballImage: return 2;
            case snowmanHeadImage: return 3; }
        return -1;
    }

    // Processes piece movement like sliding physics and stacking logic.
    private void movePiece(int dc, int dr) {
        // Makes sure that the only pieces that move are valid and not proceed if the game is in the play stage.
        if (phase != Phase.playStage || selectedId == null) return;
        Piece mover = findPiece(selectedId);
        if (mover == null) return;
        // Ensur that when a large snowball is moved the smaller pieces move too.
        java.util.Set<String> moving = new java.util.HashSet<>();
        moving.add(mover.id);
        int moverRank = rank(mover.kind);
        for (Piece p : pieces) {
            if (p.selectedBoardColumn == mover.selectedBoardColumn && p.selectedBoardRow == mover.selectedBoardRow && rank(p.kind) > moverRank) {
                moving.add(p.id);
            }
        }
        // Setup the state for the move like where it starts, what it hits, and if it falls off the board.
        int selectedColumn = mover.selectedBoardColumn, selectedRow = mover.selectedBoardRow;
        Piece blockedBy = null;
        boolean fellOff = false;
        // Executes a movement loop that checks for board edges, obstacles, and if the piece can stack or must stop.
        while (true) {
            int nc = selectedColumn + dc, nr = selectedRow +dr;
            if (nc < 0 || nc >= numberOfBoardColumns || nr < 0 || nr >= numberOfBoardRows) {fellOff = true; break; }

            List<Piece> here = new ArrayList<>();
            for (Piece p : trees) if (p.selectedBoardColumn == nc && p.selectedBoardRow == nr &&!moving.contains(p.id)) here.add(p);
            for (Piece p : pieces) if (p.selectedBoardColumn == nc && p.selectedBoardRow == nr &&!moving.contains(p.id)) here.add(p);
            here.sort(Comparator.comparingInt((Piece p) -> rank(p.kind)).reversed());
            Piece obstacle = here.isEmpty() ? null : here.get(0);

            if (obstacle != null) {
                boolean canStack =
                (mover.kind == Kind.smallSnowballImage && obstacle.kind == Kind.largeSnowballImage) || 
                (mover.kind == Kind.snowmanHeadImage && obstacle.kind == Kind.smallSnowballImage);
                if (canStack) {selectedColumn = nc; selectedRow =nr; }
                blockedBy = obstacle;
                break;
            }
            selectedColumn = nc; selectedRow = nr;
            if (mover.kind == Kind.snowmanHeadImage) break; //Snowman head moves 1 step
        }
        // Ends the game if a piece false off the board.
        if (fellOff && blockedBy == null) {
            setMessage("The " + mover.kind.name().toLowerCase() + " slid off the board!");
            phase = Phase.onePieceSlidOffBoard;
            selectedId = null;
            boardSnowProblem.repaint();
            updateControls();
            return;
        }
        // Checks if the piece moved. If it's in the same spot, it notifies the player that the path is blocked.
        if (selectedColumn == mover.selectedBoardColumn && selectedRow == mover.selectedBoardRow) {
            setMessage("Blocked try another direction.");
            return;
        }
        // Updates the positions of all pieces in the stack, and checks if the player won.
        int dCol = selectedColumn - mover.selectedBoardColumn, dRow = selectedRow - mover.selectedBoardRow;
        for (Piece p : pieces) {
            if (moving.contains(p.id)) {p.selectedBoardColumn += dCol; p.selectedBoardRow += dRow; }
        }
        moveCount++;
        movesLabel.setText("Moves: " + moveCount);
        selectedId = null;
        boardSnowProblem.repaint();
        if (checkWin()) {
            phase = Phase.builtSnowman;
            setMessage("Yay! You rebuilt the snowman");
        } else {
            setMessage("Nice. Pick another piece.");
        }
        updateControls();
    }

    // Managing the game board by detecting mouse clicks, rendering the grid, and drawing all pieces in their correct stcking order.
    private class BoardPanel extends JPanel {
        // Sets the board's background color and creates a system detecting which grid square the player selectswith the mouse.
        BoardPanel() {
            setBackground(new Color(0x1f, 0x2a, 0x44));
            addMouseListener(new MouseAdapter(){
                @Override public void mouseClicked(MouseEvent e) {
                    int cw = getWidth() / numberOfBoardColumns, ch = getHeight() / numberOfBoardRows;
                    if (cw == 0 || ch == 0) return;
                    int selectedBoardColumn = e.getX() / cw, selectedBoardRow = e.getY() / ch;
                    if (selectedBoardColumn >= 0 && selectedBoardColumn < numberOfBoardColumns && selectedBoardRow >= 0 && selectedBoardRow < numberOfBoardRows) handleCellClick(selectedBoardColumn, selectedBoardRow);
                }
            });
        }

        // Refreshes the game window by drawing the board background and then layering the pieces on top from largest to smallest.
        @Override protected void paintComponent(Graphics g0) {
            // Enables advanced rendering hints to prevent pixelation.
            super.paintComponent(g0);
            Graphics2D boardGraphics = (Graphics2D) g0;
            boardGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boardGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); 
            // Calculating the width ans height of a single grid cell by divideng the total window size by the number of columns and rows.
            int W = getWidth(), H = getHeight();
            int cw = W / numberOfBoardColumns, ch = H / numberOfBoardRows;
            // Fill the board with color white with a black boarder and turn yellow if it contains the curnt selection piece.
            for (int r = 0; r < numberOfBoardRows; r++) {
                for (int c = 0; c < numberOfBoardColumns; c++) {
                    boardGraphics.setColor(new Color(245,245,245)); // Reference: https://rgb.to/rgb/245,245,245
                    Piece sel = selectedId == null ? null : findPiece(selectedId);
                    if (sel != null && sel.selectedBoardColumn == c && sel.selectedBoardRow == r) {
                        boardGraphics.setColor(new Color(0xff, 0xe7, 0x9a));
                    }
                    boardGraphics.fillRoundRect(c * cw + 3, r * ch + 3, cw -6, ch -6, 12, 12);
                }
            }
            // Draws the pieces on the board, swapping in the stacked or completed snowman image when applicable.
            for (Piece t : trees) placeTreeBallHead(boardGraphics, t, cw, ch);
            Piece large = findPiece("large");
            Piece small = findPiece("small");
            Piece head = findPiece("head");
            if (checkWin() && large != null && imageSnowmanComplete != null) {
                drawImageAtCell(boardGraphics, imageSnowmanComplete, large.selectedBoardColumn, large.selectedBoardRow, cw, ch);
            } else if (snowballsStacked() && imageSnowballsStacked != null) {
                drawImageAtCell(boardGraphics, imageSnowballsStacked, large.selectedBoardColumn, large.selectedBoardRow, cw, ch);
                if (head != null) placeTreeBallHead(boardGraphics, head, cw, ch);
            } else {
                List<Piece> all = new ArrayList<>(pieces);
                all.sort(Comparator.comparingInt(p -> rank(p.kind)));
                for (Piece p : all) placeTreeBallHead(boardGraphics, p, cw, ch);
            }
        }

        // Selects the appropriate image for the pieces and draws it at the correct size and position on the game board.
        private void placeTreeBallHead(Graphics2D boardGraphics, Piece p, int cw, int ch) {
            Image imageOfPiece = null;
            switch (p.kind) {
                case treeImage: imageOfPiece = imageTree; break;
                case largeSnowballImage: imageOfPiece = imageLargeSnowball; break;
                case smallSnowballImage: imageOfPiece = imageSmallSnowball; break;
                case snowmanHeadImage: imageOfPiece = imageSnowmanHead; break;
            }
            int x = p.selectedBoardColumn * cw + 6, y = p.selectedBoardRow * ch + 6;
            int w = cw - 12, h = ch - 12;
            boardGraphics.drawImage(imageOfPiece, x, y, w, h, this);
        }

        // Draws an image inside a chosen board cell, leaving some space around it so it looks neat.
        private void drawImageAtCell(Graphics2D g, Image img, int col, int row, int cw, int ch) {
            int x = col * cw + 6, y = row * ch + 6;
            int w = cw - 12, h = ch - 12;
            g.drawImage(img, x, y, w, h, this);
        }
    }
    
    // Using SwingUtilities.invokeLater to launch the game window smothly and safely.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnowProblem().setVisible(true));
    }
}