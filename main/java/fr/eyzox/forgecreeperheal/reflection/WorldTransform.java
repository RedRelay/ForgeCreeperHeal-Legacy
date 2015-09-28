package fr.eyzox.forgecreeperheal.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;

public class WorldTransform {

	private static Method isValidMethod = Reflect.getMethodForClass(World.class, "isValid", "func_175701_a");
	
	private final World world;
	
	private Map<Chunk, ChunkTransform> cachedChunkTransform = new HashMap<Chunk, ChunkTransform>();
	
	
	public WorldTransform(World world) {
		this.world = world;
	}
	
	public boolean removeSilentBlockState(BlockPos pos) {
		
		final IBlockState newState = Blocks.air.getDefaultState();
		final int flags = 2;
		
		if(!((Boolean)Reflect.call(world, isValidMethod, pos)).booleanValue())
		{
			return false;
		}
		else if(!this.world.isRemote && this.world.getWorldInfo().getTerrainType() == WorldType.DEBUG_WORLD)
		{
			return false;
		}
		else
		{
			Chunk chunk = this.world.getChunkFromBlockCoords(pos);
			Block block = newState.getBlock();
	        net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;
	        if(this.world.captureBlockSnapshots && !this.world.isRemote)
	        {
	        	blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(this.world, pos, flags);
                this.world.capturedBlockSnapshots.add(blockSnapshot);
	        }
	        
	        int oldLight = world.getBlockState(pos).getBlock().getLightValue(this.world, pos);
            
            //##TRANSFORM[IBlockState iblockstate1 = chunk.setBlockState(pos, newState);]:BEGIN##//
            
            ChunkTransform chunkTransform = this.cachedChunkTransform.get(chunk);
            if(chunkTransform == null) {
            	chunkTransform = new ChunkTransform(chunk);
            	this.cachedChunkTransform.put(chunk, chunkTransform);
            }
            
            IBlockState iblockstate1 = chunkTransform.setBlockState(pos, newState);
            
            //##TRANSFORM:END##//

            if (iblockstate1 == null)
            {
                if (blockSnapshot != null) this.world.capturedBlockSnapshots.remove(blockSnapshot);
                return false;
            }
            
            else
            {
                Block block1 = iblockstate1.getBlock();

                if (block.getLightOpacity() != block1.getLightOpacity() || block.getLightValue(this.world, pos) != oldLight)
                {
                    this.world.theProfiler.startSection("checkLight");
                    this.world.checkLight(pos);
                    this.world.theProfiler.endSection();
                }

                if (blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    this.world.markAndNotifyBlock(pos, chunk, iblockstate1, newState, flags); // Modularize client and physic updates
                }

                return true;
            }
		}
	}
	
	public void clearCache() {
		cachedChunkTransform.clear();
	}
	
}
