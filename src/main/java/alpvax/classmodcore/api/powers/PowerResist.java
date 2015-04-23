package alpvax.classmodcore.api.powers;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public abstract class PowerResist implements IPower
{
	private float multiplier;
	private String display;
	
	public PowerResist(String displayType)
	{
		display = displayType;
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
		return false;
	}

	@Override
	public void resetPower(EntityPlayer player, Map<String, Object> additionalData)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onTick(EntityPlayer player, Map<String, Object> additionalData)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public String getDisplayName()
	{
		return display + " Resistance";
	}

	@Override
	public void initialise(Map<String, Object> additionalData)
	{
		multiplier = additionalData.containsKey("multiplier") ? ((Float)additionalData.get("multiplier")).floatValue() : 1.0F;
	}
	
	public float modifyDamage(DamageSource src, EntityPlayer player, float amount)
	{
		return shouldModify(src, player) ? amount * multiplier : amount;
	}
	
	public abstract boolean shouldModify(DamageSource src, EntityPlayer player);
}
