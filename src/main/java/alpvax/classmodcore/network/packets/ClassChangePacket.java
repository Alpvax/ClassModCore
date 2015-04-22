package alpvax.classmodcore.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import alpvax.classmodcore.api.classes.IPlayerClass;
import alpvax.classmodcore.api.classes.PlayerClassHelper;
import alpvax.classmodcore.api.classes.PlayerClassRegistry;
import alpvax.classmodcore.core.ClassMod;


/**
 * @author Alpvax
 *
 */
public class ClassChangePacket implements IMessage
{
	private String username;
	private String classID;

	public ClassChangePacket()
	{
	}

	public ClassChangePacket(EntityPlayer player, IPlayerClass playerclass)
	{
		username = player.getName();
		classID = playerclass.getClassID();
	}


	@Override
	public void fromBytes(ByteBuf buf)
	{
		username = ByteBufUtils.readUTF8String(buf);
		classID = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, username);
		ByteBufUtils.writeUTF8String(buf, classID);
	}

	public static class ClientHandler implements IMessageHandler<ClassChangePacket, IMessage>
	{
		@Override
		public IMessage onMessage(ClassChangePacket message, MessageContext ctx)
		{
			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(message.username);
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message.username + " has become a " + PlayerClassRegistry.getPlayerClass(message.classID).getDisplayName()));//XXX
			PlayerClassHelper.setPlayerClass(PlayerClassRegistry.getPlayerClass(message.classID), player);
			return null;
		}
	}

	public static class ServerHandler implements IMessageHandler<ClassChangePacket, IMessage>
	{
		@Override
		public IMessage onMessage(ClassChangePacket message, MessageContext ctx)
		{
			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(message.username);
			if(PlayerClassHelper.setPlayerClass(PlayerClassRegistry.getPlayerClass(message.classID), player, ctx.getServerHandler().playerEntity))
			{
				if(PlayerClassHelper.UPDATE_ALL_CLIENTS)
				{
					ClassMod.packetHandler.sendToAll(message);
					return null;
				}
				return message;
			}
			return null;
		}
	}
}
