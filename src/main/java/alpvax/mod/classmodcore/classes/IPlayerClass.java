package alpvax.mod.classmodcore.classes;

import java.util.List;

import alpvax.mod.classmodcore.powers.IPower;

/**
 * @author Alpvax
 *
 */
public interface IPlayerClass
{
	/**
	 * @return a unique id to reference this class. Uses the form <group>.<id>
	 */
	public String getClassID();
	
	public String getDisplayName();

	public List<IPower> getPowers();

}
