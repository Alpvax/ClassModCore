package alpvax.mod.classmodcore.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.SelectValueEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import alpvax.common.util.SaveHelper;
import alpvax.mod.classmodcore.core.ClassMod;
import alpvax.mod.classmodcore.core.ModInfo;
import alpvax.mod.common.config.CategoryConfigGui;
import alpvax.mod.common.config.SimpleCategoryElement;

public class ConfigGui extends GuiConfig
{
	private Configuration config;

	public ConfigGui(GuiScreen parentScreen)
	{
		super(parentScreen, getConfigElements(getConfig()), ModInfo.MOD_ID, false, false, "");
		config = getConfig();
		String worldName = SaveHelper.getWorldName();
		if(worldName == null)
		{
			title = I18n.format("classmodcore.configtitle.default1");
			titleLine2 = I18n.format("classmodcore.configtitle.default2");
		}
		else
		{
			title = I18n.format("classmodcore.configtitle.world", worldName);
			titleLine2 = "(" + GuiConfig.getAbridgedConfigPath(config.toString()) + ")";
		}
	}

	private static List<IConfigElement> getConfigElements(Configuration config)
	{
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.add(new SimpleCategoryElement(config.getCategory(ConfigConstants.CATEGORY_RULES), RulesEntry.class));
		// TODO:list.add(new SimpleCategoryElement(config.getCategory(ConfigConstants.CATEGORY_CLASSES), ClassesEntry.class));
		// TODO:list.add(new SimpleCategoryElement(config.getCaConfigConstants.CATEGORY.CONFIG_MODULES), ModulesEntry.class));
		return list;
	}

	private static Configuration getConfig()
	{
		File save = DimensionManager.getCurrentSaveRootDirectory();
		return save != null ? new Configuration(new File(save, "config/ClassMod.cfg")) : ClassMod.defaultConfig;
	}

	private class RulesEntry extends CategoryEntry
	{
		public RulesEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
		{
			super(owningScreen, owningEntryList, configElement);
		}

		@Override
		protected GuiScreen buildChildScreen()
		{
			List<IConfigElement> list = new ArrayList<IConfigElement>();
            
            list.add(new DummyCategoryElement("classSelection", "classmodcore.rules.classSelection", SelectValueEntry.class));
            
			return new CategoryConfigGui(owningScreen, config.getCategory(ConfigConstants.CATEGORY_RULES), list, configElement, title);
		}
	}
}
