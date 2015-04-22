package alpvax.classmodcore.api.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.classmodcore.api.ClassUtil;


/**
 * @author Alpvax
 *
 */
class PlayerClassSaveData extends WorldSavedData
{
	private Map<String, PlayerClassInstance> data = new HashMap<String, PlayerClassInstance>();

	public PlayerClassSaveData()
	{
		super("PlayerClass");
	}

	public PlayerClassInstance getPlayerClass(String name)
	{
		if(hasPlayerClass(name))
		{
			return data.get(name);
		}
		PlayerClassInstance pci = new PlayerClassInstance(MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name));
		data.put(name, pci);
		markDirty();
		return pci;
	}

	public boolean hasPlayerClass(String name)
	{
		return data.containsKey(name) && data.get(name).getPlayerClass() != null;
	}

	public void setPlayerClass(String name, IPlayerClass playerclass)
	{
		setPlayerClass(MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name), playerclass);
	}

	public void setPlayerClass(EntityPlayer player, IPlayerClass playerclass)
	{
		PlayerClassInstance pci = getPlayerClass(player.getName());
		if(pci == null)
		{
			pci = new PlayerClassInstance(player);
		}
		pci.setPlayerClass(playerclass);
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList(ClassUtil.KEY_CLASSES, NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			String name = tag.getString(ClassUtil.KEY_PLAYER);
			PlayerClassInstance pci = new PlayerClassInstance(MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name));
			pci.readFromNBT(tag);
			data.put(name, pci);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(Entry<String, PlayerClassInstance> e : data.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString(ClassUtil.KEY_PLAYER, e.getKey());
			e.getValue().writeToNBT(tag);
			list.appendTag(tag);
		}
		nbt.setTag(ClassUtil.KEY_CLASSES, list);
	}

	@Override
	public boolean isDirty()
	{
		if(super.isDirty())
		{
			return true;
		}
		for(PlayerClassInstance pci : data.values())
		{
			if(pci.isDirty())
			{
				return true;
			}
		}
		return false;
	}
}
