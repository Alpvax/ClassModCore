package alpvax.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		try
		{
			EntityPlayer sender = (EntityPlayer) player;
			ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);
			int packetId = in.readUnsignedByte();
			AlpModPacket modPacket = AlpModPacket.constructPacket(packetId);
			modPacket.read(in);
			modPacket.execute(sender, sender.worldObj.isRemote ? Side.CLIENT : Side.SERVER);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}