package alpvax.mod.classmodcore.classes;

import alpvax.mod.common.util.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Alpvax
 *
 */
public class PlayerClassHelper
{
	public static IPlayerClass getPlayerClass(EntityPlayer player)
	{
		return getPlayerClass(player, player.worldObj);
	}

	public static IPlayerClass getPlayerClass(EntityPlayer player, World world)
	{
		String name = PlayerHelper.getPlayerName(player);
		PlayerClassSaveData data = (PlayerClassSaveData)world.loadItemData(PlayerClassSaveData.class, name);
		if(data == null)
		{
			data = new PlayerClassSaveData(name);
			world.setItemData(name, data);
		}
		return PlayerClassRegistry.getPlayerClass(data.id);
	}
}
