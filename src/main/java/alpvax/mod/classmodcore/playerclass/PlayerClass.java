package alpvax.mod.classmodcore.playerclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import alpvax.common.mods.ModData;
import alpvax.mod.classmodcore.core.ClassUtil;
import alpvax.mod.classmodcore.power.IPowerActive;
import alpvax.mod.classmodcore.power.Power;
import alpvax.mod.classmodcore.power.PowerRegistry;

public abstract class PlayerClass
{
	//**********PlayerClass Handling**********
	/** Also doubles as unique class id*/
	public String className;
	private String displayName;
	
	/** Jump height in blocks */
	public float jumpHeight = 1F;
	/** Movement speed multiplier */
	public float speedModifier = 1F;
	/** Maximum health multiplier */
	public float healthModifier = 1F;
	/** Knockback resistance (1 = immune)*/
	public float knockResist = 0F;
	/** Reach extension (default 4.5 in s,a. 5 in c). Added to base value */
	public float reachModifier = 0F;
	/** Should show player names */
	public float trackDistance = 0.0F;
	public float trackDistanceSneak = -1.0F;
	/** Nightvision level 1 = potion */
	public float nightVision = 0F;
	
	public PlayerClass(String name)
	{
		ClassUtil.throwClassNameError(name);
		className = name;
		PlayerClassRegistry.addToConfig(className);
		setDisplay(true);
	}


	public void onUpdate(EntityPlayer player)
	{
		for(int i : slotToPower.keySet())
		{
			Power p = slotToPower.get(i);
			IPowerActive power;
			if(p instanceof IPowerActive)
			{
				if(getPowerCooldown(i) == -1 || ExtendedPlayer.get(player).getPowerDuration(i) > 0)
				{
					((IPowerActive)p).onUpdate(player);
				}
			}
		}
	}

	public List<ItemStack> getDeathDrops()
	{
		return new ArrayList<ItemStack>();
	};
	
	public void onDamageEntity(EntityPlayer player, Entity target, DamageSource source, float ammount)	{}
	
	public void onDeath(EntityPlayer player)	{}

	public void onTakeDamage(EntityPlayer player, DamageSource source, float ammount)	{}

	public abstract boolean getIsOblivious(Entity entity);
	
	public PlayerClass setDisplay(boolean flag)
	{
		if(flag && !PlayerClassRegistry.allowedClasses.contains(className))
		{
			PlayerClassRegistry.allowedClasses.add(className);
		}
		else if(!flag && PlayerClassRegistry.allowedClasses.contains(className))
		{
			PlayerClassRegistry.allowedClasses.remove(className);
		}
		return this;
	}
	
	/**
	 * Use null or "" to remove the display name mapping
	 * @param name display name to use
	 * @return the PlayerClass instance so it can be called on initialisation (new PlayerClass(s).setDisplayName(n))
	 */
	public PlayerClass setDisplayName(String name)
	{
		if(name != null && !name.equals(""))
		{
			displayName = className;
		}
		else
		{
			displayName = name;
		}
		return this;
	}
	/**
	 * @return the display name or className if none found
	 */
	public String getDisplayName()
	{
		return displayName == null ? className : displayName;
	}
	
	public ResourceLocation getIcon()
	{
		return new ResourceLocation(ModData.classModID, ClassUtil.classIconPath + className.toLowerCase());
	}
	
	
	
	//**********Power Handling**********
	private int nextPowerIndex = 0;
	//private Map<Integer, Integer> slotToID = new HashMap<Integer, Integer>();
	private Map<Integer, Power> slotToPower = new HashMap<Integer, Power>();
	private Map<Power, Integer> powerToSlot = new HashMap<Power, Integer>();
	private Map<Integer, Integer> slotToCooldown = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> slotToDuration = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> keyToSlot = new HashMap<Integer, Integer>();
	
	
	public void addPassivePower(Power power)
	{
		addPower(power, -1, -1, -1);
	}
	public void addPower(Power power, int cooldown, int duration)
	{
		addPower(power, cooldown, duration, -1);
	}
	public void addPower(Power power, int cooldown, int duration, int keybind)
	{
		int index = nextPowerIndex++;
		//slotToID.put(Integer.valueOf(index), Integer.valueOf(PowerRegistry.getPowerID(power)));
		slotToPower.put(Integer.valueOf(index), power);
		powerToSlot.put(power, Integer.valueOf(index));
		slotToCooldown.put(Integer.valueOf(index), Integer.valueOf(cooldown));
		slotToDuration.put(Integer.valueOf(index), Integer.valueOf(duration));
		if(keybind > 0)
		{
			keyToSlot.put(Integer.valueOf(keybind), Integer.valueOf(index));
		}
	}
	/*public boolean hasPower(int id)
	{
		return slotToID.containsValue(id);
	}*/
	public int getNumPowers()
	{
		return nextPowerIndex;
	}
	public Power getPower(int index)
	{
		return slotToPower.get(Integer.valueOf(index));
	}
	public int getPowerSlot(Power power)
	{
		return powerToSlot.get(power);
	}
	public int getPowerID(int index)
	{
		return PowerRegistry.getPowerID(slotToPower.get(Integer.valueOf(index)));
		//return slotToID.get(index);
	}
	public int getPowerCooldown(int index)
	{
		return slotToCooldown.get(Integer.valueOf(index));
	}
	public int getPowerDuration(int index)
	{
		return slotToDuration.get(Integer.valueOf(index));
	}
	public boolean isPowerConstant(int index)
	{
		return getPowerCooldown(index) == -1;
	}
	
	public void triggerPower(int key, EntityPlayer player)
	{
		int i = keyToSlot.get(Integer.valueOf(key));
		PowerRegistry.triggerPower(getPowerID(i), player);
		ExtendedPlayer ep = ExtendedPlayer.get(player);
		ep.setPowerCooldown(i, getPowerCooldown(i));
		ep.setPowerDuration(i, getPowerDuration(i));
	}
	
	public List<Power> getPowers()
	{
		//Iterator<IPower> iterator = slotToID.values().iterator();
		List<Power> list = new ArrayList<Power>();
		list.addAll(slotToPower.values());
		return list;
	}
}
