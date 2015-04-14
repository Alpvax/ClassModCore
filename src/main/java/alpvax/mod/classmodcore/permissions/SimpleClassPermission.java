package alpvax.mod.classmodcore.permissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Alpvax
 *
 */
public class SimpleClassPermission implements IPlayerClassPermission
{
	private boolean enabled = true;
	
	public SimpleClassPermission(boolean enabled)
	{
		this.enabled = enabled;
	}

	public SimpleClassPermission setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		return this;
	}
	
	@Override
	public boolean isAvailableInGui(EntityPlayer player)
	{
		return enabled;
	}
	
	@Override
	public boolean isAvailableForCommand(ICommandSender commandSender)
	{
		return enabled;
	}

}
