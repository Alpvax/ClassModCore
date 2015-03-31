package alpvax.mod.classmodcore.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import alpvax.mod.classmodcore.client.GuiClassSelect;
import alpvax.mod.classmodcore.core.ClassUtil;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	private static Map<String, NBTTagCompound> epMap = new HashMap<String, NBTTagCompound>();
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == ClassUtil.classGUIID) return new GuiClassSelect();
		/*TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile == null) return null;
		if(tile instanceof TileEntityForge)
		{
			return new GuiForge(player.inventory, (TileEntityForge)tile);
		}
		if(tile instanceof TileEntityNetherSmelter)
		{
			return new GuiNetherSmelter(player.inventory, (TileEntityNetherSmelter)tile);
		}*/
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		/*TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile == null) return null;
		if(tile instanceof TileEntityForge)
		{
			return new ContainerForge(player.inventory, (TileEntityForge)tile);
		}
		if(tile instanceof TileEntityNetherSmelter)
		{
			return new ContainerNetherSmelter(player.inventory, (TileEntityNetherSmelter)tile);
		}*/
		return null;
	}
	
	public static void storeEntityData(String name, NBTTagCompound compound)
	{
		epMap.put(name, compound);
	}
	
	public static NBTTagCompound getEntityData(String name)
	{
		return epMap.remove(name);
	}

	public void registerClientHandlers()
	{
	}

	public void registerRenderInformation()
	{
	}
}
