package alpvax.classmodcore.api.powers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import alpvax.classmodcore.api.powers.IPower.IPowerEventListener;


public abstract class PowerResist extends DummyPower implements IPowerEventListener<LivingHurtEvent>
{
	private float multiplier;

	public PowerResist(String displayType, float damageMult)
	{
		super(displayType, "Resistance");
		multiplier = damageMult;
	}

	public float modifyDamage(DamageSource src, EntityPlayer player, float amount)
	{
		return shouldModify(src, player) ? amount * multiplier : amount;
	}

	public abstract boolean shouldModify(DamageSource src, EntityPlayer player);

	@Override
	public void listenToEvent(LivingHurtEvent e, EntityPlayer player)
	{
		if(shouldModify(e.source, (EntityPlayer)e.entityLiving))
		{
			e.ammount *= multiplier;
			if(e.ammount < 0)
			{
				player.heal(-1 * e.ammount);
			}
			//Threshold of 0.2 (tenth of a heart)
			if(e.ammount < 0.2F)
			{
				e.setCanceled(true);
			}
		}
	}

	@Override
	public Class<LivingHurtEvent> getEventClass()
	{
		return LivingHurtEvent.class;
	}
}
