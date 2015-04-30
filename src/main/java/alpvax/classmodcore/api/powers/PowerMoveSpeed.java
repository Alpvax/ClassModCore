package alpvax.classmodcore.api.powers;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import alpvax.classmodcore.api.ClassUtil;
import alpvax.classmodcore.api.powers.IPower.IToggledPower;
import alpvax.common.util.EntityHelper;


/**
 * @author Alpvax
 *
 */
public class PowerMoveSpeed extends DummyPower implements IToggledPower
{
	private float multiplier;

	public PowerMoveSpeed(String displayType, float damageMult)
	{
		super(displayType, "Move Speed");
		multiplier = damageMult;
	}

	@Override
	public boolean shouldTrigger(EntityPlayer player)
	{
		return false;
	}

	@Override
	public boolean shouldReset(EntityPlayer player)
	{
		return false;
	}

	@Override
	public boolean triggerPower(EntityPlayer player)
	{
		EntityHelper.applyAttributeModifier(player, SharedMonsterAttributes.movementSpeed, new AttributeModifier(ClassUtil.attModIDPower, "ClassModSpeedBoost", multiplier, 1), 2);
		return true;
	}

	@Override
	public void resetPower(EntityPlayer player)
	{
		EntityHelper.removeAttributeModifier(player, SharedMonsterAttributes.movementSpeed, new AttributeModifier(ClassUtil.attModIDPower, "ClassModSpeedBoost", multiplier, 1), 2);
	}
}
