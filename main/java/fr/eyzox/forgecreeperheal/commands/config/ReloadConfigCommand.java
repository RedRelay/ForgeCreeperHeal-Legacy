package fr.eyzox.forgecreeperheal.commands.config;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ReloadConfigCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "reload";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] p_71515_2_) {
		ForgeCreeperHeal.reloadConfig();
		ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Config reloaded"));
	}

}
