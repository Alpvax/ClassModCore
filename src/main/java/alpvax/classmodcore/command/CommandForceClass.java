package alpvax.classmodcore.command;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import alpvax.classmodcore.api.classes.IPlayerClass;
import alpvax.classmodcore.api.classes.PlayerClassHelper;
import alpvax.classmodcore.api.classes.PlayerClassRegistry;


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

	@Override
	public int getRequiredPermissionLevel()
	{
		return 3;
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1)
		{
			throw new WrongUsageException("Must specify a class");
		}
		String cname = args[0];
		if(!PlayerClassRegistry.availableClasses(sender).contains(cname))
		{
			if(PlayerClassRegistry.getPlayerClass(cname) != null)
			{
				throw new CommandException("command.changeclass.permission", cname);
				/*IChatComponent chatcomponenttranslation = new ChatComponentTranslation("command.changeclass.permission", cname);
				chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
				sender.addChatMessage(chatcomponenttranslation);*/
			}
			else
			{
				throw new CommandException("command.changeclass.notfound", cname);
				/*IChatComponent chatcomponenttranslation = new ChatComponentTranslation("command.changeclass.notfound", cname);
				chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
				sender.addChatMessage(chatcomponenttranslation);*/
			}
		}
		else
		{
			EntityPlayer player;
			if(args.length > 1)
			{
				player = getPlayer(sender, args[1]);
			}
			else
			{
				player = getCommandSenderAsPlayer(sender);
			}
			IPlayerClass pc = PlayerClassRegistry.getPlayerClass(cname);
			do_change(pc, player, sender);
			notifyOperators(sender, this, "command.changeclass.success", pc.getDisplayName(), player.getDisplayNameString());
		}
	}

	protected void do_change(IPlayerClass pc, EntityPlayer player, ICommandSender sender)
	{
		PlayerClassHelper.setPlayerClass(pc, player, sender);
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
