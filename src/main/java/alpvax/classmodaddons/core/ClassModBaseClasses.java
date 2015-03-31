package alpvax.classmodaddons.core;

import net.minecraft.util.DamageSource;
import alpvax.classmodaddons.playerclass.Demon;
import alpvax.classmodaddons.playerclass.Dwarf;
import alpvax.common.mods.ModData;
import alpvax.common.network.AlpModPacket;
import alpvax.common.network.PacketHandler;
import alpvax.mod.classmodcore.core.ConnectionHandler;
import alpvax.mod.classmodcore.playerclass.PlayerClass;
import alpvax.mod.classmodcore.playerclass.PlayerClassRegistry;
import alpvax.mod.classmodcore.power.Power;
import alpvax.mod.classmodcore.power.PowerMine;
import alpvax.mod.classmodcore.power.PowerMineSpeed;
import alpvax.mod.classmodcore.power.PowerRegistry;
import alpvax.mod.classmodcore.power.PowerResistance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod(modid = ModData.classModBasicID, name = ModData.classModBasicName, version = ModData.classModBasicVersion)//, acceptedMinecraftVersions = "[1.4]")
@NetworkMod(clientSideRequired=true,serverSideRequired=false, channels = {AlpModPacket.channel}, packetHandler = PacketHandler.class, connectionHandler = ConnectionHandler.class)
public class ClassModBaseClasses
{
	public static final PlayerClass demon = new Demon();
	public static final PlayerClass dwarf = new Dwarf();
	//public static final PlayerClass endSpawn = new EndSpawn();
	
	public static final Power mineSpeed = new PowerMineSpeed("pickaxe", 1.2F);
	public static final Power woodPick = new PowerMine("pickaxe", 1);

	public static final Power fireProof = new PowerResistance(1F, DamageSource.inFire, DamageSource.onFire, DamageSource.lava);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		PlayerClassRegistry.registerPlayerClass(demon);
		PlayerClassRegistry.registerPlayerClass(dwarf);

		PowerRegistry.registerPower(mineSpeed);
		PowerRegistry.registerPower(woodPick);
		PowerRegistry.registerPower(fireProof);
	}
}
