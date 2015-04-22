package alpvax.classmodcore.config;

import java.util.Locale;

import net.minecraft.client.resources.I18n;


public enum EnumClassSelect
{
	ONCE,
	ON_DEATH,
	NEVER;

	@Override
	public String toString()
	{
		return I18n.format("config.rule.classselect." + name().toLowerCase(Locale.ENGLISH) + ".name");
	}

	public String getDescription()
	{
		return I18n.format("config.rule.classselect." + name().toLowerCase(Locale.ENGLISH) + ".desc");
	}
}
