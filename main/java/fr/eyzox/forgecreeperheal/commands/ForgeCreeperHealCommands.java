package fr.eyzox.forgecreeperheal.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;

public class ForgeCreeperHealCommands extends CommandsContainer {

	@Override
	public String getCommandName() {
		return ForgeCreeperHeal.MODID;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return null;
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> aliases = new ArrayList<String>(1);
		aliases.add("fch");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server,ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0 && args[0] != null) {
			super.execute(server,sender, args);
		}else {
			addChatMessage(sender, new TextComponentTranslation("Server version : "+ ForgeCreeperHeal.VERSION));
		}
	}
	
	
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

		return true;
	}

//	public static boolean isOp(ICommandSender sender) {
//		return !(sender instanceof EntityPlayer) || MinecraftServer.getServer().getConfigurationManager().canSendCommands(((EntityPlayer)sender).getGameProfile());
//	}
//	
	public static void addChatMessage(ICommandSender sender, TextComponentTranslation msg) {
		TextComponentTranslation cct = new TextComponentTranslation(String.format("[%s] ", ForgeCreeperHeal.MODNAME));
		cct.appendSibling(msg);
		sender.addChatMessage(cct);
	}

	public static void addChatMessage(ICommandSender sender, String string) {
		addChatMessage(sender,new TextComponentTranslation(string));
	}

}
