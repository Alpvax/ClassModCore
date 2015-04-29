package alpvax.classmodcore.api.powers;


/**
 * @author Alpvax
 *
 */
public enum EnumPowerCastType
{
	/**
	 * Targets the entity casting, or entities around caster
	 */
	SELF,
	/**
	 * Targets the entity looked at, or entities around the target entity
	 */
	OTHER,
	/**
	 * Targets entities around the targeted point
	 */
	POINT;


	/*public static EnumTargetType[] values = values();

	public static EnumTargetType get(int i)
	{
		return values[i];
	}

	public static EnumTargetType get(String name)
	{
		for(EnumTargetType e : values)
		{
			if(e.name().equals(name) || e.toString().equalsIgnoreCase(name))
			{
				return e;
			}
		}
		return null;
	}*/
}
