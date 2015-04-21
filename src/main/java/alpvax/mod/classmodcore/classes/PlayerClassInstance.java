package alpvax.mod.classmodcore.classes;

import static alpvax.mod.classmodcore.core.ClassUtil.KEY_ID;
import static alpvax.mod.classmodcore.core.ClassUtil.*;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants.NBT;
import alpvax.mod.classmodcore.powers.PowerEntry;
import alpvax.mod.classmodcore.powers.PowerInstance;

public class PlayerClassInstance
{
	private final EntityPlayer player;
	private IPlayerClass playerclass = null;
	private PowerInstance[] powers;
	private List<Integer> manualIndexes;
	
	public PlayerClassInstance(EntityPlayer player)
	{
		this.player = player;
	}

	protected void setPlayerClass(String classID)
	{
		setPlayerClass(PlayerClassRegistry.getPlayerClass(classID));
	}
	
	protected void setPlayerClass(IPlayerClass playerclass)
	{
		if(this.playerclass != playerclass)
		{
			this.playerclass = playerclass;
			for(PowerInstance power : powers)
			{
				power.stop(player);
			}
			List<PowerEntry> list = playerclass.getPowers();
			if(list != null)
			{
				for(int i = 0; i < list.size(); i++)
				{
					powers[i] = list.get(i).createInstance(i);
					powers[i].init(player);
					if(powers[i].setKeybind(i))
					{
						manualIndexes.add(Integer.valueOf(i++));
					}
				}
			}
		}
	}
	
	public void tick()
	{
		for(PowerInstance p : powers)
		{
			p.tickPower(player);
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		setPlayerClass(nbt.getString(KEY_ID));
		NBTTagList list = nbt.getTagList(KEY_POWERS, NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			powers[tag.getInteger(KEY_SLOT)].readFromNBT(tag);
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString(KEY_ID, playerclass.getClassID());
		NBTTagList list = new NBTTagList();
		for(PowerInstance power : powers)
		{
			NBTTagCompound tag = new NBTTagCompound();
			power.writeToNBT(tag);
			list.appendTag(tag);
		}
		nbt.setTag(KEY_POWERS, list);
	}

	public ResourceLocation classImage()
	{
		String classID = playerclass.getClassID();
		return new ResourceLocation(PlayerClassRegistry.modIDMap.get(classID) + ":textures/classes/" + (classID.length() < 1 ? "steve" : classID.replace(".", "/")) + ".png");
	}

	public void togglePower(int index)
	{
		if(index >= 0 && index < manualIndexes.size())
		{
			powers[manualIndexes.get(index).intValue()].togglePower(player);
		}
	}
}
