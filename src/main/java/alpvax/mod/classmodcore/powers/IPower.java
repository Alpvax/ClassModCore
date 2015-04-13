package alpvax.mod.classmodcore.powers;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Alpvax
 *
 */
public interface IPower
{
	public boolean shouldTrigger(EntityPlayer player);

	/**
	 * Only needs an implementation if it isn't an instant effect power
	 */
	public void onTick(EntityPlayer player);
}
