package alpvax.classmodcore.api.powers;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;


public interface ITickingPower extends IPower
{
	public int onTick(EntityPlayer player, int ticksElapsed, Map<String, Object> instanceData);
}
