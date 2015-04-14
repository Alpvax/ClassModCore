package alpvax.mod.classmodcore.block;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraftforge.common.config.Configuration;

public class Blocks
{
	public static Block forge;
	public static Block nSmelter;

	public static double nSmeltSpd;
	public static double nSmeltThreshold;
	public static double nSmeltMaxHeat;
	public static int nSmeltCapacity;

	public static Configuration config;

	public static void init(File configDir)
	{
		config = new Configuration(new File(configDir, "Blocks.cfg"));
		config.load();

		// forge = new BlockForge(forgeID).setUnlocalizedName("forge");
		// nSmelter = new
		// BlockNetherSmelter(nSmelterID).setUnlocalizedName("nSmelter");

		nSmeltSpd = config.get("NetherSmelter", "BaseSpeedMultiplier", 0.25F).getDouble(0.25F);
		nSmeltThreshold = config.get("NetherSmelter", "NoFuelHeatThreshold", 2.0F).getDouble(2.0F);
		nSmeltMaxHeat = config.get("NetherSmelter", "MaximumHeatCapacity", 10.0F).getDouble(10.0F);
		nSmeltCapacity = config.get("NetherSmelter", "TankCapacity", 3).getInt();
		config.save();

		registerBlocks();
	}

	private static void registerBlocks()
	{
		// GameRegistry.registerBlock(forge, "dwarvenForge");
		// GameRegistry.registerBlock(nSmelter, "netherSmelter");
	}
}