package Block;

public class OBlock extends Block{
	public OBlock() {
		super();
		this.block = new int[][][] {
			{
				{1,1},
				{1,1}				
			}
		};
		rotateNum = 1;
		rotateNow = 0;
		arrayHeight = 2;
		arrayWidth = 2;
		color = 3;
	}
	public int[][] getBlock(){
		return block[rotateNow];
	}
}
