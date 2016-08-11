package fr.eyzox.forgecreeperheal.commands.config;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
	public void execute(MinecraftServer server, ICommandSender sender, String[] p_71515_2_) {
		ForgeCreeperHeal.getProxy().loadConfig();
		ForgeCreeperHealCommands.addChatMessage(sender, new TextComponentString("Config reloaded"));
	}

}
