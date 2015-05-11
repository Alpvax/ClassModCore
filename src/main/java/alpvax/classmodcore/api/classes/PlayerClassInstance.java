package alpvax.classmodcore.api.classes;

import static alpvax.classmodcore.api.ClassUtil.KEY_ID;
import static alpvax.classmodcore.api.ClassUtil.KEY_POWERS;
import static alpvax.classmodcore.api.ClassUtil.KEY_SLOT;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.classmodcore.api.ClassUtil;
import alpvax.classmodcore.api.powers.IPower;
import alpvax.classmodcore.api.powers.PowerEntry;
import alpvax.classmodcore.api.powers.PowerEntry.PlayerTriggeredPowerEntry;
import alpvax.classmodcore.api.powers.PowerInstance;


public class PlayerClassInstance
{
	public final EntityPlayer player;
	private IPlayerClass playerclass = null;
	private PowerInstance[] powers = new PowerInstance[0];
	private int[] manualIndexes;
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
			powers = new PowerInstance[0];
			manualIndexes = new int[ClassUtil.maxNumActivePowers];
			List<PowerEntry> list = playerclass.getPowers();
			if(list != null)
			{
				int num = list.size();
				powers = new PowerInstance[num];
				for(int i = 0; i < num; i++)
				{
					PowerEntry e = list.get(i);
					powers[i] = e.createInstance();
					if(e.triggerInstantly())
					{
						powers[i].init(player);
					}
					if(e instanceof PlayerTriggeredPowerEntry)
					{
						manualIndexes[((PlayerTriggeredPowerEntry)e).index] = i;
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
		if(nbt.hasKey(KEY_ID))
		{
			setPlayerClass(nbt.getString(KEY_ID));
			NBTTagList list = nbt.getTagList(KEY_POWERS, NBT.TAG_COMPOUND);
			for(int i = 0; i < list.tagCount(); i++)
			{
				NBTTagCompound tag = list.getCompoundTagAt(i);
				powers[tag.getInteger(KEY_SLOT)].readFromNBT(tag);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		if(playerclass != null)//TODO:null class
		{
			nbt.setString(KEY_ID, playerclass.getClassID());
			NBTTagList list = new NBTTagList();
			for(int i = 0; i < powers.length; i++)
			{
				NBTTagCompound tag = new NBTTagCompound();
				powers[i].writeToNBT(tag);
				tag.setInteger(KEY_SLOT, i);
				list.appendTag(tag);
			}
			nbt.setTag(KEY_POWERS, list);
		}
		dirty = false;
	}

	public void togglePower(int index)
	{
		if(index >= 0 && index < manualIndexes.length)
		{
			powers[manualIndexes[index]].togglePower(player);
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

	public List<PowerInstance> getPowers(Class<? extends IPower> powerclass)
	{
		List<PowerInstance> list = new ArrayList<PowerInstance>();
		for(PowerInstance p : powers)
		{
			if(powerclass == null || powerclass.isAssignableFrom(p.getPower().getClass()))
			{
				list.add(p);
			}
		}
		return list;
	}
}
