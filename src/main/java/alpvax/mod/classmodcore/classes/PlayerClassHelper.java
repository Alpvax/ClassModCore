package alpvax.mod.classmodcore.classes;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import alpvax.mod.classmodcore.events.ChangeClassEvent;
import alpvax.mod.common.util.EntityHelper;

/**
 * @author Alpvax
 *
 */
public class PlayerClassHelper
{
	/**
	 *Change to true to provide waila support or custom renders
	 */
	//TODO: change
	public static boolean PER_WORLD_CLASSES = false;

	/**
	 *Change to true to provide waila support or custom renders
	 */
	//TODO: change
	public static boolean UPDATE_ALL_CLIENTS = false;

	public static IPlayerClass getPlayerClass(EntityPlayer player, World world)
	{
		return PlayerClassRegistry.getPlayerClass(getSaveData(player).id);
	}

	public static boolean setPlayerClass(IPlayerClass playerclass, EntityPlayer player)
	{
		return setPlayerClass(playerclass, player, MinecraftServer.getServer());
	}
	public static boolean setPlayerClass(IPlayerClass playerclass, EntityPlayer player, ICommandSender sender)
	{
		if(MinecraftForge.EVENT_BUS.post(new ChangeClassEvent(player, playerclass, sender)))
		{
			return false;
		}
		PlayerClassSaveData data = getSaveData(player);
		
		return true;
	}
	
	private static PlayerClassSaveData getSaveData(EntityPlayer player)
	{
		String name = EntityHelper.getPlayerName(player);
		PlayerClassSaveData data = (PlayerClassSaveData)world.loadItemData(PlayerClassSaveData.class, name);
		if(data == null)
		{
			data = new PlayerClassSaveData(name);
			world.setItemData(name, data);
		}
		return data;
	}
}
