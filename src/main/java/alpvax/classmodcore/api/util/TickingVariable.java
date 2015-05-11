package alpvax.classmodcore.api.util;

/**
 * @author Alpvax
 *
 */
public class TickingVariable
{
	private boolean exists = false;
	private boolean countUp = false;
	private int modifier;
	private int limit;
	private int value = 0;

	public TickingVariable(int limit)
	{
		this.limit = limit;
	}

	public TickingVariable countUp()
	{
		countUp = true;
		return this;
	}

	/**
	 * Increments/Decrements value by 1
	 * @return true if the counter reaches the limit
	 */
	public boolean tick()
	{
		if(exists)
		{
			if(countUp)
			{
				if(value < limit)
				{
					value++;
					if(value >= limit)
					{
						value = limit;
						return true;
					}
				}
			}
			else
			{
				if(value > limit)
				{
					value--;
					if(value <= limit)
					{
						value = limit;
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Increments/Decrements value by modifier
	 * @return the old value
	 */
	public int apply()
	{
		int i = value;
		if(exists)
		{
			if(countUp)
			{
				value -= modifier;
			}
			else
			{
				value += modifier;
			}
		}
		return i;
	}

	public void setModifier(Integer i)
	{
		if(i != null)
		{
			modifier = i.intValue();
			exists = true;
		}
	}

	public int value()
	{
		return value;
	}

	public void setValue(int i)
	{
		value = i;
	}

	public boolean exists()
	{
		return exists;
	}

	@Override
	public String toString()
	{
		return value + "(" + (countUp ? "+" : "-") + modifier + ")";
	}
}
