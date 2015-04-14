package alpvax.mod.classmodcore.power;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;

public abstract class PowerRandom extends Power implements IPowerActive
{
	private Random random;

	public PowerRandom(Random rand)
	{
		random = rand;
	}

	@Override
	public void onUpdate(EntityPlayer player)
	{
		onUpdate(player, random);
	}

	public abstract void onUpdate(EntityPlayer player, Random random);
}
