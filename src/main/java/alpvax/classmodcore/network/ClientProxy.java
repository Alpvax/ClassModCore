package alpvax.classmodcore.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import alpvax.classmodcore.api.ClassUtil;
import alpvax.classmodcore.client.GuiClassSelect;


@net.minecraftforge.fml.relauncher.SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == ClassUtil.classGUIID)
		{
			return new GuiClassSelect();
		}
		return super.getClientGuiElement(ID, player, world, x, y, z);
	}

	@Override
	public void registerClientHandlers()
	{
		// KeyBindingRegistry.registerKeyBinding(new ClassKeyHandler());
		// TickRegistry.registerTickHandler(new GUITickHandler(), Side.CLIENT);
	}

	@Override
	public void registerRenderInformation()
	{
		/*
		 * MinecraftForgeClient.preloadTexture("/gui/classHud.png"); for(PlayerClass playerclass : PlayerClass.classList) { MinecraftForgeClient.preloadTexture(playerclass.getImage()); }
		 */
	}
}