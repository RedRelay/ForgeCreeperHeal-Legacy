package fr.eyzox.forgecreeperheal;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class IngameConfigFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft mc) {
	}

//	@Override
//	public Class<? extends GuiScreen> mainConfigGuiClass() {
//		return IngameConfigGui.class;
//	}
//	@Override
//	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
//		return null;
//	}


  @Override
  public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
    Set<RuntimeOptionCategoryElement> set =  new HashSet<RuntimeOptionCategoryElement>();
    
    set.add(new RuntimeOptionCategoryElement(null,"forgecreeperheal"));
    return set;// "forgecreeperheal"  
  }

  @Override
  public boolean hasConfigGui() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public GuiScreen createConfigGui(GuiScreen parentScreen) {
    // TODO Auto-generated method stub
    return null;
  }
}
