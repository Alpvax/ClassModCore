package alpvax.classmodcore.api.powers;

import static alpvax.classmodcore.api.ClassUtil.KEY_ACTIVE;
import static alpvax.classmodcore.api.ClassUtil.KEY_CD;
import static alpvax.classmodcore.api.ClassUtil.KEY_DATA;
import static alpvax.classmodcore.api.ClassUtil.KEY_DUR;
import static alpvax.classmodcore.api.ClassUtil.KEY_KEYBIND;
import static alpvax.classmodcore.api.ClassUtil.KEY_SLOT;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.classmodcore.api.events.ChangePowerStateEvent.ResetPowerEvent;
import alpvax.classmodcore.api.events.ChangePowerStateEvent.ResetPowerForClassChangeEvent;
import alpvax.classmodcore.api.events.ChangePowerStateEvent.StartContinuousPowerEvent;
import alpvax.classmodcore.api.events.ChangePowerStateEvent.TriggerPowerEvent;
import alpvax.classmodcore.api.events.ChangePowerStateEvent.TriggerPowerTimedEvent;


public class PowerInstance
{
	private final IPower power;
	private final EnumPowerType type;
	public final boolean manual;
	private final int index;
	private boolean dirty = false;
	private int keyIndex;
	private int maxCD;
	private int maxDur;
	private boolean active = false;
	private int cooldown = 0;
	private int duration = 0;
	protected Map<String, Object> data;

	protected PowerInstance(IPower power, EnumPowerType type, boolean manualTrigger, int index, Map<String, Object> additionalData)
	{
		this.power = power;
		this.type = type;
		this.index = index;
		data = additionalData;
		manual = !active && manualTrigger;
		maxCD = data.containsKey(PowerEntry.KEY_COOLDOWN) ? ((Integer)data.get(PowerEntry.KEY_COOLDOWN)).intValue() : -1;
		maxDur = data.containsKey(PowerEntry.KEY_DURATION) ? ((Integer)data.get(PowerEntry.KEY_DURATION)).intValue() : -1;
		power.initialise(data);
	}

	public void tickPower(EntityPlayer player)
	{
		if(hasDuration() && duration > 0)
		{
			if(--duration < 0)
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
		if(type != EnumPowerType.CONTINUOUS)
		{
			if(cooldown > 0)
			{
				cooldown--;
			}
			if(!manual)
			{
				if(!active && power.shouldTrigger(player, data))
				{
					triggerPower(player);
				}
				if(active && type == EnumPowerType.TOGGLED && power.shouldReset(player, data))
				{
					resetPower(player);
				}
			}
		}
		dirty = true;
	}

	public void togglePower(EntityPlayer player)
	{
		dirty = active ? resetPower(player) : triggerPower(player);
	}

	public void init(EntityPlayer player)
	{
		if(type == EnumPowerType.CONTINUOUS)
		{
			MinecraftForge.EVENT_BUS.post(new StartContinuousPowerEvent(player, power, data));
			active = true;
			power.triggerPower(player, data);
		}
	}

	public void stop(EntityPlayer player)
	{
		MinecraftForge.EVENT_BUS.post(new ResetPowerForClassChangeEvent(player, power, 0, data));
		active = false;
		power.resetPower(player, data);
	}

	private boolean triggerPower(EntityPlayer player)
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

	private boolean resetPower(EntityPlayer player)
	{
		ResetPowerEvent e = new ResetPowerEvent(player, power, cooldown, data);
		if(MinecraftForge.EVENT_BUS.post(e))
		{
			return false;
		}
		active = false;
		power.resetPower(player, data);
		return true;
	}

	public boolean isActive()
	{
		return active;
	}

	public boolean canTrigger()
	{
		return type != EnumPowerType.CONTINUOUS && cooldown < 1;
	}

	private boolean hasDuration()
	{
		return maxDur >= 0;
	}

	private boolean hasCooldown()
	{
		return maxCD > 0;
	}

	public boolean setKeybind(int keyIndex)
	{
		if(manual)
		{
			this.keyIndex = keyIndex;
			return true;
		}
		return false;
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
		if(nbt.hasKey(KEY_KEYBIND))
		{
			keyIndex = nbt.getInteger(KEY_KEYBIND);
		}
		if(power instanceof IExtendedPower)
		{
			NBTTagCompound tag = nbt.getCompoundTag(KEY_DATA);
			((IExtendedPower)power).readFromNBT(tag);
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger(KEY_SLOT, index);
		nbt.setBoolean(KEY_ACTIVE, active);
		if(hasDuration())
		{
			nbt.setInteger(KEY_DUR, duration);
		}
		if(hasCooldown())
		{
			nbt.setInteger(KEY_CD, cooldown);
		}
		if(manual)
		{
			nbt.setInteger(KEY_KEYBIND, keyIndex);
		}
		if(power instanceof IExtendedPower)
		{
			NBTTagCompound tag = new NBTTagCompound();
			((IExtendedPower)power).writeToNBT(tag);
			nbt.setTag(KEY_DATA, tag);
		}
		dirty = false;
	}

	/**
	 * @return
	 */
	public boolean isDirty()
	{
		return dirty;
	}
}
