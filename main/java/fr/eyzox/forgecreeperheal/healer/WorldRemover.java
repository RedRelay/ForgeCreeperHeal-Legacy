package fr.eyzox.forgecreeperheal.healer;

import fr.eyzox.forgecreeperheal.reflection.WorldTransform;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldRemover {

	private final WorldTransform worldTransform;
	
	public WorldRemover(final World world) {
		this.worldTransform = new WorldTransform(world);
	}
	
	public void remove(final BlockPos pos) {
		this.worldTransform.removeSilentBlockState(pos);
	}

}
