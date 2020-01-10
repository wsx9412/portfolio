package Block;

public class ZBlock extends Block{
	public ZBlock() {
		super();
		this.block = new int[][][] {
			{
				{0,1,1},
				{1,1,0},
				{0,0,0}				
			},
			{
				{1,0,0},
				{1,1,0},
				{0,1,0}	
			}
		};
		rotateNum = 2;
		rotateNow = 0;
		arrayHeight = 3;
		arrayWidth = 3;
		color = 7;
	}
	public int[][] getBlock(){
		return block[rotateNow];
	}
}
