package game;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class Bullet {
	private double x, y, direction, damage;
	private Robot shotFrom;
	private BitmapDrawable picture = null;
	
	
	public Bullet(Robot shotFrom, double direction) {
		this.shotFrom = shotFrom;
		this.direction = direction;
		this.x = shotFrom.getX()+shotFrom.getPicture().getBitmap().getWidth()/2;
		this.y = shotFrom.getY()+shotFrom.getPicture().getBitmap().getHeight()/2;
		this.damage = shotFrom.getDamage();
	}
	
	public Bullet(Robot shotFrom, double direction, BitmapDrawable picture) {
		this(shotFrom, direction);
		this.setPicture(picture);
	}
	
	public void draw(Canvas c) {
		 c.drawBitmap(picture.getBitmap(), (float)x, (float)y, null);
	}
	
	public Robot getShotFrom() {
		return shotFrom;
	}
	
	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	public double getDamage() {
		return damage;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDirection() {
		return direction;
	}

	public BitmapDrawable getPicture() {
		return picture;
	}

	public void setPicture(BitmapDrawable picture) {
		this.picture = picture;
	}
}
