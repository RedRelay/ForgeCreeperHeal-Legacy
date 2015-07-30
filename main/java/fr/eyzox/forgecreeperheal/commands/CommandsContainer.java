package fr.eyzox.forgecreeperheal.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public abstract class CommandsContainer extends CommandBase {

	private List<CommandBase> cmds = new ArrayList<CommandBase>(); 
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0 && args[0] != null) {
			for(CommandBase c : cmds) {
				if(args[0].equalsIgnoreCase(c.getName()) || (c.getAliases() != null && c.getAliases().contains(args[0].toLowerCase()))) {
					if(c.canCommandSenderUse(sender)) {
						c.execute(sender, Arrays.copyOfRange(args, 1, args.length));
					}else {
						sender.addChatMessage(new ChatComponentText("Not enought permissions"));
					}
				}
			}
		}
	}
	
	public void register(CommandBase cmd) {
		cmds.add(cmd);
	}
	
	public void unregister(CommandBase cmd) {
		cmds.remove(cmd);
	}
	
	public Collection<CommandBase> getRegisteredCommands() {
		return cmds;
	}

}
