package alpvax.classmodcore.api.powers;

import java.util.HashMap;
import java.util.Map;


public class PowerEntry
{
	public static final String KEY_COOLDOWN = "cooldown";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_TRIGGERINDEX = "triggerKey";

	private final IPower power;
	private boolean passive = true;
	private boolean active = false;
	protected Map<String, Object> data = new HashMap<String, Object>();

	public PowerEntry(IPower power)
	{
		this.power = power;
	}

	public PowerEntry addData(String key, Object data)
	{
		this.data.put(key, data);
		return this;
	}

	public PowerEntry setCooldown(int i)
	{
		return addData(KEY_COOLDOWN, Integer.valueOf(i));
	}

	public PowerEntry setDuration(int i)
	{
		return addData(KEY_DURATION, Integer.valueOf(i));
	}

	public PowerEntry disableAutomaticActivation()
	{
		passive = false;
		return this;
	}

	public PowerEntry setStartActive()
	{
		active = true;
		return this;
	}

	public boolean triggerInstantly()
	{
		return active;
	}

	public PowerInstance createInstance()
	{
		return new PowerInstance(power, passive, data);
	}

	public static class PlayerTriggeredPowerEntry extends PowerEntry
	{
		public final int index;

		public PlayerTriggeredPowerEntry(IPower power, int keybindIndex)
		{
			super(power);
			index = keybindIndex;
		}

	}
}
