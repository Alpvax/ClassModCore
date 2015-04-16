package alpvax.mod.classmodcore.classes;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
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

	public static IPlayerClass getPlayerClass(EntityPlayer player)
	{
		return getSaveData(player).getPlayerClass();
	}

	public static boolean setPlayerClass(IPlayerClass playerclass, EntityPlayer player)
	{
		return setPlayerClass(playerclass, player, MinecraftServer.getServer());
	}
	public static boolean setPlayerClass(IPlayerClass playerclass, EntityPlayer player, ICommandSender sender)
	{
		PlayerClassSaveData data = getSaveData(player);
		IPlayerClass pc1 = data.getPlayerClass();
		//If currentClass is newClass ignore
		if((playerclass == null ? pc1 == null : playerclass.equals(pc1)) || MinecraftForge.EVENT_BUS.post(new ChangeClassEvent(player, playerclass, sender)))
		{
			return false;
		}
		data.setPlayerClass(playerclass);
		data.markDirty();
		return true;
	}
	
	private static PlayerClassSaveData getSaveData(EntityPlayer player)
	{
		String name = EntityHelper.getPlayerName(player);
		World world = PER_WORLD_CLASSES ? player.worldObj : DimensionManager.getWorld(0);
		PlayerClassSaveData data = (PlayerClassSaveData)world.loadItemData(PlayerClassSaveData.class, name);
		if(data == null)
		{
			data = new PlayerClassSaveData(name);
			world.setItemData(name, data);
		}
		return data;
	}

	public static void initPlayerClass(EntityPlayer player)
	{
		// TODO Auto-generated method stub
	}
}
