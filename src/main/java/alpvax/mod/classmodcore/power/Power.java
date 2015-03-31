package alpvax.mod.classmodcore.power;

import java.util.HashMap;
import java.util.Map;

import alpvax.mod.classmodcore.playerclass.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class Power
{
	private String displayName = null;
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public Power setDisplayName(String name)
	{
		displayName = name;
		return this;
	}
}
