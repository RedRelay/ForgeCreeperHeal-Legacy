package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealerUtils;

public class HealableBlock implements IHealable{
	private IBlockState state;
	private NBTTagCompound tileEntityTag;
	private BlockPos pos;

	public HealableBlock(World world, BlockPos pos, IBlockState state) {
		this.state = state;
		this.pos = pos;
		TileEntity te = getTileEntityToStore(world, pos, state);
		if(te != null) {
			this.tileEntityTag = new NBTTagCompound();
			te.writeToNBT(tileEntityTag);
		}
	}

	public IBlockState getBlockState() {
		return state;
	}

	public NBTTagCompound getTileEntityTag() {
		return tileEntityTag;
	}

	public void readFromNBT(NBTTagCompound tag) {
		/*
		BlockData prevBlockData = null;
		BlockData currentBlockData = this;
		NBTTagCompound cursorTag = tag;
		
		while(!(cursorTag = cursorTag.getCompoundTag("BlockData")).hasNoTags()) {
			Block block = Block.getBlockFromName(cursorTag.getString("block"));
			if(block != null) {
				currentBlockData.blockState = block.getStateFromMeta(cursorTag.getInteger("metadata"));
				currentBlockData.blockPos = BlockPos.fromLong(cursorTag.getLong("coords"));
				currentBlockData.tileEntityTag = cursorTag.getCompoundTag("tileentity");
				if(currentBlockData.tileEntityTag.hasNoTags()) currentBlockData.tileEntityTag = null;
			}
			
			if(prevBlockData != null) {
				prevBlockData.next = currentBlockData;
			}
			
			prevBlockData = currentBlockData;
		}
		*/
	}

	public void writeToNBT(NBTTagCompound tag) {
/*
		BlockData cursorBlockData = this;
		NBTTagCompound cursorTag = tag;

		do {

			NBTTagCompound blockDataTag = new NBTTagCompound();
			cursorTag.setTag("BlockData", blockDataTag);

			blockDataTag.setString("block", GameData.getBlockRegistry().getNameForObject(cursorBlockData.blockState.getBlock()).toString());
			int metadata = cursorBlockData.blockState.getBlock().getMetaFromState(cursorBlockData.blockState);
			if(metadata != 0) {
				blockDataTag.setInteger("metadata", metadata);
			}
			blockDataTag.setLong("coords", cursorBlockData.blockPos.toLong());
			if(cursorBlockData.tileEntityTag != null) {
				blockDataTag.setTag("tileentity", cursorBlockData.tileEntityTag);
			}

			
			cursorTag = blockDataTag;

		}while((cursorBlockData = cursorBlockData.getNext()) != null);*/
	}

	public void setBlockState(IBlockState blockState) {
		this.state = blockState;
	}

	public void setTileEntityTag(NBTTagCompound tileEntityTag) {
		this.tileEntityTag = tileEntityTag;
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public Object getKey() {
		return pos;
	}
	
	@Override
	public Object[] getDependencies() {
		return null;
	}

	@Override
	public boolean isAvailable(Collection<Object> dependenciesLeft) {
		return true;
	}

	@Override
	public void heal(World world, int flag) {
		Block currentBlock = world.getBlockState(pos).getBlock();
		boolean isAir = currentBlock.isAir(world, pos);

		if(ForgeCreeperHeal.getConfig().isOverride() || isAir || (ForgeCreeperHeal.getConfig().isOverrideFluid() && FluidRegistry.lookupFluidForBlock(currentBlock) != null)) {
			if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock() && !isAir) {
				world.spawnEntityInWorld(WorldHealerUtils.getEntityItem(world, pos, new ItemStack(currentBlock), world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));

				TileEntity te = world.getTileEntity(pos);
				if(te instanceof IInventory) {
					WorldHealerUtils.dropInventory(world, pos, (IInventory) te);
				}
			}

			world.setBlockState(pos, state, flag);

			if(tileEntityTag != null) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null) {
					te.readFromNBT(tileEntityTag);
					world.setTileEntity(pos, te);
				}
			}

		}else if(ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock()){
			world.spawnEntityInWorld(WorldHealerUtils.getEntityItem(world, pos, new ItemStack(state.getBlock()),world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F));
			if(tileEntityTag != null) {
				TileEntity te = state.getBlock().createTileEntity(world, state);
				if(te instanceof IInventory) {
					te.readFromNBT(tileEntityTag);
					WorldHealerUtils.dropInventory(world, pos, (IInventory) te);
				}
			}

		}
		
	}
	
	protected TileEntity getTileEntityToStore(World world, BlockPos blockPos, IBlockState blockstate) {
		TileEntity tileEntity = world.getTileEntity(blockPos);
		if(tileEntity instanceof IInventory) {
			if(ForgeCreeperHeal.getConfig().isDropItemsFromContainer()) {
				NBTTagCompound buf = new NBTTagCompound();
				tileEntity.writeToNBT(buf);
				tileEntity = blockstate.getBlock().createTileEntity(world, blockstate);
				tileEntity.readFromNBT(buf);
			}
			((IInventory)tileEntity).clear();
		}
		return tileEntity;
	}

	@Override
	public void removeFromWorld(World world) {
		world.setBlockState(pos, Blocks.air.getDefaultState(), 2);
	}

	@Override
	public String toString() {
		return "HealableBlock [Block=" + state.getBlock().getLocalizedName() + ", TileEntity="
				+ (tileEntityTag != null) + ", pos=" + pos + "]";
	}
	
	

}
