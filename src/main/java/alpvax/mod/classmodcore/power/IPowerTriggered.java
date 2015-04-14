package alpvax.mod.classmodcore.power;

import net.minecraft.entity.player.EntityPlayer;

public interface IPowerTriggered
{
	public boolean shouldTrigger(EntityPlayer player);

	public void trigger(EntityPlayer player);
}