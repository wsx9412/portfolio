import java.awt.Image;

import javax.swing.*;

public class Tetris {
	public static Image BASIC_BLOCK = new ImageIcon(Tetris.class.getResource("/images/block.png")).getImage();
	public static Image BACKGROUND = new ImageIcon(Tetris.class.getResource("/images/background.png")).getImage();
	public static Image GAME_OVER = new ImageIcon(Tetris.class.getResource("/images/GameOver.png")).getImage();
	public static int BLOCK_SIZE = 10;	
	public static void main(String[] args) {
		Ui ui = new Ui();
	}
}