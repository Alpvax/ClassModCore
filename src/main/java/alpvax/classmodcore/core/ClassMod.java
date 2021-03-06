package alpvax.classmodcore.core;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import alpvax.classmodcore.api.ClassUtil;
import alpvax.classmodcore.api.classes.PlayerClassRegistry;
import alpvax.classmodcore.api.classes.SimplePlayerClass;
import alpvax.classmodcore.command.CommandChangeClass;
import alpvax.classmodcore.network.CommonProxy;
import alpvax.classmodcore.network.packets.ClassChangePacket;
import alpvax.classmodcore.network.packets.TriggerPowerPacket;
import alpvax.common.network.AlpPacketManager;
import alpvax.common.network.OpenGuiPacket;


@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION)
//TODO:Finish config, canBeDeactivated = true, guiFactory = "alpvax.classmodcore.config.ConfigGuiFactory")
// , acceptedMinecraftVersions = "[1.4]")
public class ClassMod
{
	@Instance(ModInfo.MOD_ID)
	public static ClassMod instance;

	@SidedProxy(clientSide = "alpvax.classmodcore.network.ClientProxy", serverSide = "alpvax.classmodcore.network.CommonProxy")
	public static CommonProxy proxy;

	public static AlpPacketManager packetHandler;

	//private static Configuration defaultConfig;

	// Rules
	/*public static boolean lockClasses;
	public static int startDelay;
	public static boolean startOnCooldown;
	public static boolean delayPassive;
	public static boolean delayAllPassive;*/

	// Modules
	public static boolean blocks;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		/*defaultConfig = new Configuration(event.getSuggestedConfigurationFile());
		defaultConfig.load();
		defaultConfig.addCustomCategoryComment(ConfigConstants.CATEGORY_RULES, "Rules");
		defaultConfig.addCustomCategoryComment(ConfigConstants.CATEGORY_CLASSES, "Enabled Classes");
		defaultConfig.addCustomCategoryComment(ConfigConstants.CATEGORY_MODULES, "Enabled Modules");*/
		packetHandler = new AlpPacketManager(ModInfo.MOD_ID);
		initPackets();
		instance = this;

		ClassUtil.init(new File(event.getModConfigurationDirectory(), "ClassMod"));

		PlayerClassRegistry.registerNullClass(new SimplePlayerClass(""){}.setDisplayName("Steve"));

		/*selectGUIMaxC = ClassMod.defaultConfig.get("GUI", "Columns", 4).getInt();
		selectGUIMaxR = ClassMod.defaultConfig.get("GUI", "Rows", 1).getInt();

		// MultiBlock.init(event);

		lockClasses = defaultConfig.get("Rules", "Lock Classes", true).getBoolean(true);
		startDelay = defaultConfig.get("Rules", "Start Delay", 120).getInt(120);
		startOnCooldown = defaultConfig.get("Rules", "Start On Cooldown", false).getBoolean(false);
		delayPassive = defaultConfig.get("Rules", "Delay Triggerable Passive Powers", false).getBoolean(false);
		delayPassive = defaultConfig.get("Rules", "Delay All Passive Powers", false).getBoolean(false);*/

		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		ClassHooks hooks = new ClassHooks();
		MinecraftForge.EVENT_BUS.register(hooks);
		FMLCommonHandler.instance().bus().register(hooks);
	}

	@EventHandler
	public void Init(FMLInitializationEvent event)
	{
		KeyBindings.init();
		proxy.registerClientHandlers();
		proxy.registerRenderInformation();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		PlayerClassRegistry.setClassStates();
		//TODO:defaultConfig.save();
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandChangeClass());
		//TODO:event.registerServerCommand(new CommandForceClass());
	}

	private void initPackets()
	{
		packetHandler.register2WayMessage(ClassChangePacket.ClientHandler.class, ClassChangePacket.ServerHandler.class, ClassChangePacket.class);
		packetHandler.registerMessage(OpenGuiPacket.Handler.class, OpenGuiPacket.class, Side.CLIENT);
		packetHandler.registerMessage(TriggerPowerPacket.Handler.class, TriggerPowerPacket.class, Side.CLIENT);
		// AlpModPacket.registerPacket(packetClass);
		// AlpModPacket.registerPacket(ClassSelectPacket.class);
		// AlpModPacket.registerPacket(DataStringPacket.class);
		// AlpModPacket.registerPacket(OpenGUIPacket.class);
		/*
		 * packets.put(0, ClassSelectPacket.class); packets.put(1, TriggerPowerPacket.class); packets.put(2, TargetPacket.class); packets.put(3, FluidFillPacket.class); packets.put(4, FluidDrainPacket.class);
		 */
	}
}