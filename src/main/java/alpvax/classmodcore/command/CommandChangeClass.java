package alpvax.classmodcore.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import alpvax.classmodcore.api.classes.IPlayerClass;
import alpvax.classmodcore.api.classes.PlayerClassHelper;
import alpvax.classmodcore.core.ClassMod;
import alpvax.classmodcore.core.ModInfo;
import alpvax.common.network.OpenGuiPacket;


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
			Entity e = sender.getCommandSenderEntity();
			if(e != null && e instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)e;
				ClassMod.packetHandler.sendTo(new OpenGuiPacket(ModInfo.MOD_ID, 0), (EntityPlayerMP)player);
				//player.openGui(ClassMod.instance, 0, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
			}
			else
			{
				throw new WrongUsageException("Must specify a class when not clientside");
			}
			return;
		}
		super.execute(sender, args);
	}

	@Override
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
