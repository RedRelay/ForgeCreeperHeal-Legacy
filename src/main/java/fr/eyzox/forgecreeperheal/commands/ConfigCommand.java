package fr.eyzox.forgecreeperheal.commands;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealCommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ConfigCommand extends ForgeCreeperHealCommands{

	@Override
	protected String getFCHCommandName() {
		return "config";
	}
	
	@Override
	protected String getFCHUsage() {
		return "[-r] [-s] [<property>:<value>]";
	}

	@Override
	protected String getHelp() {
		return "fch.command.config.help";
	}

	@Override
	protected void _execute(MinecraftServer server, ICommandSender sender, String[] args) throws ForgeCreeperHealCommandException {
		if(args.length < 1) {
			throw new ForgeCreeperHealCommandException(sender, "fch.command.exception.missingparams", new Object[]{getCommandUsage(sender)});
		}
		
		int shift = 0;
		
		boolean reload = false, save = false;
		while(shift < args.length && args[shift].startsWith("-")) {
			for(int i=1; i<args[shift].length(); i++) {
				switch(args[shift].charAt(i)) {
				case 'r' :
					reload = true;
					break;
				case 's' :
					save = true;
					break;
				}
			}
			shift++;
		}
		
		if(reload) {
			ForgeCreeperHeal.getProxy().loadConfig();
			final ITextComponent reloadMsg = buildChatMessage(sender, buildTranslationMessage(sender, "fch.command.config.action.reload"), MessageType.SUCCESS);
			sender.addChatMessage(reloadMsg);
		}
		
		if(shift < args.length) {
			final ITextComponent sorry = buildChatMessage(sender, buildTranslationMessage(sender, "fch.command.config.action.edit"));
			sorry.getStyle().setColor(TextFormatting.DARK_RED);
			sender.addChatMessage(sorry);
		}
		
		if(save) {
			final ITextComponent saveMsg = buildChatMessage(sender, buildTranslationMessage(sender, "fch.command.config.action.save"));
			saveMsg.getStyle().setColor(TextFormatting.DARK_RED);
			sender.addChatMessage(saveMsg);
		}
	}

}
