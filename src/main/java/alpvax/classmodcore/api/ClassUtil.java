package alpvax.classmodcore.api;

import java.io.File;
import java.util.UUID;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import alpvax.common.mods.ModData;

import com.google.common.collect.ImmutableList;


public final class ClassUtil
{
	public static final String KEY_ID = "Id";
	public static final String KEY_POWERS = "Powers";
	public static final String KEY_SLOT = "Slot";
	public static final String KEY_ACTIVE = "Active";
	public static final String KEY_TICKSELAPSED = "ElapsedTicks";
	public static final String KEY_CD = "Cooldown";
	public static final String KEY_DUR = "Duration";
	public static final String KEY_CLASSES = "PlayerClasses";
	public static final String KEY_PLAYER = "Player";

	public static final ImmutableList<String> nullKeys = new ImmutableList.Builder<String>().add("", "null", "none").build();

	public static UUID attModIDPower;
	public static int maxNumActivePowers = 4;//TODO:dynamic

	// public static UUID nightvisionAttID;

	public static final int classGUIID = 0;

	public static final String classIconPath = "textures/gui/";
	public static final ResourceLocation classGUIMain = new ResourceLocation(ModData.classModID, classIconPath + "class_select.png");

	public static void init(File configDir)
	{
		Configuration config = new Configuration(new File(configDir, "ClassMod.cfg"));
		config.load();
		attModIDPower = UUID.fromString(config.get("Rules.UUIDs", "AttMod", UUID.randomUUID().toString(), "Unique ID for any attribute modifiers used by powers").getString());
		// nightvisionAttID = UUID.fromString(config.get("UUIDs", "Nightvision", UUID.randomUUID().toString(), "Unique ID for nightvision attribute modifiers").getString());
		config.save();
	}
}
