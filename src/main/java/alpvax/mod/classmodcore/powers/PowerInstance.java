package alpvax.mod.classmodcore.powers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author Alpvax
 *
 */
public class PowerInstance
{
	private static final String KEY_TYPE = "Type";
	private static final String KEY_POWER = "Power";
	private static final String KEY_CD = "Cooldown";
	private static final String KEY_DUR = "Duration";
	private static final String KEY_DATA = "Data";
	
	
	private IPower power;
	private int maxCD;
	private int cooldown;
	private int maxDur;
	private int duration;
	private boolean active;
	private byte type;
	private NBTTagCompound data = null;
	
	protected PowerInstance(IPower power, byte type, int... timers)
	{
		this.power = power;
		this.type = type;
		this.active = (type & PowerType.CONTINUOUS) == PowerType.CONTINUOUS;
		//TODO: timers
	}
	
	public void tickPower(EntityPlayer player)
	{
		if(hasDuration() && duration > 0)
		{
			if(--duration < 0)
			{
				duration = 0;
			}
			power.onTick(player);
		}
		if(hasCooldown() && cooldown > 0)
		{
			cooldown--;
		}
	}
	private void triggerPower(EntityPlayer player)
	{
		
	}
	
	public boolean canTrigger()
	{
		return PowerType.hasCooldown(type) && cooldown < 1;
	}
	
	private boolean hasDuration()
	{
		return PowerType.hasDuration(type);
	}
	private boolean hasCooldown()
	{
		return PowerType.hasCooldown(type) && maxCD > 0;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		type = nbt.getByte(KEY_TYPE);
		power = PowerRegistry.getPower(nbt.getString(KEY_POWER));
		if(nbt.hasKey(KEY_DUR, NBT.TAG_ANY_NUMERIC))
		{
			duration = nbt.getInteger(KEY_DUR);
		}
		if(nbt.hasKey(KEY_CD, NBT.TAG_ANY_NUMERIC))
		{
			cooldown = nbt.getInteger(KEY_CD);
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setByte(KEY_TYPE, type);
		nbt.setString(KEY_POWER, PowerRegistry.getPowerID(power));
		if(hasDuration())
		{
			nbt.setInteger(KEY_DUR, duration);
		}
		if(hasCooldown())
		{
			nbt.setInteger(KEY_CD, cooldown);
		}
		if(data != null)
		{
			nbt.setTag(KEY_DATA, data);
		}
	}
}
