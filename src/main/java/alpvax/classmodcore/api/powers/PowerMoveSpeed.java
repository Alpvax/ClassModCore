package alpvax.classmodcore.api.powers;

import java.util.Map;

import alpvax.classmodcore.api.ClassUtil;
import alpvax.common.util.EntityHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Alpvax
 *
 */
public class PowerMoveSpeed implements IPower
{
	private double multiplier;
	private String display;

	public PowerMoveSpeed()
	{
		this("");
	}
	public PowerMoveSpeed(String displayType)
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
		EntityHelper.applyAttributeModifier(player, SharedMonsterAttributes.movementSpeed, new AttributeModifier(ClassUtil.attModIDPower, "ClassModSpeedBoost", multiplier, 1), 2);
		return true;
	}

	@Override
	public void resetPower(EntityPlayer player, Map<String, Object> additionalData)
	{
		EntityHelper.removeAttributeModifier(player, SharedMonsterAttributes.movementSpeed, new AttributeModifier(ClassUtil.attModIDPower, "ClassModSpeedBoost", multiplier, 1), 2);
	}

	@Override
	public void onTick(EntityPlayer player, Map<String, Object> additionalData)
	{
	}

	@Override
	public String getDisplayName()
	{
		return (display != null ? display + " " : "") + "Move Speed";
	}

	/* (non-Javadoc)
	 * @see alpvax.classmodcore.api.powers.IPower#initialise(java.util.Map)
	 */
	@Override
	public void initialise(Map<String, Object> additionalData)
	{
		multiplier = additionalData.containsKey("multiplier") ? ((Float)additionalData.get("multiplier")).floatValue() : 1.0F;
	}

}
