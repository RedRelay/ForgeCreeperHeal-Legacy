package fr.eyzox.forgecreeperheal.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

	//private ProfilerRenderEventHandler profilerRenderEventHandler;
	
	@Override
	public void onInit(FMLInitializationEvent event) {
		super.onInit(event);
		//profilerRenderEventHandler = new ProfilerRenderEventHandler();
		//MinecraftForge.EVENT_BUS.register(new PlayerModDataHandler());
		//MinecraftForge.EVENT_BUS.register(profilerRenderEventHandler);
	}

	/*
	public ProfilerRenderEventHandler getProfilerRenderEventHandler() {
		return profilerRenderEventHandler;
	}
	*/

}
