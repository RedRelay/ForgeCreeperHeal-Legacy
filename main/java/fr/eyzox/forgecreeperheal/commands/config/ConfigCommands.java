package fr.eyzox.forgecreeperheal.commands.config;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import fr.eyzox.forgecreeperheal.commands.CommandsContainer;

public class ConfigCommands extends CommandsContainer {

	@Override
	public String getCommandName() {
		return "config";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return null;
	}

	private final static int	OP	= 2;
	boolean requiresOP = true;
	@Override
	public boolean checkPermission(MinecraftServer server,ICommandSender sender) {
		return (requiresOP) ? sender.canCommandSenderUseCommand(OP, this.getCommandName()) : true;
	}
}
