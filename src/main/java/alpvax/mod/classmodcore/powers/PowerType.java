package alpvax.mod.classmodcore.powers;

/**
 * @author Alpvax
 *
 */
public class PowerType
{
	/** No user input required, either automatically activated or always active */
	public static final byte PASSIVE = 0b000;
	/** Can be activated by the player */
	public static final byte ACTIVE = 0b001;
	/** When triggered, does something, then stops
	 * (Has no Duration)
	 */
	public static final byte INSTANT = 0b010;
	/** Is constantly active or active until another event occurs (e.g. speed boost in a certain fluid)
	 * (Has variable Duration)
	 */
	public static final byte CONTINUOUS = 0b100;

	public static boolean PASSIVE(int type)
	{
		return check((byte)type, PASSIVE);
	}
	public static boolean ACTIVE(int type)
	{
		return check((byte)type, ACTIVE);
	}
	public static boolean INSTANT(int type)
	{
		return check((byte)type, INSTANT);
	}
	/**
	 * Checks to see if the flags contain {@link #CONTINUOUS}
	 * @param type
	 */
	public static boolean CONTINUOUS(int type)
	{
		return check((byte)type, CONTINUOUS);
	}
	
	private static boolean check(byte flags, byte type)
	{
		return (flags & type) == type;
	}
}
