package alpvax.mod.classmodcore.classes;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import alpvax.mod.classmodcore.events.ChangeClassEvent;

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

	/**
	 * Gets the class of the specified player. Will never return null, so to check if the class has been set use {@link #hasPlayerClass(EntityPlayer)}
	 * @return the playerclass, or the default playerclass if the class has not been set yet
	 */
	public static IPlayerClass getPlayerClass(EntityPlayer player)
	{
		IPlayerClass pc = getSaveData(player).getPlayerClass();
		return pc != null ? pc : PlayerClassRegistry.getPlayerClass("");
	}
	
	/**
	 * @return whether or not the specified player's class has been set
	 */
	public static boolean hasPlayerClass(EntityPlayer player)
	{
		return getSaveData(player).hasPlayerClass();
	}

	public static boolean setPlayerClass(IPlayerClass playerclass, EntityPlayer player)
	{
		return setPlayerClass(playerclass, player, MinecraftServer.getServer());
	}
	public static boolean setPlayerClass(IPlayerClass playerclass, EntityPlayer player, ICommandSender sender)
	{
		PlayerClassSaveData data = getSaveData(player);
		//Ignore if something saved and classes match
		boolean ignore = data.hasPlayerClass() && playerclass != null && playerclass.equals(data.getPlayerClass());
		if(ignore || MinecraftForge.EVENT_BUS.post(new ChangeClassEvent(player, playerclass, sender)))
		{
			return false;
		}
		System.err.println("Now to save the data");//XXX
		data.setPlayerClass(playerclass);
		data.markDirty();
		return true;
	}
	
	private static PlayerClassSaveData getSaveData(EntityPlayer player)
	{
		String name = player.getDisplayNameString();
		MapStorage m = (PER_WORLD_CLASSES ? player.worldObj : DimensionManager.getWorld(0)).getPerWorldStorage();
		PlayerClassSaveData data = (PlayerClassSaveData)m.loadData(PlayerClassSaveData.class, "PlayerClass/" + name);
		if(data == null)
		{
			data = new PlayerClassSaveData(name);
			m.setData(name, data);
		}
		return data;
	}
}
