package Block;

public class RZBlock extends Block{
	public RZBlock() {
		super();
		this.block = new int[][][] {
			{
				{1,1,0},
				{0,1,1},
				{0,0,0}				
			},
			{
				{0,1,0},
				{1,1,0},
				{1,0,0}	
			}
		};
		rotateNum = 2;
		rotateNow = 0;
		arrayHeight = 3;
		arrayWidth = 3;
		color = 5;
	}
	public int[][] getBlock(){
		return block[rotateNow];
	}
}
