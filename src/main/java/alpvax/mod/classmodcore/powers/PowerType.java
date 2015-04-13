package alpvax.mod.classmodcore.powers;

/**
 * @author Alpvax
 *
 */
public class PowerType
{
	/** No user input required, either automatically activated or always active */
	public static final byte PASSIVE = 0b0;
	/** Can be activated by the player */
	public static final byte ACTIVE = 0b1;
	/** Is active for a limited time-frame<br>
	 * (has limited duration)
	 */
	public static final byte TIMED = 0b10;
	/** Cannot be triggered, either by the player or passively, overrides {@link #ACTIVE}<br>
	 * (no cooldown)
	 */
	public static final byte CONTINUOUS = 0b100;
	
	public static boolean hasCooldown(byte flags)
	{
		return (flags & CONTINUOUS) == 0; 
	}
	
	public static boolean hasDuration(byte flags)
	{
		return (flags & TIMED) == TIMED;
	}
}
