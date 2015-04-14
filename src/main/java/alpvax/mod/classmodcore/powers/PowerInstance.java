package alpvax.mod.classmodcore.powers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.mod.classmodcore.events.TriggerPowerEvent;
import alpvax.mod.classmodcore.events.TriggerPowerEvent.ResetPowerEvent;
import alpvax.mod.classmodcore.events.TriggerPowerEvent.TriggerPowerTimedEvent;

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

	private final IPower power;
	private EntityPlayer player;
	private int maxCD;
	private int cooldown;
	private int maxDur;
	private int duration;
	private boolean active;
	private final byte type;
	private NBTTagCompound data = null;

	/**
	 * An instance of an IPower
	 * @param power the {@link IPower} this is an instance of
	 * @param type the combination of {@link PowerType}s this instance is categorised as
	 * @param timers the max cooldown and max duration, in that order, but only if there is a cooldown or duration respectively.
	 */
	protected PowerInstance(IPower power, EntityPlayer entityplayer, byte type, int... timers)
	{
		this.power = power;
		player = entityplayer;
		this.type = PowerType.validate(type);
		active = (type & PowerType.EFFECT_MASK) == PowerType.CONTINUOUS;
		int idx = 0;
		if(hasCooldown())
		{
			maxCD = timers[idx];
		}
		if(hasDuration())
		{
			maxDur = timers[idx];
		}
	}

	public void tickPower()
	{
		if(hasDuration() && duration > 0)
		{
			if( --duration < 0)
			{
				duration = 0;
				if((type & PowerType.EFFECT_MASK) != PowerType.CONTINUOUS)
				{
					resetPower();
				}
			}
		}
		if(active)
		{
			power.onTick(player);
		}
		if(hasCooldown())
		{
			if(cooldown > 0)
			{
				cooldown-- ;
			}
			if(PowerType.automaticActivation(type))
			{
				if((type & PowerType.EFFECT_MASK) != PowerType.CONTINUOUS && power.shouldTrigger(player))
				{
					triggerPower();
				}
				if((type & PowerType.EFFECT_MASK) == PowerType.TOGGLED && power.shouldReset(player))
				{
					resetPower();
				}
			}
		}
	}

	public void triggerPower()
	{
		TriggerPowerEvent e;
		if(hasDuration())
		{
			e = new TriggerPowerTimedEvent(player, power, cooldown + maxCD, duration + maxDur, data);
		}
		else
		{
			e = new TriggerPowerEvent(player, power, cooldown + maxCD, data);
		}
		if(MinecraftForge.EVENT_BUS.post(e))
		{
			return;
		}
		if((type & PowerType.EFFECT_MASK) != PowerType.INSTANT)
		{
			active = true;
		}
		power.triggerPower(player);
	}
	
	public void resetPower()
	{
		ResetPowerEvent e = new ResetPowerEvent(player, power, cooldown, data);
		if(MinecraftForge.EVENT_BUS.post(e))
		{
			return;
		}
		active = false;
		power.resetPower(player);
	}
	
	public boolean isActive()
	{
		return active;
	}

	public boolean canTrigger()
	{
		return hasCooldown() && cooldown < 1;
	}

	private boolean hasDuration()
	{
		return PowerType.hasDuration(type);
	}
	
	public boolean hasCooldown()
	{
		return PowerType.triggerable(type);
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		//type = nbt.getByte(KEY_TYPE);
		//power = PowerRegistry.getPower(nbt.getString(KEY_POWER));
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
