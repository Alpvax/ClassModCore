package alpvax.classmodcore.api.events;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
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
public abstract class TogglePowerEvent extends PlayerEvent
{
	public final PowerInstance instance;
	public Map<String, Object> instanceData;

	public TogglePowerEvent(EntityPlayer player, PowerInstance powerInstance, Map<String, Object> instanceData)
	{
		super(player);
		instance = powerInstance;
		this.instanceData = instanceData;
	}

	@Cancelable
	public static class TriggerPowerEvent extends TogglePowerEvent
	{
		public TriggerPowerEvent(EntityPlayer player, PowerInstance powerInstance, Map<String, Object> instanceData)
		{
			super(player, powerInstance, instanceData);
		}
	}

	public static class StartContinuousPowerEvent extends TriggerPowerEvent
	{
		public StartContinuousPowerEvent(EntityPlayer player, PowerInstance powerInstance, Map<String, Object> instanceData)
		{
			super(player, powerInstance, instanceData);
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
	public static class ResetPowerEvent extends TogglePowerEvent
	{
		public ResetPowerEvent(EntityPlayer player, PowerInstance powerInstance, Map<String, Object> instanceData)
		{
			super(player, powerInstance, instanceData);
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
		public ResetPowerForClassChangeEvent(EntityPlayer player, PowerInstance powerInstance, Map<String, Object> instanceData)
		{
			super(player, powerInstance, instanceData);
		}
	}
}
