package alpvax.mod.classmodcore.classes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author Alpvax
 *
 */
public class PlayerClassHelper
{
	public static String getPlayerName(EntityPlayer player)
	{
		return player.getDisplayNameString();
	}
	
	public static PlayerClass getPlayerClass(EntityPlayer player)
	{
		return getPlayerClass(player, player.worldObj);
	}
	
	public static PlayerClass getPlayerClass(EntityPlayer player, World world)
	{
		String name = getPlayerName(player);
        PlayerClassSaveData data = (PlayerClassSaveData) world.loadItemData(PlayerClassSaveData.class, name);
        if(data == null)
        {
            data = new PlayerClassSaveData();
            world.setItemData(event.ownerNetwork, data);
        }
	}
}
