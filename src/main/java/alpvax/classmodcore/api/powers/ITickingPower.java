package alpvax.classmodcore.api.powers;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

public interface ITickingPower extends IPower
{
	public void onTick(EntityPlayer player, Map<String, Object> instanceData);
}
