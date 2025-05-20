import javax.swing.*;
import java.awt.*;

public class Minesweeper {
    private final JFrame frame = new JFrame("Minesweeper");
    private final JPanel panel  = new JPanel(new GridLayout(10, 10, 0, 0));
    public static Tile[][] BOARD = new Tile[10][10];
    public static boolean GAME_ACTIVE = false;


    public Minesweeper() {
        initialize();
    }

    private void initialize() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Tile tile = new Tile(new JButton(),i,j);
                BOARD[i][j] = tile;
                panel.add(tile.getButton());
            }
        }

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
