package alpvax.common.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public abstract class AlpModPacket
{
	public static final String channel = "AlpvaxModChannel";
	
	private static final Map<Integer, Class<? extends AlpModPacket>> idMap = new HashMap<Integer, Class<? extends AlpModPacket>>();
	private static final Map<Class<? extends AlpModPacket>, Integer> classMap = new HashMap<Class<? extends AlpModPacket>, Integer>();
	private static int nextID = 0;
    
    public static int registerPacket(Class<? extends AlpModPacket> packetClass)
    {
    	if(classMap.containsKey(packetClass))
    	{
    		throw new RuntimeException("Packet " + packetClass.getSimpleName() + " has already been registered!");
    	}
    	int id = nextID++;
    	idMap.put(Integer.valueOf(id), packetClass);
    	classMap.put(packetClass, Integer.valueOf(id));
    	return id;
    }
    
    public static AlpModPacket constructPacket(int packetId) throws ProtocolException, ReflectiveOperationException
    {
        Class<? extends AlpModPacket> clazz = idMap.get(Integer.valueOf(packetId));
        if (clazz == null)
        {
        	throw new ProtocolException("Unknown Packet Id!");
        }
        else
        {
        	return clazz.newInstance();
        }
    }

    public static class ProtocolException extends Exception
    {

        public ProtocolException()
        {
        }
        public ProtocolException(String message, Throwable cause)
        {
                super(message, cause);
        }
        public ProtocolException(String message)
        {
                super(message);
        }
        public ProtocolException(Throwable cause)
        {
                super(cause);
        }
	}
    
    private final int getPacketId()
	{
		if (classMap.containsKey(getClass()))
		{
			return classMap.get(getClass()).intValue();
		}
		else
		{
			throw new RuntimeException("Packet " + getClass().getSimpleName() + " is missing a mapping!");
		}
	}
    
    public final Packet makePacket()
    {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeByte(getPacketId());
		write(out);
		return PacketDispatcher.getPacket(channel, out.toByteArray());
    }
    
    public abstract void read(ByteArrayDataInput in) throws ProtocolException;
    
    public abstract void write(ByteArrayDataOutput out);
    
    public abstract void execute(EntityPlayer player, Side side) throws ProtocolException;
}
