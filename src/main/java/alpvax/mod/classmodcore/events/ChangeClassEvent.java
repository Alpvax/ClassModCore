package alpvax.mod.classmodcore.events;

import alpvax.mod.classmodcore.classes.IPlayerClass;
import alpvax.mod.classmodcore.classes.PlayerClassHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * This event is fired on the server whenever a players class is changed.<br>
 * <br>
 * This event is fired from {@link PlayerClassHelper#setPlayerClass(EntityPlayer, net.minecraft.world.World)}.<br>
 * <br>
 * {@link #playerclass} is the the IPlayerClass that is being set. (Can be modified). <br>
 * {@link #setSource} is the the ICommandSender that is causing the class to be changed set. (Cannot be modified). <br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * <br>
 * This event does not have a result. {@link HasResult}<br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.
 **/
@Cancelable
public class ChangeClassEvent extends PlayerEvent
{
	public IPlayerClass playerclass;
	public final ICommandSender setSource;
	
	/**
	 * @param player
	 */
	public ChangeClassEvent(EntityPlayer player, IPlayerClass playerclass, ICommandSender sender)
	{
		super(player);
		this.playerclass = playerclass;
		setSource = sender;
	}

}
