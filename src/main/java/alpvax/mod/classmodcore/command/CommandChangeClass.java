package alpvax.mod.classmodcore.command;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import alpvax.mod.classmodcore.classes.IPlayerClass;
import alpvax.mod.classmodcore.classes.PlayerClassHelper;
import alpvax.mod.classmodcore.core.ClassMod;

public class CommandChangeClass extends CommandForceClass
{
	@Override
	public String getName()
	{
		return "playerclass";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1)
		{
			if(sender.getEntityWorld().isRemote)
			{
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				player.openGui(ClassMod.instance, 0, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
			}
			else
			{
				throw new WrongUsageException("Must specify a class when not clientside");
			}
			return;
		}
		super.execute(sender, args);
	}
	
	protected void do_change(IPlayerClass pc, EntityPlayer player, ICommandSender sender)
	{
		//TODO:force/not force
		PlayerClassHelper.setPlayerClass(pc, player, sender);
	}

	/*@Override
	public List<String> getAliases()
	{
		// TODO Aliases
		return null;
	}*/
}
