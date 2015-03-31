package alpvax.mod.classmodcore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import alpvax.common.network.AlpModPacket;
import alpvax.mod.classmodcore.playerclass.ExtendedPlayer;
import alpvax.mod.classmodcore.playerclass.PlayerClass;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;

public class ClassSelectPacket extends AlpModPacket
{
	private String uname;
	private String cname;
	
	public ClassSelectPacket(){}
	
	public ClassSelectPacket(EntityPlayer player, PlayerClass playerclass)
	{
		uname = player.username;
		cname = playerclass.className.toLowerCase();
	}

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException
	{
		uname = in.readUTF();
		cname = in.readUTF();
	}

	@Override
	public void write(ByteArrayDataOutput out)
	{
		out.writeUTF(uname);
		out.writeUTF(cname);
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException
	{
		if(side.isServer())
		{
	    	ExtendedPlayer.get(player).setPlayerClassWithCheck(cname);
	        //PlayerClass.startPowerDelay(player);
		}
	}

}
