package alpvax.classmodcore.api.classes;

import static alpvax.classmodcore.api.ClassUtil.KEY_ID;
import static alpvax.classmodcore.api.ClassUtil.KEY_POWERS;
import static alpvax.classmodcore.api.ClassUtil.KEY_SLOT;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.classmodcore.api.powers.PowerEntry;
import alpvax.classmodcore.api.powers.PowerInstance;


public class PlayerClassInstance
{
	private final EntityPlayer player;
	private IPlayerClass playerclass = null;
	private PowerInstance[] powers;
	private List<Integer> manualIndexes;
	private boolean dirty = false;

	public PlayerClassInstance(EntityPlayer player)
	{
		this.player = player;
	}

	protected void setPlayerClass(String classID)
	{
		setPlayerClass(PlayerClassRegistry.getPlayerClass(classID));
	}

	protected void setPlayerClass(IPlayerClass playerclass)
	{
		if(this.playerclass != playerclass)
		{
			this.playerclass = playerclass;
			for(PowerInstance power : powers)
			{
				power.stop(player);
			}
			List<PowerEntry> list = playerclass.getPowers();
			if(list != null)
			{
				for(int i = 0; i < list.size(); i++)
				{
					powers[i] = list.get(i).createInstance(i);
					powers[i].init(player);
					if(powers[i].setKeybind(i))
					{
						manualIndexes.add(Integer.valueOf(i++));
					}
				}
			}
			dirty = true;
		}
	}

	public void tick()
	{
		for(PowerInstance p : powers)
		{
			p.tickPower(player);
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		setPlayerClass(nbt.getString(KEY_ID));
		NBTTagList list = nbt.getTagList(KEY_POWERS, NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			powers[tag.getInteger(KEY_SLOT)].readFromNBT(tag);
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString(KEY_ID, playerclass.getClassID());
		NBTTagList list = new NBTTagList();
		for(PowerInstance power : powers)
		{
			NBTTagCompound tag = new NBTTagCompound();
			power.writeToNBT(tag);
			list.appendTag(tag);
		}
		nbt.setTag(KEY_POWERS, list);
		dirty = false;
	}

	public void togglePower(int index)
	{
		if(index >= 0 && index < manualIndexes.size())
		{
			powers[manualIndexes.get(index).intValue()].togglePower(player);
		}
	}

	public IPlayerClass getPlayerClass()
	{
		return playerclass;
	}

	/**
	 * @return true if something has changed (this needs saving)
	 */
	public boolean isDirty()
	{
		if(dirty)
		{
			return true;
		}
		for(PowerInstance p : powers)
		{
			if(p.isDirty())
			{
				return true;
			}
		}
		return false;
	}
}
