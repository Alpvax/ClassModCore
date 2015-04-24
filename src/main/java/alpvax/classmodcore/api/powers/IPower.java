package alpvax.classmodcore.api.powers;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;


/**
 * @author Alpvax
 *
 */
public interface IPower
{
	/**
	 * Only needs an implementation if it is automatically triggered.
	 * @param player the player this power is acting from
	 * @param instanceData and additional data
	 */
	public boolean shouldTrigger(EntityPlayer player, Map<String, Object> instanceData);

	/**
	 * Opposite of {@link #shouldTrigger(EntityPlayer)}<br>
	 * Needs an implementation if it is automatically disabled, but not timed.
	 */
	public boolean shouldReset(EntityPlayer player, Map<String, Object> instanceData);

	/**
	 * Called whenever the power is triggered (either automatically or manually)<br>
	 * Also called to start continuous powers (i.e. for setting attributes)
	 * @return true if the trigger was successful, cooldowns and durations will be adjusted
	 */
	public boolean triggerPower(EntityPlayer player, Map<String, Object> instanceData);

	/**
	 * Called whenever the power is reset (either automatically or manually)<br>
	 * Needs an implementation if it is automatically disabled, or disabled after a timer.
	 */
	public void resetPower(EntityPlayer player, Map<String, Object> instanceData);

	/**
	 * Only required if the power will show on GUI
	 */
	public String getDisplayName();
}
