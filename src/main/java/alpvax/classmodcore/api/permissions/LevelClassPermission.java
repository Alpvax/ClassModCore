package alpvax.classmodcore.api.permissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * @author Alpvax
 *
 */
public class LevelClassPermission implements IPlayerClassPermission
{
	private int guiPermLevel;
	private int cmdPermLevel;
	private boolean enabled = true;

	public LevelClassPermission(int permLevel)
	{
		this(permLevel, permLevel);
	}

	public LevelClassPermission(int guiPermLevel, int cmdPermLevel)
	{
		this.guiPermLevel = guiPermLevel;
		this.cmdPermLevel = cmdPermLevel;
	}

	@Override
	public void setFromConfig(Property configProperty)
	{
		enabled = configProperty.getBoolean();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isAvailableInGui(EntityPlayer player)
	{
		return enabled && player.canUseCommand(guiPermLevel, "forceclass");
	}

	@Override
	public boolean isAvailableForCommand(ICommandSender commandSender)
	{
		return enabled && commandSender.canUseCommand(cmdPermLevel, "forceclass");
	}
}
