package alpvax.mod.classmodcore.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import alpvax.mod.classmodcore.classes.PlayerClassRegistry;
import alpvax.mod.classmodcore.core.ClassUtil;
import alpvax.mod.classmodcore.playerclass.ExtendedPlayer;

public class CommandChangeClass extends CommandBase
{
	@Override
	public String getName()
	{
		return "class";
	}

	@Override
	public void execute(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
	{
		int l = getAllowedLevel(par1ICommandSender);
		if(par2ArrayOfStr.length < 1)
		{
			throw new WrongUsageException(getCommandUsage(par1ICommandSender), new Object[0]);
		}
		EntityPlayerMP entityplayermp;
		if(par2ArrayOfStr.length > 1 && l > 0)
		{
			entityplayermp = getPlayer(par1ICommandSender, par2ArrayOfStr[0]);
			par2ArrayOfStr = Arrays.copyOfRange(par2ArrayOfStr, 1, par2ArrayOfStr.length);
		}
		else
		{
			entityplayermp = getCommandSenderAsPlayer(par1ICommandSender);
		}
		String cname = par2ArrayOfStr[0];
		ExtendedPlayer ep = ExtendedPlayer.get(entityplayermp);
		boolean flag = false;
		if(l > 1 || PlayerClassRegistry.allowedClasses.contains(cname))
		{
			flag = ep.setPlayerClassWithCheck(cname) > 0;
		}
		/*
		 * TODO: if(flag) { //notifyOperators(par1ICommandSender, ClassUtil.COMMANDSUCESS, new Object[] {ep.getPlayerClassName(), entityplayermp.getName()}); } else { throw new CommandException(ClassUtil.COMMANDNOTFOUND, new Object[] {cname}); }
		 */
	}

	/**
	 * Adds the strings available in this command to the given list of tab completion options.
	 */
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
	{
		int i = getAllowedLevel(par1ICommandSender);
		int j = par2ArrayOfStr.length;
		if(i == 0 || j == 1)
		{
			return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
		}
		if(j == 2)
		{
			return getListOfStringsMatchingLastWord(par2ArrayOfStr, PlayerClassRegistry.allowedClasses.toArray(new String[0]));
		}
		return null;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return getAllowedLevel(icommandsender) > 0 ? ClassUtil.COMMANDUSAGEFULL : ClassUtil.COMMANDUSAGE;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return i == 0;
	}

	private int getAllowedLevel(ICommandSender sender)
	{
		if(sender.getName().equalsIgnoreCase("Alpvax") || sender.canUseCommand(5, getName()))
		{
			return 2;
		}
		if(sender.canUseCommand(2, getName()))
		{
			return 1;
		}
		return 0;
	}
}
