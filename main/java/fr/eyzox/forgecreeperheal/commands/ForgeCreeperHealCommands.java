package fr.eyzox.forgecreeperheal.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

public class ForgeCreeperHealCommands extends CommandsContainer {

	@Override
	public String getName() {
		return ForgeCreeperHeal.MODID;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return null;
	}

	@Override
	public List getAliases() {
		List<String> aliases = new ArrayList<String>(1);
		aliases.add("fch");
		return aliases;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0 && args[0] != null) {
			super.execute(sender, args);
		}else {
			addChatMessage(sender, new ChatComponentText("Server version : "+ ForgeCreeperHeal.VERSION));
		}
	}
	
	
	
	@Override
	public boolean canCommandSenderUse(ICommandSender p_71519_1_) {
		return true;
	}

	public static boolean isOp(ICommandSender sender) {
		return !(sender instanceof EntityPlayer) || MinecraftServer.getServer().getConfigurationManager().canSendCommands(((EntityPlayer)sender).getGameProfile());
	}
	
	public static void addChatMessage(ICommandSender sender, IChatComponent msg) {
		ChatComponentText cct = new ChatComponentText(String.format("[%s] ", ForgeCreeperHeal.MODNAME));
		cct.appendSibling(msg);
		sender.addChatMessage(cct);
	}

}
