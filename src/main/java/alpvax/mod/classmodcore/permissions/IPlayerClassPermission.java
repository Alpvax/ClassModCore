package alpvax.mod.classmodcore.permissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Alpvax
 *
 */
public interface IPlayerClassPermission
{
	public boolean isAvailableInGui(EntityPlayer player);

	public boolean isAvailableForCommand(ICommandSender commandSender);
}
