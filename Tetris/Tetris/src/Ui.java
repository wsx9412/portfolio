import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import Block.Block;

public class Ui extends JFrame {
	protected int[][] rect = new int[10][20];
	// [가로][세로]
	protected int x, y;
	protected int blockSize = 10;
	protected int width = 1;
	protected int height = 1;
	protected Image screenImage;
	protected Graphics screenGraphic;
	protected ArrayList<Integer> randomBlock = new ArrayList<>();
	protected Block block;
	protected Random r = new Random();

	public Ui() {
		addKeyListener(new Key());
		setUndecorated(true);
		setSize(300, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		addblock();
	}

	public void paint(Graphics g) {
		screenImage = createImage(120, 220);
		screenGraphic = screenImage.getGraphics();
		screenPaint(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
	}

	// 계속해서 순서대로 그리면 깜빡이는 현상이 존재하기 때문에 다른 그래픽 객체에 한번에 그려준 다음 화면에 출력함
	public void screenPaint(Graphics g) {
		g.drawImage(Tetris.BACKGROUND, 10, 10, null);
		for (int i = 0; i < rect.length; i++) {
			for (int j = 0; j < rect[0].length; j++) {
				if (rect[i][j] == 1)
					g.drawImage(Tetris.BASIC_BLOCK, (i + width) * blockSize, (j + height) * blockSize, null);
			}
		}
		if (block != null && !block.isOver()) {
			block.screenDraw(g, rect);
			if (block.isStop()) {
				rect = block.getRect();
				addblock();

				block.clearLine(rect);
			}
		}else if(block != null && block.isOver()) {
			g.drawImage(Tetris.GAME_OVER, width * blockSize, 9 * blockSize, null);			
		}

		paintComponents(g);
		repaint();
	}

	public void addblock() {
		if(randomBlock.isEmpty()) {
			for(int i = 0; i < 7; i++) {
				randomBlock.add(i);
			}
		}
		int rnd = r.nextInt(randomBlock.size());
		block = BlockFactory.create(randomBlock.get(rnd));
		randomBlock.remove(rnd);
		block.start();
	}

	public class Key implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (block != null && block.isOver() == false) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					block.moveLeft(rect);
					break;
				case KeyEvent.VK_UP:
					block.rotate(rect);
					break;
				case KeyEvent.VK_RIGHT:
					block.moveRight(rect);
					break;
				case KeyEvent.VK_DOWN:
					block.moveDown(rect);
					break;
				case KeyEvent.VK_SPACE:
					block.quickDrop(rect);
					break;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

	}

}
