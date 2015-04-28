package alpvax.classmodcore.api.powers;

import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;


/**
 * @author Alpvax
 *
 */
public class PowerInMaterialSelfDamage extends PowerSelfDamage
{
	private Material material;

	public PowerInMaterialSelfDamage(String displayString, DamageSource source, float amount, int intervalTicks, Material material)
	{
		super(displayString, source, amount, intervalTicks);
		this.material = material;
	}

	@Override
	protected boolean shouldDamage(EntityPlayer player, int ticksElapsed, Map<String, Object> instanceData)
	{
		return player.isInsideOfMaterial(material);
	}

}
