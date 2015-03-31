package alpvax.mod.classmodcore.powers;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Alpvax
 *
 */
public class PowerInstance
{
	private Power power;
	private int maxCD;
	private int cooldown;
	private int maxDur;
	private int duration;
	private byte type;
	private NBTTagCompound data;
	
	protected PowerInstance(Power power, int type)
	{
		this.power = power;
		this.type = (byte)type;
	}
	
	public void tickPower()
	{
		if(PowerType.CONTINUOUS(type))
		if(maxDur > 0 && duration > 0)
		{
			duration--;
			
		}
		if(maxCD > 0 && cooldown > 0)
		{
			cooldown--;
		}
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		
	}
}
