package fr.eyzox.forgecreeperheal.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkTransform {
	
	private final static Field FIELD_PRECIPITATION_HEIGHT_MAP = Reflect.getFieldForClass(Chunk.class, "precipitationHeightMap", "field_76638_b");
	private final static Method METHOD_RELIGHT_BLOCK = Reflect.getMethodForClass(Chunk.class, "relightBlock", "func_76615_h");
	private final static Method METHOD_PROPAGATE_SKYLIGHT_OCCLUSION = Reflect.getMethodForClass(Chunk.class, "propagateSkylightOcclusion", "func_76595_e");
	
	private final Chunk chunk;
	
	private final int[] precipitationHeightMap;
	
	public ChunkTransform(final Chunk chunk) {
		this.chunk = chunk;
		precipitationHeightMap = (int[]) Reflect.getDataFromField(FIELD_PRECIPITATION_HEIGHT_MAP, this.chunk);
	}
	
	public IBlockState setBlockState(BlockPos pos, IBlockState state) {
		int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;
        int l = k << 4 | i;

        if (j >= this.precipitationHeightMap[l] - 1)
        {
            this.precipitationHeightMap[l] = -999;
        }

        int i1 = this.chunk.getHeightMap()[l];
        IBlockState iblockstate1 = this.chunk.getBlockState(pos);

        if (iblockstate1 == state)
        {
            return null;
        }
        else
        {
            Block block = state.getBlock();
            Block block1 = iblockstate1.getBlock();
            ExtendedBlockStorage extendedblockstorage = this.chunk.getBlockStorageArray()[j >> 4];
            boolean flag = false;

            if (extendedblockstorage == null)
            {
                if (block == Blocks.air)
                {
                    return null;
                }

                extendedblockstorage = this.chunk.getBlockStorageArray()[j >> 4] = new ExtendedBlockStorage(j >> 4 << 4, !this.chunk.getWorld().provider.getHasNoSky());
                flag = j >= i1;
            }

            int j1 = block.getLightOpacity(this.chunk.getWorld(), pos);

            extendedblockstorage.set(i, j & 15, k, state);

            //if (block1 != block)
            {
                if (!this.chunk.getWorld().isRemote)
                {
                   
                	//##TRANSFORM[ if (iblockstate1.getBlock() != state.getBlock()) block1.breakBlock(this.chunk.getWorld(), pos, iblockstate1);  //Only fire block breaks when the block changes.]:BEGIN##//
                	//##TRANSFORM:END##//
                    	
                    TileEntity te = this.chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.chunk.getWorld(), pos, iblockstate1, state)) this.chunk.getWorld().removeTileEntity(pos);
                }
                else if (block1.hasTileEntity(iblockstate1))
                {
                    TileEntity te = this.chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.chunk.getWorld(), pos, iblockstate1, state))
                    this.chunk.getWorld().removeTileEntity(pos);
                }
            }

            if (extendedblockstorage.getBlockByExtId(i, j & 15, k) != block)
            {
                return null;
            }
            else
            {
                if (flag)
                {
                    this.chunk.generateSkylightMap();
                }
                else
                {
                    int k1 = block.getLightOpacity(this.chunk.getWorld(), pos);

                    if (j1 > 0)
                    {
                        if (j >= i1)
                        {
                        	Reflect.call(this.chunk, METHOD_RELIGHT_BLOCK, i, j + 1, k);
                        }
                    }
                    else if (j == i1 - 1)
                    {
                        Reflect.call(this.chunk, METHOD_RELIGHT_BLOCK, i, j, k);
                    }

                    if (j1 != k1 && (j1 < k1 || this.chunk.getLightFor(EnumSkyBlock.SKY, pos) > 0 || this.chunk.getLightFor(EnumSkyBlock.BLOCK, pos) > 0))
                    {
                    	Reflect.call(this.chunk, METHOD_PROPAGATE_SKYLIGHT_OCCLUSION, i, k);
                    }
                }

                TileEntity tileentity;

                if (!this.chunk.getWorld().isRemote && block1 != block)
                {
                    block.onBlockAdded(this.chunk.getWorld(), pos, state);
                }

                if (block.hasTileEntity(state))
                {
                    tileentity = this.chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity == null)
                    {
                        tileentity = block.createTileEntity(this.chunk.getWorld(), state);
                        this.chunk.getWorld().setTileEntity(pos, tileentity);
                    }

                    if (tileentity != null)
                    {
                        tileentity.updateContainingBlockInfo();
                    }
                }

                this.chunk.setModified(true);
                return iblockstate1;
            }
        }
	}
}
