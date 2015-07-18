package alpvax.classmodcore.api.powers;

import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import alpvax.classmodcore.api.powers.IPower.IExtendedPower;
import alpvax.classmodcore.api.powers.IPower.IToggledPower;
import alpvax.common.util.EntityHelper;


/**
 * @author Alpvax
 *
 */
public class PowerMoveSpeed extends DummyPower implements IToggledPower, IExtendedPower
{
	//private float multiplier;
	private AttributeModifier modifier;

	public PowerMoveSpeed(String displayType, float damageMult)
	{
		this(displayType, new AttributeModifier(UUID.randomUUID(), "ClassModSpeedBoost", damageMult, 1));
	}

	public PowerMoveSpeed(String displayType, AttributeModifier modifier)
	{
		super(displayType, "Move Speed");
		this.modifier = modifier.setSaved(false);
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
		EntityHelper.applyAttributeModifier(player, SharedMonsterAttributes.movementSpeed, /*new AttributeModifier(ClassUtil.attModIDPower, "ClassModSpeedBoost", multiplier, 1)*/modifier, 2);
		return true;
	}

	@Override
	public void resetPower(EntityPlayer player, int ticksActive)
	{
		EntityHelper.removeAttributeModifier(player, SharedMonsterAttributes.movementSpeed, /*new AttributeModifier(ClassUtil.attModIDPower, "ClassModSpeedBoost", multiplier, 1)*/modifier, 2);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		long least = nbt.getLong("UUIDLeast");
		long most = nbt.getLong("UUIDMost");
		modifier = new AttributeModifier(new UUID(most, least), modifier.getName(), modifier.getAmount(), modifier.getOperation()).setSaved(false);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		UUID id = modifier.getID();
		nbt.setLong("UUIDLeast", id.getLeastSignificantBits());
		nbt.setLong("UUIDMost", id.getMostSignificantBits());
	}
}
