package alpvax.mod.classmodcore.power;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.world.World;

public class PowerRandSpawn extends PowerRandom
{
	private float radius;
	
	public PowerRandSpawn(Random rand, float radius)
	{
		super(rand);
		this.radius = radius;
	}

	private boolean trySpawn(World world, double posX, double posY, double posZ, Random random)
	{
		if(!world.isRemote && world.provider.isSurfaceWorld() && random.nextInt(2000) < 2 * world.difficultySetting)
        {
            int var6;

            for (var6 = (int)posY; !world.doesBlockHaveSolidTopSurface((int)posX, (int)var6, (int)posZ) && var6 > 0; --var6)
            {
            }

            if (var6 > 0 && !world.isBlockNormalCube((int)posX, var6 + 1, (int)posZ))
            {
                Entity entity = ItemMonsterPlacer.spawnCreature(world, 57, (double)posX + 0.5D, (double)var6 + 1.1D, (double)posZ + 0.5D);
                if(random.nextInt(20) == 0)
                {
                	EntitySkeleton skeleton = (EntitySkeleton)ItemMonsterPlacer.spawnCreature(world, 51, (double)posX + 0.5D, (double)var6 + 1.1D, (double)posZ + 0.5D);
                	skeleton.setSkeletonType(1);
                	entity = skeleton;
                }

                if (entity != null)
                {
                	entity.timeUntilPortal = entity.getPortalCooldown();
                	return true;
                }
            }
        }
		return false;
	}

	@Override
	public void onUpdate(EntityPlayer player, Random random)
	{
		//trySpawn(world, posX, posY, posZ, random);
	}
}
