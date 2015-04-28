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
		display = (displayType == null || displayType == "" ? "" : displayType + " ") + displaySuffix;

	}

	@Override
	public String getDisplayName()
	{
		return display;
	}
}
