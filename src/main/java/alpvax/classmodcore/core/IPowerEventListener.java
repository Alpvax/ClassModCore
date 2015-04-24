package alpvax.classmodcore.core;

import java.util.List;

import alpvax.classmodcore.api.powers.IPower;
import net.minecraftforge.fml.common.eventhandler.Event;

public interface IPowerEventListener extends IPower
{
	List<Class<? extends Event>> getValidEvents();
	
	public <T extends Event> T listenToEvent(T event);
}
