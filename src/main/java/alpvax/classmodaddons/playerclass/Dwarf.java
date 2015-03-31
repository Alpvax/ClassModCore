package alpvax.classmodaddons.playerclass;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCaveSpider;
import alpvax.classmodaddons.core.ClassModBaseClasses;
import alpvax.mod.classmodcore.playerclass.PlayerClass;

public class Dwarf extends PlayerClass
{
	public Dwarf()
	{
		super("Dwarf");
		reachModifier = -1F;
		speedModifier = 0.8F;
		addPassivePower(ClassModBaseClasses.mineSpeed);
		addPassivePower(ClassModBaseClasses.woodPick);
		//addPower(ClassMod.lesserKey, new PowerProspect(5, 1, 3));
		//powerMap.put(ClassMod.greaterKey, new PowerMineEscape(60, 15, 1.5F));
		//lesserPower = new PowerProspect(5, 3, 3);
		//greaterPower = new PowerMineEscape(60, 15, 1.5F);
		nightVision = 0.5F;
		//height = 1F;
	}

	@Override
	public boolean getIsOblivious(Entity entity)
	{
		if(entity instanceof EntityCaveSpider) return true;
		return false;
	}

}
