package alpvax.classmodcore.api.powers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import alpvax.classmodcore.api.powers.IPower.ITickingPower;

/**
 * @author Alpvax
 *
 */
public abstract class PowerSelfDamage extends DummyPower implements ITickingPower
{
	private DamageSource source;
	private float damage;
	private int interval;

	public PowerSelfDamage(String displayType, DamageSource source, float amount, int intervalTicks)
	{
		super(displayType, "Damage");
		this.source = source;
		damage = amount;
		interval = intervalTicks;
	}

	@Override
	public void onTick(EntityPlayer player, int ticksActive)
	{
		if(shouldDamage(player))
		{
			if(interval < 1 || ticksActive % interval == 0)
			{
				player.attackEntityFrom(source, damage);//TODO:Attacking other entities
			}
		}
	}

	protected abstract boolean shouldDamage(EntityPlayer player);
}
