package alpvax.mod.classmodcore.power;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowerMineSpeed extends Power
{
	public List<String> tools = new ArrayList<String>();
	/** Dig speed multiplier */
	public float digSpeed;

	public PowerMineSpeed(String toolClass, float speed)
	{
		digSpeed = speed;
		tools.add(toolClass);
	}
	public PowerMineSpeed(int id, String[] toolClasses, float speed)
	{
		digSpeed = speed;
		tools = Arrays.asList(toolClasses);
	}
	
	@Override
	public String getDisplayName()
	{
		return null;
	}
}
