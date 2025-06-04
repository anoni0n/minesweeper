import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Tile {

    private final JButton button;
    private final int row;
    private final int col;
    private int bombCount;
    private ImageIcon icon;
    private boolean bomb;
    private boolean flagged;
    private static Tile[][] BOARD = Minesweeper.BOARD;
    private ActionListener actionListener;
    public static int TILES_CLEARED = 0;
    public static int imgDimensions = 600/Minesweeper.BOARD_DIMENSIONS;
    private static java.util.Timer seconds = new Timer();
    private static int secondElapsed = 0;
    private static ArrayList<Tile> bombTiles = new ArrayList<>();
    public static ImageIcon CLEARED_TILE = new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-cleared.png")))).getImage()).getScaledInstance(imgDimensions,imgDimensions, Image.SCALE_SMOOTH));
    public static ImageIcon UNCLEARED_TILE = new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-uncleared.png")))).getImage()).getScaledInstance(imgDimensions,imgDimensions,java.awt.Image.SCALE_SMOOTH));
    public static ImageIcon BOMB_TILE = new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-bomb.png")))).getImage()).getScaledInstance(imgDimensions,imgDimensions,java.awt.Image.SCALE_SMOOTH));
    public static ImageIcon FLAG_TILE = new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-flag.png")))).getImage()).getScaledInstance(imgDimensions,imgDimensions,java.awt.Image.SCALE_SMOOTH));
    public Tile(JButton button, int row, int col){
        this.button = button;
        this.row = row;
        this.col = col;
        this.setIcon(UNCLEARED_TILE);
        bombCount = 0;
        flagged = false;
        button.setPreferredSize(new Dimension(imgDimensions,imgDimensions));
        button.setVisible(true);
        button.setEnabled(true);
        MouseAdapter rightClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Minesweeper.GAME_ACTIVE  && getIcon().equals(UNCLEARED_TILE)){
                if (flagged){
                    button.setIcon(UNCLEARED_TILE);
                }
                else if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    button.setIcon(FLAG_TILE);
                }
                flagged = !flagged;
            }
                }
        };
        ActionListener actionListener = e -> {
            if (Minesweeper.GAME_ACTIVE == null){
                Minesweeper.GAME_ACTIVE = true;
                seconds.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (Minesweeper.GAME_ACTIVE) {
                            Minesweeper.time.setText(Integer.toString(secondElapsed++));
                        }
                    }
                },0,1000);
                ArrayList<Tile> emptyTiles = new ArrayList<>();
                int clearedCount = 0;
                int bombsAdded = 0;
                for (int i = 0; i < Minesweeper.BOARD_DIMENSIONS; i++){
                    for (int j = 0; j < Minesweeper.BOARD_DIMENSIONS; j++){
                        if (isNear(i,row,1) && isNear(j,col,1)){
                            BOARD[i][j].setIcon(CLEARED_TILE);
                            BOARD[i][j].disableButton();
                            clearedCount++;
                        }
                    }
                }
                while (bombsAdded < Minesweeper.BOMB_COUNT){
                    int randomRow = (int) (Math.random()* Minesweeper.BOARD_DIMENSIONS);
                    int randomCol = (int) (Math.random()*Minesweeper.BOARD_DIMENSIONS);
                    if (!BOARD[randomRow][randomCol].isBomb() && BOARD[randomRow][randomCol].getIcon().equals(UNCLEARED_TILE)){
                        BOARD[randomRow][randomCol].setIsBomb();
                        bombTiles.add(BOARD[randomRow][randomCol]);
                        bombsAdded++;
                    }
                }
                for (Tile bombTile : bombTiles) {
                    for (int i = 0; i < Minesweeper.BOARD_DIMENSIONS; i++){
                        for (int j = 0; j < Minesweeper.BOARD_DIMENSIONS; j++) {
                            if (isNear(BOARD[i][j].getRow(), bombTile.getRow(), 1) && isNear(BOARD[i][j].getCol(), bombTile.getCol(), 1)) {
                                BOARD[i][j].incrementBombCount();
                            }
                        }
                    }
                }
                while (clearedCount < Minesweeper.BOARD_DIMENSIONS){
                    int randomRow = (int) (Math.random()*Minesweeper.BOARD_DIMENSIONS);
                    int randomCol = (int) (Math.random()*Minesweeper.BOARD_DIMENSIONS);
                    if (isNear(randomRow,row,2) && isNear(randomCol,col,2) && !BOARD[randomRow][randomCol].isBomb()){
                        if (BOARD[randomRow][randomCol].getBombCount() == 0){
                            BOARD[randomRow][randomCol].setIcon(CLEARED_TILE);
                        }
                        else {
                            BOARD[randomRow][randomCol].setIcon();
                        }
                        BOARD[randomRow][randomCol].disableButton();
                        clearedCount++;
                    }
                }
                for (int i = 0; i < Minesweeper.BOARD_DIMENSIONS; i++){
                    for (int j = 0; j < Minesweeper.BOARD_DIMENSIONS; j++){
                        if (isNear(i,row,1) && isNear(j,col,1)){
                            if (BOARD[i][j].getBombCount() == 0) {
                                BOARD[i][j].setIcon(CLEARED_TILE);
                            }
                            else {
                                BOARD[i][j].setIcon();
                            }
                            BOARD[i][j].disableButton();
                        }
                        if (BOARD[i][j].getIcon().equals(CLEARED_TILE)){
                            emptyTiles.add(BOARD[i][j]);
                        }
                    }
                }
//                emptyTiles.add(this);
//                for (Tile tile : emptyTiles){
//                    clearEmptyTiles(tile);
//                }
                for (int k = 0; k < emptyTiles.size(); k++) {
                    for (int i = 0; i < Minesweeper.BOARD_DIMENSIONS; i++) {
                        for (int j = 0; j < Minesweeper.BOARD_DIMENSIONS; j++) {
                            if (!BOARD[i][j].getIcon().equals(CLEARED_TILE) && isNear(BOARD[i][j].getRow(), emptyTiles.get(k).getRow(), 1) && isNear(BOARD[i][j].getCol(), emptyTiles.get(k).getCol(), 1)) {
                                if (BOARD[i][j].getBombCount() == 0) {
                                    BOARD[i][j].setIcon(CLEARED_TILE);
                                    emptyTiles.add(BOARD[i][j]);
                                } else {
                                    BOARD[i][j].setIcon();
                                }
                                BOARD[i][j].disableButton();
                            }
                        }
                    }
                    emptyTiles.remove(k);
                    k--;
                }
                for (int i = 0; i < Minesweeper.BOARD_DIMENSIONS; i++){
                    for (int j = 0; j < Minesweeper.BOARD_DIMENSIONS; j++){
                        if(!BOARD[i][j].getIcon().equals(UNCLEARED_TILE)){
                            TILES_CLEARED++;
                        }
                    }
                }
            }
            else if (Minesweeper.GAME_ACTIVE && getIcon().equals(UNCLEARED_TILE) && !flagged){
                disableButton();
                if (isBomb()){
                    for (Tile bombTile : bombTiles){
                        bombTile.setIcon(BOMB_TILE);
                    }
                    JOptionPane.showMessageDialog(null,"You Lost!");
                    Minesweeper.GAME_ACTIVE = false;
                }
                else if (bombCount == 0){
                    //clearEmptyTiles(this);
                    ArrayList<Tile> emptyTiles = new ArrayList<>();
                    emptyTiles.add(this);
                    for (int k = 0; k < emptyTiles.size(); k++) {
                        for (int i = 0; i < Minesweeper.BOARD_DIMENSIONS; i++) {
                            for (int j = 0; j < Minesweeper.BOARD_DIMENSIONS; j++) {
                                if (BOARD[i][j].getIcon().equals(UNCLEARED_TILE) && isNear(BOARD[i][j].getRow(), emptyTiles.get(k).getRow(), 1) && isNear(BOARD[i][j].getCol(), emptyTiles.get(k).getCol(), 1)) {
                                    if (BOARD[i][j].getBombCount() == 0) {
                                        BOARD[i][j].setIcon(CLEARED_TILE);
                                        emptyTiles.add(BOARD[i][j]);
                                    } else {
                                        BOARD[i][j].setIcon(new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-" + BOARD[i][j].getBombCount() + ".png")))).getImage()).getScaledInstance(imgDimensions, imgDimensions, Image.SCALE_SMOOTH)));
                                    }
                                    BOARD[i][j].disableButton();
                                    TILES_CLEARED++;
                                }
                            }
                        }
                        emptyTiles.remove(k);
                        k--;
                    }
                }
                else {
                    if (getBombCount()>0){
                        BOARD[getRow()][getCol()].setIcon();
                        TILES_CLEARED++;
                    }
                }
                if (TILES_CLEARED == Math.pow(Minesweeper.BOARD_DIMENSIONS,2)-bombTiles.size()){
                    JOptionPane.showMessageDialog(null,"You won in "+secondElapsed+" seconds!");
                    Minesweeper.GAME_ACTIVE = false;
                }
            }
        };
        button.addActionListener(actionListener);
        button.addMouseListener(rightClick);
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

    public JButton getButton(){
        return button;
    }

    private boolean isNear(int a, int b, double tolerance){
        return Math.abs(a-b) <= tolerance;
    }

    private boolean isNear(Tile a, Tile b, double tolerance){
        return isNear(a.getRow(),b.getRow(),tolerance) && isNear(a.getCol(),b.getCol(),tolerance);
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

    private void setIcon(){
        this.setIcon(new ImageIcon((new ImageIcon((Objects.requireNonNull(Tile.class.getResource("images/minesweeper-" + this.getBombCount() + ".png")))).getImage()).getScaledInstance(imgDimensions, imgDimensions, Image.SCALE_SMOOTH)));
    }

    public void disableButton(){
        button.removeActionListener(this.actionListener);
    }

    public void clearEmptyTiles(Tile tile){
        if (tile.getBombCount() == 0){
            tile.setIcon(CLEARED_TILE);
            for (int i = 0; i < Minesweeper.BOARD_DIMENSIONS; i++){
                for (int j = 0; j < Minesweeper.BOARD_DIMENSIONS; j++){
                    if (isNear(tile,BOARD[i][j],1)){
                        clearEmptyTiles(BOARD[i][j]);
                    }
                }
            }
        }
        else {
            tile.setIcon();
        }
    }
}
