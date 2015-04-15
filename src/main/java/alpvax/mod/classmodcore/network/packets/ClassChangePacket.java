package alpvax.mod.classmodcore.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import alpvax.mod.classmodcore.classes.IPlayerClass;
import alpvax.mod.classmodcore.classes.PlayerClassHelper;
import alpvax.mod.classmodcore.core.ClassMod;
import alpvax.mod.common.util.EntityHelper;

/**
 * @author Alpvax
 *
 */
public class ClassChangePacket implements IMessage
{
	private int playerID;
	private int dimension;
	private String classID;

	public ClassChangePacket()
	{
	}
	public ClassChangePacket(EntityPlayer player, IPlayerClass playerclass)
	{
		playerID = player.getEntityId();
		classID = playerclass.getClassID();
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		playerID = buf.readInt();
		classID = ByteBufUtils.readUTF8String(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(playerID);
		ByteBufUtils.writeUTF8String(buf, classID);
	}

	public static class ClientHandler implements IMessageHandler<ClassChangePacket, IMessage>
	{
		@Override
		public IMessage onMessage(ClassChangePacket message, MessageContext ctx)
		{
			MinecraftServer.getServer().get
			EntityPlayer player = EntityHelper.getEntityByID(message.playerID, world);
			PlayerClassHelper.setPlayerClass()
			return null;
		}
	}

	public static class ServerHandler implements IMessageHandler<ClassChangePacket, IMessage>
	{
		@Override
		public IMessage onMessage(ClassChangePacket message, MessageContext ctx)
		{
			MinecraftServer.getServer().get
			EntityPlayer player = EntityHelper.getEntityByID(message.playerID, world);
			if(ctx.side == Side.CLIENT)
			{
				PlayerClassHelper.setPlayerClass()
				return null;
			}
			if(UPDATE_ALL_CLIENTS)
			{
				ClassMod.packetHandler.sendToAll(message);
			}
		}
	}
}
