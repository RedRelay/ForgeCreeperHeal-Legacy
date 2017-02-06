package fr.eyzox.forgecreeperheal.i18n;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;

class LocaleMap {
	
	private final Map<String, LanguageMap> cachedLocales = new HashMap<String, LanguageMap>();
	private final Map<EntityPlayerMP, LanguageMap> cachedPlayerLocales = new HashMap<EntityPlayerMP, LanguageMap>();
	
	protected LocaleMap() {
	}
	
	public synchronized LanguageMap getLocalFor(EntityPlayerMP player, String locale) {
		LanguageMap playerLocale = cachedPlayerLocales.get(player);
		if(playerLocale != null) {
			if(locale.equals(playerLocale.getLocale())) {
				return playerLocale;
			}
			unregister(player, playerLocale);
		}
		
		playerLocale = cachedLocales.get(locale);
		if(playerLocale == null) {
			playerLocale = LanguageMap.getFromLocale(locale);
			if(playerLocale != null) {
				cachedLocales.put(locale, playerLocale);
			}
		}
		
		if(playerLocale != null) {
			register(player, playerLocale);
		}
		
		return playerLocale;

	}
	
	public void onPlayerLeave(EntityPlayerMP player) {
		LanguageMap playerLocale = cachedPlayerLocales.get(player);
		if(playerLocale != null) {
			unregister(player, playerLocale);
		}
	}
	
	private void unregister(EntityPlayerMP player, LanguageMap playerLocale) {
		playerLocale.getUsedBy().remove(player);
		if(playerLocale.getUsedBy().isEmpty()) {
			cachedLocales.remove(playerLocale.getLocale());
		}
	}
	
	private void register(EntityPlayerMP player, LanguageMap playerLocale) {
		playerLocale.getUsedBy().add(player);
		cachedPlayerLocales.put(player, playerLocale);
	}
	
	protected LanguageMap remove(String locale) {
		LanguageMap dico = cachedLocales.get(locale);
		if(dico != null) {
			for(EntityPlayerMP player : dico.getUsedBy()) {
				cachedPlayerLocales.remove(player);
			}
			dico.getUsedBy().clear();
		}
		return dico;
	}
	
}
