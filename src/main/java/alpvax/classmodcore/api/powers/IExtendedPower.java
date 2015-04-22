package alpvax.classmodcore.api.powers;

import net.minecraft.nbt.NBTTagCompound;


/**
 * An extended version of Ipowe which allows saving of additional data to NBT
 * @author Alpvax
 *
 */
public interface IExtendedPower extends IPower
{
	/**
	 * Read additional data from NBT if necessary
	 */
	public void readFromNBT(NBTTagCompound nbt);

	/**
	 * Write additional data to NBT if necessary
	 */
	public void writeToNBT(NBTTagCompound nbt);
}
