package fr.eyzox.forgecreeperheal.commands;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class ForgeCreeperHealCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return ForgeCreeperHeal.MODID;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(!sender.getEntityWorld().isRemote && args.length > 0 && args[0].equalsIgnoreCase("reload") && sender instanceof EntityPlayer && MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayer)sender).getGameProfile())) {
			ForgeCreeperHeal.reloadConfig();
			sender.addChatMessage(new ChatComponentText("["+ForgeCreeperHeal.MODID+"] Config reloaded"));
		}

	}

}
