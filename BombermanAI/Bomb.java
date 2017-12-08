

public class Bomb extends CellObject{

	private int power;

	private int count;

	public Bomb(boolean passable, boolean destructible, boolean bombUp, boolean powerUp, boolean danger, int power, int count){
		super(passable, destructible, bombUp, powerUp, danger);

		this.power = power;
		this.count = count;	
	}

	public int getPower()
	{
		return power;
	}

	public int count()
	{
		return count;
	}	
}