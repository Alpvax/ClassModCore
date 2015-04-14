package alpvax.mod.classmodcore.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import alpvax.mod.classmodcore.powers.IPower;
import alpvax.mod.classmodcore.powers.PowerInstance;//Required import for javadoc

/**
 * This event is fired whenever a power is triggered.<br>
 * <br>
 * This event is fired via {@link PowerInstance#triggerPower(EntityPlayer)}.<br>
 * <br>
 * {@link #power} contains the the power that is triggering, and cannot be modified. <br>
 * {@link #cooldown} contains the new cooldown. <br>
 * {@link #additionalData} contains any additional data stored about this instance of the power. <br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 **/
@Cancelable
public class TriggerPowerEvent extends PlayerEvent
{
	public final IPower power;
	public int cooldown;
	public NBTTagCompound additionalData;
	
	public TriggerPowerEvent(EntityPlayer player, IPower power, int cooldown, NBTTagCompound data)
	{
		super(player);
		this.power = power;
		this.cooldown = cooldown;
		additionalData = data;
	}

	/**
	 * This event is a version of {@link TriggerPowerEvent} that handles powers that automatically disable after a period of time.<br>
	 * <br>
	 * {@link #power} contains the the power that is triggering, and cannot be modified.
	 **/
	@Cancelable
	public static class TriggerPowerTimedEvent extends TriggerPowerEvent
	{
		public int duration;
		
		public TriggerPowerTimedEvent(TriggerPowerEvent e, int duration)
		{
			this(e.entityPlayer, e.power, e.cooldown, duration, e.additionalData);
		}

		public TriggerPowerTimedEvent(EntityPlayer player, IPower power, int cooldown, int duration, NBTTagCompound data)
		{
			super(player, power, cooldown, data);
			this.duration = duration;
		}
	}
	
	/**
	 * This event is the opposite of {@link TriggerPowerEvent} that is fired whenever a power deactivates.<br>
	 * <br>
	 * This event is fired via {@link PowerInstance#triggerPower(EntityPlayer)}.<br>
	 * <br>
	 * {@link #power} contains the the power that is triggering, and cannot be modified.
	 **/
	@Cancelable
	public static class ResetPowerEvent extends TriggerPowerEvent
	{
		public ResetPowerEvent(EntityPlayer player, IPower power, int cooldown, NBTTagCompound data)
		{
			super(player, power, cooldown, data);
		}
	}
}
