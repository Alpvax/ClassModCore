package alpvax.classmodcore.api.powers;

import static alpvax.classmodcore.api.ClassUtil.KEY_ACTIVE;
import static alpvax.classmodcore.api.ClassUtil.KEY_CD;
import static alpvax.classmodcore.api.ClassUtil.KEY_DATA;
import static alpvax.classmodcore.api.ClassUtil.KEY_DUR;
import static alpvax.classmodcore.api.ClassUtil.KEY_KEYBIND;
import static alpvax.classmodcore.api.ClassUtil.KEY_SLOT;
import static alpvax.classmodcore.api.ClassUtil.KEY_TICKSELAPSED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.classmodcore.api.events.TogglePowerEvent.ResetPowerEvent;
import alpvax.classmodcore.api.events.TogglePowerEvent.ResetPowerForClassChangeEvent;
import alpvax.classmodcore.api.events.TogglePowerEvent.StartContinuousPowerEvent;
import alpvax.classmodcore.api.events.TogglePowerEvent.TriggerPowerEvent;
import alpvax.classmodcore.api.powers.IPower.IExtendedPower;
import alpvax.classmodcore.api.powers.IPower.IMultiTargetPower;
import alpvax.classmodcore.api.powers.IPower.ITargetedPower;
import alpvax.classmodcore.api.powers.IPower.ITickingPower;
import alpvax.common.util.EntityHelper;

import com.google.common.base.Predicate;


public class PowerInstance
{
	private final IPower power;
	private final EnumPowerType type;
	private EnumPowerCastType targetType;//TODO:make final
	public final boolean manual;
	private final int index;
	private boolean dirty = false;
	private int keyIndex;
	private int maxCD;
	private int maxDur;
	private boolean active = false;
	private int cooldown = 0;
	private int duration = 0;
	private int ticks = 0;
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
					resetPower(player, data);
				}
			}
		}
		if(active && power instanceof ITickingPower)
		{
			ticks = ((ITickingPower)power).onTick(player, ticks++, data);
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
					triggerPower(player, data);
				}
				if(active && type == EnumPowerType.TOGGLED && power.shouldReset(player, data))
				{
					resetPower(player, data);
				}
			}
		}
		dirty = true;
	}

	public Map<String, Object> togglePower(EntityPlayer player, Map<String, Object> instanceData)
	{
		if(instanceData == null)
		{
			instanceData = new HashMap<String, Object>();
		}
		instanceData.putAll(data);
		dirty = active ? resetPower(player, instanceData) : triggerPower(player, instanceData);
		return instanceData;
	}

	public void init(EntityPlayer player)
	{
		if(type == EnumPowerType.CONTINUOUS)
		{
			MinecraftForge.EVENT_BUS.post(new StartContinuousPowerEvent(player, this, data));
			active = true;
			power.triggerPower(player, data);
		}
	}

	public void stop(EntityPlayer player)
	{
		MinecraftForge.EVENT_BUS.post(new ResetPowerForClassChangeEvent(player, this, data));
		active = false;
		power.resetPower(player, data);
	}

	private boolean triggerPower(EntityPlayer player, Map<String, Object> instanceData)
	{
		if(!canTrigger())
		{
			return false;
		}
		int c = cooldown;
		int d = duration;
		cooldown += maxCD;
		duration += maxDur;
		TriggerPowerEvent e = new TriggerPowerEvent(player, this, instanceData);
		if(MinecraftForge.EVENT_BUS.post(e) || !power.triggerPower(player, instanceData))
		{
			cooldown = c;
			duration = d;
			return false;
		}
		if(type != EnumPowerType.INSTANT)
		{
			active = true;
		}
		return true;
	}

	private boolean resetPower(EntityPlayer player, Map<String, Object> instanceData)
	{
		ResetPowerEvent e = new ResetPowerEvent(player, this, instanceData);
		if(MinecraftForge.EVENT_BUS.post(e))
		{
			return false;
		}
		active = false;
		power.resetPower(player, instanceData);
		ticks = 0;
		return true;
	}

	private List<Entity> getTargetEntities(EntityPlayer player, Map<String, Object> instanceData)
	{
		List<Entity> list = new ArrayList<Entity>();
		Predicate<Entity> filter = power instanceof ITargetedPower ? ((ITargetedPower)power).getEntityFilter(instanceData) : EntityHelper.EntitySelector.ALL;
		if(targetType == EnumPowerCastType.SELF)
		{
			if(power instanceof IMultiTargetPower)
			{
				return ((IMultiTargetPower)power).getTargetEntities(player, player.getPositionVector(), instanceData);
			}
			list.add(player);
			return list;
		}
		//TODO:"viewdistance" limit (currently 160D=16 chunks)
		MovingObjectPosition mop = EntityHelper.getLookingAt(player, 160D, filter);
		if(targetType == EnumPowerCastType.OTHER)
		{
			double homing = power instanceof ITargetedPower ? ((ITargetedPower)power).homingRadius(instanceData) : 0D;
			Entity e = EntityHelper.getClosestEntity(player.worldObj, mop, homing, filter);
			if(power instanceof IMultiTargetPower)
			{
				return ((IMultiTargetPower)power).getTargetEntities(e, homing != 0D ? e.getPositionVector() : mop.hitVec, instanceData);
			}
			list.add(e);
		}
		else if(power instanceof IMultiTargetPower)
		{
			return ((IMultiTargetPower)power).getTargetEntities(null, mop.hitVec, instanceData);
		}
		return list;
	}

	public int getCooldown()
	{
		return cooldown;
	}

	public int getDurationRemaining()
	{
		return duration;
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

	public IPower getPower()
	{
		return power;
	}

	public EnumPowerType getPowerType()
	{
		return type;
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
		if(power instanceof ITickingPower)
		{
			ticks = nbt.getInteger(KEY_TICKSELAPSED);
		}
		if(power instanceof IExtendedPower)
		{
			NBTTagCompound tag = nbt.getCompoundTag(KEY_DATA);
			((IExtendedPower)power).readFromNBT(tag, data);
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
		if(power instanceof ITickingPower)
		{
			nbt.setInteger(KEY_TICKSELAPSED, keyIndex);
		}
		if(power instanceof IExtendedPower)
		{
			NBTTagCompound tag = new NBTTagCompound();
			((IExtendedPower)power).writeToNBT(tag, data);
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
