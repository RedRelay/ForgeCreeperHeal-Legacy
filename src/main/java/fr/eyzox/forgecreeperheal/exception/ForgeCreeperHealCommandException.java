package fr.eyzox.forgecreeperheal.exception;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.translation.I18n;

public class ForgeCreeperHealCommandException extends CommandException {

	public ForgeCreeperHealCommandException(String message, Object[] objects) {
		this(message, objects, null);
	}

	public ForgeCreeperHealCommandException(String message, Object[] objects, CommandException cause) {
		super(String.format("[%s] %s", ForgeCreeperHeal.MODNAME, I18n.translateToLocalFormatted(message, objects) + (cause == null ? "" : (" : "+I18n.translateToLocalFormatted(cause.getMessage(), cause.getErrorObjects())))), new Object[]{});
	}
}
