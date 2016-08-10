package fr.eyzox.forgecreeperheal.commands.heal;

import fr.eyzox.forgecreeperheal.commands.CommandsContainer;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import net.minecraft.command.ICommandSender;

public class HealCommand extends CommandsContainer {

	public HealCommand() {
		this.register(new HealAllCommand());
	}
	
	@Override
	public String getName() {
		return "heal";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return ForgeCreeperHealCommands.isOp(sender);
	}
	
	

}
