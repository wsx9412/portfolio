package Block;

import java.awt.Graphics;
import java.awt.Image;
import java.lang.reflect.Array;

import javax.swing.ImageIcon;

public class Block extends Thread {
	protected Image basicBlock = new ImageIcon(Block.class.getResource("/images/block.png")).getImage();
	protected int SLEEP_TIME = 500;
	protected int x, y;
	protected int blockSize;
	protected boolean isDrop;
	protected boolean isStop;
	protected boolean isOver;
	protected int arrayHeight;
	protected int arrayWidth;
	protected int color;
	protected int rotateNow;
	protected int rotateNum;
	protected int[][][] block;
	protected int[][] rect;
	protected int width, height;
	// protected int SLEEP_TIME = 100;

	public Block() {
		isDrop = true;
		isStop = false;
		isOver = false;
		blockSize = 10;
		x = 4;
		y = 0;
		width = 1;
		height = 1;
	}

	public boolean checkMove(int x, int y, int[][] rect) {
		int[][] array = getBlock(rotateNow);
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] == 1) {
					if (x + j < 0 || x + j >= rect.length || y + i >= rect[0].length || rect[x + j][y + i] == 1)
						return false;
				}
			}
		}
		return true;
	}

	public boolean checkRotate(int x, int y, int rotateNum, int[][] rect) {
		int[][] array = getBlock(rotateNum);
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] == 1) {
					if (x + j < 0 || x + j >= rect.length || y >= rect[0].length || rect[x + j][y + i] == 1)
						return false;
				}
			}
		}

		return true;
	}

	public void quickDrop(int[][] rect) {
		int[][] array = getBlock(rotateNow);
		for (int i = 0; i < rect[0].length - 1; i++) {
			if (!checkMove(x, i, rect)) {
				y = i - 1;
				break;
			} else {
				y = i;
			}
		}

		setRect(rect, array);
		setIsDrop(false);
	}

	public void moveLeft(int[][] rect) {
		if (checkMove(x - 1, y, rect))
			x = x - 1;
	}

	public void moveRight(int[][] rect) {
		if (checkMove(x + 1, y, rect))
			x = x + 1;
	}

	public void moveDown(int[][] rect) {
		if (checkMove(x, y + 1, rect))
			y = y + 1;
	}

	public void rotate(int[][] rect) {
		if (checkRotate(x, y, (rotateNow + 1) % rotateNum, rect))
			rotateNow = (rotateNow + 1) % rotateNum;
		else if (checkRotate(x - 1, y, (rotateNow + 1) % rotateNum, rect)) {
			x--;
			rotateNow = (rotateNow + 1) % rotateNum;
		} else if (checkRotate(x + 1, y, (rotateNow + 1) % rotateNum, rect)) {
			x++;
			rotateNow = (rotateNow + 1) % rotateNum;
		}
	}

	public void rotateReverse(int[][] rect) {
		if (checkRotate(x, y, (rotateNow - 1 + rotateNum) % rotateNum, rect))
			rotateNow = (rotateNow - 1 + rotateNum) % rotateNum;
		else if (checkRotate(x - 1, y, (rotateNow - 1 + rotateNum) % rotateNum, rect)) {
			x--;
			rotateNow = (rotateNow - 1 + rotateNum) % rotateNum;
		} else if (checkRotate(x + 1, y, (rotateNow - 1 + rotateNum) % rotateNum, rect)) {
			x++;
			rotateNow = (rotateNow - 1 + rotateNum) % rotateNum;
		}
	}

	public int[][] getBlock(int rotateNumber) {
		return block[rotateNumber];
	}

	public void drop() {
		if (isDrop)
			y += 1;
	}

	public boolean isDrop() {
		return this.isDrop;
	}

	public boolean isStop() {
		return this.isStop;
	}
	public boolean isOver() {
		return this.isOver;
	}

	public void setIsDrop(boolean isDrop) {
		this.isDrop = isDrop;
	}

	public void screenDraw(Graphics g, int[][] rect) {

		int[][] array = getBlock(rotateNow);

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				if (array[i][j] == 1 && y + i >= 0) {
					if ((y + i >= rect[0].length || rect[x + j][y + i] == 1)
							&& (i + 1 >= array.length || array[i + 1][j] == 0)) {
						if (isDrop())
							y--;
						setRect(rect, array);
						setIsDrop(false);
						break;
					}
					g.drawImage(basicBlock, (x + j + width) * blockSize, (y + i + height) * blockSize, null);
				}else if(y + i < 0) {
					isOver = true;
				}

			}
		}
	}

	public void setRect(int[][] rect, int[][] array) {
		this.rect = rect;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				try {
					if (array[i][j] == 1)
						this.rect[x + j][y + i] = 1;					
				}catch(Exception e) {
					
				}
			}
		}
	}

	public void clearLine(int[][] rect) {
		int i = 0;
		int j = 0;
		int[] newLine = new int[rect.length];
		for (j = 0; j < rect[0].length; j++) {
			for (i = 0; i < rect.length; i++) {
				if (rect[i][j] == 0)
					break;
			}
			if (i == rect.length) {
				for (int k = j; k > 0; k--) {
					for (i = 0; i < rect.length; i++) {
						rect[i][k] = rect[i][k - 1];
					}
				}
				j--;
			}

		}
		if (i == rect.length) {
		}
	}

	public int[][] getRect() {
		return rect;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(SLEEP_TIME);
				drop();
				if (!isDrop) {
					isStop = true;
					break;
				}

			} catch (Exception e) {

			}
		}
	}
}
