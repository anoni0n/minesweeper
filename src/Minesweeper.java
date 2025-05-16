import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Minesweeper {
    private JButton[][] board = new JButton[10][10];
    private JFrame frame = new JFrame("Minesweeper");
    private JPanel buttonPanel = new JPanel();
    private JPanel gamePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    };

    public Minesweeper(){
        frame.setSize(800,800);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50,50));
                button.setFocusable(false);
                button.setEnabled(true);
                button.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("./images/minesweeper-tile.png"))));
                button.setVisible(true);
                buttonPanel.add(button);
                board[i][j] = button;
            }

        }
        frame.add(buttonPanel);
        gamePanel.repaint();
    }
}
