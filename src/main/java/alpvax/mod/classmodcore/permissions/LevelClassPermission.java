package alpvax.mod.classmodcore.permissions;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
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
	
	public LevelClassPermission(int permLevel)
	{
		this(permLevel, permLevel);
	}
	public LevelClassPermission(int guiPermLevel, int cmdPermLevel)
	{
		this.guiPermLevel = guiPermLevel;
		this.cmdPermLevel = cmdPermLevel;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isAvailableInGui(EntityPlayer player)
	{
		return player.canUseCommand(guiPermLevel, "");
	}
	
	@Override
	public boolean isAvailableForCommand(ICommandSender commandSender)
	{
		return commandSender.canUseCommand(cmdPermLevel, "");
	}
}
