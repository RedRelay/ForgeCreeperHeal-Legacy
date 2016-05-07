package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.blockdata.DefaultBlockData;
import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.blockdata.TileEntityBlockData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class DefaultBlockDataBuilder implements IBlockDataBuilder {

	public DefaultBlockDataBuilder() {}

	@Override
	public IBlockData create(World world, BlockPos pos, IBlockState state) {
		if(state.getBlock().isAir(world, pos) || ForgeCreeperHeal.getConfig().getHealException().contains(state.getBlock())) {
			return null;
		}
		
		final NBTTagCompound tileEntityNBT = BlockDataBuilderUtils.getTileEntityNBT(world, pos, state);
		if(tileEntityNBT != null) {
			return new TileEntityBlockData(pos, state, tileEntityNBT);
		}
		
		return new DefaultBlockData(pos, state);
	}

	@Override
	public IBlockData create(NBTTagCompound tag) {
		if(tag.hasKey(TileEntityBlockData.TAG_TILE_ENTITY)) {
			return new TileEntityBlockData(tag);
		}else {
			return new DefaultBlockData(tag);
		}
	}
	
	@Override
	public boolean accept(Class<? extends Block> clazz) {
		return true;
	}
}
