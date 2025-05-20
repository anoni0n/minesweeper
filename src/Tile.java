import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class Tile {

    private JButton button;
    private int row;
    private int col;
    private int bombCount;
    private ImageIcon icon;
    private boolean bomb;
    private static final ArrayList<Tile> bombTiles = new ArrayList<>();
    public static final ImageIcon CLEARED_TILE = new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-cleared.png")))).getImage()).getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH));
    public static final ImageIcon UNCLEARED_TILE = new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-uncleared.png")))).getImage()).getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH));
    public static final ImageIcon BOMB_TILE = new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-bomb.png")))).getImage()).getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH));
    public Tile(JButton button, int row, int col){
        this.button = button;
        this.row = row;
        this.col = col;
        this.setIcon(UNCLEARED_TILE);
        bombCount = 0;
        button.setPreferredSize(new Dimension(60,60));
        button.setVisible(true);
        button.setEnabled(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Minesweeper.GAME_ACTIVE){
                    Minesweeper.GAME_ACTIVE = true;
                    int clearedCount = 0;
                    int bombCount = 0;
                    for (int i = 0; i < 10; i++){
                        for (int j = 0; j < 10; j++){
                            if (isNear(i,row,1) && isNear(j,col,1)){
                                Minesweeper.BOARD[i][j].setIcon(CLEARED_TILE);
                                Minesweeper.BOARD[i][j].setEnabled(false);
                                clearedCount++;
                            }
                        }
                    }
                    while (bombCount < 15){
                        int randomRow = (int) (Math.random()*10);
                        int randomCol = (int) (Math.random()*10);
                        if (Minesweeper.BOARD[randomRow][randomCol].getIcon().equals(UNCLEARED_TILE)){
                            Minesweeper.BOARD[randomRow][randomCol].setIsBomb();
                            bombTiles.add(Minesweeper.BOARD[randomRow][randomCol]);
                            bombCount++;
                        }
                    }
                    while (clearedCount < 20){
                        int randomRow = (int) (Math.random()*10);
                        int randomCol = (int) (Math.random()*10);
                        if (isNear(randomRow,row,2) && isNear(randomCol,col,2) && !Minesweeper.BOARD[randomRow][randomCol].isBomb()){
                            Minesweeper.BOARD[randomRow][randomCol].setIcon(CLEARED_TILE);
                            Minesweeper.BOARD[randomRow][randomCol].setEnabled(false);
                            clearedCount++;
                        }
                    }
                    for (int i = 0; i < 10; i++){
                        for (int j = 0; j < 10; j++) {
                            for (Tile bombTile : bombTiles) {
                                if (isNear(Minesweeper.BOARD[i][j].getRow(), bombTile.getRow(), 1) && isNear(Minesweeper.BOARD[i][j].getCol(), bombTile.getCol(), 1)) {
                                    Minesweeper.BOARD[i][j].incrementBombCount();
                                }
                            }
                            if (Minesweeper.BOARD[i][j].getIcon().equals(CLEARED_TILE) && Minesweeper.BOARD[i][j].getBombCount() > 0) {
                                Minesweeper.BOARD[i][j].setIcon(new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-"+Minesweeper.BOARD[i][j].getBombCount()+".png")))).getImage()).getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH)));
                            }
                        }
                    }

                }
            }
        });
    }

    public void setIcon(ImageIcon icon){
        this.icon = icon;
        button.setIcon(icon);
    }

    public ImageIcon getIcon(){
        return icon;
    }

    public int getBombCount(){
        return bombCount;
    }

    public void setEnabled(boolean enabled){
        button.setEnabled(enabled);
    }

    public JButton getButton(){
        return button;
    }

    private boolean isNear(int a, int b, double tolerance){
        return Math.abs(a-b) <= tolerance;
    }

    private boolean isBomb(){
        return bomb;
    }
    private void setIsBomb(){
        this.bomb = true;
    }

    public void incrementBombCount(){
        bombCount++;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
