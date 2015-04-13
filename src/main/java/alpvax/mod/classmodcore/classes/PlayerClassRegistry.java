package alpvax.mod.classmodcore.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraftforge.common.config.Configuration;
import alpvax.mod.classmodcore.core.ClassMod;
import alpvax.mod.classmodcore.core.ClassUtil;

public final class PlayerClassRegistry
{	
	public static Configuration config;
	
	private static Map<String, IPlayerClass> idToClassMap = new HashMap<String, IPlayerClass>();
	public static List<String> allowedClasses = new ArrayList<String>();
	
	public static final List<String> enabledClasses = new ArrayList<String>();
	
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
	
	public static Set<String> getCompleteClassList()
	{
		return idToClassMap.keySet();
	}
	
	public static void setEnabledClasses()
	{
		if(config == null)
		{
			init(ClassMod.configDir);
		}
		config.load();
		//Set<String> list = PlayerClass.getCompleteClassList();
		Iterator<String> i = getCompleteClassList().iterator();
		while(i.hasNext())
		{
			String classID = i.next();
			if(config.get("Classes", classID, true).getBoolean(true))
			{
				enabledClasses.add(classID.toLowerCase());
			}
		}
		config.save();
	}

	public static void addToConfig(String classID)
	{
		if(config == null)
		{
			init(ClassMod.configDir);
		}
		config.load();
		config.get("Classes", classID, true).getBoolean(true);
		config.save();
	}
	
	public static boolean isClassEnabled(String className)
	{
		return enabledClasses.contains(className.toLowerCase());
	}
	
	private static void init(File configDir)
	{
		config = new Configuration(new File(configDir, "Classes.cfg"));
	}
}
