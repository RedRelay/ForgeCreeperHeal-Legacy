package fr.eyzox.forgecreeperheal.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.proxy.CommonProxy;

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
	public void execute(MinecraftServer server,ICommandSender sender, String[] p_71515_2_) {
		ForgeCreeperHeal.reloadConfig();
		CommonProxy.addChatMessage(sender, "Config reloaded");
	}

}
