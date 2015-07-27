package fr.eyzox.forgecreeperheal.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public abstract class CommandsContainer extends CommandBase {

	private List<CommandBase> cmds = new ArrayList<CommandBase>(); 
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length > 0 && args[0] != null) {
			for(CommandBase c : cmds) {
				if(args[0].equalsIgnoreCase(c.getCommandName()) || (c.getCommandAliases() != null && c.getCommandAliases().contains(args[0].toLowerCase()))) {
					if(c.canCommandSenderUseCommand(sender)) {
						c.processCommand(sender, Arrays.copyOfRange(args, 1, args.length));
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
