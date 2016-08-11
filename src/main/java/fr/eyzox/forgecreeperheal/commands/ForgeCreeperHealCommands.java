package fr.eyzox.forgecreeperheal.commands;

import java.util.ArrayList;
import java.util.List;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.commands.config.ConfigCommands;
import fr.eyzox.forgecreeperheal.commands.heal.HealCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ForgeCreeperHealCommands extends CommandsContainer {

	public ForgeCreeperHealCommands() {
		this.register(new ConfigCommands());
		this.register(new HealCommand());
	}
	
	@Override
	public String getCommandName() {
		return ForgeCreeperHeal.MODID;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return null;
	}

	@Override
	public List getCommandAliases() {
		List<String> aliases = new ArrayList<String>(1);
		aliases.add("fch");
		return aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0 && args[0] != null) {
			super.execute(server, sender, args);
		}else {
			addChatMessage(sender, new TextComponentString("Server version : "+ ForgeCreeperHeal.VERSION));
		}
	}
	
	public static void addChatMessage(ICommandSender sender, ITextComponent msg) {
		TextComponentString cct = new TextComponentString(String.format("[%s] ", ForgeCreeperHeal.MODNAME));
		cct.appendSibling(msg);
		sender.addChatMessage(cct);
	}

}
