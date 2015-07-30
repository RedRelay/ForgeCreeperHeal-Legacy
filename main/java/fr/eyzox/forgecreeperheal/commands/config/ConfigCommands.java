package fr.eyzox.forgecreeperheal.commands.config;

import net.minecraft.command.ICommandSender;
import fr.eyzox.forgecreeperheal.commands.CommandsContainer;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;

public class ConfigCommands extends CommandsContainer {

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
