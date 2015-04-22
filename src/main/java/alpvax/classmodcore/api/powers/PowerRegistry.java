package alpvax.classmodcore.api.powers;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Alpvax
 *
 */
public class PowerRegistry
{
	private static Map<String, IPower> idToPowerMap = new HashMap<String, IPower>();
	private static Map<IPower, String> powerToIDMap = new HashMap<IPower, String>();

	public static IPower getPower(String id)
	{
		IPower power = idToPowerMap.get(id);
		if(power == null)
		{
			throw new NullPointerException("Failed to retrieve Power: No Power registered with ID: \"" + id + "\".");
		}
		return power;
	}

	public static String getPowerID(IPower power)
	{
		if(power == null)
		{
			throw new NullPointerException("Failed to retrieve id of null Power.");
		}
		if(!powerToIDMap.containsKey(power))
		{
			throw new NullPointerException("Failed to retrieve id of Power: Are you sure it has been registered correctly.");
		}
		return powerToIDMap.get(power);
	}

	public static IPower registerPower(IPower power, String id)
	{
		if(power == null)
		{
			throw new IllegalArgumentException("Failed to register null Power.");
		}
		if(idToPowerMap.containsKey(id))
		{
			throw new IllegalArgumentException("Failed to register Power: " + power + ". ID \"" + id + "\" already taken.");
		}
		idToPowerMap.put(id, power);
		return power;
	}
}
