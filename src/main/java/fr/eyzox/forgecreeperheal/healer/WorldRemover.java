package fr.eyzox.forgecreeperheal.healer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.reflection.transform.WorldTransform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
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
		
		IBlockState oldState = world.getBlockState(pos);
		
		if(oldState.getBlock().hasTileEntity(oldState)) {
			TileEntity te = world.getTileEntity(pos);
			if(te != null) {
				this.onTileEntityRemoved(te);
			}
		}
		
		this.oldStates.put(pos, oldState);
		this.worldTransform.removeSilentBlockState(pos, 0);
		world.removeTileEntity(pos);
	}
	
	public void update(final int flags) {
		for(final Entry<BlockPos, IBlockState> entry : oldStates.entrySet()) {
			this.world.markAndNotifyBlock(entry.getKey(), world.getChunkFromBlockCoords(entry.getKey()), entry.getValue(), Blocks.AIR.getDefaultState(), flags);
		}
		oldStates.clear();
	}

	private void onTileEntityRemoved(TileEntity tileEntity) {
		if(ForgeCreeperHeal.getConfig().isDropItems() && tileEntity instanceof IInventory) {
			InventoryHelper.dropInventoryItems(tileEntity.getWorld(), tileEntity.getPos(), (IInventory) tileEntity);
		}
	}
	
}
