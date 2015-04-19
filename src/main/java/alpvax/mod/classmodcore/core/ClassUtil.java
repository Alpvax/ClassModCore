package alpvax.mod.classmodcore.core;

import java.io.File;
import java.util.UUID;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import alpvax.mod.common.mods.ModData;

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
	
	// public static final String COMMANDIDFLAG = "-i";

	public static int CLASS_WATCHER;

	// NONE OF THESE CAN BE THE SAME
	public static final String NULL_CHAR = "-";
	public static final String CLASS_SPLIT = ":";
	public static final String PWR_START = "[";
	public static final String PWR_END = "]";
	public static final String PWR_SUBSPLIT = ";";

	public static UUID attModIDClass;
	public static UUID attModIDPower;

	// public static UUID nightvisionAttID;

	public static final int classGUIID = 0;

	public static final String classIconPath = "textures/gui/";
	public static final ResourceLocation classGUIMain = new ResourceLocation(ModData.classModID, classIconPath + "class_select.png");

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
	}
}
