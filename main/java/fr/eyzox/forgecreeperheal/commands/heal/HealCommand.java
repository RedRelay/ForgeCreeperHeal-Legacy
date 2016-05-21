package fr.eyzox.forgecreeperheal.commands.heal;

import fr.eyzox.forgecreeperheal.commands.CommandsContainer;
import net.minecraft.command.ICommandSender;

public class HealCommand extends CommandsContainer {

	public HealCommand() {
		this.register(new HealAllCommand());
	}
	
	@Override
	public String getName() {
		return "heal";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

	/*
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		//TODO HealCommand
		
		if(args.length > 0) {
			if("all".equalsIgnoreCase(args[0])) {
				for(WorldServer world : DimensionManager.getWorlds()) {
					HealerManager wh = HealerManager.getChunkHealerManager(world);
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
				HealerManager wh = HealerManager.getChunkHealerManager((WorldServer) DimensionManager.getWorld(dimensionID));
				if(wh == null) {
					ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Invalid dimension"));
				}else {
					wh.healAll();
				}
			}
		}else if(sender instanceof EntityPlayer){
			HealerManager wh = HealerManager.getChunkHealerManager((WorldServer) sender.getEntityWorld());
			if(wh == null) {
				ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("Forge Creeper Heal is not enabled for this dimension"));
			}else {
				wh.healAll();
			}
		}else {
			ForgeCreeperHealCommands.addChatMessage(sender, new ChatComponentText("You must specified a dimension id"));
		}
		
	}
	*/

	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return ForgeCreeperHealCommands.isOp(sender);
	}
	
	

}
