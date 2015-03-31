package alpvax.mod.classmodcore.network;

import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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