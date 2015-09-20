package fr.eyzox.forgecreeperheal.healtimeline.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner.BlockBannerHanging;
import net.minecraft.block.BlockBanner.BlockBannerStanding;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healtimeline.factory.impl.HealableBedFactory;
import fr.eyzox.forgecreeperheal.healtimeline.factory.impl.HealableDefaultFactory;
import fr.eyzox.forgecreeperheal.healtimeline.factory.impl.HealableDoorFactory;
import fr.eyzox.forgecreeperheal.healtimeline.factory.impl.HealableFacingFactory;
import fr.eyzox.forgecreeperheal.healtimeline.factory.impl.HealableLeverFactory;
import fr.eyzox.forgecreeperheal.healtimeline.factory.impl.HealableOppositeFacingFactory;
import fr.eyzox.forgecreeperheal.healtimeline.factory.impl.HealableSupportByBottomFactory;
import fr.eyzox.forgecreeperheal.healtimeline.factory.impl.HealableVineFactory;
import fr.eyzox.forgecreeperheal.healtimeline.healable.IHealable;

public class HealableFactories {

	private static final HealableFactories INSTANCE = loadDefaultConfig();
	private final Map<Class<? extends Block>, IHealableFactory> factories = new HashMap<Class<? extends Block>, IHealableFactory>();
	private final List<IHealableFactory> factoriesList = new LinkedList<IHealableFactory>();
	private final IHealableFactory defaultFactory = new HealableDefaultFactory();

	private HealableFactories() {
	}

	public IHealable create(World world, BlockPos pos, IBlockState state) {
		IHealableFactory factory = factories.get(state.getBlock().getClass());
		if(factory == null) {
			for(IHealableFactory factoryFromList : factoriesList) {
				if(factoryFromList.accept().isAssignableFrom(state.getBlock().getClass())) {
					factory = factoryFromList;
					break;
				}
			}
			
			if(factory == null){
				factory = defaultFactory;
			}
			
			putCache(state.getBlock().getClass(), factory);
			
		}

		return  factory.create(world, pos, state);
	}

	public static HealableFactories getInstance() {
		return INSTANCE;
	}

	public void register(IHealableFactory factory) {
		factoriesList.add(factory);
		ForgeCreeperHeal.getLogger().info("IHealableFactory registered : "+factory.getClass().getSimpleName()+" : "+factory.accept().getSimpleName());
	}

	private void putCache(Class<? extends Block> clazz, IHealableFactory factory) {
		factories.put(clazz, factory);
		ForgeCreeperHeal.getLogger().info("Put "+factory.getClass().getSimpleName()+" : "+factory.accept().getSimpleName()+" cached for "+clazz.getSimpleName());
	}
	
	public void unregister(IHealableFactory factory) {
		factoriesList.remove(factory);
		Iterator<IHealableFactory> it = factories.values().iterator();
		while(it.hasNext()) {
			IHealableFactory registeredFactory = it.next();
			if(factory.equals(registeredFactory)) {
				it.remove();
			}
		}
	}

	public void clearCache() {
		factories.clear();
	}

	public static HealableFactories loadDefaultConfig() {
		HealableFactories hf = new HealableFactories();
		
		hf.register(new HealableDoorFactory());
		hf.register(new HealableBedFactory());
		hf.register(new HealableFacingFactory(BlockTorch.class, BlockTorch.FACING));
		hf.register(new HealableFacingFactory(BlockLadder.class, BlockLadder.FACING));
		hf.register(new HealableFacingFactory(BlockWallSign.class, BlockWallSign.FACING));
		hf.register(new HealableFacingFactory(BlockTrapDoor.class, BlockTrapDoor.FACING));
		hf.register(new HealableFacingFactory(BlockButton.class, BlockButton.FACING));
		hf.register(new HealableFacingFactory(BlockBannerHanging.class, BlockBannerHanging.FACING));
		hf.register(new HealableFacingFactory(BlockTripWireHook.class, BlockTripWireHook.FACING));
		hf.register(new HealableVineFactory());
		hf.register(new HealableLeverFactory());
		hf.register(new HealableOppositeFacingFactory(BlockCocoa.class, BlockCocoa.FACING));
		hf.register(new HealableSupportByBottomFactory(BlockFalling.class));
		hf.register(new HealableSupportByBottomFactory(BlockBasePressurePlate.class));
		hf.register(new HealableSupportByBottomFactory(BlockBannerStanding.class));
		hf.register(new HealableSupportByBottomFactory(BlockRedstoneDiode.class));
		hf.register(new HealableSupportByBottomFactory(BlockRedstoneWire.class));
		hf.register(new HealableSupportByBottomFactory(BlockStandingSign.class));
		hf.register(new HealableSupportByBottomFactory(BlockCrops.class));
		hf.register(new HealableSupportByBottomFactory(BlockCactus.class));
		hf.register(new HealableSupportByBottomFactory(BlockRailBase.class));
		hf.register(new HealableSupportByBottomFactory(BlockReed.class));
		hf.register(new HealableSupportByBottomFactory(BlockSnow.class));
		hf.register(new HealableSupportByBottomFactory(BlockTripWire.class));
		hf.register(new HealableSupportByBottomFactory(BlockCake.class));
		hf.register(new HealableSupportByBottomFactory(BlockCarpet.class));
		hf.register(new HealableSupportByBottomFactory(BlockDragonEgg.class));
		hf.register(new HealableSupportByBottomFactory(BlockFlowerPot.class));
		
		return hf;
		
	}

}
