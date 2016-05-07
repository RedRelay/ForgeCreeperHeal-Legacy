package fr.eyzox.forgecreeperheal.builder.blockdata;

import fr.eyzox.forgecreeperheal.blockdata.IBlockData;
import fr.eyzox.forgecreeperheal.builder.ISerializableHealableBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IBlockDataBuilder extends ISerializableHealableBuilder<Class<? extends Block>> {
	public IBlockData create(final World w, final BlockPos pos, final IBlockState state);
	
}
