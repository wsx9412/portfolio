package Block;

public class LBlock extends Block{
	public LBlock() {
		super();
		this.block = new int[][][] {
			{
				{1,0,0},
				{1,0,0},
				{1,1,0}				
			},
			{
				{1,1,1},
				{1,0,0},
				{0,0,0}	
			},
			{
				{1,1,0},
				{0,1,0},
				{0,1,0}	
			},
			{
				{0,0,1},
				{1,1,1},
				{0,0,0}	
			}
		};
		rotateNum = 4;
		rotateNow = 0;
		arrayHeight = 3;
		arrayWidth = 3;
		color = 2;
	}
	public int[][] getBlock(){
		return block[rotateNow];
	}
}
