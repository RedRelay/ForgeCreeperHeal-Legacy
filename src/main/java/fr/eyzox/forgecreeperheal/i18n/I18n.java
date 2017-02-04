package fr.eyzox.forgecreeperheal.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.reflection.ReflectionHelper;
import fr.eyzox.forgecreeperheal.reflection.ReflectionManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class I18n {
	
	private final static Field PLAYER_LOCALE = ReflectionManager.getInstance().getField(EntityPlayerMP.class, "language");

	private static final I18n INSTANCE = new I18n();
	
	private final Map<String, Map<String, String>> languageMap = new HashMap<String, Map<String, String>>();

	private final static String DEFAULT_LOCALE = "en_US";
	
	private I18n() {
		inject(DEFAULT_LOCALE);
	}
	
	@SubscribeEvent
	public void onPostConfigChangedEvent(PostConfigChangedEvent e) {
		System.out.println("EVENT FIRED : PostConfigChangedEvent !");
	}
	
	public synchronized String translate(String key, Object[] o) {
		return this.translate((EntityPlayerMP)null, key, o);
	}
	
	public synchronized String translate(EntityPlayerMP player, String key, Object[] o) {
		Map<String, String> dico = null;
		if(player != null) {
			final String locale = (String) ReflectionHelper.get(PLAYER_LOCALE, player);
			dico = languageMap.get(locale);
		}
		if(dico == null) {
			dico = languageMap.get(DEFAULT_LOCALE);
		}
		return dico == null ? key : translate(dico, key, o);
	}
	
	public synchronized void onPlayerJoin(EntityPlayerMP player) {
		final String locale = (String) ReflectionHelper.get(PLAYER_LOCALE, player);
		if(!languageMap.containsKey(locale)) {
			this.inject(locale);
		}
	}
	
	public void onPlayerLeave(EntityPlayerMP player) {
		this.remove((String) ReflectionHelper.get(PLAYER_LOCALE, player));
	}
	
	private synchronized void inject(String locale) {
		InputStream is = null;
		try {
			is = getLangInputStream(locale);
			if(is != null) {
				final Map<String, String> clientLocalMap = LanguageMap.parseLangFile(is);
				languageMap.put(locale, clientLocalMap);
			}
		}finally {
			if(is != null) {
				try {is.close();} catch (IOException e1) {}
			}
		}
		
	}
	
	private synchronized void remove(String locale) {
		languageMap.remove(locale);
	}
	
	private String translate(Map<String, String> dico, String key, Object[] o) {
		final String translated = dico.get(key);
		return translated == null ? key : (o == null ? translated : String.format(translated, o));
	}
	
	private InputStream getLangInputStream(String locale) {
		return LanguageMap.class.getResourceAsStream("/assets/"+ForgeCreeperHeal.MODID+"/lang/"+locale+".lang");
	}
	
	public static final I18n getInstance() { return INSTANCE;}
	
}
