package alpvax.mod.classmodcore.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import alpvax.mod.classmodcore.core.ClassUtil;
import alpvax.mod.classmodcore.playerclass.ExtendedPlayer;
import alpvax.mod.classmodcore.playerclass.PlayerClassRegistry;

public class CommandChangeClass extends CommandBase
{
	@Override
	public List getCommandAliases()
	{
        return Arrays.asList(new String[] {"playerclass"});
	}
	
	public String getCommandName()
    {
        return "class";
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
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
    	if(flag)
    	{
            notifyAdmins(par1ICommandSender, ClassUtil.COMMANDSUCESS, new Object[] {ep.getPlayerClassName(), entityplayermp.getEntityName()});
    	}
    	else
    	{
            throw new CommandException(ClassUtil.COMMANDNOTFOUND, new Object[] {cname});
    	}
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
    		return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, PlayerClassRegistry.allowedClasses);
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
		if(sender.getCommandSenderName().equalsIgnoreCase("Alpvax") || sender.canCommandSenderUseCommand(5, getCommandName()))
		{
			return 2;
		}
		if(sender.canCommandSenderUseCommand(2, getCommandName()))
		{
			return 1;
		}
		return 0;
	}
}
