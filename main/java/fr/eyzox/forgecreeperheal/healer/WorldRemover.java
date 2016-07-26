package fr.eyzox.forgecreeperheal.healer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.eyzox.forgecreeperheal.reflection.WorldTransform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldRemover {

	private final World world;
	private final WorldTransform worldTransform;
	private final Map<BlockPos, IBlockState> oldStates = new HashMap<BlockPos, IBlockState>();
	
	public WorldRemover(final World world) {
		this.world = world;
		this.worldTransform = new WorldTransform(world);
	}
	
	public void remove(final BlockPos pos) {
		this.oldStates.put(pos, world.getBlockState(pos));
		this.worldTransform.removeSilentBlockState(pos, 0);
	}
	
	public void update(final int flags) {
		for(final Entry<BlockPos, IBlockState> entry : oldStates.entrySet()) {
			this.world.markAndNotifyBlock(entry.getKey(), world.getChunkFromBlockCoords(entry.getKey()), entry.getValue(), Blocks.air.getDefaultState(), flags);
		}
		oldStates.clear();
	}

}
