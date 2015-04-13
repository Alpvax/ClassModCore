package alpvax.mod.classmodcore.network;

import net.minecraftforge.fml.relauncher.Side;

@net.minecraftforge.fml.relauncher.SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public void registerClientHandlers()
	{
		//KeyBindingRegistry.registerKeyBinding(new ClassKeyHandler());
		//TickRegistry.registerTickHandler(new GUITickHandler(), Side.CLIENT);
	}
	
	@Override
	public void registerRenderInformation()
	{
		/*MinecraftForgeClient.preloadTexture("/gui/classHud.png");
		for(PlayerClass playerclass : PlayerClass.classList)
		{
			MinecraftForgeClient.preloadTexture(playerclass.getImage());
		}*/
	}
}