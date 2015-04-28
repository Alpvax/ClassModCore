package alpvax.classmodcore.api.powers;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

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
	public int onTick(EntityPlayer player, int ticksElapsed, Map<String, Object> instanceData)
	{
		if(shouldDamage(player, ticksElapsed, instanceData))
		{
			if(ticksElapsed <= 0)
			{
				player.attackEntityFrom(source, damage);//TODO:Attacking other entities
				ticksElapsed += interval;
			}
		}
		return ticksElapsed;
	}

	protected abstract boolean shouldDamage(EntityPlayer player, int ticksElapsed, Map<String, Object> instanceData);

	@Override
	public boolean shouldTrigger(EntityPlayer player, Map<String, Object> instanceData)
	{
		return false;
	}

	@Override
	public boolean shouldReset(EntityPlayer player, Map<String, Object> instanceData)
	{
		return false;
	}

	@Override
	public boolean triggerPower(EntityPlayer player, Map<String, Object> instanceData)
	{
		return false;
	}

	@Override
	public void resetPower(EntityPlayer player, Map<String, Object> instanceData)
	{
	}
}
