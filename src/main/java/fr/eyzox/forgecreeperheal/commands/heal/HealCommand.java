package fr.eyzox.forgecreeperheal.commands.heal;

import fr.eyzox.forgecreeperheal.commands.CommandsContainer;
import net.minecraft.command.ICommandSender;

public class HealCommand extends CommandsContainer {

	public HealCommand() {
		this.register(new HealAllCommand());
	}
	
	@Override
	public String getCommandName() {
		return "heal";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

}