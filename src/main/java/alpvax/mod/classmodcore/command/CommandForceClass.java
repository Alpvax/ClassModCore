package alpvax.mod.classmodcore.command;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import alpvax.mod.classmodcore.classes.PlayerClassRegistry;

/**
 * @author Alpvax
 *
 */
public class CommandForceClass extends CommandBase
{
	@Override
	public String getName()
	{
		return "forceclass";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return I18n.format("command.changeclass.usage", getName());
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return i == 1;
	}

    public int getRequiredPermissionLevel()
    {
        return 2;
    }
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException
	{

	}
	
	/**
	 * Adds the strings available in this command to the given list of tab completion options.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		if(args.length == 0)
		{
			return func_175762_a(args, PlayerClassRegistry.availableClasses(sender));
		}
		if(args.length == 1 && sender.canUseCommand(2, getName()))
		{
			return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
		}
		return null;
	}
}
