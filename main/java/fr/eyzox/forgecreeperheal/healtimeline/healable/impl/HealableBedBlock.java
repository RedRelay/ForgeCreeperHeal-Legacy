package fr.eyzox.forgecreeperheal.healtimeline.healable.impl;

import java.util.Collection;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HealableBedBlock extends HealableMultiBlock {

	private final BlockPos footPos;
	
	public HealableBedBlock(World world, BlockPos pos, IBlockState state) {
		super(world, pos, state);
		footPos = pos.offset(((EnumFacing) state.getValue(BlockBed.FACING)).getOpposite());
		getLinkedHealables().put(footPos, world.getBlockState(footPos));
	}

	@Override
	public Object[] getDependencies() {
		return new BlockPos[]{HealableSupportByBottomBlock.getDependencies(this.getPos()), HealableSupportByBottomBlock.getDependencies(this.footPos)};
	}

	@Override
	public boolean isAvailable(Collection<Object> dependenciesLeft) {
		return !(dependenciesLeft.contains(getPos()) || dependenciesLeft.contains(footPos));
	}
	
	
	
	

}
