package alpvax.mod.classmodcore.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import alpvax.mod.classmodcore.permissions.IPlayerClassPermission;
import alpvax.mod.classmodcore.permissions.SimpleClassPermission;
import alpvax.mod.common.util.SaveHelper;

public final class PlayerClassRegistry
{
	private static Map<String, IPlayerClass> idToClassMap = new HashMap<String, IPlayerClass>();
	private static Map<String, String> modIDMap = new HashMap<String, String>();
	private static Map<String, IPlayerClassPermission> classStates = new HashMap<String, IPlayerClassPermission>();
	
	private static boolean DONE = false;

	public static void registerPlayerClass(IPlayerClass playerclass)
	{
		registerPlayerClass(playerclass, null);
	}
	public static void registerPlayerClass(IPlayerClass playerclass, String group)
	{
		if(playerclass == null)
		{
			throw new IllegalArgumentException("Failed to register null PlayerClass.");
		}
		String id = playerclass.getClassID();
		if(DONE)
		{
			//TODO:Change to logging warning and continuing
			throw new RuntimeException("Classes must be registered before FMLPostInitialisation event is fired. Skipping PlayerClass with id: " + id + ".");
		}
		if(id == null || id.length() < 1)
		{
			throw new IllegalArgumentException("Failed to register PlayerClass with no id: " + id + ". Class id invalid.");
		}
		if(group != null)
		{
			id = group + id;
		}
		if(idToClassMap.containsKey(id.toLowerCase()))
		{
			throw new IllegalArgumentException("Failed to register PlayerClass with id: " + id + ". Class with that id already exists.");
		}
		idToClassMap.put(id.toLowerCase(), playerclass);
		modIDMap.put(id.toLowerCase(), Loader.instance().activeModContainer().getModId());
	}

	public static IPlayerClass getPlayerClass(String classID)
	{
		return idToClassMap.get(classID.toLowerCase());
	}
	
	@SideOnly(Side.CLIENT)
	public static List<IPlayerClass> availableClassesForGUI(EntityPlayer player)
	{
		List<IPlayerClass> list = new ArrayList<IPlayerClass>();
		for(String id : idToClassMap.keySet())
		{
			if(!DONE)
			{
				throw new RuntimeException("Classes have not yet been initialised");
			}
			if(classStates.get(id).isAvailableInGui(player))
			{
				list.add(idToClassMap.get(id));
			}
		}
		return list;
	}
	
	public static List<IPlayerClass> availableClasses(ICommandSender commandSender)
	{
		List<IPlayerClass> list = new ArrayList<IPlayerClass>();
		for(String id : idToClassMap.keySet())
		{
			if(!DONE)
			{
				throw new RuntimeException("Classes have not yet been initialised");
			}
			if(classStates.get(id).isAvailableForCommand(commandSender))
			{
				list.add(idToClassMap.get(id));
			}
		}
		return list;
	}

	public static void setClassStates()
	{
		Configuration[] c = SaveHelper.getConfigs("ClassMod", "Classes.cfg");
		Configuration defConfig = c[0];
		defConfig.load();
		Configuration config = c[1];
		if(config != null)
		{
			config.load();
		}
		Iterator<String> i = idToClassMap.keySet().iterator();
		while(i.hasNext())
		{
			String classID = i.next();
			//TODO:More complex class permissions in config
			classStates.put(classID, new SimpleClassPermission(getProperty(classID, defConfig, config).getBoolean()));
		}
		if(config != null)
		{
			config.save();
		}
		else
		{
			defConfig.save();
		}
		DONE = true;
	}
	
	private static Property getProperty(String classID, Configuration defConfig, Configuration config)
	{
		int i = classID.lastIndexOf('.');
		String category = classID.substring(0, i);
		String key = classID.substring(i + 1);
		Property p = defConfig.get(category, key, true);
		return config != null ? config.get(category, key, p.getBoolean()) : p;
	}

	public static ResourceLocation getClassImage(String classID)
	{
		return new ResourceLocation(modIDMap.get(classID) + ":textures/classes/" + classID.replace(".", "/") + ".png");
	}
}
