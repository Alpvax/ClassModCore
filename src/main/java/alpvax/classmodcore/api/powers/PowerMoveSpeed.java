package alpvax.classmodcore.api.powers;

import java.util.Map;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import alpvax.classmodcore.api.ClassUtil;
import alpvax.common.util.EntityHelper;


/**
 * @author Alpvax
 *
 */
public class PowerMoveSpeed extends DummyPower implements IPower
{
	private float multiplier;

	public PowerMoveSpeed(String displayType, float damageMult)
	{
		super(displayType, "Move Speed");
		multiplier = damageMult;
	}

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
		EntityHelper.applyAttributeModifier(player, SharedMonsterAttributes.movementSpeed, new AttributeModifier(ClassUtil.attModIDPower, "ClassModSpeedBoost", multiplier, 1), 2);
		return true;
	}

	@Override
	public void resetPower(EntityPlayer player, Map<String, Object> instanceData)
	{
		EntityHelper.removeAttributeModifier(player, SharedMonsterAttributes.movementSpeed, new AttributeModifier(ClassUtil.attModIDPower, "ClassModSpeedBoost", multiplier, 1), 2);
	}
}
