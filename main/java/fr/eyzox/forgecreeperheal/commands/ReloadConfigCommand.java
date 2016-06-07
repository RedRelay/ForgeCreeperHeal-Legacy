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
	public String getCommandUsage(ICommandSender sender) {
		return "/"+getCommandName();
	}

	@Override
	public void execute(MinecraftServer server,ICommandSender sender, String[] args) {
		ForgeCreeperHeal.getProxy().getConfig().syncConfig();
		CommonProxy.addChatMessage(sender, "Config reloaded : "+ForgeCreeperHeal.getProxy().getConfig().toString());
	}

}
