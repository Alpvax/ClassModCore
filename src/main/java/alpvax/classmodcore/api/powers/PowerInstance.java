package alpvax.classmodcore.api.powers;

import static alpvax.classmodcore.api.ClassUtil.KEY_ACTIVE;
import static alpvax.classmodcore.api.ClassUtil.KEY_CD;
import static alpvax.classmodcore.api.ClassUtil.KEY_DUR;
import static alpvax.classmodcore.api.ClassUtil.KEY_EXTENDED;
import static alpvax.classmodcore.api.ClassUtil.KEY_TICKSELAPSED;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.classmodcore.api.events.TogglePowerEvent.ResetPowerEvent;
import alpvax.classmodcore.api.events.TogglePowerEvent.ResetPowerForClassChangeEvent;
import alpvax.classmodcore.api.events.TogglePowerEvent.StartContinuousPowerEvent;
import alpvax.classmodcore.api.events.TogglePowerEvent.TriggerPowerEvent;
import alpvax.classmodcore.api.powers.IPower.IExtendedPower;
import alpvax.classmodcore.api.powers.IPower.ITickingPower;
import alpvax.classmodcore.api.powers.IPower.IToggledPower;
import alpvax.classmodcore.api.powers.IPower.ITriggeredPower;
import alpvax.classmodcore.api.util.TickingVariable;


public class PowerInstance<T extends IPower>
{
	private final T power;
	//private final EnumPowerType type;
	//private final EnumPowerCastType targetType;
	public final boolean passive;

	private final TickingVariable cooldown = new TickingVariable(0);
	private final TickingVariable duration = new TickingVariable(0);
	private int ticksActive = 0;
	private boolean active = false;

	private boolean dirty = false;

	protected PowerInstance(T power/*, EnumPowerCastType targetType*/, boolean isAutomatic, Map<String, Object> data)
	{
		this.power = power;
		//this.targetType = targetType;
		passive = isAutomatic;

		cooldown.setModifier((Integer)data.get(PowerEntry.KEY_COOLDOWN));
		duration.setModifier((Integer)data.get(PowerEntry.KEY_DURATION));
	}

	public void tickPower(EntityPlayer player)
	{
		boolean flag = false;
		//Disable if duration ended
		if(duration.tick())
		{
			flag |= resetPower(player);
		}
		if(active)
		{
			ticksActive++;
			if(power instanceof ITickingPower)
			{
				((ITickingPower)power).onTick(player, ticksActive);
			}
			flag = true;
		}
		flag |= cooldown.tick();
		if(passive && power instanceof ITriggeredPower)
		{
			if(!active && ((ITriggeredPower)power).shouldTrigger(player))
			{
				flag |= triggerPower(player);
			}
			if(active && power instanceof IToggledPower && ((IToggledPower)power).shouldReset(player))
			{
				flag |= resetPower(player);
			}
		}
		dirty = flag;
	}

	public void togglePower(EntityPlayer player)
	{
		dirty = active ? resetPower(player) : triggerPower(player);
	}

	public void init(EntityPlayer player)
	{
		active = true;
		if(power instanceof ITriggeredPower)
		{
			MinecraftForge.EVENT_BUS.post(new StartContinuousPowerEvent<T>(player, this));
			//active = true;
			((ITriggeredPower)power).triggerPower(player);
		}
	}

	public void stop(EntityPlayer player)
	{
		active = false;
		if(power instanceof IToggledPower)
		{
			MinecraftForge.EVENT_BUS.post(new ResetPowerForClassChangeEvent<T>(player, this));
			//active = false;
			((IToggledPower)power).resetPower(player, ticksActive);
		}
	}

	private boolean triggerPower(EntityPlayer player)
	{
		if(!canTrigger())
		{
			return false;
		}
		int c = cooldown.apply();
		int d = duration.apply();
		TriggerPowerEvent<T> e = new TriggerPowerEvent<T>(player, this);
		if(MinecraftForge.EVENT_BUS.post(e) || !((ITriggeredPower)power).triggerPower(player))
		{
			cooldown.setValue(c);
			duration.setValue(d);
			return false;
		}
		if(duration.exists() || power instanceof IToggledPower)
		{
			active = true;
		}
		return true;
	}

	private boolean resetPower(EntityPlayer player)
	{
		if(!canReset())
		{
			return false;
		}
		int c = cooldown.apply();
		ResetPowerEvent<T> e = new ResetPowerEvent<T>(player, this);
		if(MinecraftForge.EVENT_BUS.post(e))
		{
			cooldown.setValue(c);
			return false;
		}
		active = false;
		((IToggledPower)power).resetPower(player, ticksActive);
		ticksActive = 0;
		return true;
	}

	/*TODO:is this needed?
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
	}*/

	public int getCooldownRemaining()
	{
		return cooldown.value();
	}

	public int getDurationRemaining()
	{
		return duration.value();
	}

	public boolean isActive()
	{
		return active;
	}

	public boolean canTrigger()
	{
		return (!cooldown.exists() || cooldown.value() <= 0) && power instanceof ITriggeredPower;
	}

	public boolean canReset()
	{
		return (!cooldown.exists() || cooldown.value() <= 0) && power instanceof IToggledPower;
	}

	public T getPower()
	{
		return power;
	}

	/*public EnumPowerCastType getCastType()
	{
		return targetType;
	}*/

	public void readFromNBT(NBTTagCompound nbt)
	{
		//type = nbt.getByte(KEY_TYPE);
		//power = PowerRegistry.getPower(nbt.getString(KEY_POWER));
		active = nbt.getBoolean(KEY_ACTIVE);
		if(nbt.hasKey(KEY_DUR, NBT.TAG_ANY_NUMERIC))
		{
			duration.setValue(nbt.getInteger(KEY_DUR));
		}
		if(nbt.hasKey(KEY_CD, NBT.TAG_ANY_NUMERIC))
		{
			cooldown.setValue(nbt.getInteger(KEY_CD));
		}
		if(active)
		{
			ticksActive = nbt.getInteger(KEY_TICKSELAPSED);
		}
		if(power instanceof IExtendedPower && nbt.hasKey(KEY_EXTENDED))
		{
			((IExtendedPower)power).readFromNBT(nbt.getCompoundTag(KEY_EXTENDED));
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean(KEY_ACTIVE, active);
		if(duration.exists())
		{
			nbt.setInteger(KEY_DUR, duration.value());
		}
		if(cooldown.exists())
		{
			nbt.setInteger(KEY_CD, cooldown.value());
		}
		if(active)
		{
			nbt.setInteger(KEY_TICKSELAPSED, ticksActive);
		}
		if(power instanceof IExtendedPower)
		{
			NBTTagCompound tag = new NBTTagCompound();
			((IExtendedPower)power).writeToNBT(tag);
			nbt.setTag(KEY_EXTENDED, tag);
		}
		dirty = false;
	}

	public boolean isDirty()
	{
		return dirty;
	}
}
