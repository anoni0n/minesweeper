import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class Minesweeper {
    private final JFrame frame = new JFrame("Minesweeper");
    private JPanel panel;
    public static Boolean GAME_ACTIVE;
    public static int BOMB_COUNT;
    public static int BOARD_DIMENSIONS;
    public static Tile[][] BOARD;


    public Minesweeper() {
        String[] options = {"Easy","Medium","Hard"};
        ImageIcon settingsIcon = new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-settings.png")))).getImage().getScaledInstance(30,30,java.awt.Image.SCALE_SMOOTH)));
        String mode = options[JOptionPane.showOptionDialog(null, "Choose a game mode:", "settings",
                0, 3, settingsIcon, options, options[0])];

        initialize(mode);
    }

    private void initialize(String mode) {
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        switch (mode){
            case ("Easy") -> BOARD_DIMENSIONS = 10;
            case ("Medium") -> BOARD_DIMENSIONS = 20;
            case ("Hard") -> BOARD_DIMENSIONS = 40;
        }
        panel = new JPanel(new GridLayout(BOARD_DIMENSIONS, BOARD_DIMENSIONS, 0, 0));
        BOMB_COUNT = (int) (Math.pow(BOARD_DIMENSIONS,2)/5);
        BOARD = new Tile[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        for (int i = 0; i < BOARD_DIMENSIONS; i++) {
            for (int j = 0; j < BOARD_DIMENSIONS; j++) {
                Tile tile = new Tile(new JButton(), i, j);
                BOARD[i][j] = tile;
                panel.add(tile.getButton());
            }
        }

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}