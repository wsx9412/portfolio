import Block.*;

public class BlockFactory {
	public static Block create(int i) {
		// (int)(Math.random() * 7 )
		switch (i) {
		case 0:
			return new IBlock();
		case 1:
			return new LBlock();
		case 2:
			return new OBlock();
		case 3:
			return new RLBlock();
		case 4:
			return new RZBlock();
		case 5:
			return new TBlock();
		case 6:
			return new ZBlock();
		default:
			return new IBlock();
		}
	}

	BlockFactory() {

	}
}
