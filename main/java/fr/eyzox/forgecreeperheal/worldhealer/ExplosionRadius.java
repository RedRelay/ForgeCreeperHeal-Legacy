package fr.eyzox.forgecreeperheal.worldhealer;

import net.minecraft.util.Vec3i;

public class ExplosionRadius {
	private Coords from, to;
	
	public ExplosionRadius(Vec3i base) {
		this(new Coords(base.getX(), base.getY(), base.getZ()), new Coords(base.getX(), base.getY(), base.getZ()));
	}
	
	public ExplosionRadius(Coords from, Coords to) {
		this.from = from;
		this.to = to;
	}
	
	public boolean isCollision(ExplosionRadius e) {
		 return to.getX() >= e.from.getX() && from.getX() <= e.to.getX()
			     && to.getY() >= e.from.getY() && from.getY() <= e.to.getY()
			     && to.getZ() >= e.from.getZ() && from.getZ() <= e.to.getZ();
	}
	
	public void tryMaximize(Vec3i p) {
		if(p.getX() < from.getX()) from.setX(p.getX());
		else if(p.getX() > to.getX()) to.setX(p.getX());
		
		if(p.getY() < from.getY()) from.setY(p.getY());
		else if(p.getY() > to.getY()) to.setY(p.getY());
		
		if(p.getZ() < from.getZ()) from.setZ(p.getZ());
		else if(p.getZ() > to.getZ()) to.setZ(p.getZ());
	}
}
