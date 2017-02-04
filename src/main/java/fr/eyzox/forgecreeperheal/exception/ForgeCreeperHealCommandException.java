package fr.eyzox.forgecreeperheal.exception;

import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

public class ForgeCreeperHealCommandException extends CommandException {

	public ForgeCreeperHealCommandException(ICommandSender sender, String message, Object[] objects) {
		this(sender, message, objects, null);
	}
	
	public ForgeCreeperHealCommandException(ICommandSender sender, CommandException cause) {
		this(sender, null, null, cause);
	}

	public ForgeCreeperHealCommandException(ICommandSender sender, String message, Object[] objects, CommandException cause) {
		super(ForgeCreeperHealCommands.buildChatMessage(sender, new TextComponentString("")).getText()+ForgeCreeperHealCommands.buildTranslationMessage(sender, message, objects).getText()+(cause == null ? "" : (" : "+net.minecraft.util.text.translation.I18n.translateToLocalFormatted(cause.getMessage(), cause.getErrorObjects()))), new Object[]{});
	}
}
