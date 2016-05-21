package fr.eyzox.forgecreeperheal.commands.config;

import fr.eyzox.forgecreeperheal.commands.CommandsContainer;
import net.minecraft.command.ICommandSender;

public class ConfigCommands extends CommandsContainer {

	public ConfigCommands() {
		this.register(new ReloadConfigCommand());
	}
	
	@Override
	public String getCommandName() {
		return "config";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return null;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return ForgeCreeperHealCommands.isOp(sender);
	}
}
