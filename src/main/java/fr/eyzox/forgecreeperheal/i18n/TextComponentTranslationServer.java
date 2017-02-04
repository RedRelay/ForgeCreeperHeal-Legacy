package fr.eyzox.forgecreeperheal.i18n;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

public class TextComponentTranslationServer extends TextComponentString {

	public TextComponentTranslationServer(EntityPlayerMP player, String key) {
		this(player, key, null);
	}
	
	public TextComponentTranslationServer(EntityPlayerMP player, String key, Object[] o) {
		super(I18n.getInstance().translate(player, key, o));
	}

}
