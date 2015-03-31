package alpvax.mod.classmodcore.power;

import net.minecraft.entity.player.EntityPlayer;

public class PowerMine extends Power
{
	public String tool;
	public int level;

	public PowerMine(String toolClass, int harvestLevel)
	{
		tool = toolClass;
		level = harvestLevel;
	}
}