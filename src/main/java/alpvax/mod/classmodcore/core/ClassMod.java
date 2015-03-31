package alpvax.mod.classmodcore.core;

import java.io.File;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import alpvax.common.mods.ModData;
import alpvax.common.network.AlpModPacket;
import alpvax.common.network.PacketHandler;
import alpvax.mod.classmodcore.block.Blocks;
import alpvax.mod.classmodcore.command.CommandChangeClass;
import alpvax.mod.classmodcore.network.CommonProxy;
import alpvax.mod.classmodcore.network.packet.ClassSelectPacket;
import alpvax.mod.classmodcore.network.packet.DataStringPacket;
import alpvax.mod.classmodcore.playerclass.PlayerClassRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = ModData.classModID, name = ModData.classModName, version = ModData.classModVersion)//, acceptedMinecraftVersions = "[1.4]")
@NetworkMod(clientSideRequired=true,serverSideRequired=false, channels = {AlpModPacket.channel}, packetHandler = PacketHandler.class, connectionHandler = ConnectionHandler.class)
public class ClassMod
{
	@Instance(ModData.classModID)
	public static ClassMod instance;
	
	@SidedProxy(clientSide = "alpvax.classmod.network.ClientProxy", serverSide = "alpvax.classmod.network.CommonProxy")
	public static CommonProxy proxy;

	private static Configuration config;
	public static File configDir;
	
	//Rules
	public static boolean lockClasses;
	public static int startDelay;
	public static boolean startOnCooldown;
	public static boolean delayPassive;
	public static boolean delayAllPassive;
	
	public static int selectGUIMaxC;
	public static int selectGUIMaxR;
	
	//Modules
	public static boolean blocks;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		initPackets();
		instance = this;
		//configFile = event.getSuggestedConfigurationFile();
		configDir = new File(event.getSuggestedConfigurationFile().getParent(), "ClassMod");
		configDir.mkdirs();
		config = new Configuration(new File(configDir, "ClassMod.cfg"));
		config.load();
		
		blocks = config.get("Modules", "Blocks", true).getBoolean(true);
		if(blocks)
		{
			Blocks.init(configDir);
		}
		ClassUtil.init(configDir);
		

		selectGUIMaxC = ClassMod.config.get("GUI", "Columns", 4).getInt();
		selectGUIMaxR = ClassMod.config.get("GUI", "Rows", 1).getInt();
		
		//MultiBlock.init(event);

		lockClasses = config.get("Rules", "Lock Classes", true).getBoolean(true);
		startDelay = config.get("Rules", "Start Delay", 120).getInt(120);
		startOnCooldown = config.get("Rules", "Start On Cooldown", false).getBoolean(false);
		delayPassive = config.get("Rules", "Delay Triggerable Passive Powers", false).getBoolean(false);
		delayPassive = config.get("Rules", "Delay All Passive Powers", false).getBoolean(false);
		

		MinecraftForge.EVENT_BUS.register(new ClassHooks());
	}

	@EventHandler
	public void Init(FMLInitializationEvent event)
	{
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		//NetworkRegistry.instance().registerConnectionHandler(new ConnectionHandler());
		proxy.registerClientHandlers();
		proxy.registerRenderInformation();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		PlayerClassRegistry.setEnabledClasses();
		config.save();
	}

	@EventHandler
    public void onServerStart(FMLServerStartingEvent event)
    {
	    event.registerServerCommand(new CommandChangeClass());
    }
	
	private void initPackets()
	{
		//AlpModPacket.registerPacket(packetClass);
		AlpModPacket.registerPacket(ClassSelectPacket.class);
		//AlpModPacket.registerPacket(DataStringPacket.class);
		//AlpModPacket.registerPacket(OpenGUIPacket.class);
		/*packets.put(0, ClassSelectPacket.class);
    	packets.put(1, TriggerPowerPacket.class);
    	packets.put(2, TargetPacket.class);
    	packets.put(3, FluidFillPacket.class);
    	packets.put(4, FluidDrainPacket.class);*/
	}
}