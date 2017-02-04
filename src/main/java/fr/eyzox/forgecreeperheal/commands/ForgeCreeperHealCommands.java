package fr.eyzox.forgecreeperheal.commands;

import java.util.LinkedList;
import java.util.List;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealCommandException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public abstract class ForgeCreeperHealCommands extends CommandBase {

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return '/'+buildCommandName("fch")+" [?]";
	}
	
	@Override
	public final String getCommandName() {
		return buildCommandName(ForgeCreeperHeal.MODID);
	}
	
	@Override
	public final List<String> getCommandAliases() {
		final List<String> aliases = new LinkedList<String>();
		aliases.add(buildCommandName("fch"));
		return aliases;
	}
	
	protected abstract String getFCHCommandName();
	
	protected abstract String getHelp();
	
	protected abstract void _execute(MinecraftServer server, ICommandSender sender, String[] args) throws ForgeCreeperHealCommandException;

	private String buildCommandName(String prefix) {
		return getFCHCommandName() != null ? (prefix+'-'+getFCHCommandName()) : prefix;
	}
	
	@Override
	public final void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0 && "?".equals(args[0])) {
			
			final ITextComponent head = buildChatMessage(new TextComponentString("--- "+getCommandName()+" Help ---"));
			head.getStyle().setColor(TextFormatting.DARK_GREEN);
			sender.addChatMessage(head);
			
			final ITextComponent usage = new TextComponentString("Usage : "+getCommandUsage(sender));
			sender.addChatMessage(usage);
			
			final String rawHelp = getHelp();
			if(rawHelp != null) {
				sender.addChatMessage( new TextComponentString("Info :"));
				final String[] helps = rawHelp.split("\n");
				for(int i=0; i<helps.length; i++) {
					sender.addChatMessage( new TextComponentString(helps[i]));
				}
			}
		}else {
			_execute(server, sender, args);
		}
		
	}
	
	protected static enum MessageType {
		SUCCESS(TextFormatting.GREEN), ERROR(TextFormatting.RED);
		
		private TextFormatting color;
		
		private MessageType(TextFormatting color) {
			this.color = color;
		}
		
		public TextFormatting getColor() {
			return color;
		}
	}
	
	public static ITextComponent buildChatMessage(ITextComponent msg) {
		return buildChatMessage(msg, null);
	}
	
	public static ITextComponent buildChatMessage(ITextComponent msg, MessageType type) {
		TextComponentString cct = new TextComponentString(String.format("[%s] ", ForgeCreeperHeal.MODNAME));
		if(type != null) {
			cct.getStyle().setColor(type.getColor());
		}
		cct.appendSibling(msg);
		return cct;
	}

}
