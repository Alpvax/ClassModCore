package alpvax.classmodcore.api.powers;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


public abstract class PowerResist implements IPowerEventListener<LivingHurtEvent>
{
	private float multiplier;
	private String display;

	public PowerResist(String displayType, float damageMult)
	{
		display = displayType;
		multiplier = damageMult;
	}

	@Override
	public boolean shouldTrigger(EntityPlayer player, Map<String, Object> additionalData)
	{
		return false;
	}

	@Override
	public boolean shouldReset(EntityPlayer player, Map<String, Object> additionalData)
	{
		return false;
	}

	@Override
	public boolean triggerPower(EntityPlayer player, Map<String, Object> additionalData)
	{
		/*DamageSource src = (DamageSource)additionalData.get("dsrc");
		float amount = ((Float)additionalData.get("amount")).floatValue();
		float amm1 = modifyDamage(src, player, amount);
		additionalData.put("RESULT", amm1);
		return amount == amm1;*/
		return false;
	}

	@Override
	public void resetPower(EntityPlayer player, Map<String, Object> additionalData)
	{
	}

	@Override
	public String getDisplayName()
	{
		return (display != null ? display + " " : "") + "Resistance";
	}

	public float modifyDamage(DamageSource src, EntityPlayer player, float amount)
	{
		return shouldModify(src, player) ? amount * multiplier : amount;
	}

	public abstract boolean shouldModify(DamageSource src, EntityPlayer player);

	@Override
	public void listenToEvent(LivingHurtEvent e, EntityPlayer player)
	{
		System.err.printf("Recieved %s{%s, %s, %f}%n", e.getClass().getName(), player, e.ammount);//XXX
		if(shouldModify(e.source, (EntityPlayer)e.entityLiving))
		{
			e.ammount *= multiplier;
			System.err.printf("New amount: %f%n", e.ammount);//XXX
		}
	}
}
