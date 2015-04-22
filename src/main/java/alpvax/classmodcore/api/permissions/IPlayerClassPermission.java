package alpvax.classmodcore.api.permissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Property;


/**
 * @author Alpvax
 *
 */
public interface IPlayerClassPermission
{
	public void setFromConfig(Property configProperty);

	public boolean isAvailableInGui(EntityPlayer player);

	public boolean isAvailableForCommand(ICommandSender commandSender);
}
