package alpvax.classmodcore.api.powers;

import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;


/**
 * @author Alpvax
 *
 */
public class PowerInMaterialMoveSpeed extends PowerMoveSpeed
{
	private Material material;

	public PowerInMaterialMoveSpeed(String displayString, float speedMultiplier, Material material)
	{
		super(displayString, speedMultiplier);
		this.material = material;
	}

	@Override
	public boolean shouldTrigger(EntityPlayer player, Map<String, Object> additionalData)
	{
		return player.isInsideOfMaterial(material);
	}

	@Override
	public boolean shouldReset(EntityPlayer player, Map<String, Object> additionalData)
	{
		return !player.isInsideOfMaterial(material);
	}

}
