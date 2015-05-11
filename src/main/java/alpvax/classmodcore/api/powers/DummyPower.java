package alpvax.classmodcore.api.powers;


/**
 * @author Alpvax
 *
 */
public abstract class DummyPower implements IPower
{
	private String display;

	public DummyPower(String displayType, String displaySuffix)
	{
		this((displayType == null || displayType == "" ? "" : displayType + " ") + displaySuffix);
	}

	public DummyPower(String displayString)
	{
		display = displayString;
	}

	@Override
	public String getDisplayName()
	{
		return display;
	}

	public IPower setDisplayName(String name)
	{
		display = name;
		return this;
	}
}
