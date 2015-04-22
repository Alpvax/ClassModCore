package alpvax.classmodcore.api;

import java.io.File;
import java.util.UUID;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import alpvax.common.mods.ModData;

import com.google.common.collect.ImmutableList;


public final class ClassUtil
{
	/*
	 * public static final String modID = "classmodcore"; public static final String modName = "Player Class Mod"; public static final String modVersion ="0.1.164";
	 */
	public static final String KEY_ID = "Id";
	public static final String KEY_POWERS = "Powers";
	public static final String KEY_SLOT = "Slot";
	public static final String KEY_ACTIVE = "Active";
	public static final String KEY_CD = "Cooldown";
	public static final String KEY_DUR = "Duration";
	public static final String KEY_KEYBIND = "KeyIndex";
	public static final String KEY_DATA = "Data";
	public static final String KEY_CLASSES = "PlayerClasses";
	public static final String KEY_PLAYER = "Player";

	public static final String BASE_TAG = "PlayerClass";
	public static final String CLASS_TAG = "ID";
	public static final String POWER_TAG = "Powers";
	public static final String PWR_SLOT_TAG = "Slot";

	public static final ImmutableList<String> nullKeys = new ImmutableList.Builder<String>().add("", "null", "none").build();

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
		attModIDClass = UUID.fromString(config.get("UUIDs", "Attribute Modifier ID for base changes", UUID.randomUUID().toString()).getString());
		attModIDPower = UUID.fromString(config.get("UUIDs", "Attribute Modifier ID for power changes", UUID.randomUUID().toString()).getString());
		// nightvisionAttID = UUID.fromString(config.get("UUIDs",
		// "ID for nightvision Attribute",
		// UUID.randomUUID().toString()).getString());
		config.save();
	}
}
