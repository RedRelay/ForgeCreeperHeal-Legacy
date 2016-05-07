package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.blockdata.TileEntityBlockData;
import fr.eyzox.forgecreeperheal.builder.AbstractFactoryBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityBlockDataBuilder extends AbstractFactoryBuilder implements IBlockDataBuilder {

	public TileEntityBlockDataBuilder(Class<? extends Block> clazz) {
		super(clazz);
	}

	@Override
	public IBlockData create(World w, BlockPos pos, IBlockState state) {
		return create(w, pos, state, BlockDataBuilderUtils.getTileEntityNBT(w, pos, state));
	}
	
	public IBlockData create(World w, BlockPos pos, IBlockState state, NBTTagCompound tileEntity) {
		return new TileEntityBlockData(pos, state, tileEntity);
	}

	@Override
	public IBlockData create(NBTTagCompound tag) {
		return new TileEntityBlockData(tag);
	}

}
