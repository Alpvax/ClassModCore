package alpvax.classmodcore.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import alpvax.classmodcore.api.classes.PlayerClassHelper;
import alpvax.classmodcore.api.classes.PlayerClassInstance;


/**
 * @author Alpvax
 *
 */
public class TriggerPowerPacket implements IMessage
{
	private String username;
	private int index;

	public TriggerPowerPacket()
	{
	}

	public TriggerPowerPacket(EntityPlayer player, int index)
	{
		username = player.getName();
		this.index = index;
	}


	@Override
	public void fromBytes(ByteBuf buf)
	{
		index = buf.readInt();
		username = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(index);
		ByteBufUtils.writeUTF8String(buf, username);
	}

	public static class Handler implements IMessageHandler<TriggerPowerPacket, IMessage>
	{
		@Override
		public IMessage onMessage(TriggerPowerPacket message, MessageContext ctx)
		{
			PlayerClassInstance pci = PlayerClassHelper.getPlayerClassInstance(MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(message.username));
			if(pci != null)
			{
				pci.togglePower(message.index);
			}
			return null;
		}
	}
}
