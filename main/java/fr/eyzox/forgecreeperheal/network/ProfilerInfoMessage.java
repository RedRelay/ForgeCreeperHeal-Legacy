package fr.eyzox.forgecreeperheal.network;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ProfilerInfoMessage implements IMessage {
	private String worldName;
	private int dimensionID;
	private double ticks;
	private long blocksUsed;
	private transient int displayTicks;
	
	public ProfilerInfoMessage() {}
	
	public ProfilerInfoMessage(World world, double ticks, long blocksUsed) {
		this.worldName = world.getWorldInfo().getWorldName();
		this.dimensionID = world.provider.dimensionId;
		this.ticks = ticks;
		this.blocksUsed = blocksUsed;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.worldName = ByteBufUtils.readUTF8String(buf);
		this.dimensionID = buf.readInt();
		this.ticks = buf.readDouble();
		this.blocksUsed = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, worldName);
		buf.writeInt(dimensionID);
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

	public String getWorldName() {
		return worldName;
	}
	
	public int getDimensionID() {
		return dimensionID;
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
