package alpvax.classmodcore.api.events;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import alpvax.classmodcore.api.powers.IPower;
import alpvax.classmodcore.api.powers.PowerInstance;


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
public abstract class ChangePowerStateEvent extends PlayerEvent
{
	public final IPower power;
	public Map<String, Object> additionalData;

	public ChangePowerStateEvent(EntityPlayer player, IPower power, Map<String, Object> data)
	{
		super(player);
		this.power = power;
		additionalData = data;
	}

	public static class StartContinuousPowerEvent extends ChangePowerStateEvent
	{
		public StartContinuousPowerEvent(EntityPlayer player, IPower power, Map<String, Object> data)
		{
			super(player, power, data);
		}
	}

	@Cancelable
	public static class TriggerPowerEvent extends ChangePowerStateEvent
	{
		public int cooldown;

		public TriggerPowerEvent(EntityPlayer player, IPower power, int cooldown, Map<String, Object> data)
		{
			super(player, power, data);
			this.cooldown = cooldown;
		}
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

		public TriggerPowerTimedEvent(EntityPlayer player, IPower power, int cooldown, int duration, Map<String, Object> data)
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
	public static class ResetPowerEvent extends ChangePowerStateEvent
	{
		public int cooldown;

		public ResetPowerEvent(EntityPlayer player, IPower power, int cooldown, Map<String, Object> data)
		{
			super(player, power, data);
			this.cooldown = cooldown;
		}
	}

	/**
	 * This event is fired whenever a class change occurs to deactivate all powers.<br>
	 * <br>
	 * This event is not {@link Cancelable}.<br>
	 * <br>
	 * {@link #power} contains the the power that is triggering, and cannot be modified.
	 **/
	public static class ResetPowerForClassChangeEvent extends ResetPowerEvent
	{
		public ResetPowerForClassChangeEvent(EntityPlayer player, IPower power, int cooldown, Map<String, Object> data)
		{
			super(player, power, cooldown, data);
		}
	}
}
