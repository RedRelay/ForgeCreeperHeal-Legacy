package fr.eyzox.forgecreeperheal;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class IngameConfigGui extends GuiConfig {

	public IngameConfigGui(GuiScreen parent) {
		super(parent, new ConfigElement(ForgeCreeperHeal.getConfig().getForgeConfig().getCategory(ForgeCreeperHeal.MODID)).getChildElements(), ForgeCreeperHeal.MODID,
				false, false, "Forge Creeper Heal");
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
	}
}
