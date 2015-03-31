package alpvax.mod.classmodcore.playerclass;

import static alpvax.mod.classmodcore.core.ClassUtil.BASE_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.CLASS_SPLIT;
import static alpvax.mod.classmodcore.core.ClassUtil.CLASS_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.POWER_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_CD_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_DATA_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_DUR_TAG;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_SLOT_TAG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import alpvax.common.mods.ModData;
import alpvax.mod.classmodcore.core.ClassMod;
import alpvax.mod.classmodcore.core.ClassUtil;
import alpvax.mod.classmodcore.network.CommonProxy;
import alpvax.mod.classmodcore.network.packet.DataStringPacket;
import alpvax.mod.classmodcore.power.Power;
import alpvax.mod.classmodcore.util.DataString;
import alpvax.mod.classmodcore.util.PowerEntry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ExtendedPlayerOLD implements IExtendedEntityProperties
{
	// I always include the entity to which the properties belong for easy access
	// It's final because we won't be changing which player it is
	private final EntityPlayer player;
	private String datastring;

	private String[] classIDs = null;
	private PowerEntry[] powers = null;
	
	public ExtendedPlayerOLD(EntityPlayer entityplayer)
	{
		player = entityplayer;
		//player.getDataWatcher().addObject(CLASS_WATCHER, new DataString().toString());
		datastring = new DataString().toString();
	}
	
	/**
	* Used to register these extended properties for the player during EntityConstructing event
	* This method is for convenience only;
	*/
	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(BASE_TAG, new ExtendedPlayerOLD(player));
	}
	
	/**
	* Returns ExtendedPlayer properties for player
	* This method is for convenience only;
	*/
	public static final ExtendedPlayerOLD get(EntityPlayer player)
	{
		return (ExtendedPlayerOLD)player.getExtendedProperties(BASE_TAG);
	}
	
	private String getProxySaveKey()
	{
		return player.username + ":" + BASE_TAG;
	}	

	/**
	* This cleans up the onEntityDeath event by replacing most of the code
	* with a single line: ExtendedPlayer.saveProxyData();
	*/
	public void saveProxyData()
	{
		NBTTagCompound savedData = new NBTTagCompound();
		saveNBTData(savedData);
		CommonProxy.storeEntityData(getProxySaveKey(), savedData);
	}
	/**
	* This cleans up the onEntityJoinWorld event by replacing most of the code
	* with a single line: ExtendedPlayer.loadProxyData();
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
		//compound.setString(BASE_TAG, getDataString().toString());
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
					NBTTagList list = nbt.getTagList(POWER_TAG);
					powers = new PowerEntry[list.tagCount()];
					for(int i = 0; i < list.tagCount(); i++)
					{
						powers[i] = new PowerEntry((NBTTagCompound)list.tagAt(i));
					}
				}
			}
			//saveDataString(new DataString(compound.getString(BASE_TAG)));
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
		}
	}
	
	/**
	 * @param operation 0 => add modifier to base, 1 => multiply base by modifier, 2 => multiply final result by modifier.
	 */
	private void updateAttribute(Attribute a, double modifier, int operation)
	{
		AttributeInstance att = player.getAttributeMap().getAttributeInstance(a);
		AttributeModifier m = att.getModifier(ClassUtil.attModIDClass);
		if(m != null)
		{
			att.removeModifier(m);
		}
		att.applyModifier(new AttributeModifier(ClassUtil.attModIDClass, ModData.classModID, modifier - (operation == 2 ? 1D : 0D), operation));
	}
	
	
	
	//****************PlayerClass Methods****************
	public DataString getDataString()
	{
		/*if(!(player instanceof EntityPlayerMP))
		{
			PacketDispatcher.sendPacketToServer(new DataStringPacket(null, true).makePacket());
		}*/
		//String s = player.getDataWatcher().getWatchableObjectString(CLASS_WATCHER);
		return new DataString(datastring);
	}
	public void saveDataString(DataString data)
	{
		datastring = data.toString();
		if(!player.worldObj.isRemote)
		{
			System.out.println(new DataStringPacket(data).makePacket() + " Player: " + (Player)player);
			PacketDispatcher.sendPacketToPlayer(new DataStringPacket(data).makePacket(), (Player)player);
			//player.getDataWatcher().updateObject(CLASS_WATCHER, data.toString());
		}
	}
	
	public String getPlayerClassName()
	{
		return getDataString().getPlayerClass();
	}
	private void setPlayerClass(String classID)
	{
		DataString data = getDataString();
		data.setPlayerClass(classID);
		saveDataString(data);
		init(player, player.getEntityWorld());
	}
	
	private String getNextClass()
	{
		return getDataString().getNextClass();
	}
	private void setNextClass(String classID)
	{
		DataString data = getDataString();
		data.setNextClass(classID);
		saveDataString(data);
	}

	/**
	 * Sets the player's class depending on whether they are allowed to change
	 * @param id the ID of the class to change to.
	 * @return 0 if the class was not set, 1 if it was set to change when the player respawns, 2 if it has been set straight away.
	 */
	public int setPlayerClassWithCheck(String classID)
	{
		int i = checkCanChangeClass();
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
	private int checkCanChangeClass()
	{
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
		if(name == null || ClassUtil.verifyClassName(name) > 0)
		{
			return null;
		}
		return PlayerClassRegistry.getPlayerClass(name);
	}
	public boolean hasPlayerClass()
	{
		return getPlayerClass() != null;
	}
	
	public void changeClassOnDeath()
	{
		String next = getNextClass();
		if(!getPlayerClassName().equals(next))
		{
			setPlayerClass(next);
		}
	}
	
	public void startPowerDelay()
	{
		PlayerClass pc = getPlayerClass();
		if(pc != null)
		{
			boolean flag = ClassMod.startDelay > 0;
			if(flag || ClassMod.startOnCooldown)
			{
				for(int i = 0; i < pc.getNumPowers(); i++)
				{
					if(flag && pc.getPowerCooldown(i) < 0 && ClassMod.delayPassive)
					{
						setPowerCooldown(i, ClassMod.startDelay);
					}
					else
					{
						setPowerCooldown(i, ClassMod.startDelay + (ClassMod.startOnCooldown ? pc.getPowerCooldown(i) : 0));
					}
				}
			}
		}
	}
	
	public boolean getIsOblivious(Entity entity)
	{
		return hasPlayerClass() && getPlayerClass().getIsOblivious(entity);
	}
	
	

	//****************Power Methods****************
	public int getPowerCooldown(int slot)
	{
		System.out.println("SIDE: " + FMLCommonHandler.instance().getEffectiveSide() + "; Slot: " + slot + ", Data: " + getDataString().toString());
		return getDataString().getCooldown(slot);
	}
	public void setPowerCooldown(int slot, int cooldown)
	{
		DataString data = getDataString();
		data.setCooldown(slot, cooldown);
		saveDataString(data);
	}

	public int getPowerDuration(int slot)
	{
		return getDataString().getDuration(slot);
	}
	public void setPowerDuration(int slot, int duration)
	{
		DataString data = getDataString();
		data.setDuration(slot, duration);
		saveDataString(data);
	}

	public String getPowerData(int slot)
	{
		return getDataString().getAdditionalData(slot);
	}
	public void setPowerData(int slot, String powerData)
	{
		DataString data = getDataString();
		data.setAdditionalData(slot, powerData);
		saveDataString(data);
	}
	


	public List<Integer> getActivePowers()
	{
		PlayerClass pc = getPlayerClass();
		List<Integer> list = new ArrayList<Integer>();
		if(pc == null)
		{
			return list;
		}
		for(int i = 0; i < pc.getNumPowers(); i++)
		{
			int j = getPowerDuration(i);
			if(j > 0 || pc.isPowerConstant(i))
			{
				list.add(pc.getPowerID(i));
			}
		}
		return list;
	}
	/*public <T extends Power> List<T> getActivePowers(Class<T> powerclass)
	{
		List<T> powers = new ArrayList<T>();
		Iterator<Integer> i = getActivePowers().iterator();
		while(i.hasNext())
		{
			Power power = Power.getPower(i.next());
			if(powerclass.isAssignableFrom(power.getClass()))
			{
				powers.add(powerclass.cast(power));
			}
		}
		return powers;
	}
	public boolean isPowerActive(Power power)
	{
		return isPowerActive(power.powerID);
	}
	public boolean isPowerActive(int powerID)
	{
		return getActivePowers().contains(Integer.valueOf(powerID));
	}*/
}
