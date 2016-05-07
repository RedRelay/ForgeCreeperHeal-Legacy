package fr.eyzox.forgecreeperheal.healer;

import java.util.Collection;

import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.reflection.WorldTransform;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldRemover {

	private final WorldTransform worldTransform;
	
	public WorldRemover(final World world) {
		this.worldTransform = new WorldTransform(world);
	}
	
	public void process(final Collection<? extends IBlockData> healableBlocks) {
		for(IBlockData healableBlock : healableBlocks) {
			for(final BlockPos pos : healableBlock.getAllPos()){
				this.worldTransform.removeSilentBlockState(pos);
			}
		}
		this.worldTransform.clearCache();
	}

}
