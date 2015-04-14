package alpvax.mod.classmodcore.core;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import alpvax.mod.classmodcore.playerclass.ExtendedPlayer;
import alpvax.mod.classmodcore.playerclass.PlayerClass;
import alpvax.mod.classmodcore.power.PowerResistance;

public class ClassHooks
{
	@EventHandler
	public void onEntityConstructing(EntityConstructing event)
	{
		if(event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer)event.entity) == null)
		{
			ExtendedPlayer.register((EntityPlayer)event.entity);
		}
	}

	@EventHandler
	public void onSpawn(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entity;
			ExtendedPlayer ep = ExtendedPlayer.get(player);
			ep.loadProxyData();
			// ClassUtil.openGUI(player);
		}
	}

	@EventHandler
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

	@EventHandler
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

	@EventHandler
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

	@EventHandler
	public void onPlayerJump(LivingJumpEvent e)
	{
		if(!e.isCanceled() && e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)e.entityLiving;
			if(ExtendedPlayer.get(player).hasPlayerClass())
			{
				if(player.isPotionActive(Potion.jump))
				{
					player.motionY -= (player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
				}
				player.motionY = Math.sqrt(ExtendedPlayer.get(player).getPlayerClass().jumpHeight * 0.16F);
				if(player.isPotionActive(Potion.jump))
				{
					player.motionY += (player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
				}
			}
		}
	}

	/*
	 * @EventHandler public void onPlayerMine(HarvestCheck e) { ExtendedPlayer ep = ExtendedPlayer.get(e.entityPlayer); if(!e.isCanceled() && e.success == false && ep.hasPlayerClass()) { System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; HarvestCheck"); Iterator<PowerMine> i = ep.getActivePowers(PowerMine.class).iterator(); while(e.success == false && i.hasNext()) { PowerMine power = i.next(); System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; " + power); //PowerMine p = (PowerMine)power; if(MinecraftForge.getBlockHarvestLevel(e.block, -1, power.tool) <= power.level) { e.success = true; } } } }
	 * 
	 * @EventHandler public void onPlayerDig(BreakSpeed e) { ExtendedPlayer ep = ExtendedPlayer.get(e.entityPlayer); if(!e.isCanceled() && ep.hasPlayerClass()) { Iterator<PowerMineSpeed> i = ep.getActivePowers(PowerMineSpeed.class).iterator(); while(i.hasNext()) { PowerMineSpeed power = i.next(); System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; " + power); for(int j = 0; j < power.tools.size(); j++) { if(AlpFieldAccessor.getToolEffectiveness().contains(Arrays.asList(e.block, e.metadata, power.tools.get(j)))) { e.newSpeed *= power.digSpeed; } } } } }
	 */

	@EventHandler
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

	@EventHandler
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
								e.ammount *= 1 - power.resistance;
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
	@EventHandler
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