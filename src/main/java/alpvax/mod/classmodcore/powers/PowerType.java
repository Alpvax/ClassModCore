package alpvax.mod.classmodcore.powers;

/**
 * @author Alpvax
 *
 */
public class PowerType
{
	/** No user input required, either automatically activated or always active */
	public static final byte AUTOMATIC = 4;
	/** Can be activated by the player */
	public static final byte MANUAL = 0;
	/**
	 * Does something instantaneously when triggered<br>
	 * (no duration)
	 */
	public static final byte INSTANT = 0;
	/**
	 * Is active until disabled<br>
	 * (has unlimited duration)
	 */
	public static final byte TOGGLED = 1;
	/**
	 * Is active for a limited time-frame<br>
	 * (has limited duration)
	 */
	public static final byte TIMED = 2;
	/**
	 * Cannot be triggered, either by the player or passively, overrides {@link #MANUAL}<br>
	 * (no cooldown)
	 */
	public static final byte CONTINUOUS = 3;
	/**
	 * Default is {@link #AUTOMATIC} & {@link #CONTINUOUS}
	 */
	public static final byte DEFAULT = AUTOMATIC | CONTINUOUS;

	public static final byte ACTIVATION_MASK = 4;
	public static final byte EFFECT_MASK = 3;

	public static boolean hasDuration(byte flags)
	{
		return (flags & EFFECT_MASK) == TIMED;
	}

	public static boolean triggerable(byte flags)
	{
		return (flags & EFFECT_MASK) > 0;
	}
	
	public static boolean automaticActivation(byte flags)
	{
		return flags >= 3;
	}

	public static byte validate(byte flags)
	{
		flags = (byte)(flags & 7);
		return flags == CONTINUOUS ? CONTINUOUS | AUTOMATIC : flags;
	}
}
