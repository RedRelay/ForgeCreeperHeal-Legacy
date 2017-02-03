package fr.eyzox.forgecreeperheal.commands;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ConfigCommand extends ForgeCreeperHealCommands{

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return super.getCommandUsage(sender)+" [-r] [-s] [<property>:<value>]";
	}

	@Override
	protected String getFCHCommandName() {
		return "config";
	}

	@Override
	protected String getHelp() {
		return 		"* -r : reload the config from the config file\n"
				+ 	"* -s : save to the config file\n"
				+ 	"* <property> : the name of the property you want change\n"
				+ 	"* <value> : the new value, if property is a list, you can set it up with [<value1>,<value..n>] or you can use +<value> to add or -<value> to remove";
	}

	@Override
	protected void _execute(MinecraftServer server, ICommandSender sender, String[] args) throws WrongUsageException {
		if(args.length < 1) {
			throw new WrongUsageException("Missing parameters : "+getCommandUsage(sender), new Object[]{});
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
			final ITextComponent reloadMsg = buildChatMessage(new TextComponentString("Config reloaded"));
			reloadMsg.getStyle().setColor(TextFormatting.GREEN);
			sender.addChatMessage(reloadMsg);
		}
		
		if(shift < args.length) {
			final ITextComponent sorry = buildChatMessage(new TextComponentString("Sorry, edit config from command is not implemented yet"));
			sorry.getStyle().setColor(TextFormatting.DARK_RED);
			sender.addChatMessage(sorry);
		}
		
		if(save) {
			final ITextComponent saveMsg = buildChatMessage(new TextComponentString("Sorry, saving config from command is not implemented yet"));
			saveMsg.getStyle().setColor(TextFormatting.DARK_RED);
			sender.addChatMessage(saveMsg);
		}
	}

}
