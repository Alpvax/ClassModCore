package alpvax.mod.classmodcore.powers;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Alpvax
 *
 */
public interface IPower
{
	/**
	 * Only needs an implementation if it is automatically triggered.
	 */
	public boolean shouldTrigger(EntityPlayer player);

	/**
	 * Opposite of {@link #shouldTrigger(EntityPlayer)}<br>
	 * Needs an implementation if it is automatically disabled, but not timed.
	 */
	public boolean shouldReset(EntityPlayer player);

	/**
	 * Called if {@link #shouldTrigger(EntityPlayer)} returns true<br>
	 * Needs an implementation if it is automatically triggered.
	 */
	public void triggerPower(EntityPlayer player);

	/**
	 * Called if {@link #shouldReset(EntityPlayer)} returns true or when the duration ends<br>
	 * Needs an implementation if it is automatically disabled, or disabled after a timer.
	 */
	public void resetPower(EntityPlayer player);

	/**
	 * Needs an implementation if it isn't an instant effect power
	 */
	public void onTick(EntityPlayer player);
	
	/**
	 * Only required if the power will show on GUI
	 */
	public String getDisplayName();
}
