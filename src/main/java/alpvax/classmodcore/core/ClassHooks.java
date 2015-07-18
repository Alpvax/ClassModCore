package alpvax.classmodcore.core;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Level;

import alpvax.classmodcore.api.classes.PlayerClassHelper;
import alpvax.classmodcore.api.classes.PlayerClassInstance;
import alpvax.classmodcore.api.classes.PlayerClassRegistry;
import alpvax.classmodcore.api.events.ChangeClassEvent;
import alpvax.classmodcore.api.powers.IPower.IPowerEventListener;
import alpvax.classmodcore.api.powers.PowerInstance;
import alpvax.classmodcore.network.packets.TriggerPowerPacket;
import alpvax.common.network.OpenGuiPacket;


public class ClassHooks
{
	private static String lastSaveName = null;

	@SubscribeEvent
	public void onLogIn(PlayerLoggedInEvent e)
	{
		FMLLog.log(ModInfo.MOD_ID, Level.INFO, "Player %s has logged in as a %s", e.player.getDisplayNameString(), PlayerClassHelper.getPlayerClass(e.player).getDisplayName());
		if(!PlayerClassHelper.hasPlayerClass(e.player))
		{
			ClassMod.packetHandler.sendTo(new OpenGuiPacket(ModInfo.MOD_ID, 0), (EntityPlayerMP)e.player);
		}
	}

	@SubscribeEvent
	public void onWorldChange(WorldEvent.Load e)
	{
		String name = e.world.getSaveHandler().getWorldDirectoryName();
		if(!name.equals(lastSaveName))
		{
			PlayerClassRegistry.setClassStates();
			lastSaveName = name;
		}
	}

	@SubscribeEvent
	public void onChangeClass(ChangeClassEvent e)
	{
		FMLLog.log(ModInfo.MOD_ID, Level.INFO, "Player: %s has become a %s", e.entityPlayer.getDisplayNameString(), e.playerclass.getDisplayName());
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@SubscribeEvent
	public void onEvent(LivingEvent e)
	{
		if(e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)e.entityLiving;
			PlayerClassInstance pci = PlayerClassHelper.getPlayerClassInstance(player);
			if(pci == null)
			{
				return;
			}
			List<PowerInstance<IPowerEventListener>> list = pci.getPowers(IPowerEventListener.class);
			for(PowerInstance p : list)
			{
				IPowerEventListener power = (IPowerEventListener)p.getPower();
				if(power.getEventClass().isAssignableFrom(e.getClass()))
				{
					power.listenToEvent(e, player);
				}
			}
		}
	}

	/*TODO:@SubscribeEvent
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

	/*@SubscribeEvent
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

	@SubscribeEvent
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
	}*/

	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent e)
	{
		if(e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)e.entityLiving;
			PlayerClassInstance pci = PlayerClassHelper.getPlayerClassInstance(player);
			if(pci != null)
			{
				pci.tick();
			}
		}
	}

	/*@SubscribeEvent
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
	 * @SubscribeEvent public void onPlayerMine(HarvestCheck e) { ExtendedPlayer ep = ExtendedPlayer.get(e.entityPlayer); if(!e.isCanceled() && e.success == false && ep.hasPlayerClass()) { System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; HarvestCheck"); Iterator<PowerMine> i = ep.getActivePowers(PowerMine.class).iterator(); while(e.success == false && i.hasNext()) { PowerMine power = i.next(); System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; " + power); //PowerMine p = (PowerMine)power; if(MinecraftForge.getBlockHarvestLevel(e.block, -1, power.tool) <= power.level) { e.success = true; } } } }
	 *
	 * @SubscribeEvent public void onPlayerDig(BreakSpeed e) { ExtendedPlayer ep = ExtendedPlayer.get(e.entityPlayer); if(!e.isCanceled() && ep.hasPlayerClass()) { Iterator<PowerMineSpeed> i = ep.getActivePowers(PowerMineSpeed.class).iterator(); while(i.hasNext()) { PowerMineSpeed power = i.next(); System.out.println("Side: " + FMLCommonHandler.instance().getEffectiveSide() + "; " + power); for(int j = 0; j < power.tools.size(); j++) { if(AlpFieldAccessor.getToolEffectiveness().contains(Arrays.asList(e.block, e.metadata, power.tools.get(j)))) { e.newSpeed *= power.digSpeed; } } } } }
	 */

	/*@SubscribeEvent
	public void onPlayerFall(LivingFallEvent e)
	{
		if(!e.isCanceled() && e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)e.entityLiving;
			IPlayerClass playerclass = PlayerClassHelper.getPlayerClass(player);
			if(playerclass != null && playerclass.jumpHeight != 1F)
			{
				float f = playerclass.jumpHeight - 0.5F;
				e.distance /= f;
			}
		}
	}*/

	/*TODO?@SubscribeEvent
	public void onPlayerHit(LivingHurtEvent e)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if(e.entityLiving instanceof EntityPlayer)
		{
			map.put("dsrc", e.source);
			map.put("amount", Float.valueOf(e.ammount));
			PlayerClassInstance pci = PlayerClassHelper.getPlayerClassInstance((EntityPlayer)e.entityLiving);
			for(PowerInstance p : pci.getActivePowers(PowerResist.class))
			{
				map = p.togglePower(pci.player, map);
				e.ammount = ((Float)map.get("RESULT")).floatValue();
			}
			/*EntityPlayer player = (EntityPlayer)e.entityLiving;
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
			if(e.source.getSourceOfDamage() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)e.source.getSourceOfDamage();
				ExtendedPlayer ep = ExtendedPlayer.get(player);
				PlayerClass playerclass = ep.getPlayerClass();
				if(playerclass != null)
				{
					playerclass.onDamageEntity(player, e.entity, e.source, e.ammount);
				}
			}*//*
			}
			}

			/**
			* Only triggered when entity uses the Task System
			*/
	/*@SubscribeEvent
	public void onTargetPlayer(LivingSetAttackTargetEvent e)
	{
		if(!e.isCanceled() && e.target instanceof EntityPlayer && e.isCancelable())
		{
			EntityPlayer player = (EntityPlayer)e.target;
			IPlayerClass playerclass = PlayerClassHelper.getPlayerClass(player);
			if(playerclass != null && playerclass.getIsOblivious(e.entityLiving))
			{
				//NON-CANCELLABLEe.setCanceled(true);
			}
		}
	}*/

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeyInput(KeyInputEvent e)
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		for(int i = 0; i < KeyBindings.powerBindings.size(); i++)
		{
			KeyBinding kb = KeyBindings.powerBindings.get(i);
			if(kb.isPressed())
			{
				ClassMod.packetHandler.sendToServer(new TriggerPowerPacket(player, i));
			}
		}
	}
}