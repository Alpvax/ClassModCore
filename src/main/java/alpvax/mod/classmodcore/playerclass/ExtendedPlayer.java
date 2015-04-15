package alpvax.mod.classmodcore.playerclass;

import static alpvax.mod.classmodcore.core.ClassUtil.BASE_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.CLASS_SPLIT;
import static alpvax.mod.classmodcore.core.ClassUtil.CLASS_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.POWER_TAG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.util.Constants.NBT;
import scala.tools.nsc.interpreter.Power;
import alpvax.mod.classmodcore.classes.PlayerClassRegistry;
import alpvax.mod.classmodcore.core.ClassMod;
import alpvax.mod.classmodcore.core.ClassUtil;
import alpvax.mod.classmodcore.network.CommonProxy;
import alpvax.mod.classmodcore.power.IPowerActive;
import alpvax.mod.classmodcore.power.IPowerTriggeredActive;
import alpvax.mod.classmodcore.power.PowerRegistry;
import alpvax.mod.classmodcore.util.PowerEntry;
import alpvax.mod.common.mods.ModData;

public class ExtendedPlayer implements IExtendedEntityProperties
{
	// I always include the entity to which the properties belong for easy
	// access
	// It's final because we won't be changing which player it is
	private final EntityPlayer player;

	private String[] classIDs = null;
	private PowerEntry[] powers = null;

	public ExtendedPlayer(EntityPlayer entityplayer)
	{
		player = entityplayer;
	}

	/**
	 * Used to register these extended properties for the player during EntityConstructing event This method is for convenience only;
	 */
	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(BASE_TAG, new ExtendedPlayer(player));
	}

	/**
	 * Returns ExtendedPlayer properties for player This method is for convenience only;
	 */
	public static final ExtendedPlayer get(EntityPlayer player)
	{
		return (ExtendedPlayer)player.getExtendedProperties(BASE_TAG);
	}

	private String getProxySaveKey()
	{
		return player.username + ":" + BASE_TAG;
	}

	/**
	 * This cleans up the onEntityDeath event by replacing most of the code with a single line: ExtendedPlayer.saveProxyData();
	 */
	public void saveProxyData()
	{
		NBTTagCompound savedData = new NBTTagCompound();
		saveNBTData(savedData);
		CommonProxy.storeEntityData(getProxySaveKey(), savedData);
	}

	/**
	 * This cleans up the onEntityJoinWorld event by replacing most of the code with a single line: ExtendedPlayer.loadProxyData();
	 */
	public void loadProxyData()
	{
		NBTTagCompound savedData = CommonProxy.getEntityData(getProxySaveKey());
		if(savedData != null)
		{
			loadNBTData(savedData);
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		if(hasPlayerClass())
		{
			nbt.setString(CLASS_TAG, classIDs[0] + CLASS_SPLIT + classIDs[1]);
			if(powers != null)
			{
				NBTTagList list = new NBTTagList();
				for(PowerEntry power : powers)
				{
					NBTTagCompound tag = power.toNBT();
					if(tag != null)
					{
						list.appendTag(tag);
					}
				}
			}
		}
		compound.setTag(BASE_TAG, nbt);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		if(compound.hasKey(BASE_TAG))
		{
			NBTTagCompound nbt = compound.getCompoundTag(BASE_TAG);
			String data;
			if(nbt.hasKey(CLASS_TAG))
			{
				data = nbt.getString(CLASS_TAG);
				if(data.equals(""))
				{
					classIDs = null;
				}
				else
				{
					classIDs = data.split(CLASS_SPLIT);
				}
				if(nbt.hasKey(POWER_TAG))
				{
					NBTTagList list = nbt.getTagList(POWER_TAG, NBT.TAG_COMPOUND);
					powers = new PowerEntry[list.tagCount()];
					for(int i = 0; i < list.tagCount(); i++ )
					{
						powers[i] = new PowerEntry(list.getCompoundTagAt(i));
					}
				}
			}
			System.out.println("[PlayerClassProps] Class from NBT: " + getPlayerClassName());
		}
		else
		{
			System.out.println("[PlayerClassProps] Class from NBT not loaded for player: " + player);
		}
	}

	@Override
	public void init(Entity entity, World world)
	{
		if(hasPlayerClass())
		{
			updateAttribute(SharedMonsterAttributes.movementSpeed, getPlayerClass().speedModifier, 2);
			updateAttribute(SharedMonsterAttributes.maxHealth, getPlayerClass().healthModifier, 2);
			updateAttribute(SharedMonsterAttributes.knockbackResistance, getPlayerClass().knockResist, 0);
			// updateAttribute(nightvision, getPlayerClass().nightVision, 0);
		}
	}

	/**
	 * @param operation 0 => add modifier to base, 1 => multiply base by modifier, 2 => multiply final result by modifier.
	 */
	private void updateAttribute(IAttribute a, double modifier, int operation)
	{
		IAttributeInstance att = player.getAttributeMap().getAttributeInstance(a);
		AttributeModifier m = att.getModifier(ClassUtil.attModIDClass);
		if(m != null)
		{
			att.removeModifier(m);
		}
		att.applyModifier(new AttributeModifier(ClassUtil.attModIDClass, ModData.classModID, modifier - (operation == 2 ? 1D : 0D), operation));
	}

	// ****************PlayerClass Methods****************
	public String getPlayerClassName()
	{
		return hasClassName() ? classIDs[0] : null;
	}

	private void setPlayerClass(String classID)
	{
		classIDs[0] = classID;
		init(player, player.getEntityWorld());
		startPowerDelay();
	}

	private String getNextClass()
	{
		return hasClassName() ? classIDs[1] : null;
	}

	private void setNextClass(String classID)
	{
		classIDs[1] = classID;
	}

	/**
	 * Sets the player's class depending on whether they are allowed to change
	 *
	 * @param id the ID of the class to change to.
	 * @return 0 if the class was not set, 1 if it was set to change when the player respawns, 2 if it has been set straight away.
	 */
	public int setPlayerClassWithCheck(String classID)
	{
		int i = checkCanChangeClass(classID);
		if(i > 0 && !hasClassName())
		{
			classIDs = new String[2];
		}
		if(i == 1)
		{
			setNextClass(classID);
		}
		if(i == 2)
		{
			setPlayerClass(classID);
		}
		return i;
	}

	private int checkCanChangeClass(String classID)
	{
		if(ClassUtil.verifyClassName(classID) > 0)
		{
			return 0;
		}
		if(hasPlayerClass())
		{
			if(ClassMod.lockClasses)
			{
				return 0;
			}
			return 1;
		}
		return 2;
	}

	public PlayerClass getPlayerClass()
	{
		String name = getPlayerClassName();
		if(name == null)
		{
			return null;
		}
		return PlayerClassRegistry.getPlayerClass(name);
	}

	public boolean hasPlayerClass()
	{
		return hasClassName() && getPlayerClass() != null;
	}

	private boolean hasClassName()
	{
		return classIDs != null && classIDs.length == 2;
	}

	public void changeClassOnDeath()
	{
		String next = getNextClass();
		if(!getPlayerClassName().equals(next))
		{
			setPlayerClass(next);
		}
	}

	public boolean getIsOblivious(Entity entity)
	{
		return hasPlayerClass() && getPlayerClass().getIsOblivious(entity);
	}

	public float getNightVision()
	{
		if(hasPlayerClass())
		{
			return getPlayerClass().nightVision;
		}
		return 0F;
	}

	private void startPowerDelay()
	{
		if(hasPlayerClass())
		{
			PlayerClass pc = getPlayerClass();
			boolean flag = ClassMod.startDelay > 0;
			if(flag || ClassMod.startOnCooldown)
			{
				for(int i = 0; i < pc.getNumPowers(); i++ )
				{
					if(flag && ClassMod.delayAllPassive && pc.getPower(i) instanceof IPowerActive)
					{
						setPowerCooldown(i, ClassMod.startDelay);
					}
					else if(flag && ClassMod.delayPassive && pc.getPower(i) instanceof IPowerTriggeredActive)
					{
						int cd = pc.getPowerCooldown(i);
						setPowerCooldown(i, ClassMod.startDelay + (ClassMod.startOnCooldown && cd >= 0 ? cd : 0));
					}
					else
					{
						setPowerCooldown(i, ClassMod.startDelay + (ClassMod.startOnCooldown ? pc.getPowerCooldown(i) : 0));
					}
				}
			}
		}
	}

	// ****************Power Methods****************
	public int getPowerCooldown(int slot)
	{
		// System.out.println("SIDE: " +
		// FMLCommonHandler.instance().getEffectiveSide() + "; Slot: " + slot +
		// ", Data: " + getDataString().toString());
		return powers[slot].getCooldown();
	}

	public void setPowerCooldown(int slot, int cooldown)
	{
		powers[slot].setCooldown(cooldown);
	}

	public int getPowerDuration(int slot)
	{
		return powers[slot].getDuration();
	}

	public void setPowerDuration(int slot, int duration)
	{
		powers[slot].setDuration(duration);
	}

	public String getPowerData(int slot)
	{
		return powers[slot].getAdditionalData();
	}

	public void setPowerData(int slot, String powerData)
	{
		powers[slot].setAdditionalData(powerData);
	}

	public List<IPowerActive> getActivePowers()
	{
		PlayerClass pc = getPlayerClass();
		List<IPowerActive> list = new ArrayList<IPowerActive>();
		if(pc == null)
		{
			return list;
		}
		List<Power> pwrs = pc.getPowers();
		for(Power p : pwrs)
		{
			System.out.println("EP.gAP: " + p);
			if(p instanceof IPowerActive)
			{
				int i = pc.getPowerSlot(p);
				if(getPowerDuration(i) > 0 || pc.isPowerConstant(i))
				{
					list.add((IPowerActive)p);
				}
			}
		}
		/*
		 * for(int i = 0; i < pc.getNumPowers(); i++) { Power p = pc.getPower(i); System.out.println(p + " " + i + "/" + (pc.getNumPowers() -1)); if(p instanceof IPowerActive) { if(getPowerDuration(i) > 0 || pc.isPowerConstant(i)) { list.add((IPowerActive)p); } } }
		 */
		return list;
	}

	public <T extends Power>List<T> getActivePowers(Class<T> powerclass)
	{
		List<T> powers = new ArrayList<T>();
		Iterator<IPowerActive> i = getActivePowers().iterator();
		while(i.hasNext())
		{
			IPowerActive power = i.next();
			System.out.println("EP.gAP(C): " + power);
			if(powerclass.isAssignableFrom(power.getClass()))
			{
				powers.add(powerclass.cast(power));
			}
		}
		return powers;
	}

	public boolean isPowerActive(Power power)
	{
		return getActivePowers().contains(power);
	}

	public boolean isPowerActive(int powerID)
	{
		return isPowerActive(PowerRegistry.getPower(powerID));
	}

	// Static Attributes
	// public static final Attribute nightvision = (new
	// RangedAttribute("generic.nightVision", 0F, 0.0D,
	// 1F)).func_111117_a("Night Vision").setShouldWatch(true);
}
