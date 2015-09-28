package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableBedBlock;

public class HealableBedFactory extends AbstractHealableFactory {

	public HealableBedFactory() {
		super(BlockBed.class);
	}

	@Override
	public IHealableBlock create(World world, BlockPos pos, IBlockState state) {
		ForgeCreeperHeal.getLogger().info("HealableBedFactory called for creating a new HealableBedBlock");
		if(state.getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD) {
			ForgeCreeperHeal.getLogger().info("HealableBedFactory : HealableBedBlock created");
			return new HealableBedBlock(world, pos, state);
		}
		ForgeCreeperHeal.getLogger().info("HealableBedFactory : HealableBedBlock not created");
		return null;
	}

}
