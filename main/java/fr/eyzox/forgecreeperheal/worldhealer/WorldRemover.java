package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.Collection;

import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;
import fr.eyzox.forgecreeperheal.reflection.WorldTransform;
import fr.eyzox.timeline.ICollector;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldRemover implements ICollector<BlockPos> {

	private final WorldTransform worldTransform;
	
	public WorldRemover(final World world) {
		this.worldTransform = new WorldTransform(world);
	}
			
	@Override
	public void collect(BlockPos value) {
		this.worldTransform.removeSilentBlockState(value);
		
	}
	
	public synchronized void process(final Collection<IHealableBlock> healableBlocks) {
		for(IHealableBlock healableBlock : healableBlocks) {
			healableBlock.collectBlockPos(this);
		}
		this.worldTransform.clearCache();
	}

}
