package fr.eyzox.forgecreeperheal.commands.config;

import fr.eyzox.forgecreeperheal.commands.CommandsContainer;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import net.minecraft.command.ICommandSender;

public class ConfigCommands extends CommandsContainer {

	public ConfigCommands() {
		this.register(new ReloadConfigCommand());
	}
	
	@Override
	public String getName() {
		return "config";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return null;
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return ForgeCreeperHealCommands.isOp(sender);
	}
}
