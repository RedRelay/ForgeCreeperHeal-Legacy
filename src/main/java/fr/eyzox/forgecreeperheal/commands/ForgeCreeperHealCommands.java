package fr.eyzox.forgecreeperheal.commands;

import java.util.LinkedList;
import java.util.List;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealCommandException;
import fr.eyzox.forgecreeperheal.i18n.TextComponentTranslationServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class ForgeCreeperHealCommands extends CommandBase {

	protected final static String BASE_ALIAS = "fch";
	
	protected static final ForgeCreeperHealCommands[] COMMANDS = new ForgeCreeperHealCommands[] {
			new VersionCommand(),
			new ConfigCommand(),
			new HealCommand(),
			new HelpCommand()
	};
	
	@Override
	public final String getCommandUsage(ICommandSender sender) {
		return '/'+buildCommandName(BASE_ALIAS)+" [?] "+getFCHUsage();
	}
	
	@Override
	public final String getCommandName() {
		return buildCommandName(ForgeCreeperHeal.MODID);
	}
	
	@Override
	public final List<String> getCommandAliases() {
		final List<String> aliases = new LinkedList<String>();
		aliases.add(buildCommandName(BASE_ALIAS));
		return aliases;
	}
	
	protected abstract String getFCHCommandName();
	
	protected abstract String getFCHUsage();
	
	protected abstract String getHelp();
	
	protected abstract void _execute(MinecraftServer server, ICommandSender sender, String[] args) throws ForgeCreeperHealCommandException;
	
	private String buildCommandName(String prefix) {
		return getFCHCommandName() != null ? (prefix+'-'+getFCHCommandName()) : prefix;
	}
	
	@Override
	public final void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0 && "?".equals(args[0])) {
			
			final ITextComponent head = buildChatMessage(sender, buildTranslationMessage(sender, "fch.command.generic.help.head", new Object[]{buildCommandName(BASE_ALIAS)}));
			head.getStyle().setColor(TextFormatting.DARK_GREEN);
			sender.addChatMessage(head);
			
			final ITextComponent usage = buildTranslationMessage(sender, "fch.command.generic.help.usage",new Object[]{getCommandUsage(sender)});
			usage.getStyle().setColor(TextFormatting.GRAY);
			sender.addChatMessage(usage);
			
			final String rawHelp = buildTranslationMessage(sender, getHelp()).getFormattedText();
			final String[] helps = rawHelp.split("\\\\n");
			for(int i=0; i<helps.length; i++) {
				sender.addChatMessage(new TextComponentString(helps[i]));
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
	
	public static TextComponentTranslationServer buildChatMessage(ICommandSender sender, ITextComponent msg) {
		return buildChatMessage(sender, msg, null);
	}
	
	public static TextComponentTranslationServer buildChatMessage(ICommandSender sender, ITextComponent msg, MessageType type) {
		TextComponentTranslationServer cct = buildTranslationMessage(sender, "fch.command.prefix", new Object[]{ForgeCreeperHeal.MODNAME});
		if(type != null) {
			cct.getStyle().setColor(type.getColor());
		}
		cct.appendSibling(msg);
		return cct;
	}
	
	public static TextComponentTranslationServer buildTranslationMessage(ICommandSender sender, String key) {
		return buildTranslationMessage(sender, key, null);
	}
	
	public static TextComponentTranslationServer buildTranslationMessage(ICommandSender sender, String key, Object[] o) {
		return new TextComponentTranslationServer((sender instanceof EntityPlayerMP ? (EntityPlayerMP)sender : null), key, o);
	}
	
	public static void register() {
		ServerCommandManager m = (ServerCommandManager) FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
		for(ForgeCreeperHealCommands c : COMMANDS) {
			m.registerCommand(c);
		}
	}

}
