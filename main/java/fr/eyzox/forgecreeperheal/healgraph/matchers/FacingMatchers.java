package fr.eyzox.forgecreeperheal.healgraph.matchers;

import fr.eyzox.forgecreeperheal.healgraph.BlockData;


public abstract class FacingMatchers implements IBlockDataMatcher {

	public static final IBlockDataMatcher PLACED_ABOVE = new IBlockDataMatcher() {
		
		@Override
		public boolean matches(BlockData node, BlockData data) {
			return node.getBlockPos().getX() == data.getBlockPos().getX() && node.getBlockPos().getZ() == data.getBlockPos().getZ() && node.getBlockPos().getY() == data.getBlockPos().getY()-1;
		}
	};
	
	public static final IBlockDataMatcher PLACED_BELOW = new IBlockDataMatcher() {
		
		@Override
		public boolean matches(BlockData node, BlockData data) {
			return node.getBlockPos().getX() == data.getBlockPos().getX() && node.getBlockPos().getZ() == data.getBlockPos().getZ() && node.getBlockPos().getY() == data.getBlockPos().getY()+1;
		}
	};
	
	public static final IBlockDataMatcher PLACED_NORTH = new IBlockDataMatcher() {
		
		@Override
		public boolean matches(BlockData node, BlockData data) {
			return node.getBlockPos().getX() == data.getBlockPos().getX() && node.getBlockPos().getY() == data.getBlockPos().getY() && node.getBlockPos().getZ() == data.getBlockPos().getZ()+1;
		}
	};
	public static final IBlockDataMatcher PLACED_EAST = new IBlockDataMatcher() {
		
		@Override
		public boolean matches(BlockData node, BlockData data) {
			return node.getBlockPos().getY() == data.getBlockPos().getY() && node.getBlockPos().getZ() == data.getBlockPos().getZ() && node.getBlockPos().getX() == data.getBlockPos().getX()-1;
		}
	};
	
	public static final IBlockDataMatcher PLACED_SOUTH = new IBlockDataMatcher() {
		
		@Override
		public boolean matches(BlockData node, BlockData data) {
			return node.getBlockPos().getX() == data.getBlockPos().getX() && node.getBlockPos().getY() == data.getBlockPos().getY() && node.getBlockPos().getZ() == data.getBlockPos().getZ()-1;
		}
	};
	
	public static final IBlockDataMatcher PLACED_WEST= new IBlockDataMatcher() {
		
		@Override
		public boolean matches(BlockData node, BlockData data) {
			return node.getBlockPos().getY() == data.getBlockPos().getY() && node.getBlockPos().getZ() == data.getBlockPos().getZ() && node.getBlockPos().getX() == data.getBlockPos().getX()+1;
		}
	};

}
