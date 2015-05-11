package alpvax.classmodcore.api.powers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.ItemInWorldManager;
import alpvax.classmodcore.api.powers.IPower.IToggledPower;


public class PowerReach extends DummyPower implements IToggledPower
{
	private float modifier;

	public PowerReach(float modifier)
	{
		super("Reach distance");
		this.modifier = modifier;
	}

	@Override
	public boolean shouldTrigger(EntityPlayer player)
	{
		return false;
	}

	@Override
	public boolean triggerPower(EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP)
		{
			ItemInWorldManager im = ((EntityPlayerMP)player).theItemInWorldManager;
			im.setBlockReachDistance(im.getBlockReachDistance() + modifier);
			return true;
		}
		return false;// TODO Auto-generated method stub
	}

	@Override
	public boolean shouldReset(EntityPlayer player)
	{
		return false;
	}

	@Override
	public void resetPower(EntityPlayer player, int ticksActive)
	{
		if(player instanceof EntityPlayerMP)
		{
			ItemInWorldManager im = ((EntityPlayerMP)player).theItemInWorldManager;
			im.setBlockReachDistance(im.getBlockReachDistance() - modifier);
		}
	}

}
