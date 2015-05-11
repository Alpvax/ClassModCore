package alpvax.classmodcore.api.powers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import alpvax.classmodcore.api.powers.IPower.IPowerEventListener;


public class PowerMineSpeed extends DummyPower implements IPowerEventListener<BreakSpeed>
{
	private float multiplier;
	private String type;

	public PowerMineSpeed(float multiplier, String harvestType)
	{
		super(harvestType, "Mining Speed");
		this.multiplier = multiplier;
		type = harvestType;
	}

	@Override
	public void listenToEvent(BreakSpeed e, EntityPlayer player)
	{
		if(shouldModifySpeed(e, player))
		{
			e.newSpeed *= multiplier;
		}
	}

	public boolean shouldModifySpeed(BreakSpeed e, EntityPlayer player)
	{
		if(type == null)
		{
			return true;
		}
		ItemStack stack = player.inventory.getCurrentItem();
		if(stack != null && stack.getItem().getHarvestLevel(stack, type) >= 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public Class<BreakSpeed> getEventClass()
	{
		return BreakSpeed.class;
	}
}
