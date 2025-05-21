import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class Tile {

    private final JButton button;
    private final int row;
    private final int col;
    private int bombCount;
    private ImageIcon icon;
    private boolean bomb;
    private ActionListener actionListener;
    public static Tile[][] BOARD = new Tile[10][10];
    public static int TILES_CLEARED = 0;
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
        button.setPreferredSize(new Dimension(58,58));
        button.setVisible(true);
        button.setEnabled(true);
        ActionListener actionListener = e -> {
            if (Minesweeper.GAME_ACTIVE == null){
                Minesweeper.GAME_ACTIVE = true;
                ArrayList<Tile> emptyTiles = new ArrayList<>();
                int clearedCount = 0;
                int bombsAdded = 0;
                for (int i = 0; i < 10; i++){
                    for (int j = 0; j < 10; j++){
                        if (isNear(i,row,1) && isNear(j,col,1)){
                            BOARD[i][j].setIcon(CLEARED_TILE);
                            BOARD[i][j].disableButton();
                            clearedCount++;
                        }
                    }
                }
                while (bombsAdded < 10){
                    int randomRow = (int) (Math.random()*10);
                    int randomCol = (int) (Math.random()*10);
                    if (!BOARD[randomRow][randomCol].isBomb() && BOARD[randomRow][randomCol].getIcon().equals(UNCLEARED_TILE)){
                        BOARD[randomRow][randomCol].setIsBomb();
                        bombTiles.add(BOARD[randomRow][randomCol]);
                        bombsAdded++;
                    }
                }
                for (Tile bombTile : bombTiles) {
                    for (int i = 0; i < 10; i++){
                        for (int j = 0; j < 10; j++) {
                            if (isNear(BOARD[i][j].getRow(), bombTile.getRow(), 1) && isNear(BOARD[i][j].getCol(), bombTile.getCol(), 1)) {
                                BOARD[i][j].incrementBombCount();
                            }
                        }
                    }
                }
                while (clearedCount < 20){
                    int randomRow = (int) (Math.random()*10);
                    int randomCol = (int) (Math.random()*10);
                    if (isNear(randomRow,row,2) && isNear(randomCol,col,2) && !BOARD[randomRow][randomCol].isBomb()){
                        if (BOARD[randomRow][randomCol].getBombCount() == 0){
                            BOARD[randomRow][randomCol].setIcon(CLEARED_TILE);
                        }
                        else {
                            BOARD[randomRow][randomCol].setIcon(new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-"+BOARD[randomRow][randomCol].getBombCount()+".png")))).getImage()).getScaledInstance(60,60, Image.SCALE_SMOOTH)));
                        }
                        BOARD[randomRow][randomCol].disableButton();
                        clearedCount++;
                    }
                }
                for (int i = 0; i < 10; i++){
                    for (int j = 0; j < 10; j++){
                        if (isNear(i,row,1) && isNear(j,col,1)){
                            if (BOARD[i][j].getBombCount() == 0) {
                                BOARD[i][j].setIcon(CLEARED_TILE);
                            }
                            else {
                                BOARD[i][j].setIcon(new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-"+BOARD[i][j].getBombCount()+".png")))).getImage()).getScaledInstance(60,60, Image.SCALE_SMOOTH)));
                            }
                            BOARD[i][j].disableButton();
                        }
                        if (BOARD[i][j].getIcon().equals(CLEARED_TILE)){
                            emptyTiles.add(BOARD[i][j]);
                        }
                    }
                }
                while (!emptyTiles.isEmpty()){
                    for (int k = 0; k < emptyTiles.size(); k++){
                        for (int i = 0; i < 10; i++){
                            for (int j = 0; j < 10; j++){
                                if (!BOARD[i][j].getIcon().equals(CLEARED_TILE) && isNear(BOARD[i][j].getRow(), emptyTiles.get(k).getRow(), 1) && isNear(BOARD[i][j].getCol(), emptyTiles.get(k).getCol(), 1)) {
                                    if (BOARD[i][j].getBombCount() == 0) {
                                        BOARD[i][j].setIcon(CLEARED_TILE);
                                        emptyTiles.add(BOARD[i][j]);
                                    }
                                    else {
                                        BOARD[i][j].setIcon(new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-"+BOARD[i][j].getBombCount()+".png")))).getImage()).getScaledInstance(60,60, Image.SCALE_SMOOTH)));

                                    }
                                    BOARD[i][j].disableButton();
                                }
                            }
                        }
                        emptyTiles.remove(k);
                        k--;
                    }
                }
                for (int i = 0; i < 10; i++){
                    for (int j = 0; j < 10; j++){
                        if(!BOARD[i][j].getIcon().equals(UNCLEARED_TILE)){
                            TILES_CLEARED++;
                        }
                    }
                }
            }
            else if (Minesweeper.GAME_ACTIVE && getIcon().equals(UNCLEARED_TILE)){
                disableButton();
                if (isBomb()){
                    for (Tile bombTile : bombTiles){
                        bombTile.setIcon(BOMB_TILE);
                    }
                    JOptionPane.showMessageDialog(null,"You Lost!");
                    Minesweeper.GAME_ACTIVE = false;
                }
                else if (bombCount == 0){
                    ArrayList<Tile> emptyTiles = new ArrayList<>();
                    emptyTiles.add(this);
                    //while (!emptyTiles.isEmpty()) {
                        for (int k = 0; k < emptyTiles.size(); k++) {
                            for (int i = 0; i < 10; i++) {
                                for (int j = 0; j < 10; j++) {
                                    if (BOARD[i][j].getIcon().equals(UNCLEARED_TILE) && isNear(BOARD[i][j].getRow(), emptyTiles.get(k).getRow(), 1) && isNear(BOARD[i][j].getCol(), emptyTiles.get(k).getCol(), 1)) {
                                        if (BOARD[i][j].getBombCount() == 0) {
                                            BOARD[i][j].setIcon(CLEARED_TILE);
                                            emptyTiles.add(BOARD[i][j]);
                                        } else {
                                            BOARD[i][j].setIcon(new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-" + BOARD[i][j].getBombCount() + ".png")))).getImage()).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
                                        }
                                        BOARD[i][j].disableButton();
                                        TILES_CLEARED++;
                                    }
                                }
                            }
                            emptyTiles.remove(k);
                            k--;
                        }
                    //}
                }
                else {
                    if (getBombCount()>0){
                        BOARD[getRow()][getCol()].setIcon(new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-"+BOARD[getRow()][getCol()].getBombCount()+".png")))).getImage()).getScaledInstance(60,60, Image.SCALE_SMOOTH)));
                        TILES_CLEARED++;
                    }
                }
                if (TILES_CLEARED == 100-bombTiles.size()){
                    JOptionPane.showMessageDialog(null,"You Win!");
                    Minesweeper.GAME_ACTIVE = false;
                }
            }
        };
        button.addActionListener(actionListener);
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

    public int getCol() {
        return col;
    }

    public void disableButton(){
        button.removeActionListener(this.actionListener);
    }
}
