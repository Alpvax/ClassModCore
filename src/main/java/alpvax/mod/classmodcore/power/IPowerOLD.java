package alpvax.mod.classmodcore.power;

/**
 *	Preferable to extend alpvax.classmod.power.Power
 *	(or some class that extends it) in order to retain
 *	ExtendedPlayer.getActivePowers(Class<T> powerclass)
 *	compatibility.
 */
public interface IPowerOLD
{
	/**
	 * Only required if the power will show on GUI
	 */
	public String getDisplayName();
}
