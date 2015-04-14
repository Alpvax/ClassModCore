package alpvax.mod.classmodcore.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import alpvax.mod.classmodcore.core.ClassUtil;
import alpvax.mod.classmodcore.permissions.IPlayerClassPermission;
import alpvax.mod.classmodcore.permissions.SimpleClassPermission;
import alpvax.mod.common.util.SaveHelper;

public final class PlayerClassRegistry
{
	private static Map<String, IPlayerClass> idToClassMap = new HashMap<String, IPlayerClass>();
	private static Map<String, IPlayerClassPermission> classStates = new HashMap<String, IPlayerClassPermission>();
	
	private static boolean DONE = false;

	public static void registerPlayerClass(IPlayerClass playerclass)
	{
		registerPlayerClass(playerclass, null);
	}

	public static void registerPlayerClass(IPlayerClass playerclass, String prefix)
	{
		if(playerclass == null)
		{
			throw new IllegalArgumentException("Failed to register null PlayerClass.");
		}
		String name = playerclass.getClassID();
		if(DONE)
		{
			//TODO:Change to logging warning and continuing
			throw new RuntimeException("Classes must be registered before FMLPostInitialisation event is fired. Skipping PlayerClass with name: " + name + ".");
		}
		if(ClassUtil.verifyClassName(name) != 0)
		{
			throw new IllegalArgumentException("Failed to register PlayerClass with name: " + name + ". Class name invalid.");
		}
		if(prefix != null)
		{
			name = prefix + name;
		}
		if(idToClassMap.containsKey(name.toLowerCase()))
		{
			throw new IllegalArgumentException("Failed to register PlayerClass with name: " + name + ". Class with that name already exists.");
		}
		idToClassMap.put(name.toLowerCase(), playerclass);
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
}
