package alpvax.classmodcore.api.powers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;

public interface IPowerEventListener<T extends LivingEvent> extends IPower
{	
	public void listenToEvent(T e, EntityPlayer player);
}
