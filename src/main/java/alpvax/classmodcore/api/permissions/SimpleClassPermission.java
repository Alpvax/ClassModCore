package alpvax.classmodcore.api.permissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Property;


/**
 * @author Alpvax
 *
 */
public class SimpleClassPermission implements IPlayerClassPermission
{
	private boolean enabled;

	public SimpleClassPermission(boolean enabled)
	{
		this.enabled = enabled;
	}

	@Override
	public void setFromConfig(Property configProperty)
	{
		enabled = configProperty.getBoolean();
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
