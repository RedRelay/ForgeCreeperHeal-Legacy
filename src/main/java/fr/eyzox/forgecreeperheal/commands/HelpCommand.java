package fr.eyzox.forgecreeperheal.commands;

import java.util.Iterator;
import java.util.List;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealCommandException;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandHelp;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class HelpCommand extends ForgeCreeperHealCommands {

	private static final class WrappedVanillaHelpCommand extends CommandHelp {
		@Override
		protected List<ICommand> getSortedPossibleCommands(ICommandSender sender, MinecraftServer server) {
			final List<ICommand> possibleCommands = super.getSortedPossibleCommands(sender, server);
			for(Iterator<ICommand> it = possibleCommands.iterator(); it.hasNext();) {
				ICommand c = it.next();
				if(!(c instanceof ForgeCreeperHealCommands)) {
					it.remove();
				}
			}
			return possibleCommands;
		}
	}
	
	private final static WrappedVanillaHelpCommand wrap = new WrappedVanillaHelpCommand();
	
	@Override
	protected String getFCHCommandName() {
		return "help";
	}

	@Override
	protected String getHelp() {
		return "Display all "+ForgeCreeperHeal.MODNAME+" available commands";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return wrap.getRequiredPermissionLevel();
	}

	@Override
	protected void _execute(MinecraftServer server, ICommandSender sender, String[] args) throws ForgeCreeperHealCommandException {
		try {
			wrap.execute(server, sender, args);
		} catch (CommandException e) {
			throw new ForgeCreeperHealCommandException(e);
		}
	}

}
