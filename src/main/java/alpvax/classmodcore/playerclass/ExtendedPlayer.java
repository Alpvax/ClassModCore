package alpvax.classmodcore.playerclass;

//TODO:EMPTY CLASS, IMPLEMENT METHODS ELSEWHERE
public class ExtendedPlayer
{
	/**
	 * @param operation 0 => add modifier to base, 1 => multiply base by modifier, 2 => multiply final result by modifier.
	 */
	/*private void updateAttribute(IAttribute a, double modifier, int operation)
	{
		IAttributeInstance att = player.getAttributeMap().getAttributeInstance(a);
		AttributeModifier m = att.getModifier(ClassUtil.attModIDClass);
		if(m != null)
		{
			att.removeModifier(m);
		}
		att.applyModifier(new AttributeModifier(ClassUtil.attModIDClass, ModData.classModID, modifier - (operation == 2 ? 1D : 0D), operation));
	}*/

	// ****************PlayerClass Methods****************

	/*TODO:ChangeClassOnDeath
	private String getNextClass()

	{
		return hasClassName() ? classIDs[1] : null;
	}

	private void setNextClass(String classID)
	{
		classIDs[1] = classID;
	}*/

	/*TODO:Enity ignore, nightvision ect.
	public boolean getIsOblivious(Entity entity)
	{
		return hasPlayerClass() && getPlayerClass().getIsOblivious(entity);
	}

	public float getNightVision()
	{
		if(hasPlayerClass())
		{
			return getPlayerClass().nightVision;
		}
		return 0F;
	}*/

	/*TODO:DelayAllPowers
	private void startPowerDelay()
	{
		if(hasPlayerClass())
		{
			PlayerClass pc = getPlayerClass();
			boolean flag = ClassMod.startDelay > 0;
			if(flag || ClassMod.startOnCooldown)
			{
				for(int i = 0; i < pc.getNumPowers(); i++ )
				{
					if(flag && ClassMod.delayAllPassive && pc.getPower(i) instanceof IPowerActive)
					{
						setPowerCooldown(i, ClassMod.startDelay);
					}
					else if(flag && ClassMod.delayPassive && pc.getPower(i) instanceof IPowerTriggeredActive)
					{
						int cd = pc.getPowerCooldown(i);
						setPowerCooldown(i, ClassMod.startDelay + (ClassMod.startOnCooldown && cd >= 0 ? cd : 0));
					}
					else
					{
						setPowerCooldown(i, ClassMod.startDelay + (ClassMod.startOnCooldown ? pc.getPowerCooldown(i) : 0));
					}
				}
			}
		}
	}*/

	/*TODO:getActivePowers
	public List<IPower> getActivePowers()
	{
		PlayerClass pc = getPlayerClass();
		List<IPower> list = new ArrayList<IPowerActive>();
		if(pc == null)
		{
			return list;
		}
		List<Power> pwrs = pc.getPowers();
		for(Power p : pwrs)
		{
			System.out.println("getActivePowers: " + p);
			if(p instanceof IPower)
			{
				int i = pc.getPowerSlot(p);
				if(getPowerDuration(i) > 0 || pc.isPowerConstant(i))
				{
					list.add((IPowerActive)p);
				}
			}
		}
		return list;
	}

	public <T extends IPower>List<T> getActivePowers(Class<T> powerclass)
	{
		List<T> powers = new ArrayList<T>();
		Iterator<IPowerActive> i = getActivePowers().iterator();
		while(i.hasNext())
		{
			IPower power = i.next();
			System.out.println("EP.gAP(C): " + power);
			if(powerclass.isAssignableFrom(power.getClass()))
			{
				powers.add(powerclass.cast(power));
			}
		}
		return powers;
	}

	public boolean isPowerActive(int powerID)
	{
		return isPowerActive(PowerRegistry.getPower(powerID));
	}*/

	//TODO:Static Attributes
	//public static final Attribute nightvision = (new RangedAttribute("generic.nightVision", 0F, 0.0D, 1F)).func_111117_a("Night Vision").setShouldWatch(true);
}
