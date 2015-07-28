package fr.eyzox.forgecreeperheal.network.profiler;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ProfilerInfoMessage implements IMessage {
	private World world;
	private double ticks;
	private long blocksUsed;
	private transient int displayTicks;
	
	public ProfilerInfoMessage() {}
	
	public ProfilerInfoMessage(World world, double ticks, long blocksUsed) {
		this.world = world;
		this.ticks = ticks;
		this.blocksUsed = blocksUsed;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.world = DimensionManager.getWorld(buf.readInt());
		this.ticks = buf.readDouble();
		this.blocksUsed = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if(this.world != null && this.world.provider != null) {
			buf.writeInt(this.world.provider.dimensionId);
		}else {
			buf.writeInt(-1);
		}
		buf.writeDouble(this.ticks);
		buf.writeLong(this.blocksUsed);
	}
	
	@SideOnly(Side.CLIENT)
	public int getDisplayTicks() {
		return displayTicks;
	}

	@SideOnly(Side.CLIENT)
	public void setDisplayTicks(int displayTicks) {
		this.displayTicks = displayTicks;
	}

	public World getWorld() {
		return world;
	}

	public double getTicks() {
		return ticks;
	}

	public long getBlocksUsed() {
		return blocksUsed;
	}

	public static class Handler implements IMessageHandler<ProfilerInfoMessage, IMessage> {

		@Override
		public IMessage onMessage(ProfilerInfoMessage message, MessageContext ctx) {
			if(ForgeCreeperHeal.getProxy() instanceof ClientProxy) {
				((ClientProxy)ForgeCreeperHeal.getProxy()).getProfilerRenderEventHandler().onMessage(message);
			}
			return null;
		}
	}
}
