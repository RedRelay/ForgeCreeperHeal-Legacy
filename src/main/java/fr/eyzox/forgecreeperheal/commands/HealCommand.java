package fr.eyzox.forgecreeperheal.commands;

import fr.eyzox.forgecreeperheal.ForgeCreeperHeal;
import fr.eyzox.forgecreeperheal.exception.ForgeCreeperHealCommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class HealCommand extends ForgeCreeperHealCommands {

	@Override
	protected String getFCHCommandName() {
		return "heal";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender) {
		return super.getCommandUsage(sender)+" [<dim>|all]";
	}

	@Override
	protected String getHelp() {
		return "Instant heal terrain\n"
				+ "* <dim> : the dimension id to heal\n"
				+ "* all : heal all dimension\n"
				+ "* no parameters : heal the dimension where the command sender is currently located";
	}

	@Override
	protected void _execute(MinecraftServer server, ICommandSender sender, String[] args) throws ForgeCreeperHealCommandException {
		if(args.length > 0 && "all".equalsIgnoreCase(args[0])) {
			final WorldServer[] worlds = DimensionManager.getWorlds();
			for(WorldServer world : worlds) {
				ForgeCreeperHeal.getHealerManager(world).heal();
			}
			
			final ITextComponent healAllMsg = buildChatMessage(new TextComponentString("All worlds fully healed."));
			healAllMsg.getStyle().setColor(TextFormatting.GREEN);
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
					throw new ForgeCreeperHealCommandException("<dim> must be the dimension id", new Object[]{}, e);
				}
			}
			
			if(world == null) {
				throw new ForgeCreeperHealCommandException("Unable to find World from "+(rawDimId == null ? ("command sender "+sender) : ("dimension id "+rawDimId)), new Object[]{});
			}else {
				ForgeCreeperHeal.getHealerManager(world).heal();
				final ITextComponent healedMsg = buildChatMessage(new TextComponentString("World "+world.getWorldInfo().getWorldName()+":"+world.provider.getDimension()+" fully healed."));
				healedMsg.getStyle().setColor(TextFormatting.GREEN);
				sender.addChatMessage(healedMsg);
			}
		}
	}

}
