package alpvax.mod.classmodcore.powers;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.mod.classmodcore.events.TriggerPowerEvent;
import alpvax.mod.classmodcore.events.TriggerPowerEvent.ResetPowerEvent;
import alpvax.mod.classmodcore.events.TriggerPowerEvent.TriggerPowerTimedEvent;

public class PowerInstance
{
	private static final String KEY_ACTIVE = "Active";
	private static final String KEY_CD = "Cooldown";
	private static final String KEY_DUR = "Duration";
	private static final String KEY_DATA = "Data";
	
	
	private final IPower power;
	private final EnumPowerType type;
	public final boolean manual;
	private boolean active;
	private int maxCD;
	private int maxDur;
	private int cooldown = 0;
	private int duration = 0;
	protected Map<String, Object> data;
	
	protected PowerInstance(IPower power, EnumPowerType type, boolean manualTrigger, Map<String, Object> additionalData)
	{
		this.power = power;
		this.type = type;
		data = additionalData;
		active = type == EnumPowerType.CONTINUOUS;
		manual = !active && manualTrigger;
		maxCD = data.containsKey(PowerEntry.KEY_COOLDOWN) ? ((Integer)data.get(PowerEntry.KEY_COOLDOWN)).intValue() : -1;
		maxDur = data.containsKey(PowerEntry.KEY_DURATION) ? ((Integer)data.get(PowerEntry.KEY_DURATION)).intValue() : -1;
		power.initialise(data);
	}

	public void tickPower(EntityPlayer player)
	{
		if(hasDuration() && duration > 0)
		{
			if( --duration < 0)
			{
				duration = 0;
				if(type != EnumPowerType.CONTINUOUS)
				{
					resetPower(player);
				}
			}
		}
		if(active)
		{
			power.onTick(player, data);
		}
		if(triggerable())
		{
			if(cooldown > 0)
			{
				cooldown-- ;
			}
			if(!manual)
			{
				if(!active && type != EnumPowerType.CONTINUOUS && power.shouldTrigger(player, data))
				{
					triggerPower(player);
				}
				if(active && type  == EnumPowerType.TOGGLED && power.shouldReset(player, data))
				{
					resetPower(player);
				}
			}
		}
	}

	public boolean triggerPower(EntityPlayer player)
	{
		if(!canTrigger())
		{
			return false;
		}
		TriggerPowerEvent e;
		if(hasDuration())
		{
			e = new TriggerPowerTimedEvent(player, power, cooldown + maxCD, duration + maxDur, data);
		}
		else
		{
			e = new TriggerPowerEvent(player, power, cooldown + maxCD, data);
		}
		if(MinecraftForge.EVENT_BUS.post(e) || !power.triggerPower(player, data))
		{
			return false;
		}
		
		if(type != EnumPowerType.INSTANT)
		{
			active = true;
		}
		
		return true;
	}
	
	public void resetPower(EntityPlayer player)
	{
		ResetPowerEvent e = new ResetPowerEvent(player, power, cooldown, data);
		if(MinecraftForge.EVENT_BUS.post(e))
		{
			return;
		}
		active = false;
		power.resetPower(player, data);
	}
	
	public boolean isActive()
	{
		return active;
	}

	public boolean canTrigger()
	{
		return triggerable() && cooldown < 1;
	}

	private boolean hasDuration()
	{
		return maxDur >= 0;
	}

	private boolean hasCooldown()
	{
		return maxCD > 0;
	}
	private boolean triggerable()
	{
		return type != EnumPowerType.CONTINUOUS;
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		//type = nbt.getByte(KEY_TYPE);
		//power = PowerRegistry.getPower(nbt.getString(KEY_POWER));
		active = nbt.getBoolean(KEY_ACTIVE);
		if(nbt.hasKey(KEY_DUR, NBT.TAG_ANY_NUMERIC))
		{
			duration = nbt.getInteger(KEY_DUR);
		}
		if(nbt.hasKey(KEY_CD, NBT.TAG_ANY_NUMERIC))
		{
			cooldown = nbt.getInteger(KEY_CD);
		}
		if(power instanceof IExtendedPower)
		{
			NBTTagCompound tag = nbt.getCompoundTag(KEY_DATA);
			((IExtendedPower)power).readFromNBT(tag);
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		if(hasDuration())
		{
			nbt.setInteger(KEY_DUR, duration);
		}
		if(hasCooldown())
		{
			nbt.setInteger(KEY_CD, cooldown);
		}
		if(power instanceof IExtendedPower)
		{
			NBTTagCompound tag = new NBTTagCompound();
			((IExtendedPower)power).writeToNBT(tag);
			nbt.setTag(KEY_DATA, tag);
		}
	}
}
