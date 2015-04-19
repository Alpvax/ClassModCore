package alpvax.mod.classmodcore.classes;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import alpvax.mod.classmodcore.powers.PowerEntry;
import alpvax.mod.classmodcore.powers.PowerInstance;

public class PlayerClassInstance
{
	private final IPlayerClass playerclass;
	private final EntityPlayer player;
	private PowerInstance[] powers;
	
	public PlayerClassInstance(IPlayerClass playerclass, EntityPlayer player)
	{
		this.playerclass = playerclass;
		this.player = player;
		List<PowerEntry> list = playerclass.getPowers();
		int num = 0;
		if(list != null)
		{
			num = list.size();
		}
		powers = new PowerInstance[num];
		for(int i = 0; i < num; i++)
		{
			powers[i] = list.get(i).createInstance();
		}
	}
	
	public void tick()
	{
		for(PowerInstance p : powers)
		{
			p.tickPower(player);
		}
	}
}
