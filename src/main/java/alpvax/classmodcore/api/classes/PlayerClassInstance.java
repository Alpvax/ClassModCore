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
import alpvax.classmodcore.api.powers.IPower;
import alpvax.classmodcore.api.powers.PowerEntry;
import alpvax.classmodcore.api.powers.PowerInstance;


public class PlayerClassInstance
{
	public final EntityPlayer player;
	private IPlayerClass playerclass = null;
	private PowerInstance[] powers = new PowerInstance[0];
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
				int num = list.size();
				powers = new PowerInstance[num];
				for(int i = 0; i < num; i++)
				{
					powers[i] = list.get(i).createInstance(i);
					powers[i].init(player);
					if(powers[i].setKeybind(i))
					{
						manualIndexes.add(Integer.valueOf(i++));
					}
				}
			}
			powers = new PowerInstance[0];
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
		if(playerclass != null)//TODO:null class
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
		}
		dirty = false;
	}

	public void togglePower(int index)
	{
		if(index >= 0 && index < manualIndexes.size())
		{
			powers[manualIndexes.get(index).intValue()].togglePower(player, null);
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

	public List<PowerInstance> getActivePowers(Class<? extends IPower> powerclass)
	{
		if(powerclass == null)
		{
			powerclass = IPower.class;
		}
		List<PowerInstance> list = new ArrayList<PowerInstance>();
		System.err.println("Num powers: " + powers.length);//XXX
		for(PowerInstance p : powers)
		{
			System.err.println(p.getPower().getDisplayName() + ": " + p.isActive());//XXX
			if(p.isActive() && powerclass.isAssignableFrom(p.getPower().getClass()))
			{
				list.add(p);
			}
		}
		return list;
	}
}
