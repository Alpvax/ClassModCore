package alpvax.mod.classmodcore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import alpvax.common.network.AlpModPacket;
import alpvax.mod.classmodcore.playerclass.ExtendedPlayerOLD;
import alpvax.mod.classmodcore.util.DataString;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class DataStringPacket extends AlpModPacket
{
	private String data;
	//private byte flags;
	//private boolean isRequest;
	
	public DataStringPacket(){}
	
	/*
	 * @param datastring
	 * @param type 0 = sending data s-c; 1 = requesting data c-s
	 */
	public DataStringPacket(DataString datastring)//, boolean request)
	{
		data = datastring.toString();
		//flags = type;
		//isRequest = request;
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException
	{
		data = in.readUTF();
		//flags = in.readByte();
		//isRequest = in.readBoolean();
	}

	@Override
	public void write(ByteArrayDataOutput out)
	{
		out.writeUTF(data);
		//out.writeBoolean(isRequest);
		//out.writeByte(flags);
	}

	@Override
	public void execute(EntityPlayer player, Side side)	throws ProtocolException
	{
		ExtendedPlayerOLD ep = ExtendedPlayerOLD.get(player);
		if(side == Side.CLIENT)
		{
			System.out.println("LOC: Packet; SIDE: "+side+"; " + data);
			ep.saveDataString(new DataString(data));
		}
	}

}
