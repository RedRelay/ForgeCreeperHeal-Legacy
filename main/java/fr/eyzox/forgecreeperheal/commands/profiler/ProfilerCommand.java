package fr.eyzox.forgecreeperheal.commands.profiler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.commands.ForgeCreeperHealCommands;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class ProfilerCommand extends CommandBase{

	@Override
	public String getCommandName() {
		return "profiler";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return null;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return ForgeCreeperHealCommands.isOp(sender);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length > 0) {
			if(args[0] != null && args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
				WorldHealer w;
				if(args.length > 1) {
					int dimensionID;
					try {
						dimensionID = Integer.parseInt(args[1]);
					}catch(NumberFormatException e) {
						ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Dimension must be an integer"));
						return;
					}

					w = ForgeCreeperHeal.getWorldHealer((WorldServer) DimensionManager.getWorld(dimensionID));
				}else {
					w = ForgeCreeperHeal.getWorldHealer((WorldServer) sender.getEntityWorld());
				}
				
				if(w == null) {
					ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Dimension doesn't exist or is not loaded"));
					return;
				}

				if(args[0].equalsIgnoreCase("enable")) {
					w.enableProfiler();
				}else {
					w.disableProfiler();
				}
			}
		}

	}

}
