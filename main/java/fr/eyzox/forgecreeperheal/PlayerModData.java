package fr.eyzox.forgecreeperheal;

import java.util.Map;

import fr.eyzox.forgecreeperheal.network.ModDataMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerModData implements IExtendedEntityProperties {
	
	public final String MOD_VERSION;
	
	public PlayerModData(String modVersion) {
		this.MOD_VERSION = modVersion;
		System.out.println("OKOKOKOKOK");
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound) {}
	@Override
	public void loadNBTData(NBTTagCompound compound) {}
	@Override
	public void init(Entity entity, World world) {}

	public final static String getKey() {
		return ForgeCreeperHeal.MODID+":PlayerProperties";
	}
	
	public static final void register(EntityPlayer player, PlayerModData data) {
		player.registerExtendedProperties(getKey(), data);
	}
	
	public static final PlayerModData get(EntityPlayer player) {
		return (PlayerModData) player.getExtendedProperties(getKey());
	}

}
