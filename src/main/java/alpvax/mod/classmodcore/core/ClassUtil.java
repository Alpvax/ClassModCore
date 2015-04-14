package alpvax.mod.classmodcore.core;

import java.io.File;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import alpvax.common.mods.ModData;
import alpvax.mod.classmodcore.playerclass.ExtendedPlayer;

public final class ClassUtil
{
	/*
	 * public static final String modID = "classmodcore"; public static final String modName = "Player Class Mod"; public static final String modVersion ="0.1.164";
	 */

	public static final String BASE_TAG = "PlayerClass";
	public static final String CLASS_TAG = "ID";
	public static final String POWER_TAG = "Powers";
	public static final String PWR_SLOT_TAG = "Slot";
	public static final String PWR_CD_TAG = "Cooldown";
	public static final String PWR_DUR_TAG = "Duration";
	public static final String PWR_DATA_TAG = "Data";

	public static final String COMMANDUSAGE = "command.changeclass.usage";
	public static final String COMMANDUSAGEFULL = "command.changeclass.usage";
	public static final String COMMANDSUCESS = "command.changeclass.sucess";
	public static final String COMMANDNOTFOUND = "command.changeclass.notfound";
	// public static final String COMMANDIDFLAG = "-i";

	public static int CLASS_WATCHER;

	// NONE OF THESE CAN BE THE SAME
	public static final String NULL_CHAR = "-";
	public static final String CLASS_SPLIT = ":";
	public static final String PWR_START = "[";
	public static final String PWR_END = "]";
	public static final String PWR_SUBSPLIT = ";";

	// private static final String[] disallowed = new String[]{NULL_CHAR,
	// CLASS_SPLIT, /*CLASS_PWR_SPLIT, */PWR_SPLIT, PWR_SUBSPLIT};
	private static final String[] disallowed = new String[]{NULL_CHAR, CLASS_SPLIT, PWR_START, PWR_END, PWR_SUBSPLIT};

	public static UUID attModIDClass;
	public static UUID attModIDPower;

	// public static UUID nightvisionAttID;

	public static final int classGUIID = 0;

	public static final String classIconPath = "textures/gui/";
	public static final ResourceLocation classGUIMain = new ResourceLocation(ModData.classModID, classIconPath + "class_select.png");

	/**
	 * @return 0 = valid; else throw error using throwNameError
	 */
	public static int verifyClassName(String name)
	{
		if(name == null || name.length() < 1)
		{
			return 1;
		}
		if(name.contains(" "))
		{
			return 2;
		}
		for(String s : disallowed)
		{
			if(name.contains(s))
			{
				return 3;
			}
		}
		return 0;
	}

	public static void throwClassNameError(String name)
	{
		int i = verifyClassName(name);
		switch(i)
		{
			case 0:
				return;
			case 1:
				throw new IllegalArgumentException("The name of the class must be unique (case insensitive) and not null or an empty string.");
			case 2:
				throw new IllegalArgumentException("The name of the class cannot contain spaces.");
			case 3:
				throw new IllegalArgumentException("The name of the class cannot contain any of these strings: " + getDisallowed());
		}
	}

	public static String getDisallowed()
	{
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < disallowed.length; i++ )
		{
			s.append("\"" + disallowed[i] + "\"");
			if(i < disallowed.length - 1)
			{
				s.append(", ");
			}
		}
		/*
		 * s.append(NULL_CHAR).append("\", \""); s.append(CLASS_SPLIT).append("\", \""); //s.append(CLASS_PWR_SPLIT).append("\", \""); s.append(PWR_SPLIT).append("\", \""); s.append(PWR_SUBSPLIT);
		 */
		return s.toString();
	}

	public static void openGUI(EntityPlayer player)
	{
		ExtendedPlayer ep = ExtendedPlayer.get(player);
		System.out.println("Opening GUI: Player: " + player + ", Current class: " + ep.getPlayerClass());
		if(!ep.hasPlayerClass())
		{
			player.openGui(ClassMod.instance, ClassUtil.classGUIID, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
	}

	public static void init(File configDir)
	{
		Configuration config = new Configuration(new File(configDir, "ClassMod.util"));
		config.load();
		CLASS_WATCHER = config.get("IDs", "DataWatcher property", 20).getInt();
		attModIDClass = UUID.fromString(config.get("UUIDs", "Attribute Modifier ID for base changes", UUID.randomUUID().toString()).getString());
		attModIDPower = UUID.fromString(config.get("UUIDs", "Attribute Modifier ID for power changes", UUID.randomUUID().toString()).getString());
		// nightvisionAttID = UUID.fromString(config.get("UUIDs",
		// "ID for nightvision Attribute",
		// UUID.randomUUID().toString()).getString());
		config.save();
		addLocalisations(LanguageRegistry.instance());
	}

	/** TODO: */
	private static void addLocalisations(LanguageRegistry lr)
	{
		lr.addStringLocalization(COMMANDUSAGE, "/playerclass <class>");
		lr.addStringLocalization(COMMANDUSAGEFULL, "/playerclass [player] <class>");
		lr.addStringLocalization(COMMANDSUCESS, "Set %2$s's class to %1$s");
		lr.addStringLocalization(COMMANDNOTFOUND, "Cannot find class %s");
	}
}
