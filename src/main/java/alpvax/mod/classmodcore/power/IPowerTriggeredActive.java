package alpvax.mod.classmodcore.power;

import net.minecraft.entity.player.EntityPlayer;

public interface IPowerTriggeredActive extends IPowerActive, IPowerTriggered
{
	public boolean shouldReset(EntityPlayer player);
	public void reset(EntityPlayer player);
}
