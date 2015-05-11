package alpvax.classmodcore.api.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Level;

import alpvax.classmodcore.api.ClassUtil;
import alpvax.classmodcore.api.permissions.IPlayerClassPermission;
import alpvax.classmodcore.api.permissions.SimpleClassPermission;
import alpvax.common.util.SaveHelper;


public final class PlayerClassRegistry
{
	private static Map<String, IPlayerClass> idToClassMap = new HashMap<String, IPlayerClass>();
	protected static Map<String, String> modIDMap = new HashMap<String, String>();
	private static Map<String, IPlayerClassPermission> classStates = new HashMap<String, IPlayerClassPermission>();

	private static boolean DONE = false;

	public static IPlayerClass registerPlayerClass(IPlayerClass playerclass)
	{
		return registerPlayerClass(playerclass, null);
	}

	/*public static IPlayerClass registerPlayerClass(IPlayerClass playerclass, String group)
	{
		return registerPlayerClass(playerclass, group, null);
	}

	public static IPlayerClass registerPlayerClass(IPlayerClass playerclass, String group, IPlayerClassPermission permission)*/
	public static IPlayerClass registerPlayerClass(IPlayerClass playerclass, IPlayerClassPermission permission)
	{
		if(playerclass == null)
		{
			FMLLog.log("ClassMod", Level.WARN, new IllegalArgumentException(), "Failed to register PlayerClass: playerclass cannot be null.");
			return null;
		}
		String id = playerclass.getClassID();
		String name = playerclass.getDisplayName();
		if(DONE)
		{
			FMLLog.log("ClassMod", Level.WARN, "Classes must be registered before FMLPostInitialisation event is fired. Skipping PlayerClass: \"%1$s\" with id: \"%2$s\".", name, id);
			return null;
		}
		if(id == null || ClassUtil.nullKeys.contains(id))
		{
			FMLLog.log("ClassMod", Level.WARN, new IllegalArgumentException(), "Failed to register PlayerClass: \"%1$s\" with no id. Class id invalid.", name);
			return null;
		}
		/*if(group != null)
		{
			id = group + "." + id;
		}*/
		if(idToClassMap.containsKey(id.toLowerCase()))
		{
			FMLLog.log("ClassMod", Level.WARN, new IllegalArgumentException(), "Failed to register PlayerClass: \"%1$s\" with id: \"%2$s\". Class with that id already exists.", name, id);
			return null;
		}
		do_register(id.toLowerCase(), playerclass, permission);
		return playerclass;
	}

	private static void do_register(String classID, IPlayerClass playerclass, IPlayerClassPermission permission)
	{
		String modID = Loader.instance().activeModContainer().getModId();
		FMLLog.log("ClassMod", Level.INFO, "Mod: \"%1$s\" is registering " + (classID.equals("") ? " a null class: \"%2$s\"" : "\"%2$s\" with ID \"%3$s\""), modID, playerclass.getDisplayName(), playerclass.getClassID());
		idToClassMap.put(classID, playerclass);
		modIDMap.put(classID, modID);
		classStates.put(classID, permission);
	}

	/**
	 * Registers a default or null playerclass. Normally "Steve", who is nothing special. (Has no powers or attributes)
	 *
	 * @param playerclass
	 */
	public static void registerNullClass(IPlayerClass playerclass)
	{
		if(playerclass == null)
		{
			FMLLog.log("ClassMod", Level.WARN, new IllegalArgumentException(), "Failed to register null PlayerClass: playerclass cannot be null.");
		}
		IPlayerClassPermission nullPerm = new IPlayerClassPermission(){

			@Override
			public boolean isAvailableInGui(EntityPlayer player)
			{
				return true;
			}

			@Override
			public boolean isAvailableForCommand(ICommandSender commandSender)
			{
				return true;
			}

			@Override
			public void setFromConfig(Property configProperty)
			{
			}
		};
		do_register("", playerclass, nullPerm);
	}

	public static IPlayerClass getPlayerClass(String classID)
	{
		return idToClassMap.get(classID);
	}

	public static ResourceLocation getClassImage(String classID)
	{
		return new ResourceLocation(PlayerClassRegistry.modIDMap.get(classID) + ":textures/playerclasses/" + (classID.length() < 1 ? "steve" : classID.replace(".", "/")) + ".png");
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

	public static List<String> availableClasses(ICommandSender commandSender)
	{
		List<String> list = new ArrayList<String>();
		for(String id : idToClassMap.keySet())
		{
			if(!DONE)
			{
				throw new RuntimeException("Classes have not yet been initialised");
			}
			if(classStates.get(id).isAvailableForCommand(commandSender))
			{
				list.add(id);
			}
		}
		return list;
	}

	public static void setClassStates()
	{
		Iterator<String> i = idToClassMap.keySet().iterator();
		while(i.hasNext())
		{
			String classID = i.next();
			if(classID.equals(""))
			{
				continue;
			}
			int index = classID.lastIndexOf(".");
			String group = index >= 0 ? classID.substring(0, index) : "";
			String id = index >= 0 ? classID.substring(index + 1) : classID;
			Property p = SaveHelper.getProperty("ClassMod", "Classes.cfg", group, id, Boolean.TRUE.toString(), "Is " + idToClassMap.get(classID).getDisplayName() + " enabled?", Property.Type.BOOLEAN);
			if(classStates.get(classID) == null)
			{
				classStates.put(classID, new SimpleClassPermission(p.getBoolean()));
			}
			else
			{
				classStates.get(classID).setFromConfig(p);
			}
		}
		DONE = true;
	}
}
