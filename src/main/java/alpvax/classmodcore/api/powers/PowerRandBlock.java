package alpvax.classmodcore.api.powers;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import alpvax.classmodcore.api.powers.IPower.ITickingPower;


public abstract class PowerRandBlock extends DummyPower implements ITickingPower
{
	private double radius;
	private float chance;

	public PowerRandBlock(String displayString, double radius, float chance)
	{
		super(displayString);
		this.radius = radius;
		this.chance = chance;
	}

	@Override
	public void onTick(EntityPlayer player, int ticksActive)
	{
		Random r = player.getRNG();
		if(r.nextFloat() < chance)
		{
			BlockPos pos = player.getPosition().add(r.nextDouble() * radius * 2 - radius, r.nextDouble() * radius * 2 - radius, r.nextDouble() * radius * 2 - radius);
			if(player.getDistanceSq(pos) <= radius * radius)
			{
				doEffect(player, player.worldObj, pos, ticksActive);
			}
		}
	}

	public abstract void doEffect(EntityPlayer player, World world, BlockPos pos, int ticksActive);
}
