package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableOppositeFacingBlock;

public class HealableOppositeFacingFactory extends HealableFacingFactory {

	public HealableOppositeFacingFactory(Class<? extends Block> clazz, PropertyDirection facingProperty) {
		super(clazz, facingProperty);
	}

	@Override
	public IHealable create(World world, BlockPos pos, IBlockState state) {
		return new HealableOppositeFacingBlock(facingProperty, world, pos, state);
	}
	
	

}
