package alpvax.classmodaddons.playerclass;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import alpvax.classmodaddons.core.ClassModBaseClasses;
//import alpvax.classmod.power.PowerFluidSpeed;
//import alpvax.classmod.power.PowerRandBlock;
import alpvax.mod.classmodcore.playerclass.PlayerClass;

public class Demon extends PlayerClass
{
	private boolean inWater = false;

	public Demon()
	{
		super("Demon");
		addPassivePower(ClassModBaseClasses.fireProof);
		/*passivePowers.add(new PowerFluidDamage(Material.water, 2));
		passivePowers.add(new PowerFluidDamage(Material.lava, -1, 10));
		Random rand = new Random();
		passivePowers.add(new PowerRandBlock(rand, 30, 1, 50, Block.fire.blockID));
		passivePowers.add(new PowerRandBlock(rand, 30, 1, 200, Block.netherrack.blockID));
		passivePowers.add(new PowerFluidSpeed(Material.lava, 1.0D));*/
	}

	/*@Override
	public void initPassive(EntityPlayer player)
	{
	}

	@Override
	public void handlePassivePower(EntityPlayer player)
	{
		super.handlePassivePower(player);
		
		player.extinguish();
		player.isImmuneToFire = true;

		trySpawn(player.worldObj, player.posX, player.posY, player.posZ, new Random());
	}*/
	
	@Override
	public void onTakeDamage(EntityPlayer player, DamageSource source, float damage)
	{
		Entity var3 = source.getEntity();
        List var4 = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(32.0D, 32.0D, 32.0D));

        for (int var5 = 0; var5 < var4.size(); ++var5)
        {
            Entity var6 = (Entity)var4.get(var5);

            if (var6 instanceof EntityPigZombie)
            {
                EntityPigZombie var7 = (EntityPigZombie)var6;
                var7.setTarget(var3);
            }
        }
	}
	
	@Override
	public void onDamageEntity(EntityPlayer player, Entity target, DamageSource source, float damage)
	{
		Random r = new Random();
		if(r.nextInt(20) == 0)
		{
			target.setFire(1 + r.nextInt(4) + r.nextInt(5));
		}
		if(target instanceof EntityLiving && r.nextInt(200) == 0)
		{
			((EntityLiving)target).addPotionEffect(new PotionEffect(Potion.wither.getId(), (1 + r.nextInt(2) + r.nextInt(2))));
		}
	}

	@Override
	public boolean getIsOblivious(Entity entity)
	{
		if(entity instanceof EntityPigZombie) return true;
		if(entity instanceof EntitySkeleton && ((EntitySkeleton)entity).getSkeletonType() == 1) return true;
		return false;
	}
}
