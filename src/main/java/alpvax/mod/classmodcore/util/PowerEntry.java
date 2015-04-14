package alpvax.mod.classmodcore.util;

import static alpvax.mod.classmodcore.core.ClassUtil.PWR_CD_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_DATA_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_DUR_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_END;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_SLOT_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_START;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_SUBSPLIT;
import net.minecraft.nbt.NBTTagCompound;

public class PowerEntry
{
	private int slot = -1;
	private int cooldown = -1;
	private int duration = -1;
	private String data = "";

	public PowerEntry(int i, int j)
	{
		slot = i;
		cooldown = j;
		duration = 0;
	}

	public PowerEntry(String s)
	{
		String[] s1 = s.split(PWR_SUBSPLIT);
		for(String s2 : s1)
		{
			if(s2.startsWith(PWR_SLOT_TAG))
			{
				slot = Integer.parseInt(s2.substring(PWR_SLOT_TAG.length() + 1));
			}
			if(s2.startsWith(PWR_CD_TAG))
			{
				cooldown = Integer.parseInt(s2.substring(PWR_CD_TAG.length() + 1));
			}
			if(s2.startsWith(PWR_DUR_TAG))
			{
				duration = Integer.parseInt(s2.substring(PWR_DUR_TAG.length() + 1));
			}
			if(s2.startsWith(PWR_DATA_TAG))
			{
				data = s2.substring(PWR_DATA_TAG.length() + 1);
			}
		}
	}

	public PowerEntry(NBTTagCompound tag)
	{
		if(tag.hasKey(PWR_SLOT_TAG))
		{
			slot = tag.getInteger(PWR_SLOT_TAG);
		}
		if(tag.hasKey(PWR_CD_TAG))
		{
			cooldown = tag.getInteger(PWR_CD_TAG);
		}
		if(tag.hasKey(PWR_DUR_TAG))
		{
			duration = tag.getInteger(PWR_DUR_TAG);
		}
		if(tag.hasKey(PWR_DATA_TAG))
		{
			data = tag.getString(PWR_DATA_TAG);
		}
	}

	public NBTTagCompound toNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		if(slot == -1 || cooldown == -1 || duration == -1)
		{
			return null;
		}
		tag.setInteger(PWR_SLOT_TAG, slot);
		tag.setInteger(PWR_CD_TAG, cooldown);
		tag.setInteger(PWR_DUR_TAG, duration);
		if(data != null && data.length() > 0)
		{
			tag.setString(PWR_DATA_TAG, data);
		}
		return tag;
	}

	public int getSlot()
	{
		return slot;
	}

	public int getCooldown()
	{
		return cooldown;
	}

	public int getDuration()
	{
		return duration;
	}

	public String getAdditionalData()
	{
		return data;
	}

	public void setCooldown(int i)
	{
		cooldown = i;
	}

	public void setDuration(int i)
	{
		duration = i;
	}

	public void setAdditionalData(String s)
	{
		data = s;
	}

	@Override
	public String toString()
	{
		return PWR_START + PWR_SLOT_TAG + "=" + Integer.toString(slot) + PWR_SUBSPLIT + PWR_CD_TAG + "=" + Integer.toString(cooldown) + PWR_SUBSPLIT + PWR_DUR_TAG + "=" + Integer.toString(duration) + PWR_SUBSPLIT + PWR_DATA_TAG + "=" + data + PWR_END;
	}
}