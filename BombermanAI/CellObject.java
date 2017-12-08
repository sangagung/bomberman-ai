

public class CellObject{
	protected boolean passable;

	protected boolean destructible;

	protected boolean bombUp;

	protected boolean powerUp;

	protected boolean danger;

	public CellObject(boolean passable, boolean destructible, boolean bombUp, boolean powerUp, boolean danger){
		this.passable = passable;
		this.destructible = destructible;
		this.bombUp = bombUp;
		this.powerUp = powerUp;
		this.danger = danger;
	}

	public boolean isPassable(){
		return passable;
	}

	public boolean isDestructible(){
		return destructible;
	}

	public boolean hasBombUp(){
		return bombUp;
	}

	public boolean hasPowerUp(){
		return powerUp;
	}

	public boolean isDangerous(){
		return danger;
	}
}