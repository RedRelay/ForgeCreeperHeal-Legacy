package fr.eyzox.forgecreeperheal.healtimeline.factory.impl;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealableBlock;
import fr.eyzox.forgecreeperheal.healtimeline.healable.impl.HealableFacingBlock;

public class HealableFacingFactory extends AbstractHealableFactory {

	protected final PropertyDirection facingProperty;
	
	public HealableFacingFactory(Class<? extends Block> clazz, PropertyDirection facingProperty) {
		super(clazz);
		this.facingProperty = facingProperty;
	}
	
	@Override
	public IHealableBlock create(World world, BlockPos pos, IBlockState state) {
		return new HealableFacingBlock(facingProperty, world, pos, state);
	}
}
