package fr.eyzox.forgecreeperheal.healtimeline;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
import net.minecraft.util.BlockPos;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.healtimeline.requirement.factory.FacingRequirementFactory;
import fr.eyzox.forgecreeperheal.healtimeline.requirement.factory.OppositeFacingRequirementFactory;
import fr.eyzox.forgecreeperheal.healtimeline.requirement.factory.SupportByBottomRequirementFactory;
import fr.eyzox.forgecreeperheal.healtimeline.requirement.factory.VineRequirementFactory;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;
import fr.eyzox.timeline.ITimelineSelector;
import fr.eyzox.timeline.Key;
import fr.eyzox.timeline.Timeline;
import fr.eyzox.timeline.factory.IRequirementFactory;

public class HealTimeline extends Timeline<BlockPos, BlockData> {
	
	private static LinkedList<IRequirementFactory<BlockPos, BlockData>> requirementFactories = loadFactories();
	private static ITimelineSelector randomSelector = new ITimelineSelector() {
		private Random rdn = new Random();
		@Override
		public <K, V> int select(List<Key<K, V>> availableItems) {
			return rdn.nextInt(availableItems.size());
		}
	};
	
	private WorldHealer healer;
	private Random rdn = new Random();
	private int tickLeftBeforeNextHeal;
	
	public HealTimeline(WorldHealer healer, Collection<Key<BlockPos,BlockData>> blockdata) {
		super(randomSelector, requirementFactories);
		this.addAll(blockdata, blockdata);
		this.healer = healer;
		this.tickLeftBeforeNextHeal = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal() + ForgeCreeperHeal.getConfig().getRandomTickVar();
	}
	
	public void onTick() {
		tickLeftBeforeNextHeal--;
		if(tickLeftBeforeNextHeal < 0) {
			healer.heal(this.poll());
			this.tickLeftBeforeNextHeal = ForgeCreeperHeal.getConfig().getRandomTickVar();
		}
	}
	
	private static LinkedList<IRequirementFactory<BlockPos, BlockData>> loadFactories() {
		requirementFactories = new LinkedList<IRequirementFactory<BlockPos, BlockData>>();
		requirementFactories.add(new FacingRequirementFactory(BlockTorch.class, BlockTorch.FACING));
		requirementFactories.add(new FacingRequirementFactory(BlockLadder.class, BlockLadder.FACING));
		requirementFactories.add(new FacingRequirementFactory(BlockWallSign.class, BlockWallSign.FACING));
		requirementFactories.add(new FacingRequirementFactory(BlockTrapDoor.class, BlockTrapDoor.FACING));
		requirementFactories.add(new FacingRequirementFactory(BlockButton.class, BlockButton.FACING));
		requirementFactories.add(new FacingRequirementFactory(BlockBannerHanging.class, BlockBannerHanging.FACING));
		requirementFactories.add(new FacingRequirementFactory(BlockTripWireHook.class, BlockTripWireHook.FACING));
		requirementFactories.add(new VineRequirementFactory());
		requirementFactories.add(new OppositeFacingRequirementFactory(BlockCocoa.class, BlockCocoa.FACING));
		//blockDataVisitorFactories.add(new FacingMatcherFactory(BlockPistonExtension.class, BlockPistonExtension.FACING));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockFalling.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockBasePressurePlate.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockBannerStanding.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockRedstoneDiode.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockRedstoneWire.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockStandingSign.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockCrops.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockCactus.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockRailBase.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockReed.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockSnow.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockTripWire.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockCake.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockCarpet.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockDragonEgg.class));
		requirementFactories.add(new SupportByBottomRequirementFactory(BlockFlowerPot.class));
		return requirementFactories;
	}
}
