package alpvax.mod.classmodcore.power;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class PowerResistance extends Power
{
	public List<DamageSource> damagetypes = new ArrayList<DamageSource>();
	public float resistance;

	/**
	 * @param amount the amount of resistance: 0 = no resistance, 1 = 100% resistance
	 */
	public PowerResistance(float amount, DamageSource damagesource)
	{
		resistance = amount;
		damagetypes.add(damagesource);
	}

	/**
	 * @param amount the amount of resistance: 0 = no resistance, 1 = 100% resistance
	 */
	public PowerResistance(float amount, DamageSource... damagesources)
	{
		resistance = amount;
		damagetypes = Arrays.asList(damagesources);
	}
}
