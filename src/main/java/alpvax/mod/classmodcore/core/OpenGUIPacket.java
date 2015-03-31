package alpvax.mod.classmodcore.core;

import net.minecraft.entity.player.EntityPlayer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import alpvax.common.network.AlpModPacket;
import alpvax.mod.classmodcore.playerclass.ExtendedPlayer;

public class OpenGUIPacket extends AlpModPacket
{

	@Override
	public void read(ByteArrayDataInput in) throws ProtocolException
	{
		
	}

	@Override
	public void write(ByteArrayDataOutput out)
	{
		
	}

	@Override
	public void execute(EntityPlayer player, Side side) throws ProtocolException
	{
		if(!ExtendedPlayer.get(player).hasPlayerClass())
		{
			player.openGui(ClassMod.instance, ClassUtil.classGUIID, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
	}

}
