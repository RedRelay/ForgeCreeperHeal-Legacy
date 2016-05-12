package fr.eyzox.forgecreeperheal.commands.heal;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class HealAllCommand extends CommandBase {

	public HealAllCommand() {}

	@Override
	public String getName() {
		return "all";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "all [me|Dimension ID]";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0) {
			String param = args[0];
			WorldServer world = null;
			if(param.equalsIgnoreCase("me")) {
				world = (WorldServer) sender.getEntityWorld();
			}else {
				int dimID;
				try {
					dimID = Integer.parseInt(param);
				}catch(NumberFormatException e) {
					ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Dimension ID must be a number."));
					return;
				}
				world = DimensionManager.getWorld(dimID);
			}
			
			if(world == null) {
				ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Unable to find this world"));
			}else {
				ForgeCreeperHeal.getHealerManager(world).heal();
				ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("World "+world.provider.getDimensionName()+":"+world.provider.getDimensionId()+" fully healed."));
			}
		}else {
			final WorldServer[] worlds = DimensionManager.getWorlds();
			for(WorldServer world : worlds) {
				ForgeCreeperHeal.getHealerManager(world).heal();
			}
			ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("All world fully healed."));
		}

	}

}
