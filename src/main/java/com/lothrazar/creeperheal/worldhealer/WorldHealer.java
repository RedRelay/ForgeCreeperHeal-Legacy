package com.lothrazar.creeperheal.worldhealer;

import com.lothrazar.creeperheal.ConfigRegistry;
import com.lothrazar.creeperheal.ForgeCreeperHeal;
import com.lothrazar.creeperheal.data.BlockStatePosWrapper;
import com.lothrazar.creeperheal.data.TickContainer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.world.ExplosionEvent;

public class WorldHealer extends SavedData implements Supplier<Object> {

  private Level world;
  private TickingHealTask healTask;
  static final String DATAKEY = ForgeCreeperHeal.MODID + ":" + WorldHealer.class.getSimpleName();

  public WorldHealer() {
    healTask = new TickingHealTask();
  }

  public void onTick() {
    Collection<BlockStatePosWrapper> blocksToHeal = healTask.tick();
    if (blocksToHeal != null) {
      for (BlockStatePosWrapper blockData : blocksToHeal) {
        heal(blockData);
      }
    }
  }

  public void onDetonate(ExplosionEvent.Detonate event) {
    Level world = event.getWorld();
    int maxTicksBeforeHeal = 0;
    //Process primary blocks
    for (BlockPos blockPosExplosion : event.getAffectedBlocks()) {
      BlockState blockStateExplosion = world.getBlockState(blockPosExplosion);
      if (!isValid(blockStateExplosion)) {
        continue;
      }
      if (!blockStateExplosion.isAir()) {
        int ticksBeforeHeal = ConfigRegistry.getMinimumTicksBeforeHeal() + world.random.nextInt(ConfigRegistry.getRandomTickVar());
        if (ticksBeforeHeal > maxTicksBeforeHeal) {
          maxTicksBeforeHeal = ticksBeforeHeal;
        }
        onBlockHealed(blockPosExplosion, blockStateExplosion, ticksBeforeHeal);
      }
    }
    maxTicksBeforeHeal++;
    //Process secondary blocks. ex: Leaves must come AFTER dirt
    for (BlockPos blockPosExplosion : event.getAffectedBlocks()) {
      BlockState blockStateExplosion = world.getBlockState(blockPosExplosion);
      if (!isValid(blockStateExplosion)) {
        continue;
      }
      if (!blockStateExplosion.isAir()) {
        onBlockHealed(blockPosExplosion, blockStateExplosion, maxTicksBeforeHeal + world.random.nextInt(ConfigRegistry.getRandomTickVar()));
      }
    }
  }

  private boolean isValid(BlockState state) {
    if (state.is(BlockTags.DOORS) || state.is(BlockTags.BEDS) || state.is(BlockTags.TALL_FLOWERS)) {
      return false;
    }
    return true;
  }

  private void onBlockHealed(BlockPos blockPosExplosion, BlockState blockStateExplosion, int ticks) {
    healTask.add(ticks, new BlockStatePosWrapper(world, blockPosExplosion, blockStateExplosion));
    world.removeBlockEntity(blockPosExplosion);
    world.setBlock(blockPosExplosion, Blocks.AIR.defaultBlockState(), 7);
  }

  private void heal(BlockStatePosWrapper blockData) {
    boolean isAir = this.world.isEmptyBlock(blockData.getBlockPos());
    if (ConfigRegistry.isOverride() || isAir) {
      if (ConfigRegistry.isDropIfAlreadyBlock() && !isAir) {
        if (world.getBlockState(blockData.getBlockPos()).getBlock() != null) {
          dropAsItem(blockData);
        }
      }
      world.setBlock(blockData.getBlockPos(), blockData.getBlockState(), 7);
      if (blockData.getTileEntityTag() != null) {
        BlockEntity te = world.getBlockEntity(blockData.getBlockPos());
        if (te != null) {
          te.load(blockData.getTileEntityTag());
          world.setBlockEntity(te);
        }
      }
    }
    else if (ConfigRegistry.isDropIfAlreadyBlock() && blockData.getBlockState().getBlock() != null) {
      Block block = blockData.getBlockState().getBlock();
      ItemEntity ei = WorldHealerUtils.getEntityItem(world, blockData.getBlockPos(),
          new ItemStack(block),
          world.random.nextFloat() * 0.8F + 0.1F, world.random.nextFloat() * 0.8F + 0.1F, world.random.nextFloat() * 0.8F + 0.1F, 0.05F);
      if (ei != null) {
        world.addFreshEntity(ei);
        if (blockData.getTileEntityTag() != null && block instanceof EntityBlock) {
          BlockEntity te = ((EntityBlock) block).newBlockEntity(blockData.getBlockPos(), blockData.getBlockState());
          if (te instanceof Container) {
            te.load(blockData.getTileEntityTag());
            Containers.dropContents(world, blockData.getBlockPos(), (Container) te);
          }
        }
      }

    }
  }

  private void dropAsItem(BlockStatePosWrapper blockData) {
    ItemEntity ei = WorldHealerUtils.getEntityItem(world, blockData.getBlockPos(),
        new ItemStack(world.getBlockState(blockData.getBlockPos()).getBlock()),
        world.random.nextFloat() * 0.8F + 0.1F, world.random.nextFloat() * 0.8F + 0.1F, world.random.nextFloat() * 0.8F + 0.1F, 0.05F);
    if (ei != null) {
      world.addFreshEntity(ei);
      BlockEntity te = world.getBlockEntity(blockData.getBlockPos());
      if (te instanceof Container) {
        Containers.dropContents(world, blockData.getBlockPos(), (Container) te);
      }
    }
  }

//  @Override
//  public void load(CompoundTag nbt) {
//    deserializeNBT(nbt);
//  }
//
//  @Override
//  public CompoundTag serializeNBT() {
//    CompoundTag tag = new CompoundTag();
//    return save(tag);
//  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    ListTag tagList = new ListTag();
    for (TickContainer<Collection<BlockStatePosWrapper>> tc : this.healTask.getLinkedList()) {
      CompoundTag tickContainerTag = new CompoundTag();
      tickContainerTag.putInt("ticks", tc.getTick());
      ListTag blockDataListTag = new ListTag();
      for (BlockStatePosWrapper blockData : tc.getData()) {
        CompoundTag blockDataTag = new CompoundTag();
        blockData.writeToNBT(blockDataTag);
        blockDataListTag.add(blockDataTag);
      }
      tickContainerTag.put("blockdatalist", blockDataListTag);
      tagList.add(tickContainerTag);
    }
    tag.put("healtasklist", tagList);
    return tag;
  }


  public void deserializeNBT(CompoundTag tag) {
    ListTag tagList = tag.getList("healtasklist", 2);
    for (ListIterator<Tag> iter = tagList.listIterator(); iter.hasNext();) {
      CompoundTag tickContainerTag = (CompoundTag) iter.next();
      int ticksLeft = tickContainerTag.getInt("ticks");
      LinkedList<BlockStatePosWrapper> blockDataList = new LinkedList<BlockStatePosWrapper>();
      ListTag blockDataListTag = tickContainerTag.getList("blockdatalist", 2);
      for (ListIterator<Tag> iter0 = blockDataListTag.listIterator(); iter.hasNext();) {
        CompoundTag blockDataTag = (CompoundTag) iter0.next();
        BlockStatePosWrapper blockData = new BlockStatePosWrapper();
        blockData.readFromNBT(blockDataTag);
        blockDataList.add(blockData);
      }
      healTask.getLinkedList().addLast(new TickContainer<Collection<BlockStatePosWrapper>>(ticksLeft, blockDataList));
    }
  }

  public static WorldHealer loadWorldHealer(ServerLevel w) {
    //first get data saved from last time we used this world
    DimensionDataStorage storage = w.getDataStorage();
    WorldHealer result = storage.computeIfAbsent(p -> {
      WorldHealer wNew = new WorldHealer();
      wNew.deserializeNBT(p);
      return wNew;
      }, () -> {
        return new WorldHealer();
      }, DATAKEY);

    result.world = w;
    return result;
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