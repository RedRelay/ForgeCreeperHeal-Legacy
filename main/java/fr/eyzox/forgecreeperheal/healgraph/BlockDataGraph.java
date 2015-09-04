package fr.eyzox.forgecreeperheal.healgraph;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.block.BlockBanner.BlockBannerHanging;
import net.minecraft.block.BlockBanner.BlockBannerStanding;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFalling;
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
import fr.eyzox.forgecreeperheal.healgraph.matcherfactories.FacingMatcherFactory;
import fr.eyzox.forgecreeperheal.healgraph.matcherfactories.IBlockDataMatcherFactory;
import fr.eyzox.forgecreeperheal.healgraph.matcherfactories.SupportByBottomMatcherFactory;
import fr.eyzox.forgecreeperheal.healgraph.matchers.IBlockDataMatcher;

public class BlockDataGraph extends BlockDataNode{

	private static LinkedList<IBlockDataMatcherFactory> blockDataVisitorFactories;

	public BlockDataGraph(Collection<BlockData> nodes) {
		for(BlockData data : nodes) {
			boolean factoryFound = false;
			boolean nodeFound = false;
			Iterator<IBlockDataMatcherFactory> factoryIterator = getIBlockDataVisitorFactories().iterator();
			while(!factoryFound && factoryIterator.hasNext()) {
				IBlockDataMatcherFactory factory = factoryIterator.next();
				IBlockDataMatcher visitor = factory.getMatcher(data);
				if(visitor != null) {
					factoryFound = true;
					Iterator<BlockData> nodeIterator = nodes.iterator();
					while(!nodeFound && nodeIterator.hasNext()) {
						BlockData node = nodeIterator.next();
						if(visitor.matches(node, data)) {
							nodeFound = true;
							node.add(data);
						}
					}
				}
			}

			if(!factoryFound || !nodeFound) {
				this.add(data);
			}
		}
	}
	
	public static LinkedList<IBlockDataMatcherFactory> getIBlockDataVisitorFactories() {
		if(blockDataVisitorFactories == null) {
			blockDataVisitorFactories = new LinkedList<IBlockDataMatcherFactory>();
			blockDataVisitorFactories.add(new FacingMatcherFactory(BlockTorch.class, BlockTorch.FACING));
			blockDataVisitorFactories.add(new FacingMatcherFactory(BlockLadder.class, BlockLadder.FACING));
			blockDataVisitorFactories.add(new FacingMatcherFactory(BlockWallSign.class, BlockWallSign.FACING));
			blockDataVisitorFactories.add(new FacingMatcherFactory(BlockTrapDoor.class, BlockTrapDoor.FACING));
			blockDataVisitorFactories.add(new FacingMatcherFactory(BlockButton.class, BlockButton.FACING));
			blockDataVisitorFactories.add(new FacingMatcherFactory(BlockBannerHanging.class, BlockBannerHanging.FACING));
			blockDataVisitorFactories.add(new FacingMatcherFactory(BlockTripWireHook.class, BlockTripWireHook.FACING));
			//blockDataVisitorFactories.add(new FacingMatcherFactory(BlockPistonExtension.class, BlockPistonExtension.FACING));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockFalling.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockBasePressurePlate.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockBannerStanding.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockRedstoneDiode.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockRedstoneWire.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockStandingSign.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockCrops.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockCactus.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockRailBase.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockReed.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockSnow.class));
			blockDataVisitorFactories.add(new SupportByBottomMatcherFactory(BlockTripWire.class));
			
		}
		return blockDataVisitorFactories;
	}
	
	


}
