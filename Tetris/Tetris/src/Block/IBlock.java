package Block;

public class IBlock extends Block{
	public IBlock() {
		super();
		this.block = new int[][][] {
			{
				{1,0,0,0},
				{1,0,0,0},
				{1,0,0,0},
				{1,0,0,0}				
			},
			{
				{1,1,1,1},
				{0,0,0,0},
				{0,0,0,0},
				{0,0,0,0}
			}
		};
		rotateNum = 2;
		rotateNow = 0;
		arrayHeight = 4;
		arrayWidth = 4;
		color = 1;
	}
	public int[][] getBlock(){
		return block[rotateNow];
	}

}
