package alpvax.classmodcore.api.powers;

import static alpvax.classmodcore.api.powers.EnumPowerType.CONTINUOUS;
import static alpvax.classmodcore.api.powers.EnumPowerType.INSTANT;
import static alpvax.classmodcore.api.powers.EnumPowerType.TOGGLED;

import java.util.HashMap;
import java.util.Map;


public abstract class PowerEntry
{
	public static final String KEY_COOLDOWN = "cooldown";
	public static final String KEY_DURATION = "duration";

	private final IPower power;
	private EnumPowerType type;
	private boolean manual;
	protected Map<String, Object> data = new HashMap<String, Object>();

	protected PowerEntry(IPower power, EnumPowerType type)
	{
		this.power = power;
		this.type = type;
		manual = false;
	}

	public PowerEntry addData(String key, Object data)
	{
		this.data.put(key, data);
		return this;
	}

	public void setPlayerActivated()
	{
		manual = type != CONTINUOUS;
	}

	public PowerInstance createInstance(int index)
	{
		return new PowerInstance(power, type, manual, index, data);
	}

	public static class Passive extends PowerEntry
	{
		public Passive(IPower power)
		{
			super(power, CONTINUOUS);
		}
	}

	protected static abstract class Active extends PowerEntry
	{
		public Active(IPower power, EnumPowerType type, int cooldown)
		{
			super(power, type);
			addData(KEY_COOLDOWN, Integer.valueOf(cooldown));
		}
	}

	public static class Instant extends Active
	{
		public Instant(IPower power, int cooldown)
		{
			super(power, INSTANT, cooldown);
		}
	}

	public static class Toggle extends Active
	{
		public Toggle(IPower power, int cooldown)
		{
			super(power, TOGGLED, cooldown);
		}
	}

	public static class Timed extends Active
	{
		public Timed(IPower power, int cooldown, int duration)
		{
			super(power, TOGGLED, cooldown);
			addData(KEY_DURATION, Integer.valueOf(duration));
		}
	}
}
