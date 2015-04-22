package alpvax.classmodcore.api.events;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import alpvax.classmodcore.api.classes.IPlayerClass;
import alpvax.classmodcore.api.classes.PlayerClassHelper;


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
	public static final int FLAG_FORCE = 1;

	public IPlayerClass playerclass;
	public final ICommandSender setSource;
	public final int flags;

	public ChangeClassEvent(EntityPlayer player, IPlayerClass playerclass, ICommandSender sender)
	{
		this(player, playerclass, sender, 0);
	}

	public ChangeClassEvent(EntityPlayer player, IPlayerClass playerclass, ICommandSender sender, int flags)
	{
		super(player);
		this.playerclass = playerclass;
		setSource = sender;
		this.flags = flags;
	}

}
