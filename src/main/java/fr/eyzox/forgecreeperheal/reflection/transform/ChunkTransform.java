package fr.eyzox.forgecreeperheal.reflection.transform;

import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import fr.eyzox.forgecreeperheal.reflection.ReflectionHelper;
import fr.eyzox.forgecreeperheal.reflection.ReflectionManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkTransform {
	
	private final static Field FIELD_PRECIPITATION_HEIGHT_MAP = ReflectionManager.getInstance().getField(Chunk.class, "precipitationHeightMap");
	private final static Method METHOD_RELIGHT_BLOCK = ReflectionManager.getInstance().getMethod(Chunk.class, "relightBlock", new Class<?>[]{int.class,int.class,int.class});
	private final static Method METHOD_PROPAGATE_SKYLIGHT_OCCLUSION = ReflectionManager.getInstance().getMethod(Chunk.class, "propagateSkylightOcclusion", new Class<?>[]{int.class,int.class});
	
	private final Chunk chunk;
	
	private final int[] precipitationHeightMap;
	private final int[] heightMap;
	private final World worldObj;
	private final ExtendedBlockStorage[] storageArrays;
	
	
	public ChunkTransform(final Chunk chunk) {
		this.chunk = chunk;
		this.precipitationHeightMap = (int[]) ReflectionHelper.get(FIELD_PRECIPITATION_HEIGHT_MAP, this.chunk);
		this.heightMap = this.chunk.getHeightMap();
		this.worldObj = this.chunk.getWorld();
		this.storageArrays = this.chunk.getBlockStorageArray();
	}
	
	private IBlockState getBlockState(BlockPos pos) {
		return this.chunk.getBlockState(pos);
	}
	
	private TileEntity getTileEntity(BlockPos pos, Chunk.EnumCreateEntityType p_177424_2_) {
		return this.chunk.getTileEntity(pos, p_177424_2_);
	}
	
	private void generateSkylightMap() {
		this.chunk.generateSkylightMap();
	}
	
	private void relightBlock(int x, int y, int z) {
		ReflectionHelper.call(this.chunk, METHOD_RELIGHT_BLOCK, x, y, z);
	}
	
	private int getLightFor(EnumSkyBlock p_177413_1_, BlockPos pos){
		return this.chunk.getLightFor(p_177413_1_, pos);
	}
	
	private void propagateSkylightOcclusion(int x, int z){
		ReflectionHelper.call(this.chunk, METHOD_PROPAGATE_SKYLIGHT_OCCLUSION, x, z);
	}
	
	private void _isModified(boolean v) {
		this.chunk.setModified(true);
	}
	
	/**
	 * 
	 * From {@link net.minecraft.world.chunk.Chunk#setBlockState(BlockPos, IBlockState)}
	 * Edition : Don't call {@link Block#breakBlock(net.minecraft.world.World, BlockPos, IBlockState)} 
	 * 
	 * @param pos
	 * @param state
	 * @return
	 */
	public IBlockState setBlockState(BlockPos pos, IBlockState state) {
		
		int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;
        int l = k << 4 | i;

        if (j >= this.precipitationHeightMap[l] - 1)
        {
            this.precipitationHeightMap[l] = -999;
        }

        int i1 = this.heightMap[l];
        IBlockState iblockstate = this.getBlockState(pos);

        if (iblockstate == state)
        {
            return null;
        }
        else
        {
            Block block = state.getBlock();
            Block block1 = iblockstate.getBlock();
            int k1 = iblockstate.getLightOpacity(this.worldObj, pos); // Relocate old light value lookup here, so that it is called before TE is removed.
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
            boolean flag = false;

            if (extendedblockstorage == NULL_BLOCK_STORAGE)
            {
                if (block == Blocks.AIR)
                {
                    return null;
                }

                extendedblockstorage = this.storageArrays[j >> 4] = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
                flag = j >= i1;
            }

            extendedblockstorage.set(i, j & 15, k, state);

            //if (block1 != block)
            {
                if (!this.worldObj.isRemote)
                {
                	/* ====== REMOVED ===========
                    if (block1 != block) //Only fire block breaks when the block changes.
                    block1.breakBlock(this.worldObj, pos, iblockstate);
                    ============================= */
                    TileEntity te = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.worldObj, pos, iblockstate, state)) this.worldObj.removeTileEntity(pos);
                }
                else if (block1.hasTileEntity(iblockstate))
                {
                    TileEntity te = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.worldObj, pos, iblockstate, state))
                    this.worldObj.removeTileEntity(pos);
                }
            }

            if (extendedblockstorage.get(i, j & 15, k).getBlock() != block)
            {
                return null;
            }
            else
            {
                if (flag)
                {
                    this.generateSkylightMap();
                }
                else
                {
                    int j1 = state.getLightOpacity(this.worldObj, pos);

                    if (j1 > 0)
                    {
                        if (j >= i1)
                        {
                            this.relightBlock(i, j + 1, k);
                        }
                    }
                    else if (j == i1 - 1)
                    {
                        this.relightBlock(i, j, k);
                    }

                    if (j1 != k1 && (j1 < k1 || this.getLightFor(EnumSkyBlock.SKY, pos) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, pos) > 0))
                    {
                        this.propagateSkylightOcclusion(i, k);
                    }
                }

                // If capturing blocks, only run block physics for TE's. Non-TE's are handled in ForgeHooks.onPlaceItemIntoWorld
                if (!this.worldObj.isRemote && block1 != block && (!this.worldObj.captureBlockSnapshots || block.hasTileEntity(state)))
                {
                    block.onBlockAdded(this.worldObj, pos, state);
                }

                if (block.hasTileEntity(state))
                {
                    TileEntity tileentity1 = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity1 == null)
                    {
                        tileentity1 = block.createTileEntity(this.worldObj, state);
                        this.worldObj.setTileEntity(pos, tileentity1);
                    }

                    if (tileentity1 != null)
                    {
                        tileentity1.updateContainingBlockInfo();
                    }
                }

                this._isModified(true);
                return iblockstate;
            }
		
        }
	}
}
