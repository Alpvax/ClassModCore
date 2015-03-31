package alpvax.mod.classmodcore.util;

import static alpvax.mod.classmodcore.core.ClassUtil.CLASS_SPLIT;
import static alpvax.mod.classmodcore.core.ClassUtil.NULL_CHAR;
import static alpvax.mod.classmodcore.core.ClassUtil.PWR_SUBSPLIT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraftforge.fml.common.FMLCommonHandler;
import alpvax.mod.classmodcore.core.ClassMod;
import alpvax.mod.classmodcore.playerclass.PlayerClass;
import alpvax.mod.classmodcore.playerclass.PlayerClassRegistry;

public class DataString
{
	private String[] names;
	private PowerEntry[] powers;
	
	/**
	 * Initialisation of null values
	 */
	public DataString()
	{
		this(null);
	}
	/**
	 * Functional method.
	 * @param data the string to read the values from
	 */
	public DataString(String data)
	{
		if(data == null)
		{
			data = NULL_CHAR + CLASS_SPLIT + NULL_CHAR + CLASS_SPLIT + NULL_CHAR;
		}
		names = getNameArray(data);
		if(!names[0].equals(NULL_CHAR))
		{
			System.out.println("LOC: DSCon; SIDE: " + FMLCommonHandler.instance().getEffectiveSide() + "; " + getArray(data)[2]);
			if(getArray(data)[2].equals(NULL_CHAR))
			{
				powers = getPowersForClass();
			}
			else
			{
				powers = getPowers(data);
			}
		}
		else
		{
			powers = new PowerEntry[0];
		}
		System.out.println("LOC: DSCon; SIDE: " + FMLCommonHandler.instance().getEffectiveSide() + "; " + toString());
	}
	/**
	 * Method to simplify the creation of this. Simply calls this(data) with the string read from the players DataWatcher
	 * @param player the player to call this class on
	 */
	/*public DataString(EntityPlayer player)
	{
		this(player.getDataWatcher().getWatchableObjectString(CLASS_WATCHER));
	}*/
	
	private String[] getArray(String data)
	{
		return data.split(CLASS_SPLIT);
	}
	private String[] getNameArray(String data)
	{
		return Arrays.copyOfRange(getArray(data), 0, 2);
	}
	private PowerEntry[] getPowers(String data)
	{
		String s = getArray(data)[2];
		if(!s.equals(NULL_CHAR))
		{
			Matcher m = Pattern.compile("(\\[(\\d+" + PWR_SUBSPLIT + ")+\\w*\\])+").matcher(s);
			if (m.matches())
			{
			    System.out.println("SIDE: " + FMLCommonHandler.instance().getEffectiveSide() + "; Whole String matched: " + m.group());
			    // resets matcher
			    m.reset();
			    List<String> list = new ArrayList<String>();
			    // iterates over found
			    while(m.find())
			    {
			        System.out.println("Found: " + m.group(1));
			        list.add(m.group(1));
			    }
			    if(list.size() > 0)
			    {
					PowerEntry[] pe = new PowerEntry[list.size()];
					for(int i = 0; i < list.size(); i++)
					{
						pe[i] = new PowerEntry(list.get(i));
					}
					return pe;
			    }
			}
		}
		return null;
	}
	private PowerEntry[] getPowersForClass()
	{
		PlayerClass pc = PlayerClassRegistry.getPlayerClass(names[0]);
		System.out.println("SIDE: " + FMLCommonHandler.instance().getEffectiveSide() + "; Finding powers for: " + pc + ", ClassName: " + names[0]);
		if(pc != null)
		{
			System.out.println(pc.className + ": " + pc.getNumPowers());
			PowerEntry[] p = new PowerEntry[pc.getNumPowers()];
			for(int i = 0; i < pc.getNumPowers(); i++)
			{
				p[i] = new PowerEntry(i, ClassMod.startOnCooldown ? pc.getPowerCooldown(i) : 0);
				System.out.println(p[i].toString());
			}
			return p;
		}
		else
		{
			return null;
		}
	}

	public String getPlayerClass()
	{
		return names[0];
	}
	/**
	 * Automatically updates the next class ID to be the same
	 */
	public void setPlayerClass(String ID)
	{
		if(PlayerClassRegistry.getPlayerClass(ID) != null)
		{
			names[0] = ID;
			setNextClass(ID);
			powers = getPowersForClass();
		}
	}

	public String getNextClass()
	{
		return names[1];
	}
	public void setNextClass(String ID)
	{
		names[1] = PlayerClassRegistry.getPlayerClass(ID) != null ? ID : names[1];
	}

	public int getCooldown(int slot)
	{
		return powers[slot].getCooldown();
	}
	public void setCooldown(int slot, int cooldown)
	{
		powers[slot].setCooldown(cooldown);
	}

	public int getDuration(int slot)
	{
		return powers[slot].getDuration();
	}
	public void setDuration(int slot, int duration)
	{
		powers[slot].setDuration(duration);
	}

	public String getAdditionalData(int slot)
	{
		return powers[slot].getAdditionalData();
	}
	public void setAdditionalData(int slot, String data)
	{
		powers[slot].setAdditionalData(data);
	}
	
	
	
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		s.append(names[0]).append(CLASS_SPLIT).append(names[1]);
		s.append(CLASS_SPLIT);
		System.out.println("LOC: DS; SIDE: " + FMLCommonHandler.instance().getEffectiveSide() + "; Powers = " + powers);
		System.out.println("LOC: DS; SIDE: " + FMLCommonHandler.instance().getEffectiveSide() + "; Num Powers = " + (powers != null ? powers.length : null));
		if(powers != null && powers.length > 0)
		{
			for(PowerEntry power : powers)
			{
				s.append(power.toString());
			}
		}
		else
		{
			s.append(NULL_CHAR);
		}
		return s.toString();
	}
}
