package fr.eyzox.forgecreeperheal.network;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ModDataMessage implements IMessage {

	private String modVersion;
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.modVersion = String.format("%d.%d.%d", buf.readByte(), buf.readByte(), buf.readByte());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String[] versionTab = modVersion.split("\\.");
		for(int i=0; i<3; i++) {
			buf.writeByte(Byte.parseByte(versionTab[i]));
		}
	}
	
	public ModDataMessage fill() {
		this.modVersion = ForgeCreeperHeal.VERSION;
		
		return this;
	}
	
	public static class Handler implements IMessageHandler<ModDataMessage, IMessage> {

		@Override
		public IMessage onMessage(ModDataMessage message, MessageContext ctx) {
			System.out.println("mod version data packet?"+message.modVersion);
//			if(message.modVersion != null) {
//				PlayerModData data = new PlayerModData(message.modVersion);
//				PlayerModData.register(ctx.getServerHandler().playerEntity, data);
//			}
			return null;
		}
		
	}

}
