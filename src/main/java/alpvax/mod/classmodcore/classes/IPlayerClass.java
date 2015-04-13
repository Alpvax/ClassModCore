package alpvax.mod.classmodcore.classes;

import java.util.List;

import alpvax.mod.classmodcore.powers.IPower;

/**
 * @author Alpvax
 *
 */
public interface IPlayerClass
{
	public String getClassID();
	public List<IPower> getPowers();
}
