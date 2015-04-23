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
	
	public PowerInMaterialMoveSpeed(String displayString)
	{
		super(displayString);
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

	@Override
	public void initialise(Map<String, Object> additionalData)
	{
		super.initialise(additionalData);
		material = (Material)additionalData.get("material");
	}

}
