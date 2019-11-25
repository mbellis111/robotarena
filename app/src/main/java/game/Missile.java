package game;

public class Missile extends Bullet{
	private double radius;
	
	public Missile(Robot shotFrom, double direction) {
		super(shotFrom, direction);
		setDamage((double)Constants.MISSILE_DAMAGE);
		this.radius = Constants.MISSILE_EXPLODE_RADIUS;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}	
}
