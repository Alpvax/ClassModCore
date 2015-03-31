package alpvax.mod.classmodcore.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.AlpFieldAccessor;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import alpvax.mod.classmodcore.playerclass.ExtendedPlayer;
import alpvax.mod.classmodcore.playerclass.PlayerClass;
import alpvax.mod.classmodcore.power.PowerMine;
import alpvax.mod.classmodcore.power.PowerMineSpeed;
import alpvax.mod.classmodcore.power.PowerResistance;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClassHooks
{
	@ForgeSubscribe
	public void onEntityConstructing(EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer) event.entity) == null)
		{
			ExtendedPlayer.register((EntityPlayer) event.entity);
		}
	}
	
	@ForgeSubscribe
	public void onSpawn(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entity;
			ExtendedPlayer ep = ExtendedPlayer.get(player);
			ep.loadProxyData();
			//ClassUtil.openGUI(player);
		}
	}

	@ForgeSubscribe
	public void onEntityDeath(LivingDeathEvent e)
	{
		if(!e.isCanceled() && e.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)e.entity;
			ExtendedPlayer ep = ExtendedPlayer.get(player);
			if(ep != null && ep.hasPlayerClass())
			{
				ep.getPlayerClass().onDeath(player);
			}
			ep.changeClassOnDeath();
			ep.saveProxyData();
		}
	}

	@ForgeSubscribe
	public void handleDropsOnPlayerDeath(PlayerDropsEvent e)
	{
		EntityPlayer player = e.entityPlayer;
		ExtendedPlayer ep = ExtendedPlayer.get(player);
		if(!e.isCanceled() && ep != null && ep.hasPlayerClass())
		{
			PlayerClass playerclass = ep.getPlayerClass();
			List<ItemStack> items = playerclass.getDeathDrops();
			for(ItemStack item : items)
			{
				e.drops.add(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, item));
			}
		}
	}

	@ForgeSubscribe
	public void onPlayerUpdate(LivingUpdateEvent e)
	{
		if(!e.isCanceled() && e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)e.entityLiving;
			PlayerClass playerclass = ExtendedPlayer.get(player).getPlayerClass();
			if(playerclass != null)
			{
				playerclass.onUpdate(player);
			}
		}
	}
	
	@ForgeSubscribe
	public void onPlayerJump(LivingJumpEvent e)
	{
		if(!e.isCanceled() && e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)e.entityLiving;
			if(ExtendedPlayer.get(player).hasPlayerClass())
			{
				if(player.isPotionActive(Potion.jump))
		        {
					player.motionY -= (double)((float)(player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
		        }
				player.motionY = Math.sqrt(ExtendedPlayer.get(player).getPlayerClass().jumpHeight * 0.16F);
				if(player.isPotionActive(Potion.jump))
		        {
					player.motionY += (double)((float)(player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
		        }
			}
		}
	}
	
	@ForgeSubscribe
	public void onPlayerMine(HarvestCheck e)
	{
		ExtendedPlayer ep = ExtendedPlayer.get(e.entityPlayer);
		if(!e.isCanceled() && e.success == false && ep.hasPlayerClass())
		{
			System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; HarvestCheck");
			Iterator<PowerMine> i = ep.getActivePowers(PowerMine.class).iterator();
			while(e.success == false && i.hasNext())
			{
				PowerMine power = i.next();
				System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; " + power);
				//PowerMine p = (PowerMine)power;
				if(MinecraftForge.getBlockHarvestLevel(e.block, -1, power.tool) <= power.level)
				{
					e.success = true;
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onPlayerDig(BreakSpeed e)
	{
		ExtendedPlayer ep = ExtendedPlayer.get(e.entityPlayer);
		if(!e.isCanceled() && ep.hasPlayerClass())
		{
			Iterator<PowerMineSpeed> i = ep.getActivePowers(PowerMineSpeed.class).iterator();
			while(i.hasNext())
			{
				PowerMineSpeed power = i.next();
				System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; " + power);
				for(int j = 0; j < power.tools.size(); j++)
				{
					if(AlpFieldAccessor.getToolEffectiveness().contains(Arrays.asList(e.block, e.metadata, power.tools.get(j))))
					{
						e.newSpeed *= power.digSpeed;
					}
				}
			}
		}
	}
	
	@ForgeSubscribe
	public void onPlayerFall(LivingFallEvent e)
	{
		if(!e.isCanceled() && e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)e.entityLiving;
			PlayerClass playerclass = ExtendedPlayer.get(player).getPlayerClass();
			if(playerclass != null && playerclass.jumpHeight != 1F)
			{
				float f = playerclass.jumpHeight - 0.5F;
				e.distance /= f;
			}
		}
	}
	
	@ForgeSubscribe
	public void onPlayerHit(LivingHurtEvent e)
	{
		if(!e.isCanceled())
		{
			if(e.entityLiving instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)e.entityLiving;
				ExtendedPlayer ep = ExtendedPlayer.get(player);
				PlayerClass playerclass = ep.getPlayerClass();
				if(playerclass != null)
				{
					Iterator<PowerResistance> i = ep.getActivePowers(PowerResistance.class).iterator();
					while(!e.isCanceled() && i.hasNext())
					{
						PowerResistance power = i.next();
						System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; " + power);
						if(power.damagetypes.contains(e.source))
						{
							if(power.resistance == 1F)
							{
								e.setCanceled(true);
							}
							else
							{
								e.ammount *= (1 - power.resistance);
							}
						}
					}
					playerclass.onTakeDamage(player, e.source, e.ammount);
				}
			}
			if(e.source.getSourceOfDamage() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)e.source.getSourceOfDamage();
				ExtendedPlayer ep = ExtendedPlayer.get(player);
				PlayerClass playerclass = ep.getPlayerClass();
				if(playerclass != null)
				{
					playerclass.onDamageEntity(player, e.entity, e.source, e.ammount);
				}
			}
		}
	}
	
	/**
	 * Only triggered when entity uses the Task System
	 */
	@ForgeSubscribe
	public void onTargetPlayer(LivingSetAttackTargetEvent e)
	{
		if(!e.isCanceled() && e.target instanceof EntityPlayer && e.isCancelable())
		{
			EntityPlayer player = (EntityPlayer)e.target;
			PlayerClass playerclass = ExtendedPlayer.get(player).getPlayerClass();
			if(playerclass != null && playerclass.getIsOblivious(e.entityLiving))
			{
				e.setCanceled(true);
			}
		}
	}
}