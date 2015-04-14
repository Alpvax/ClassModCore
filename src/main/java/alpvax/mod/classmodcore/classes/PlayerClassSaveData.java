package alpvax.mod.classmodcore.classes;

import alpvax.mod.classmodcore.powers.PowerInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

/**
 * @author Alpvax
 *
 */
public class PlayerClassSaveData extends WorldSavedData
{
	protected String id;
	protected PowerInstance[] powers;

	/**
	 * @param name
	 */
	public PlayerClassSaveData(String name)
	{
		super(name);
		id = "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.world.WorldSavedData#readFromNBT(net.minecraft.nbt. NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.minecraft.world.WorldSavedData#writeToNBT(net.minecraft.nbt. NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}
}
