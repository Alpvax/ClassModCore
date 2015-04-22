package alpvax.classmodcore.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;


public class CommonProxy implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		/*
		 * TileEntity tile = world.getBlockTileEntity(x, y, z); if(tile == null) return null; if(tile instanceof TileEntityForge) { return new GuiForge(player.inventory, (TileEntityForge)tile); } if(tile instanceof TileEntityNetherSmelter) { return new GuiNetherSmelter(player.inventory, (TileEntityNetherSmelter)tile); }
		 */
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		/*
		 * TileEntity tile = world.getBlockTileEntity(x, y, z); if(tile == null) return null; if(tile instanceof TileEntityForge) { return new ContainerForge(player.inventory, (TileEntityForge)tile); } if(tile instanceof TileEntityNetherSmelter) { return new ContainerNetherSmelter(player.inventory, (TileEntityNetherSmelter)tile); }
		 */
		return null;
	}

	public void registerClientHandlers()
	{
	}

	public void registerRenderInformation()
	{
	}
}
