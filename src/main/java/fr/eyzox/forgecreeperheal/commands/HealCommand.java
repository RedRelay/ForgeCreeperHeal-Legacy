package fr.eyzox.forgecreeperheal.commands;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealCommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class HealCommand extends ForgeCreeperHealCommands {

	@Override
	protected String getFCHCommandName() {
		return "heal";
	}
	
	@Override
	protected String getFCHUsage() {
		return "[<dim>|all]";
	}

	@Override
	protected String getHelp() {
		return "fch.command.heal.help";
	}

	@Override
	protected void _execute(MinecraftServer server, ICommandSender sender, String[] args) throws ForgeCreeperHealCommandException {
		if(args.length > 0 && "all".equalsIgnoreCase(args[0])) {
			final WorldServer[] worlds = DimensionManager.getWorlds();
			for(WorldServer world : worlds) {
				ForgeCreeperHeal.getHealerManager(world).heal();
			}
			
			final ITextComponent healAllMsg = buildChatMessage(sender, buildTranslationMessage(sender, "fch.command.heal.action.allworld"), MessageType.SUCCESS);
			sender.addChatMessage(healAllMsg);
		}else {
			WorldServer world = null;
			final String rawDimId = args.length > 0 ? args[0] : null; 
			if(rawDimId == null) {
				world = (WorldServer) sender.getEntityWorld();
			}else {
				try {
					world = DimensionManager.getWorld(parseInt(rawDimId));
				}catch(NumberInvalidException e) {
					throw new ForgeCreeperHealCommandException(sender, "fch.command.heal.exception.invaliddimensionid", null, e);
				}
			}
			
			if(world == null) {
				if(rawDimId == null) {
					throw new ForgeCreeperHealCommandException(sender, "fch.command.heal.exception.noentityworld", new Object[]{sender});
				}
				throw new ForgeCreeperHealCommandException(sender, "fch.command.heal.exception.nodimensionid", new Object[]{rawDimId});
			}else {
				ForgeCreeperHeal.getHealerManager(world).heal();
				final ITextComponent healedMsg = buildChatMessage(sender, buildTranslationMessage(sender, "fch.command.heal.action.world", new Object[]{world.getWorldInfo().getWorldName(), world.provider.getDimension()}), MessageType.SUCCESS);
				sender.addChatMessage(healedMsg);
			}
		}
	}

}
