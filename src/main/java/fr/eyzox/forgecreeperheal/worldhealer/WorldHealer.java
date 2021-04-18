package fr.eyzox.forgecreeperheal.worldhealer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.ExplosionEvent;
import org.apache.logging.log4j.Level;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.ticklinkedlist.TickContainer;

public class WorldHealer extends WorldSavedData implements Supplier<Object> {

  private World world;
  private HealTask healTask;
  private boolean logspam = true;
  private ArrayList<Block> blacklist = new ArrayList<Block>();

  public WorldHealer(String key) {
    super(key);
    healTask = new HealTask();
    //for simplicity , just ignore any 2 size blocks (beds)
    blacklist.add(null);
    blacklist.add(Blocks.AIR);
    //    blacklist.add(Blocks.BED);
    //TODO: tags for bed and door
    //    blacklist.add(Blocks.ACACIA_DOOR);
    //    blacklist.add(Blocks.JUNGLE_DOOR);
    //    blacklist.add(Blocks.BIRCH_DOOR);
    //    blacklist.add(Blocks.OAK_DOOR);
    //    blacklist.add(Blocks.DARK_OAK_DOOR);
  }

  public World getWorld() {
    return world;
  }

  public void onTick() {
    Collection<BlockData> blocksToHeal = healTask.tick();
    if (blocksToHeal != null) {
      ForgeCreeperHeal.logger.info("worldHealer ontick " + blocksToHeal.size());
      for (BlockData blockData : blocksToHeal) {
        heal(blockData);
      }
    }
  }

  public void onDetonate(ExplosionEvent.Detonate event) {
    ForgeCreeperHeal.logger.info("on Detonate " + event.getAffectedBlocks());
    World world = event.getWorld();
    int maxTicksBeforeHeal = 0;
    //Process primary blocks
    for (BlockPos blockPosExplosion : event.getAffectedBlocks()) {
      BlockState blockStateExplosion = world.getBlockState(blockPosExplosion);
      if (blacklist.contains(blockStateExplosion.getBlock())) {
        continue;
      }
      if (!blockStateExplosion.getBlock().isAir(blockStateExplosion, world, blockPosExplosion)) {
        int ticksBeforeHeal = ForgeCreeperHeal.getConfig().getMinimumTicksBeforeHeal() + world.rand.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar());
        if (ticksBeforeHeal > maxTicksBeforeHeal) {
          maxTicksBeforeHeal = ticksBeforeHeal;
        }
        if (logspam) {
          ForgeCreeperHeal.logger.log(Level.INFO, "cube:" + blockStateExplosion.getBlock());
        }
        onBlockHealed(blockPosExplosion, blockStateExplosion, ticksBeforeHeal);
      }
    }
    maxTicksBeforeHeal++;
    //Process secondary blocks. ex: Leaves must come AFTER dirt
    for (BlockPos blockPosExplosion : event.getAffectedBlocks()) {
      BlockState blockStateExplosion = world.getBlockState(blockPosExplosion);
      if (blacklist.contains(blockStateExplosion.getBlock())) {
        continue;
      }
      if (!blockStateExplosion.getBlock().isAir(blockStateExplosion, world, blockPosExplosion)) {
        if (logspam) {
          ForgeCreeperHeal.logger.log(Level.INFO, "noncube:" + blockStateExplosion.getBlock());
        }
        onBlockHealed(blockPosExplosion, blockStateExplosion, maxTicksBeforeHeal + world.rand.nextInt(ForgeCreeperHeal.getConfig().getRandomTickVar()));
      }
    }
  }

  private void onBlockHealed(BlockPos blockPosExplosion, BlockState blockStateExplosion, int ticks) {
    healTask.add(ticks, new BlockData(world, blockPosExplosion, blockStateExplosion));
    world.removeTileEntity(blockPosExplosion);
    world.setBlockState(blockPosExplosion, Blocks.AIR.getDefaultState(), 7);
  }

  private void heal(BlockData blockData) {
    ForgeCreeperHeal.logger.info("worldHealer HEAL " + blockData.getBlockPos());
    boolean isAir = this.world.isAirBlock(blockData.getBlockPos());
    if (ForgeCreeperHeal.getConfig().isOverride() || isAir) {
      if (ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock() && !isAir) {
        if (world.getBlockState(blockData.getBlockPos()).getBlock() != null) {
          ItemEntity ei = WorldHealerUtils.getEntityItem(world, blockData.getBlockPos(), new ItemStack(world.getBlockState(blockData.getBlockPos()).getBlock()), world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F);
          if (ei != null) {
            world.addEntity(ei);
            TileEntity te = world.getTileEntity(blockData.getBlockPos());
            if (te instanceof IInventory) {
              WorldHealerUtils.dropInventory(world, blockData.getBlockPos(), (IInventory) te);
            }
          }
        }
      }
      world.setBlockState(blockData.getBlockPos(), blockData.getBlockState(), 7);
      if (blockData.getTileEntityTag() != null) {
        TileEntity te = world.getTileEntity(blockData.getBlockPos());
        if (te != null) {
          te.read(world.getBlockState(blockData.getBlockPos()), blockData.getTileEntityTag());
          world.setTileEntity(blockData.getBlockPos(), te);
        }
      }
    }
    else if (ForgeCreeperHeal.getConfig().isDropIfAlreadyBlock() && blockData.getBlockState().getBlock() != null) {
      ItemEntity ei = WorldHealerUtils.getEntityItem(world, blockData.getBlockPos(), new ItemStack(blockData.getBlockState().getBlock()), world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, world.rand.nextFloat() * 0.8F + 0.1F, 0.05F);
      if (ei != null) {
        world.addEntity(ei);
        if (blockData.getTileEntityTag() != null) {
          TileEntity te = blockData.getBlockState().getBlock().createTileEntity(blockData.getBlockState(), world);
          if (te instanceof IInventory) {
            te.read(world.getBlockState(blockData.getBlockPos()), blockData.getTileEntityTag());
            WorldHealerUtils.dropInventory(world, blockData.getBlockPos(), (IInventory) te);
          }
        }
      }
    }
  }

  @Override
  public void read(CompoundNBT nbt) {
    deserializeNBT(nbt);
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    return write(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    ListNBT tagList = new ListNBT();
    for (TickContainer<Collection<BlockData>> tc : this.healTask.getLinkedList()) {
      CompoundNBT tickContainerTag = new CompoundNBT();
      tickContainerTag.putInt("ticks", tc.getTick());
      ListNBT blockDataListTag = new ListNBT();
      for (BlockData blockData : tc.getData()) {
        CompoundNBT blockDataTag = new CompoundNBT();
        blockData.writeToNBT(blockDataTag);
        blockDataListTag.add(blockDataTag);
      }
      tickContainerTag.put("blockdatalist", blockDataListTag);
      tagList.add(tickContainerTag);
    }
    tag.put("healtasklist", tagList);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT tag) {
    ListNBT tagList = tag.getList("healtasklist", 2);
    for (ListIterator<INBT> iter = tagList.listIterator(); iter.hasNext();) {
      CompoundNBT tickContainerTag = (CompoundNBT) iter.next();
      int ticksLeft = tickContainerTag.getInt("ticks");
      LinkedList<BlockData> blockDataList = new LinkedList<BlockData>();
      ListNBT blockDataListTag = tickContainerTag.getList("blockdatalist", 2);
      for (ListIterator<INBT> iter0 = blockDataListTag.listIterator(); iter.hasNext();) {
        CompoundNBT blockDataTag = (CompoundNBT) iter0.next();
        BlockData blockData = new BlockData();
        blockData.readFromNBT(blockDataTag);
        blockDataList.add(blockData);
      }
      healTask.getLinkedList().addLast(new TickContainer<Collection<BlockData>>(ticksLeft, blockDataList));
    }
  }

  public static WorldHealer loadWorldHealer(ServerWorld w) {
    DimensionSavedDataManager storage = w.getSavedData();
    //    MapData storage = w.getMapData("map");
    //    storage.get
    //    MapStorage storage = w.getPerWorldStorage();
    final String KEY = getDataStorageKey();
    WorldHealer result = null;// (WorldHealer) storage.getOrLoadData(WorldHealer.class, KEY);
    if (result == null) {
      result = new WorldHealer(KEY);
      //      storage.setData(KEY, result);
      //      storage.set
      ForgeCreeperHeal.logger.info("Unable to find data for world " + w.getWorldInfo() + "[" + w.getProviderName() + "], new data created");
    }
    result.world = w;
    return result;
  }

  public static String getDataStorageKey() {
    return ForgeCreeperHeal.MODID + ":" + WorldHealer.class.getSimpleName();
  }

  @Override
  public boolean isDirty() {
    return true;
  }

  @Override
  public Object get() {
    return this;
  }
}
