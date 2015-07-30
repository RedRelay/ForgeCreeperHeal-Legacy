package fr.eyzox.forgecreeperheal.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.worldhealer.WorldHealer;

public class HealCommand extends CommandBase {

	@Override
	public String getName() {
		return "heal";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {

		if(args.length > 0) {
			if("all".equalsIgnoreCase(args[0])) {
				for(WorldServer world : DimensionManager.getWorlds()) {
					WorldHealer wh = ForgeCreeperHeal.getWorldHealer(world);
					if(wh != null) wh.healAll();
				}
			}else {
				int dimensionID;
				try {
					dimensionID = Integer.parseInt(args[0]);
				}catch(NumberFormatException e) {
					ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Dimension must be an integer"));
					return;
				}
				WorldHealer wh = ForgeCreeperHeal.getWorldHealer((WorldServer) DimensionManager.getWorld(dimensionID));
				if(wh == null) {
					ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Invalid dimension"));
				}else {
					wh.healAll();
				}
			}
		}else if(sender instanceof EntityPlayer){
			WorldHealer wh = ForgeCreeperHeal.getWorldHealer((WorldServer) sender.getEntityWorld());
			if(wh == null) {
				ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Forge Creeper Heal is not enabled for this dimension"));
			}else {
				wh.healAll();
			}
		}else {
			ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("You must specified a dimension id"));
		}
	}

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return ForgeCreeperHealCommands.isOp(sender);
	}
	
	

}
