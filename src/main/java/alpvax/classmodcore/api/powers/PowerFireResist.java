package alpvax.classmodcore.api.powers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;


public class PowerFireResist extends PowerResist
{
	public PowerFireResist(float multiplier)
	{
		super("Fire", multiplier);
	}

	@Override
	public boolean shouldModify(DamageSource src, EntityPlayer player)
	{
		return src.isFireDamage();
	}

}
