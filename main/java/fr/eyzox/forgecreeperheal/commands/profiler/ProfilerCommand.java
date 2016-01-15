package fr.eyzox.forgecreeperheal.commands.profiler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
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
		if(args.length > 0 && args[0] != null) {
			
			WorldHealer w = null;
			int startIndex = 1;
			boolean switchProfiler = false, force = false, all = false;
			
			//Gets switchProfiler
			if(args[0].equalsIgnoreCase("enable")) switchProfiler = true;
			else if(!args[0].equalsIgnoreCase("disable")) return;
			
			//Gets all
			for(int i = startIndex; i<args.length ; i++) {
				if("all".equalsIgnoreCase(args[i])){
					all = true;
					break;
				}
			}
			
			//Gets WorldHealer
			if(!all) {
				if(args.length > startIndex) {
					int dimensionID;
					try {
						dimensionID = Integer.parseInt(args[startIndex]);
					}catch(NumberFormatException e) {
						ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Dimension must be an integer"));
						return;
					}
					startIndex++;
					w = ForgeCreeperHeal.getWorldHealer((WorldServer) DimensionManager.getWorld(dimensionID));
				}else {
					w = ForgeCreeperHeal.getWorldHealer((WorldServer) sender.getEntityWorld());
				}
				
				if(w == null) {
					ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("You must specified a valid dimensionID or use \"all\""));
					return;
				}
			}
			
			//Gets force
			if(!switchProfiler) {
				for(int i = startIndex; i<args.length ; i++) {
					if("force".equalsIgnoreCase(args[i])) {
						force = true;
						break;
					}
				}
			}
			


			//Process enable
			if(switchProfiler) {
				if(all) {
					for(WorldServer world : DimensionManager.getWorlds()) {
						WorldHealer wh = ForgeCreeperHeal.getWorldHealer(world);
						if(wh != null) wh.enableProfiler(sender);
					}
				}else {
					w.enableProfiler(sender);
				}
			//Process disable
			}else {
				if(all) {
					for(WorldServer world : DimensionManager.getWorlds()) {
						WorldHealer wh = ForgeCreeperHeal.getWorldHealer(world);
						if(wh != null) {
							if(force) wh.disableProfiler();
							else wh.disableProfiler(sender);
						}
					}
				}else {
					if(force) w.disableProfiler();
					else w.disableProfiler(sender);
				}
			}
			
		}

	}

}
