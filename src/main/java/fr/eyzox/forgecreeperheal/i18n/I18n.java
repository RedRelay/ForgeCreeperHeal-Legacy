package fr.eyzox.forgecreeperheal.i18n;

import java.lang.reflect.Field;

import fr.eyzox.forgecreeperheal.reflection.ReflectionHelper;
import fr.eyzox.forgecreeperheal.reflection.ReflectionManager;
import net.minecraft.entity.player.EntityPlayerMP;

public final class I18n {
	
	private final static Field PLAYER_LOCALE = ReflectionManager.getInstance().getField(EntityPlayerMP.class, "language");

	private static final I18n INSTANCE = new I18n();
	
	private final static String DEFAULT_LOCALE = "en_US";
	
	private final LocaleMap localeMap = new LocaleMap();
	private LanguageMap defaultLanguage= LanguageMap.getFromLocale(DEFAULT_LOCALE);

	
	private I18n() {
	}
	
	public synchronized String translate(String key, Object[] o) {
		return this.translate((EntityPlayerMP)null, key, o);
	}
	
	public synchronized String translate(EntityPlayerMP player, String key, Object[] o) {
		LanguageMap dico = defaultLanguage;
		String playerLocale = (String) ReflectionHelper.get(PLAYER_LOCALE, player);
		if(!defaultLanguage.getLocale().equals(playerLocale)) {
			LanguageMap sharedDico = localeMap.getLocalFor(player, playerLocale);
			if(sharedDico != null) {
				dico = sharedDico;
			}
		}
		
		return dico == null ? key : dico.translate(key, o);
	}
	
	public synchronized void onPlayerLeave(EntityPlayerMP player) {
		final String playerLocale = (String) ReflectionHelper.get(PLAYER_LOCALE, player);
		if(!defaultLanguage.getLocale().equals(playerLocale)) {
			localeMap.onPlayerLeave(player);
		}
		
	}
	
	public synchronized void setDefaultLocale(String locale) {
		this.defaultLanguage = localeMap.remove(locale);
		if(defaultLanguage == null) {
			defaultLanguage = LanguageMap.getFromLocale(locale);
		}
	}
	
	
	public static final I18n getInstance() { return INSTANCE;}
	
}
