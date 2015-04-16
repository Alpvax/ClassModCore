package alpvax.mod.classmodcore.classes;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.mod.classmodcore.powers.PowerEntry;
import alpvax.mod.classmodcore.powers.PowerInstance;

/**
 * @author Alpvax
 *
 */
public class PlayerClassSaveData extends WorldSavedData
{
	private static final String KEY_ID = "ClassID";
	private static final String KEY_POWERS = "Powers";
	
	private String id;
	protected PowerInstance[] powers;

	/**
	 * @param name
	 */
	public PlayerClassSaveData(String name)
	{
		super(name);
		id = "";
	}

	public IPlayerClass getPlayerClass()
	{
		return PlayerClassRegistry.getPlayerClass(id);
	}
	
	public void setPlayerClass(IPlayerClass playerclass)
	{
		if(playerclass == null)
		{
			id = "";
			powers = null;
			return;
		}
		id = playerclass.getClassID();
		List<PowerEntry> list = playerclass.getPowers();
		int num = 0;
		if(list != null)
		{
			num = list.size();
		}
		powers = new PowerInstance[num];
		for(int i = 0; i < num; i++)
		{
			powers[i] = list.get(i).createInstance();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		id = nbt.hasKey(KEY_ID, NBT.TAG_STRING) ? nbt.getString(KEY_ID) : "";
		if(powers != null)
		{
			NBTTagList list = nbt.getTagList(KEY_POWERS, NBT.TAG_COMPOUND);
			for(int i = 0; i < powers.length; i++)
			{
				powers[i].readFromNBT(list.getCompoundTagAt(i));
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString(KEY_ID, id);
		NBTTagList list = new NBTTagList();
		for(PowerInstance p : powers)
		{
			NBTTagCompound nbt1 = new NBTTagCompound();
			p.writeToNBT(nbt1);
			list.appendTag(nbt1);
		}
		if(list.tagCount() > 0)
		{
			nbt.setTag(KEY_POWERS, list);
		}
	}
}
