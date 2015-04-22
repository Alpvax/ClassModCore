package alpvax.classmodcore.api.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import alpvax.classmodcore.api.powers.PowerEntry;


public abstract class SimplePlayerClass implements IPlayerClass
{
	// **********PlayerClass Handling**********
	/** Also doubles as unique class id */
	private final String classID;
	private String displayName;

	//TODO:attributes
	/** Jump height in blocks */
	//public float jumpHeight = 1F;
	/** Movement speed multiplier */
	//public float speedModifier = 1F;
	/** Maximum health multiplier */
	//public float healthModifier = 1F;
	/** Knockback resistance (1 = immune) */
	//public float knockResist = 0F;
	/** Reach extension (default 4.5 in s,a. 5 in c). Added to base value */
	//public float reachModifier = 0F;
	/** Should show player names */
	//public float trackDistance = 0.0F;
	//public float trackDistanceSneak = -1.0F;
	/** Nightvision level 1 = potion */
	//public float nightVision = 0F;

	public SimplePlayerClass(String name)
	{
		classID = name;
	}

	/*public void onUpdate(EntityPlayer player)
	{

	}

	public List<ItemStack> getDeathDrops()
	{
		return new ArrayList<ItemStack>();
	};

	public void onDamageEntity(EntityPlayer player, Entity target, DamageSource source, float ammount)
	{}

	public void onDeath(EntityPlayer player)
	{}

	public void onTakeDamage(EntityPlayer player, DamageSource source, float ammount)
	{}

	public abstract boolean getIsOblivious(Entity entity);*/

	/**
	 * Use null or "" to remove the display name mapping
	 *
	 * @param name display name to use
	 * @return the PlayerClass instance for convenience
	 */
	public IPlayerClass setDisplayName(String name)
	{
		if(name == null || name.equals(""))
		{
			displayName = classID;
		}
		else
		{
			displayName = name;
		}
		return this;
	}

	@Override
	public String getClassID()
	{
		return classID;
	}

	/**
	 * @return the display name or classID if none found
	 */
	@Override
	public String getDisplayName()
	{
		return displayName == null ? classID.substring(classID.lastIndexOf('.')) : displayName;
	}

	/*public ResourceLocation getIcon()
	{
		return new ResourceLocation(ModData.classModID, ClassUtil.classIconPath + classID.toLowerCase());
	}*/

	// **********Power Handling**********
	private List<PowerEntry> powers = new ArrayList<PowerEntry>();
	private Map<Integer, Integer> keyToSlot = new HashMap<Integer, Integer>();

	public void addPower(PowerEntry power)
	{
		addPower(power, -1);
	}

	public void addPower(PowerEntry power, int keybind)
	{
		if(keybind > 0)
		{
			keyToSlot.put(Integer.valueOf(keybind), Integer.valueOf(powers.size()));
		}
		powers.add(power);
	}

	@Override
	public abstract List<PowerEntry> getPowers();

	@Override
	public abstract void setup(EntityPlayer player);

	@Override
	public abstract void reset(EntityPlayer player);
}
