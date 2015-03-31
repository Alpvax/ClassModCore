package alpvax.mod.classmodcore.power;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import alpvax.mod.classmodcore.core.ClassUtil;
import alpvax.mod.classmodcore.playerclass.PlayerClass;

public final class PowerRegistry
{
	private static Map<Integer, Power> idToPowerMap = new HashMap<Integer, Power>();
	private static Map<Power, Integer> powerToIdMap = new HashMap<Power, Integer>();
	private static Map<Integer, IPowerTriggered> triggerablePowerMap = new HashMap<Integer, IPowerTriggered>();

	public static Power getPower(int id)
	{
		return idToPowerMap.get(Integer.valueOf(id));
	}
	public static int getPowerID(Power power)
	{
		return powerToIdMap.get(power);
	}
	
	public static void triggerPower(int id, EntityPlayer player)
	{
		IPowerTriggered power = triggerablePowerMap.get(Integer.valueOf(id));
		if(power != null)
		{
			power.trigger(player);
		}
	}
	
	//***************Static ID Methods***************
	private static int nextID = 0;

	public static final int getNextID()
	{
		while(getPower(nextID) != null)
		{
			nextID++;
		}
		return nextID;
	}
	public static void registerPower(Power power)
	{
		registerPower(power, getNextID());
	}
	public static void registerPower(Power power, int id)
	{
		if(power == null || idToPowerMap.containsKey(Integer.valueOf(id)))
		{
			throw new IllegalArgumentException("Failed to register Power: " + power + ". ID already taken.");
		}
		idToPowerMap.put(id, power);
	}
}
