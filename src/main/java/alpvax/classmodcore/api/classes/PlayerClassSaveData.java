package alpvax.classmodcore.api.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.classmodcore.api.ClassUtil;


/**
 * @author Alpvax
 *
 */
public class PlayerClassSaveData extends WorldSavedData
{
	private Map<String, PlayerClassInstance> data = new HashMap<String, PlayerClassInstance>();

	public PlayerClassSaveData()
	{
		this("PlayerClass");
	}

	public PlayerClassSaveData(String mapName)
	{
		super(mapName);
		//super("PlayerClass (" + mapName + ")");
	}

	public PlayerClassInstance getPlayerClass(String name)
	{
		if(hasPlayerClass(name))
		{
			return data.get(name);
		}
		EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name);
		if(player != null)
		{
			PlayerClassInstance pci = PlayerClassInstance.create(player);
			data.put(name, pci);
			markDirty();
			return pci;
		}
		return null;
	}

	public boolean hasPlayerClass(String name)
	{
		return data.containsKey(name) && data.get(name).getPlayerClass() != null && !StringUtils.isNullOrEmpty(data.get(name).getPlayerClass().getClassID());
	}

	public void setPlayerClass(String name, IPlayerClass playerclass)
	{
		setPlayerClass(MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name), playerclass);
	}

	public void setPlayerClass(EntityPlayer player, IPlayerClass playerclass)
	{
		if(player == null)
		{
			return;
		}
		PlayerClassInstance pci = getPlayerClass(player.getName());
		if(pci == null)
		{
			pci = PlayerClassInstance.create(player);
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
			PlayerClassInstance pci = PlayerClassInstance.create(MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name));
			if(pci != null)
			{
				pci.readFromNBT(tag);
				data.put(name, pci);
			}
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
