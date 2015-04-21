package alpvax.mod.classmodcore.command;

import java.util.List;

import org.apache.commons.lang3.ClassUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import alpvax.mod.classmodcore.classes.PlayerClassRegistry;
import alpvax.mod.classmodcore.core.ClassMod;
import alpvax.mod.classmodcore.core.ModInfo;

public class CommandChangeClass extends CommandForceClass
{
	@Override
	public String getName()
	{
		return "playerclass";
	}

	@Override
	public void execute(ICommandSender sender, String[] args)
	{
		if(args.length < 1 && sender.getEntityWorld().isRemote)
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			player.openGui(ClassMod.instance, 0, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
		EntityPlayer entityplayermp;
		if(args.length > 1 && l > 0)
		{
			entityplayermp = getPlayer(sender, args[0]);
			args = Arrays.copyOfRange(args, 1, args.length);
		}
		else
		{
			entityplayermp = getCommandSenderAsPlayer(sender);
		}
		String cname = args[0];
		boolean flag = false;
		if(l > 1 || PlayerClassRegistry.allowedClasses.contains(cname))
		{
			flag = ep.setPlayerClassWithCheck(cname) > 0;
		}
		/*
		 * TODO: if(flag) { //notifyOperators(par1ICommandSender, ClassUtil.COMMANDSUCESS, new Object[] {ep.getPlayerClassName(), entityplayermp.getName()}); } else { throw new CommandException(ClassUtil.COMMANDNOTFOUND, new Object[] {cname}); }
		 */
	}

	/*@Override
	public List<String> getAliases()
	{
		// TODO Aliases
		return null;
	}*/
}
