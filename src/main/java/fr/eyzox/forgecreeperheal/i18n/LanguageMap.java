package fr.eyzox.forgecreeperheal.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import net.minecraft.entity.player.EntityPlayerMP;

class LanguageMap {
	
	private final String locale;
	private final Map<String, String> dico;
	private final Set<EntityPlayerMP> usedBy = new HashSet<EntityPlayerMP>();
	
	private LanguageMap(InputStream is, String locale) {
		this.dico = net.minecraft.util.text.translation.LanguageMap.parseLangFile(is);
		this.locale = locale;
	}
	
	public String translate(String key, Object[] o) {
		final String translated = dico.get(key);
		return translated == null ? key : (o == null ? translated : String.format(translated, o));
	}
	
	public String getLocale() {
		return locale;
	}
	
	protected Set<EntityPlayerMP> getUsedBy() {
		return usedBy;
	}
	
	public static LanguageMap getFromLocale(String locale) {
		InputStream is = null;
		LanguageMap dico = null;
		try {
			is = net.minecraft.util.text.translation.LanguageMap.class.getResourceAsStream("/assets/"+ForgeCreeperHeal.MODID+"/lang/"+locale+".lang");
			if(is != null) {
				dico = new LanguageMap(is, locale);
			}
		}finally {
			if(is != null) {
				try {is.close();} catch (IOException e1) {}
			}
		}
		return dico;
	}
}
